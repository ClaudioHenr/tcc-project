import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentService } from '../../student/services/student/student.service'; // Use student service for ranking
import { StudentRanking } from '../../../models/student-ranking.model';
import { GradeService as ProfessorGradeService } from '../../professor/services/grade/grade.service'; // Import Professor's GradeService
import { ListexerciseService as ProfessorListService } from '../../professor/services/lists/listexercise.service'; // Import Professor's ListexerciseService
import { TokenService } from '../../../core/services/token.service';

@Component({
  selector: 'app-student-ranking',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-ranking.component.html',
  styleUrls: ['./student-ranking.component.css']
})
export class StudentRankingComponent implements OnInit {
  // Array to hold the student ranking data
  studentRankings: StudentRanking[] = [];
  // Selected grade and list for filtering
  selectedGradeId: string = '';
  selectedListId: string = '';

  // Arrays to hold available grades and lists for dropdowns
  grades: any[] = [];
  lists: any[] = [];

  constructor(
    private studentService: StudentService, // Service to fetch student ranking
    private professorGradeService: ProfessorGradeService, // Service to get professor's grades
    private professorListService: ProfessorListService, // Service to get lists for a grade
    private tokenService: TokenService, // Service to get current user ID
    private router: Router,
    private location: Location // For back navigation
  ) { }

  ngOnInit(): void {
    // Load all grades associated with the current professor when the component initializes
    this.loadGrades();
  }

  /**
   * Loads the grades associated with the currently logged-in professor.
   */
  loadGrades(): void {
    const professorId = this.tokenService.getUserId();
    if (professorId) {
      this.professorGradeService.getGrades().subscribe({
        next: (data: any) => {
          this.grades = data;
          // If there are grades, select the first one by default and load its lists
          if (this.grades.length > 0) {
            this.selectedGradeId = this.grades[0].id.toString();
            this.loadListsByGrade(this.selectedGradeId);
            this.fetchRanking(); // Fetch ranking for the initial selected grade
          }
        },
        error: (err) => {
          console.error('Error fetching grades:', err);
          alert('Failed to load grades. Please try again later.');
        }
      });
    }
  }

  /**
   * Loads lists of exercises for a selected grade.
   * @param gradeId The ID of the selected grade.
   */
  loadListsByGrade(gradeId: string): void {
    if (gradeId) {
      this.professorListService.getListExercises(gradeId).subscribe({
        next: (data: any) => {
          this.lists = data;
          // Reset selected list when grade changes
          this.selectedListId = '';
        },
        error: (err) => {
          console.error('Error fetching lists:', err);
          alert('Failed to load lists for the selected grade. Please try again later.');
        }
      });
    } else {
      this.lists = []; // Clear lists if no grade is selected
      this.selectedListId = '';
    }
  }

  /**
   * Fetches the student ranking based on the selected grade and optional list.
   */
  fetchRanking(): void {
    if (this.selectedGradeId) {
      this.studentService.getStudentRanking(this.selectedGradeId, this.selectedListId).subscribe({
        next: (data: StudentRanking[]) => {
          this.studentRankings = data.sort((a, b) => b.score - a.score); // Sort by score descending
        },
        error: (err) => {
          console.error('Error fetching ranking:', err);
          this.studentRankings = []; // Clear ranking on error
          alert(err.message || 'Failed to fetch student ranking. Please try again later.');
        }
      });
    } else {
      this.studentRankings = []; // Clear ranking if no grade is selected
    }
  }

  /**
   * Handles the change event for the grade dropdown.
   * @param event The change event.
   */
  onGradeChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.selectedGradeId = target.value;
    this.loadListsByGrade(this.selectedGradeId); // Load lists for the newly selected grade
    this.fetchRanking(); // Fetch ranking based on the new grade selection
  }

  /**
   * Handles the change event for the list dropdown.
   * @param event The change event.
   */
  onListChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.selectedListId = target.value;
    this.fetchRanking(); // Fetch ranking based on the new list selection
  }

  /**
   * Navigates back to the previous page.
   */
  back(): void {
    this.location.back();
  }
}