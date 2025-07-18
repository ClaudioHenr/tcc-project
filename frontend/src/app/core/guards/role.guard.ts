import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { TokenService } from '../services/token.service';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
//import { AppRoles } from '../constants/roles.const';

export const roleGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);
  const token = tokenService.getToken();

  if (!token) {
    router.navigate(['/auth/signin']);
    return false;
  }

  try {
    const decodedToken: any = jwtDecode(token);
    const requiredRole = route.data['role']

    if (!decodedToken.roles || !decodedToken.roles.includes(requiredRole)) {
      router.navigate(['/unauthorized']);
      return false;
    }
    
    return true;
  } catch (e) {
    console.error('Token inválido', e);
    router.navigate(['/auth/signin']);
    return false;
  }
};