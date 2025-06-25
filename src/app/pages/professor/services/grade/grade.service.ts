import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs'; // Make sure throwError is imported
import { environment } from '../../../../../environments/environment';
import { TokenService } from '../../../../core/services/token.service';

@Injectable({
  providedIn: 'root'
})
export class GradeService {
  api: String = environment.apiUrl;
  userId = this.tokenService.getUserId();

  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) { }

  getGrades() {
    const id = this.userId;
    // IMPORTANT: Check if userId is available before making the request
    if (!id) {
      // If no user ID (e.g., no token), immediately throw an error as an Observable
      return throwError(() => new Error('User ID not found. Please log in again.'));
    }

    return this.http.get(`${this.api}/grades/list`, {
      params: { "id": id }
    })
    .pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        // The `catchError` operator MUST return an Observable.
        // Your previous `return err.error;` was incorrect because `err.error`
        // could be a string, null, or a plain object, not necessarily an Observable.

        console.error('Error in GradeService (Frontend) getGrades:', err);

        let errorMessage = 'Erro desconhecido ao carregar turmas.';
        if (err.error instanceof ErrorEvent) {
          // Client-side error
          errorMessage = `Um erro ocorreu: ${err.error.message}`;
        } else if (typeof err.error === 'string') {
          // Backend returned a plain string error message
          errorMessage = err.error;
        } else if (err.error && typeof err.error === 'object' && err.error.message) {
          // Backend returned a JSON object with a 'message' field
          errorMessage = err.error.message;
        } else if (err.status) {
          // Server-side error, but no specific message in error.error
          errorMessage = `Erro ${err.status}: ${err.statusText || 'Falha na comunicação com o servidor'}`;
          if (err.status === 403) {
            errorMessage = 'Acesso negado. Você não tem permissão para ver as turmas.';
          } else if (err.status === 401) {
            errorMessage = 'Sessão expirada ou não autenticada. Por favor, faça login novamente.';
            // Optionally, force logout here if it's a 401
            // this.tokenService.removeToken();
            // this.router.navigate(['/auth/signin']); // Requires Router import
          }
        }
        
        // Always return an Observable using throwError() from RxJS
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  create(data: any) {
    return this.http.post(`${this.api}/grades/create`, data, {
      params: { "id": this.userId }
    }).pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return err.error;
      })
    )
  }

}
