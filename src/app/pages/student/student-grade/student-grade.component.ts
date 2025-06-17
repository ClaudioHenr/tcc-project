import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { GradeService } from '../services/grade/grade.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { StudentService } from '../services/student/student.service';

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
    private router: Router
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
    this.studentService.registerInGradeByCod(this.codGrade).subscribe({
      next: (data: any) => {
        console.log(data);
        alert("Estudante cadastrado na turma com sucesso!");
        this.fetch();
      },
      error: (err) => {
        console.log(err)
        alert("Não foi possível cadastrar o estudante na turma. Tente novamente mais tarde.");
      }
    })
  }

}
