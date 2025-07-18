import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  registerStudent(student: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/students/create`, student)
      .pipe(
        tap(data => console.log('Resposta da API (Student):', data)),
        catchError((err: HttpErrorResponse) => {
          console.error('Erro ao registrar estudante:', err);
          return throwError(() => err.error);
        })
      );
  }
}
