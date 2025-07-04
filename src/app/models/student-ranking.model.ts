export interface StudentRanking {
  studentId: number;
  studentName: string;
  totalCorrectAnswers: number;
  totalExercisesAttempted: number;
  score: number; // Percentage of correct answers
}