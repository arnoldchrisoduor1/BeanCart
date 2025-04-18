import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthStateService } from '../auth/auth-state.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authStateService = inject(AuthStateService);
  const currentUser = authStateService.currentUserValue;

  // Add URLs here that should be excluded from the auth header
  const excludedUrls = [
    // exclude all S3 signed URLs (or you can use a regex if dynamic)
    's3.amazonaws.com', // or use 'https://s3.amazonaws.com' depending on the format
    'amazonaws.com', // wildcard approach
  ];

  // Check if the URL should be excluded
  const shouldExclude = excludedUrls.some(url => req.url.includes(url));

  if (!shouldExclude && currentUser && currentUser.token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${currentUser.token}`
      }
    });
  }

  return next(req);
};
