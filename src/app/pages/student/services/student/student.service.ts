import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { TokenService } from '../../../../core/services/token.service';
import { catchError, tap, throwError, Observable } from 'rxjs'; // Added Observable
import { StudentRanking } from '../../../../models/student-ranking.model'; // Import the new model

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
    console.log("id: " + this.userId)
    console.log(cod);
    const studentId = this.tokenService.getUserId(); // Get userId here as the service expects it in params

    if (!studentId) {
        return throwError(() => new Error('User ID not available. Cannot register in grade.'));
    }

    return this.http.post(
      `${this.api}/students/grades/register`,
      {}, // Empty body as params are in URL
      {
        params: {
          "id": studentId, // Use the retrieved studentId
          "codGrade": cod
        },
        responseType: 'text' // <--- ADD THIS LINE! Tell Angular to expect text.
      }
    ).pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        // Now 'err.error' will likely be the text response if it's an actual backend error message
        // or the JSON parsing error if the backend sent a non-text response type.
        // For 200 OK but parsing failure, err.error.text will contain the original response.
        console.error('Error during grade registration:', err);
        let errorMessage = 'Não foi possível cadastrar o estudante na turma. Tente novamente mais tarde.';

        // Check if the error is due to parsing but the status was OK
        if (err.status === 200 && err.error && err.error.text) {
          // This means backend sent text, but frontend tried to parse as JSON.
          // The 'text' property inside 'err.error' holds the actual string response.
          // This indicates a success message was intended, but parsed as an error.
          return throwError(() => new Error(err.error.text)); // Re-throw the actual success message as an error
        }
        else if (err.error instanceof ErrorEvent) {
          errorMessage = `Um erro de rede ocorreu: ${err.error.message}`;
        } else if (typeof err.error === 'string') {
          // Backend returned a plain string error message (e.g., from an @ExceptionHandler)
          errorMessage = err.error;
        } else if (err.error && typeof err.error === 'object' && err.error.message) {
          // Backend returned a JSON object with a 'message' field
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
   * @param gradeId The ID of the grade to filter by.
   * @param listId Optional. The ID of the list to filter by.
   * @returns An Observable of an array of StudentRanking.
   */
  getStudentRanking(gradeId: string, listId?: string): Observable<StudentRanking[]> {
    let params: { [key: string]: string } = { "gradeId": gradeId };
    if (listId) {
      params["listId"] = listId;
    }
    return this.http.get<StudentRanking[]>(`${this.api}/students/ranking`, { params })
      .pipe(
        tap((data) => console.log('Ranking data:', data)),
        catchError((err: HttpErrorResponse) => {
          console.error('Error fetching student ranking:', err);
          return throwError(() => new Error('Failed to fetch student ranking. Please try again later.'));
        })
      );
  }
}