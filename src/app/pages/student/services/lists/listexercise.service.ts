import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { TokenService } from '../../../../core/services/token.service';
import { catchError, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ListexerciseService {
  api: String = environment.apiUrl;

  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) { }

  getListExercises(id: string) {
    return this.http.get(`${this.api}/grades/listexercises`, {
      params: { "id": id }
    })
      .pipe(
        tap(),
        catchError((err: HttpErrorResponse) => {
          return err.error;
        }
      )
    )
  }

}
