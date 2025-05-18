import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { GradeService } from '../services/grade/grade.service';
import { Router } from '@angular/router';

type grade = {
  id: String,
  name: String,
  subject: String
}

@Component({
  selector: 'app-student-grade',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './student-grade.component.html',
  styleUrl: './student-grade.component.css'
})
export class StudentGradeComponent implements OnInit {
  grades!: grade[];

  constructor(
    private gradeService: GradeService,
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

}
