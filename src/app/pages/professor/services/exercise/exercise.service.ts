import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { tap, catchError, throwError } from 'rxjs';
import { TokenService } from '../../../../core/services/token.service';

@Injectable({
  providedIn: 'root'
})
export class ExerciseService {
  api: String = environment.apiUrl;
  userId = this.tokenService.getUserId();
  
  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) { }

  create(data: any) {
    return this.http.post(`${this.api}/api/exercise/create`, data)
    .pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err);
      })
    )
  }

  getExercises() {
    return this.http.get(`${this.api}/api/exercise/professor/list`, {
      params: { "id": this.userId }
    })
    .pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err);
      })
    )
  }

  getExercisesByProfessorId() {
    return this.http.get(`${this.api}/api/exercise/professor/list`, {
      params: { "id": this.userId }
    })
    .pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err);
      })
    )
  }

  getExercisesByListId(id: string) {
    return this.http.get(`${this.api}/api/exercise/listexercise/list`, {
      params: { "id": id }
    })
    .pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err);
      })
    )
  }

  deleteExercise(id: string) {
    return this.http.delete(`${this.api}/api/exercise/${id}`)
    .pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err);
      })
    )
  }
}
