import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { grade } from '../../../models/grade';
import { Router } from '@angular/router';

@Component({
  selector: 'app-grades-list',
  standalone: true,
  templateUrl: './grades.component.html',
  styleUrls: ['./grades.component.css'],
  imports: [CommonModule, FormsModule]
})
export class GradesComponent {
  grades = [
    { id: 1, name: 'Turma A - 2024', active: true },
    { id: 2, name: 'Turma B - 2024', active: false },
    { id: 3, name: 'Turma C - 2024', active: true }
  ];
  constructor(private router: Router) {}
  editGrade(id: number) {
    console.log(`Editar turma com ID: ${id}`);
  }

  toggleGradeStatus(id: number) {
    const grade = this.grades.find(g => g.id === id);
    if (grade) {
      grade.active = !grade.active;
      console.log(`Turma ${grade.name} agora está ${grade.active ? 'ativa' : 'inativa'}`);
    }
  }

    // Função para redirecionar para a página de alunos da turma
    viewStudents(gradeId: number): void {
      console.log('Visualizando alunos da turma com ID:', gradeId);
      this.router.navigate(['/grade-students', gradeId]);
    }
}