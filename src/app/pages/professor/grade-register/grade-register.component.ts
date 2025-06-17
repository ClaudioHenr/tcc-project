import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { GradeService } from '../services/grade/grade.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-grade-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './grade-register.component.html',
  styleUrl: './grade-register.component.css'
})

export class GradeRegisterComponent {

  constructor(
    private router: Router,
    private gradeService: GradeService, 
    private location: Location
  ) {};

  grade = {
    name: '',
    subject: '',
    cod: ''
  };

  codGrade?: string;

  createGrade() {
    this.gradeService.create(this.grade).subscribe({
      next: (data: any) => {
        this.codGrade = data.cod;
        alert(`Turma "${this.grade.name}" cadastrada com sucesso`);
      }, error: (err: any) => {
        alert(`Erro ao cadastrar turma`);
      }
    })
  }

  goToGradeRegister() {
    this.router.navigate(['/professor/grade-register']);
  }

  cancel() {
    this.location.back();
  }
}