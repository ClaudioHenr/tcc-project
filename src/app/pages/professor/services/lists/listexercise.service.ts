import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { tap, catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ListexerciseService {
  api: String = environment.apiUrl;
  
  constructor(
    private http: HttpClient
  ) { }

  createListExercise(gradeId: string, data: any) {
    return this.http.post(`${this.api}/api/listexercise/create`, data, {
      params: { "id": gradeId }
    }).pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return err.error;
      })
    )
  }

  getListExerciseById(listId: string) {
    return this.http.get(`${this.api}/api/listexercise/${listId}`).pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return err.error;
      })
    )
  }

  getListExercises(gradeId: string) {
    return this.http.get(`${this.api}/grades/listexercises`, {
      params: { "id": gradeId }
    }).pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return err.error;
      })
    )
  }

}
