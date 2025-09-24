import {CanActivateFn, Router} from '@angular/router';
import {JwtService} from "../../services/jwt-service/jwt.service";
import {inject} from "@angular/core";

export const signinGuard: CanActivateFn = (route, state) => {

  const jwtService = inject(JwtService);
  const router = inject(Router);


  if (!jwtService.isTokenValid()) {

    return true;
  }

  return router.parseUrl('/dashboard');
};
