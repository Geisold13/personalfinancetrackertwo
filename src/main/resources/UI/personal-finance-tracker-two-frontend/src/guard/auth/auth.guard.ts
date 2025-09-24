import {CanActivateFn, Router} from '@angular/router';
import {AuthService} from "../../services/auth-service/auth.service";
import {inject} from "@angular/core";
import {JwtService} from "../../services/jwt-service/jwt.service";

export const authGuard: CanActivateFn = (route, state) => {

  const jwtService = inject(JwtService);
  const router = inject(Router);

  if (jwtService.isTokenValid()) {

    return true;
  }

  return router.parseUrl('/auth');

};
