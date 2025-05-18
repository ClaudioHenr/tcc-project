import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, filter, Observable, tap, throwError } from 'rxjs';
import { environment } from '../../../../../environments/environment';

interface ResponseExercise {
  isCorrect: boolean;
  resultQuery: Array<Record<string, any>>
};

@Injectable({
  providedIn: 'root'
})
export class ExerciseService {
  private apiUrl: string = environment.apiUrl;

  constructor(
    private http: HttpClient
  ) { }

  getExercisesByListExerciseId(id: string) {
    return this.http.get(`${this.apiUrl}/api/exercise/list`, {
      params: { "id": id }
    })
    .pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        console.log(err);
        return throwError(() => err.error);
      })
    );
  }

  getExercise(id: string) {
    return this.http.get(`${this.apiUrl}/api/exercise/${id}`)
    .pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        console.log(err);
        return throwError(() => err.error);
      })
    );
  }

  sentAnswer(answer: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/api/exercise/solve`, answer)
      .pipe(
        tap(data => console.log("Retorno: ", data)),
        catchError((err: HttpErrorResponse) => {
          console.log(err);
          return throwError(() => err.error);
        })
      );
  }

}
