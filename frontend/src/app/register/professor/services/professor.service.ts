import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProfessorService {
   private apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  registerProfessor(professor: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/professors/create`, professor)
      .pipe(
        tap(data => console.log("Resposta da API:", data)),
        catchError((err: HttpErrorResponse) => {
          console.error(err);
          return throwError(() => err.error);
        })
      );
  }
}
