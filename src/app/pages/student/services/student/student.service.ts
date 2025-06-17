import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { TokenService } from '../../../../core/services/token.service';
import { catchError, tap, throwError } from 'rxjs';

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
    return this.http.post(`${this.api}/students/grades/register`, {}, {
      params: { 
        "id": this.userId,
        "codGrade": cod
       }
    }).pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return throwError(() => err);
      })
    )
  }
}
