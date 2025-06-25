export interface StudentRanking {
  studentId: number;
  studentName: string;
  totalCorrectAnswers: number;
  totalExercises: number;
  score: number; // Percentage of correct answers
}