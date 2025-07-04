export interface StudentRanking {
  studentId: number;
  studentName: string;
  totalCorrectAnswers: number;
  // Alterado: Propriedade renomeada para corresponder ao DTO do backend
  totalExercisesAttempted: number;
  score: number; // Percentage of correct answers
}