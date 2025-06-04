import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, tap } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GradeService {
  api: String = environment.apiUrl;
  
  constructor(
    private http: HttpClient
  ) { }

  create(data: any) {
    return this.http.post(`${this.api}/grades/create`, data).pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return err.error;
      })
    )
  }

}
