import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentService } from '../../student/services/student/student.service'; 
import { StudentRanking } from '../../../models/student-ranking.model';
import { GradeService as ProfessorGradeService } from '../../professor/services/grade/grade.service'; 
import { ListexerciseService as ProfessorListService } from '../../professor/services/lists/listexercise.service'; 
import { TokenService } from '../../../core/services/token.service';
import { environment } from '../../../../environments/environment'; 

@Component({
  selector: 'app-student-ranking',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-ranking.component.html',
  styleUrls: ['./student-ranking.component.css']
})
export class StudentRankingComponent implements OnInit {
  // Array para armazenar os dados de ranking dos alunos
  studentRankings: StudentRanking[] = [];
  // ID da turma e da lista selecionados para filtragem
  selectedGradeId: string = '';
  selectedListId: string = '';

  // Arrays para armazenar as turmas e listas disponíveis para os dropdowns
  grades: any[] = [];
  lists: any[] = [];

  constructor(
    private studentService: StudentService, // Serviço para buscar o ranking de alunos
    private professorGradeService: ProfessorGradeService, // Serviço para obter as turmas do professor
    private professorListService: ProfessorListService, // Serviço para obter as listas de exercícios de uma turma
    private tokenService: TokenService, // Serviço para obter o ID do usuário atual
    private router: Router,
    private location: Location // Para navegação de retorno
  ) { }

  ngOnInit(): void {
    // Carrega todas as turmas associadas ao professor logado ao inicializar o componente
    this.loadGrades();
  }

  /**
   * Carrega as turmas associadas ao professor atualmente logado.
   */
  loadGrades(): void {
    const professorId = this.tokenService.getUserId();
    if (professorId) {
      // Chama o serviço de turma do professor para obter as turmas
      this.professorGradeService.getGrades().subscribe({
        next: (data: any) => {
          this.grades = data;
          // Se houver turmas, seleciona a primeira por padrão e carrega suas listas
          if (this.grades.length > 0) {
            this.selectedGradeId = this.grades[0].id.toString();
            this.loadListsByGrade(this.selectedGradeId);
            this.fetchRanking(); // Busca o ranking para a turma inicial selecionada
          }
        },
        error: (err) => {
          console.error('Erro ao buscar turmas:', err);
          alert('Falha ao carregar turmas. Por favor, tente novamente mais tarde.');
        }
      });
    }
  }

  /**
   * Carrega as listas de exercícios para uma turma selecionada.
   * @param gradeId O ID da turma selecionada.
   */
  loadListsByGrade(gradeId: string): void {
    if (gradeId) {
      // Chama o serviço de lista de exercícios do professor para obter as listas da turma
      // Verifica se mockData está ativo e usa o mock correspondente
      if (environment.mockData) {
        this.professorListService.getListExercises(gradeId).subscribe({
          next: (data: any) => {
            this.lists = data;
            this.selectedListId = ''; // Reinicia a lista selecionada quando a turma muda
          },
          error: (err) => {
            console.error('Erro ao buscar listas (mock):', err);
            alert('Falha ao carregar listas mockadas. Por favor, tente novamente mais tarde.');
          }
        });
      } else {
        this.professorListService.getListExercises(gradeId).subscribe({
          next: (data: any) => {
            this.lists = data;
            this.selectedListId = ''; // Reinicia a lista selecionada quando a turma muda
          },
          error: (err) => {
            console.error('Erro ao buscar listas (backend):', err);
            alert('Falha ao carregar listas para a turma selecionada. Por favor, tente novamente mais tarde.');
          }
        });
      }
    } else {
      this.lists = []; // Limpa as listas se nenhuma turma for selecionada
      this.selectedListId = '';
    }
  }

  /**
   * Busca o ranking dos alunos com base na turma selecionada e na lista opcional.
   */
  fetchRanking(): void {
    if (this.selectedGradeId) {
      // O StudentService já contém a lógica de mockData, então apenas o chamamos
      this.studentService.getStudentRanking(this.selectedGradeId, this.selectedListId).subscribe({
        next: (data: StudentRanking[]) => {
          // O backend já fará a ordenação conforme as regras (Corretos desc, Tentativas asc, Score desc)
          // mas reordenamos aqui para garantir a consistência no frontend, caso a ordem mude no backend.
          this.studentRankings = data.sort((a, b) => {
            // 1º: Exercícios corretos (decrescente)
            if (b.totalCorrectAnswers !== a.totalCorrectAnswers) {
              return b.totalCorrectAnswers - a.totalCorrectAnswers;
            }
            // 2º: Menor Número de tentativas (crescente)
            if (a.totalExercisesAttempted !== b.totalExercisesAttempted) {
              return a.totalExercisesAttempted - b.totalExercisesAttempted;
            }
            // 3º: Pontuação (decrescente)
            return b.score - a.score;
          });
        },
        error: (err) => {
          console.error('Erro ao buscar ranking:', err);
          this.studentRankings = []; // Limpa o ranking em caso de erro
          alert(err.message || 'Falha ao buscar ranking de alunos. Por favor, tente novamente mais tarde.');
        }
      });
    } else {
      this.studentRankings = []; // Limpa o ranking se nenhuma turma for selecionada
    }
  }

  /**
   * Navega de volta para a página anterior.
   */
  back(): void {
    this.location.back();
  }
}