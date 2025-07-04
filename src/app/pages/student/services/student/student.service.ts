import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { TokenService } from '../../../../core/services/token.service';
import { catchError, tap, throwError, Observable } from 'rxjs';
import { StudentRanking } from '../../../../models/student-ranking.model';

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
   * Agora, este método sempre fará uma requisição HTTP real, pois `environment.mockData` será `false`.
   * @param gradeId O ID da turma para filtrar.
   * @param listId Opcional. O ID da lista para filtrar.
   * @returns Um Observable de um array de StudentRanking.
   */
  getStudentRanking(gradeId: string, listId?: string): Observable<StudentRanking[]> {
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