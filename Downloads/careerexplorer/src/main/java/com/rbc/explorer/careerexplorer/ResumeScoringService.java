package com.rbc.explorer.careerexplorer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ResumeScoringService {

    public int calculateMatchScore(File resumeFile, List<String> requiredSkills) {
        try (PDDocument document = PDDocument.load(resumeFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document).toLowerCase();

            int matchedSkills = 0;
            for (String skill : requiredSkills) {
                if (text.contains(skill.toLowerCase())) {
                    matchedSkills++;
                }
            }

            return (int) ((matchedSkills / (double) requiredSkills.size()) * 100);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}