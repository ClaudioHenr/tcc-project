import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, tap } from 'rxjs';
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
    return this.http.get(`${this.api}/grades/list`, {
      params: { "id": this.userId }
    })
    .pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return err.error;
      })
    )
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
