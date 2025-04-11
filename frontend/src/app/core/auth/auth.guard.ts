import { Injectable } from "@angular/core";
import { Router, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { AuthStateService } from "./auth-state.service";

@Injectable({
    providedIn: 'root'
})
export class AuthGuard {
    constructor(
        private authStateService: AuthStateService,
        private router: Router
    ) {}

    anActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        if (this.authStateService.isLoggedIn) {
          return true;
        }

        return this.router.createUrlTree(['/user/login']);
    }
}