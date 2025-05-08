package com.rbc.explorer.careerexplorer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class JobController {

    private final JobRepository jobRepo;
    private final ApplicationRepository appRepo;
    private final EmailService emailService;
    private final ResumeScoringService scoringService;

    public JobController(JobRepository jobRepo, ApplicationRepository appRepo, EmailService emailService, ResumeScoringService scoringService) {
        this.jobRepo = jobRepo;
        this.appRepo = appRepo;
        this.emailService = emailService;
        this.scoringService = scoringService;
    }

    @GetMapping("/jobs")
    @ResponseBody
    public List<Job> getJobs() {
        return jobRepo.findAll();
    }

    @GetMapping("/jobs-page")
    public String showJobsPage(@RequestParam(required = false) String keyword, Model model) {
        List<Job> jobs = (keyword != null && !keyword.isEmpty())
                ? jobRepo.findByTitleContainingIgnoreCase(keyword)
                : jobRepo.findAll();

        model.addAttribute("jobs", jobs);
        model.addAttribute("keyword", keyword);
        return "jobs";
    }

    @GetMapping("/apply/{jobId}")
    public String showApplyPage(@PathVariable Long jobId, Model model) {
        Job job = jobRepo.findById(jobId).orElse(null);
        if (job == null) return "redirect:/jobs-page";
        model.addAttribute("job", job);
        return "apply";
    }

    @PostMapping("/apply")
    public String apply(@RequestParam("applicantName") String applicantName,
                        @RequestParam("email") String email,
                        @RequestParam("resumeFile") MultipartFile resumeFile,
                        @RequestParam("jobId") Long jobId,
                        Model model) throws IOException {

        Job job = jobRepo.findById(jobId).orElse(null);
        if (job == null) {
            model.addAttribute("error", "Job not found.");
            return "error";
        }

        // Create uploads folder using absolute path
        File uploadsDir = new File(System.getProperty("user.dir") + File.separator + "uploads");
        if (!uploadsDir.exists()) uploadsDir.mkdirs();

        // Save resume
        String originalFilename = resumeFile.getOriginalFilename();
        File dest = new File(uploadsDir, System.currentTimeMillis() + "_" + originalFilename);
        resumeFile.transferTo(dest);
        System.out.println("✅ Saved resume to: " + dest.getAbsolutePath());

        // Read resume text
        String resumeText = PdfUtil.extractTextFromPdf(dest);
        System.out.println("📝 Resume text extracted: " + resumeText.substring(0, Math.min(100, resumeText.length())) + "...");

        // Prepare JSON body for AI recommender
        String recommenderUrl = "http://localhost:5000/score";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("resume_text", resumeText);
        requestBody.put("job_description", job.getDescription()); // assumes this field is filled

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        Double score;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(recommenderUrl, requestEntity, Map.class);
            score = (Double) response.getBody().get("score");
            System.out.println("⭐ Received score from AI recommender: " + score);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Could not connect to AI recommender.");
            return "error";
        }

        // Save application
        Application app = new Application(applicantName, email, job);
        app.setResumePath("uploads/" + dest.getName());
        app.setAiScore(score);
        appRepo.save(app);

        emailService.sendConfirmation(email, job.getTitle());

        model.addAttribute("job", job);
        model.addAttribute("score", score);
        return "apply_success";
    }

    @GetMapping("/applications")
    public String viewApplications(Model model) {
        model.addAttribute("applications", appRepo.findAll());
        return "applications";
    }

    @PostMapping("/recommend")
    public String recommendScore(@RequestParam("resumeText") String resumeText,
                                 @RequestParam("jobId") Long jobId,
                                 Model model) {

        Job job = jobRepo.findById(jobId).orElse(null);
        if (job == null) {
            model.addAttribute("error", "Job not found.");
            return "error";
        }

        // Prepare JSON body
        String recommenderUrl = "http://localhost:5000/score";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("resume_text", resumeText);
        requestBody.put("job_description", job.getDescription());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        Double score;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(recommenderUrl, requestEntity, Map.class);
            score = (Double) response.getBody().get("score");
            System.out.println("📋 Recommendation Score: " + score);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Could not connect to AI recommender.");
            return "error";
        }

        model.addAttribute("job", job);
        model.addAttribute("score", score);
        return "apply"; // reload the same page with score
    }

    @GetMapping("/thankyou")
    public String showThankYouPage() {
        return "thankyou";
    }
}