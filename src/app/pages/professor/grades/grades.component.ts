import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { GradeService } from '../services/grade/grade.service';

@Component({
  selector: 'app-grades-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './grades.component.html',
  styleUrls: ['./grades.component.css']
})
export class GradesComponent implements OnInit {
  grades: any = [];

  constructor(
    private gradeService: GradeService,
    private router: Router,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.getGrades();
  }

  getGrades() {
    if (!this.gradeService) {
      return;
    }
    this.gradeService.getGrades().subscribe({
      next: (data: any) => {
        this.grades = data;
        console.log(data)
      }, error: (err: any) => {
        alert("Erro ao carregar turmas, tente novamente mais tarde");
      }
    })
  }

  editGrade(id: number) {
    console.log(`Editar turma com ID: ${id}`);
  }

  // Função para redirecionar para a página de alunos da turma
  viewStudents(gradeId: number): void {
    console.log('Visualizando alunos da turma com ID:', gradeId);
    this.router.navigate(['/grade-students', gradeId]);
  }

  viewLists(gradeId: number): void {
    this.router.navigate(['/professor/lists', gradeId]);
  }
  goToGradeRegister() {
    this.router.navigate(['/professor/grade-register']);
  }
  goToListExerciseRegister(gradeId: number): void {
    this.router.navigate(['/professor/list-register', gradeId], { queryParams: { isEdit: false } });
  }
  
  goToStudentRanking(): void {
    this.router.navigate(['/professor/student-ranking']);
  }

  goToManageStudents() {
    this.router.navigate(['/professor/grade-students']);
  }

  deleteGrade() {
    
  }

  back(): void {
    this.location.back();
  }

}