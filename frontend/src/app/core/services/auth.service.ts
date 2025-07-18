import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthResponse } from '../models/auth-response.model';
import { environment } from '../../../environments/environment';
import { TokenService } from './token.service';
import { Router } from '@angular/router';
import { AppRoles } from '../constants/roles.const.enum';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private tokenService = inject(TokenService);
  private router = inject(Router);
  private readonly API_URL = `${environment.apiUrl}/auth`;



  login(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/signin`, { email, password }).pipe(
      tap({
        next: (response) => {
          this.tokenService.saveToken(response.token);
          const userRole = this.tokenService.getUserRole();
          if (userRole === AppRoles.PROFESSOR) {
            this.router.navigate(['/professor/home']);
          } else if (userRole === AppRoles.STUDENT) {
            this.router.navigate(['/student/home']);
          }
        },
        error: (err) => {
          console.error('Login error:', err);
          throw err;
        }
      })
    );
  }

  logout(): void {
    this.tokenService.removeToken();
    this.router.navigate(['/auth/signin']);
  }

  isAuthenticated(): boolean {
    return this.tokenService.isTokenValid();
  }
}