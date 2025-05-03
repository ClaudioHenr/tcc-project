import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { AppRoles } from '../constants/roles.const.enum';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private readonly TOKEN_KEY = 'auth_token';

  constructor() { }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY) || sessionStorage.getItem(this.TOKEN_KEY);
  }

  saveToken(token: string, rememberMe: boolean = false): void {
    if (rememberMe) {
      localStorage.setItem(this.TOKEN_KEY, token);
    } else {
      sessionStorage.setItem(this.TOKEN_KEY, token);
    }
  }

  removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    sessionStorage.removeItem(this.TOKEN_KEY);
  }

  isTokenValid(): boolean {
    const token = this.getToken();
    if (!token) return false;
    
    try {
      const decoded = jwtDecode(token);
      return !!decoded && !this.isTokenExpired(decoded);
    } catch {
      return false;
    }
  }

  private isTokenExpired(decodedToken: any): boolean {
    if (!decodedToken.exp) return false;
    return Date.now() >= decodedToken.exp * 1000;
  }

  decodeToken(): any {
    const token = this.getToken();
    if (!token) return null;
    try {
      return jwtDecode(token);
    } catch (e) {
      console.error('Erro ao decodificar token:', e);
      return null;
    }
  }

  getUserRole(): AppRoles | null {
    const decoded = this.decodeToken();
    const role = decoded?.roles?.[0];
    return role ? role as AppRoles : null;
  }
}