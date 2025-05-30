import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
    selector: 'app-grade-register',
    standalone: true,
    imports: [FormsModule],
    templateUrl: './grade-register.component.html',
    styleUrl: './grade-register.component.css'
})

export class GradeRegisterComponent {
  constructor(private router: Router) {};
  grade = {
    name: '',
    code: ''
  };

  generateCode() {
     // Aqui talvez tenha que implementar um endpoint no backend que retorne esse codigo, pois se gerar apenas pelo front pode repetir.
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    const code = Array.from({ length: 6 }, () => chars[Math.floor(Math.random() * chars.length)]).join('');
    this.grade.code = code;
  }

  createGrade() {
    console.log('Turma cadastrada:', this.grade);
    alert(`Turma "${this.grade.name}" cadastrada com código ${this.grade.code}`);
  }

  goToGradeRegister() {
    this.router.navigate(['/professor/grade-register']);
  }
}