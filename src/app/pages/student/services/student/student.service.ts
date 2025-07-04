import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { TokenService } from '../../../../core/services/token.service';
import { catchError, tap, throwError, Observable, of } from 'rxjs'; // Adicionado 'of'
import { StudentRanking } from '../../../../models/student-ranking.model';
import { delay } from 'rxjs/operators'; // Adicionado 'delay'

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  api: String = environment.apiUrl;
  userId = this.tokenService.getUserId();

  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) { }

  registerInGradeByCod(cod: string) {
    console.log("id: " + this.userId);
    console.log(cod);
    const studentId = this.tokenService.getUserId();

    if (!studentId) {
      return throwError(() => new Error('User ID not available. Cannot register in grade.'));
    }

    return this.http.post(
      `${this.api}/students/grades/register`,
      {},
      {
        params: {
          "id": studentId,
          "codGrade": cod
        },
        responseType: 'text'
      }
    ).pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        console.error('Error during grade registration:', err);
        let errorMessage = 'Não foi possível cadastrar o estudante na turma. Tente novamente mais tarde.';

        if (err.status === 200 && err.error && (typeof err.error === 'string' || (err.error instanceof Object && 'text' in err.error))) {
          return throwError(() => new Error(typeof err.error === 'string' ? err.error : (err.error as any).text));
        } else if (err.error instanceof ErrorEvent) {
          errorMessage = `Um erro de rede ocorreu: ${err.error.message}`;
        } else if (typeof err.error === 'string') {
          errorMessage = err.error;
        } else if (err.error && typeof err.error === 'object' && 'message' in err.error) {
          errorMessage = (err.error as any).message;
        } else if (err.status) {
          errorMessage = `Erro ${err.status}: ${err.statusText || 'Falha na comunicação com o servidor'}`;
        }
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  /**
   * Fetches the student ranking for a given grade ID and optionally a list ID.
   * Verifica `environment.mockData` para decidir entre dados mockados ou backend real.
   * @param gradeId O ID da turma para filtrar.
   * @param listId Opcional. O ID da lista para filtrar.
   * @returns Um Observable de um array de StudentRanking.
   */
  getStudentRanking(gradeId: string, listId?: string): Observable<StudentRanking[]> {
    if (environment.mockData) {
      // Se mockData for true, retorna dados mockados
      return this.getMockStudentRanking(gradeId, listId);
    } else {
      // Caso contrário, faz a requisição HTTP real
      let params: { [key: string]: string } = { "gradeId": gradeId };
      if (listId) {
        params["listId"] = listId;
      }
      return this.http.get<StudentRanking[]>(`${this.api}/students/ranking`, { params })
        .pipe(
          tap((data) => console.log('Ranking data (backend):', data)),
          catchError((err: HttpErrorResponse) => {
            console.error('Error fetching student ranking (backend):', err);
            return throwError(() => new Error('Failed to fetch student ranking. Please try again later.'));
          })
        );
    }
  }

  /**
   * Criado: Gera dados mockados para o ranking de alunos, com valores e nomes que fazem sentido
   * para testar a ordenação.
   * @param gradeId O ID da turma para filtrar (usado para simular dados diferentes por turma).
   * @param listId Opcional. O ID da lista para filtrar (usado para simular dados diferentes por lista).
   * @returns Um Observable de um array de StudentRanking com dados estáticos.
   */
  private getMockStudentRanking(gradeId: string, listId?: string): Observable<StudentRanking[]> {
    console.log('Usando dados mockados para o ranking de alunos.');

    let mockData: StudentRanking[] = [];

    // Dados de exemplo para diferentes turmas
    if (gradeId === '1') { // Turma de Banco de Dados (ex: 'Turma A - Banco de Dados I')
      mockData = [
        { studentId: 101, studentName: 'Ana Clara', totalCorrectAnswers: 18, totalExercisesAttempted: 25, score: 72.0 },
        { studentId: 102, studentName: 'João Pedro', totalCorrectAnswers: 18, totalExercisesAttempted: 20, score: 90.0 }, // Mesmo acerto, menor tentativa (deve vir antes da Ana)
        { studentId: 103, studentName: 'Maria Eduarda', totalCorrectAnswers: 15, totalExercisesAttempted: 18, score: 83.33 },
        { studentId: 104, studentName: 'Carlos Alberto', totalCorrectAnswers: 15, totalExercisesAttempted: 20, score: 75.0 }, // Mesmo acerto, maior tentativa (deve vir depois da Maria)
        { studentId: 105, studentName: 'Beatriz Costa', totalCorrectAnswers: 12, totalExercisesAttempted: 15, score: 80.0 },
        { studentId: 106, studentName: 'Fernando Luiz', totalCorrectAnswers: 12, totalExercisesAttempted: 16, score: 75.0 },
        { studentId: 107, studentName: 'Sofia Mendes', totalCorrectAnswers: 10, totalExercisesAttempted: 10, score: 100.0 }, // Menor acerto, mas 100% de pontuação em um contexto limitado
        { studentId: 108, studentName: 'Lucas Lima', totalCorrectAnswers: 10, totalExercisesAttempted: 12, score: 83.33 },
        { studentId: 109, studentName: 'Julia Santos', totalCorrectAnswers: 5, totalExercisesAttempted: 8, score: 62.5 },
        { studentId: 110, studentName: 'Ricardo Pereira', totalCorrectAnswers: 5, totalExercisesAttempted: 10, score: 50.0 },
      ];
    } else if (gradeId === '2') { // Turma de Web Design (ex: 'Turma B - Banco de Dados II')
      mockData = [
        { studentId: 201, studentName: 'Guilherme Silva', totalCorrectAnswers: 20, totalExercisesAttempted: 22, score: 90.91 },
        { studentId: 202, studentName: 'Isabela Souza', totalCorrectAnswers: 17, totalExercisesAttempted: 20, score: 85.0 },
        { studentId: 203, studentName: 'Pedro Rocha', totalCorrectAnswers: 17, totalExercisesAttempted: 21, score: 80.95 },
      ];
    } else {
      mockData = []; // Nenhuma turma ou turma desconhecida
    }

    // Filtrar por lista, se um listId for fornecido (mantendo a lógica simples de exemplo)
    if (listId) {
      if (listId === '10') { // Exemplo para 'Lista SQL Básico'
        // Filtra para mostrar apenas alguns alunos, simulando um ranking para uma lista específica
        mockData = mockData.filter(ranking => [101, 102, 107, 108, 201].includes(ranking.studentId));
      } else if (listId === '11') { // Exemplo para 'Lista Joins e Subqueries'
        mockData = mockData.filter(ranking => [103, 104, 105, 202, 203].includes(ranking.studentId));
      } else {
        mockData = []; // Nenhuma lista ou lista desconhecida
      }
    }

    // A ordenação final será feita no componente, mas uma pré-ordenação aqui ajuda na consistência do mock
    // Ordem: 1º Exercícios corretos (desc), 2º Menor Nº de tentativas (asc), 3º Notas (desc)
    mockData.sort((a, b) => {
      if (b.totalCorrectAnswers !== a.totalCorrectAnswers) {
        return b.totalCorrectAnswers - a.totalCorrectAnswers;
      }
      if (a.totalExercisesAttempted !== b.totalExercisesAttempted) {
        return a.totalExercisesAttempted - b.totalExercisesAttempted;
      }
      return b.score - a.score;
    });

    // Simula uma pequena latência de rede
    return of(mockData).pipe(delay(500));
  }
}