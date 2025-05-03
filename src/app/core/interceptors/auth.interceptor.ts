import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../services/token.service';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  const excludedEndpoints = [
    '/auth/signin', 
    '/professors/create', 
    '/students/create'
  ];

  const isExcluded = excludedEndpoints.some(endpoint => 
    req.url.includes(endpoint)
  );
  
  if (isExcluded) {
    return next(req);
  }

  const token = tokenService.getToken();
  
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req).pipe(
    catchError((error) => {
      if (error.status === 401) {
        tokenService.removeToken();
        router.navigate(['/auth/signin']);
      }
      throw error;
    })
  );
};