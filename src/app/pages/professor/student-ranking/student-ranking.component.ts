import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentService } from '../../student/services/student/student.service'; // Usar o serviço de estudante para ranking
import { StudentRanking } from '../../../models/student-ranking.model';
import { GradeService as ProfessorGradeService } from '../../professor/services/grade/grade.service'; // Importar o GradeService do Professor
import { ListexerciseService as ProfessorListService } from '../../professor/services/lists/listexercise.service'; // Importar o ListexerciseService do Professor
import { TokenService } from '../../../core/services/token.service';
import { environment } from '../../../../environments/environment'; // Importado para verificar o ambiente

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
      this.professorListService.getListExercises(gradeId).subscribe({
        next: (data: any) => {
          this.lists = data;
          // Reinicia a lista selecionada quando a turma muda
          this.selectedListId = '';
        },
        error: (err) => {
          console.error('Erro ao buscar listas:', err);
          alert('Falha ao carregar listas para a turma selecionada. Por favor, tente novamente mais tarde.');
        }
      });
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
      // Chama o serviço de estudante para obter o ranking
      this.studentService.getStudentRanking(this.selectedGradeId, this.selectedListId).subscribe({
        next: (data: StudentRanking[]) => {
          console.log(data);
          this.studentRankings = data.sort((a, b) => b.score - a.score); // Ordena pela pontuação em ordem decrescente
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
   * Lida com o evento de mudança para o dropdown de turmas.
   * @param event O evento de mudança.
   */
  onGradeChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.selectedGradeId = target.value;
    this.loadListsByGrade(this.selectedGradeId); // Carrega as listas para a turma recém-selecionada
    this.fetchRanking(); // Busca o ranking com base na nova seleção de turma
  }

  /**
   * Lida com o evento de mudança para o dropdown de listas.
   * @param event O evento de mudança.
   */
  onListChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.selectedListId = target.value;
    this.fetchRanking(); // Busca o ranking com base na nova seleção de lista
  }

  /**
   * Navega de volta para a página anterior.
   */
  back(): void {
    this.location.back();
  }
}