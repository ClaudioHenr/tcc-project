package br.com.net.sqlab_backend.domain.student.dto;

public class StudentRankingDTO {

    private Long studentId;
    private String studentName;
    private int totalCorrectAnswers;
    private int totalExercisesAttempted; // Alterado: Nome do campo padronizado para camelCase
    private double score;

    public StudentRankingDTO(Long studentId, String studentName, int totalCorrectAnswers, int totalExercisesAttempted, double score) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.totalCorrectAnswers = totalCorrectAnswers;
        this.totalExercisesAttempted = totalExercisesAttempted;
        this.score = score;
    }

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

    // Alterado: Getter para o campo padronizado
    public int getTotalExercisesAttempted() {
        return totalExercisesAttempted;
    }

    // Alterado: Setter para o campo padronizado
    public void setTotalExercisesAttempted(int totalExercisesAttempted) {
        this.totalExercisesAttempted = totalExercisesAttempted;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}