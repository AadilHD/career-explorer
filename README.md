<p align="center">
  <img src="A_flat-style_logo_for_Career_Explorer.png" alt="Career Explorer Logo" width="200"/>
</p>

# 💼 Career Explorer

Career Explorer is a full-stack web application that allows users to browse tech jobs and apply with their resume. It includes an AI-based resume scoring feature powered by Python and Flask to help applicants assess how well their resume matches a job description.

## 🚀 Features

- 🔍 **Job Board**: View a list of available tech jobs and filter by keyword.
- 📄 **Job Application**: Submit an application with name, email, and resume upload (PDF).
- 🤖 **AI Resume Scoring**: Integrates with a Python Flask API to calculate a match score using sentence similarity.
- 📬 **Email Confirmation**: Sends a confirmation email to the applicant after applying.
- 📊 **Admin Panel** (Optional): View all submitted applications.

## 🛠️ Tech Stack

- **Backend**: Java, Spring Boot
- **Frontend**: Thymeleaf (HTML/CSS)
- **AI Recommender**: Python, Flask, SentenceTransformers
- **Database**: H2 (in-memory)
- **Build Tool**: Maven

## ⚙️ How It Works

1. User uploads a resume when applying for a job.
2. The backend extracts text from the PDF and sends it to a local Flask server.
3. The Flask server compares the resume text with the job description using a sentence embedding model.
4. A similarity score is returned and stored with the application.
5. The user receives a confirmation with their match score.

## 🧪 Local Setup

### Prerequisites

- Java 17+
- Python 3.9+
- Maven
- Git

### Spring Boot Setup

```bash
git clone https://github.com/AadilHD/career-explorer.git
cd career-explorer
./mvnw spring-boot:run

Python Flask AI Scoring API
cd recommender  # or wherever the recommender_service.py is
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
python recommender_service.py


Access
Spring Boot: http://localhost:8080/jobs-page
Flask Recommender API: http://localhost:5000/score

👤 Author
Aadil H. Dhalla
Computer Engineering @ TMU (2027)
LinkedIn • GitHub
