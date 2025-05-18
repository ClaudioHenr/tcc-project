import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { catchError, tap } from 'rxjs';
import { TokenService } from '../../../../core/services/token.service';
import { AppRoles } from '../../../../core/constants/roles.const.enum';

@Injectable({
  providedIn: 'root'
})
export class GradeService {
  api: String = environment.apiUrl;

  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) { }

  getGrades() {
    const idUser: string = '1';
    const role: AppRoles | null = this.tokenService.getUserRole();
    return this.http.get(`${this.api}/students/grades`, {
      params: { "id": idUser }
    }).pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return err.error;
      })
    )
  }

}
