import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { GradeService } from '../services/grade/grade.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { StudentService } from '../services/student/student.service';
import { TokenService } from '../../../core/services/token.service'; // <--- Import TokenService

type grade = {
  id: String,
  name: String,
  subject: String
}

@Component({
  selector: 'app-student-grade',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './student-grade.component.html',
  styleUrl: './student-grade.component.css'
})
export class StudentGradeComponent implements OnInit {
  grades!: grade[];
  codGrade!: string;

  constructor(
    private gradeService: GradeService,
    private studentService: StudentService,
    private router: Router,
    private tokenService: TokenService // <--- Inject TokenService
  ) {}

  ngOnInit(): void {
    this.fetch();
  }

  fetch() {
    this.gradeService.getGrades().subscribe({
      next: (data: any) => {
        this.grades = data;
        console.log(data);
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  viewLists(id: String) {
    console.log("id lista: " + id);
    this.router.navigate(['student/lists', id]);
  }

  registerInGrade() {
    const studentId = this.tokenService.getUserId(); // <--- GET THE LOGGED-IN STUDENT ID
    if (!studentId) {
      alert('Não foi possível obter o ID do usuário. Por favor, faça login novamente.');
      // Optionally, redirect to login: this.router.navigate(['/auth/signin']);
      return;
    }

    if (!this.codGrade || this.codGrade.trim() === '') {
      alert('Por favor, digite o código da turma.');
      return;
    }

    // Call the service with the actual studentId and the grade code
    this.studentService.registerInGradeByCod(this.codGrade).subscribe({ // Service already gets userId internally
      next: (data: any) => {
        console.log(data);
        alert("Estudante cadastrado na turma com sucesso!");
        this.codGrade = ''; // Clear the input field
        this.fetch(); // Refresh the list of grades
      },
      error: (err) => {
        console.log(err);
        alert(err.message || "Não foi possível cadastrar o estudante na turma. Tente novamente mais tarde.");
      }
    })
  }
}