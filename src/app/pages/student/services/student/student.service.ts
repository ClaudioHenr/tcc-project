import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { TokenService } from '../../../../core/services/token.service';
import { catchError, tap, throwError, Observable, of } from 'rxjs'; // Adicionado 'of' para Observables estáticos
import { StudentRanking } from '../../../../models/student-ranking.model'; // Importa o modelo
import { delay } from 'rxjs/operators'; // Importa delay para simular latência de rede

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

        if (err.status === 200 && err.error && err.error.text) {
          return throwError(() => new Error(err.error.text));
        } else if (err.error instanceof ErrorEvent) {
          errorMessage = `Um erro de rede ocorreu: ${err.error.message}`;
        } else if (typeof err.error === 'string') {
          errorMessage = err.error;
        } else if (err.error && typeof err.error === 'object' && err.error.message) {
          errorMessage = err.error.message;
        } else if (err.status) {
          errorMessage = `Erro ${err.status}: ${err.statusText || 'Falha na comunicação com o servidor'}`;
        }
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  /**
   * Fetches the student ranking for a given grade ID and optionally a list ID.
   * Alterna entre dados mockados e o backend real com base em environment.mockData.
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
   * Gera dados mockados para o ranking de alunos.
   * @param gradeId O ID da turma para filtrar (usado para simular dados diferentes por turma).
   * @param listId Opcional. O ID da lista para filtrar (usado para simular dados diferentes por lista).
   * @returns Um Observable de um array de StudentRanking com dados estáticos.
   */
  private getMockStudentRanking(gradeId: string, listId?: string): Observable<StudentRanking[]> {
    console.log('Using mock data for student ranking.');

    let mockData: StudentRanking[] = [];

    // Lógica para gerar dados mockados com base nos IDs da turma e lista
    // Isso é um exemplo simples, você pode expandir para ter dados mais complexos
    if (gradeId === '1') { // Exemplo para 'Turma A'
      mockData = [
        { studentId: 101, studentName: 'João Silva', totalCorrectAnswers: 15, totalExercises: 20, score: 75.0 },
        { studentId: 102, studentName: 'Maria Oliveira', totalCorrectAnswers: 18, totalExercises: 20, score: 90.0 },
        { studentId: 103, studentName: 'Pedro Souza', totalCorrectAnswers: 12, totalExercises: 20, score: 60.0 },
        { studentId: 104, studentName: 'Ana Pereira', totalCorrectAnswers: 19, totalExercises: 20, score: 95.0 },
      ];
    } else if (gradeId === '2') { // Exemplo para 'Turma B'
      mockData = [
        { studentId: 201, studentName: 'Carlos Santos', totalCorrectAnswers: 8, totalExercises: 10, score: 80.0 },
        { studentId: 202, studentName: 'Fernanda Lima', totalCorrectAnswers: 7, totalExercises: 10, score: 70.0 },
      ];
    } else {
      mockData = []; // Nenhuma turma ou turma desconhecida
    }

    // Filtrar por lista, se um listId for fornecido
    if (listId) {
      if (listId === '10') { // Exemplo para 'Lista 1'
        mockData = mockData.filter(ranking => ranking.studentId % 2 === 0); // Exemplo: apenas alunos pares
      } else if (listId === '11') { // Exemplo para 'Lista 2'
        mockData = mockData.filter(ranking => ranking.studentId % 2 !== 0); // Exemplo: apenas alunos ímpares
      } else {
        mockData = []; // Nenhuma lista ou lista desconhecida
      }
    }

    // Simula uma pequena latência de rede
    return of(mockData).pipe(delay(500));
  }
}