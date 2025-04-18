// src/app/core/http/auth.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthStateService } from '../auth/auth-state.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authStateService = inject(AuthStateService);
  const currentUser = authStateService.currentUserValue;
  
  console.log('Request URL:', req.url);
  console.log('Request method:', req.method);
  
  if (currentUser && currentUser.token) {
    console.log('Adding token:', currentUser.token);
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${currentUser.token}`
      }
    });
  } else {
    console.log('No user or token available');
  }
  
  return next(req);
};