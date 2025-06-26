package br.com.net.sqlab_backend.domain.student.dto;

public class StudentRankingDTO {
    private Long studentId;
    private String studentName;
    private int totalCorrectAnswers;
    private int totalExercises;
    private double score;

    public StudentRankingDTO(Long studentId, String studentName, int totalCorrectAnswers, int totalExercises, double score) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.totalCorrectAnswers = totalCorrectAnswers;
        this.totalExercises = totalExercises;
        this.score = score;
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getTotalCorrectAnswers() {
        return totalCorrectAnswers;
    }

    public void setTotalCorrectAnswers(int totalCorrectAnswers) {
        this.totalCorrectAnswers = totalCorrectAnswers;
    }

    public int getTotalExercises() {
        return totalExercises;
    }

    public void setTotalExercises(int totalExercises) {
        this.totalExercises = totalExercises;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}