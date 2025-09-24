import {HttpEvent, HttpHandler, HttpHandlerFn, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {Observable} from "rxjs";
import {inject} from "@angular/core";
import {JwtService} from "../../services/jwt-service/jwt.service";


export const jwtInterceptor: HttpInterceptorFn = (req:HttpRequest<any>, next:HttpHandlerFn):Observable<HttpEvent<any>> => {

  const jwtService = inject(JwtService);

  const token: string | null = jwtService.getToken();
  const expiration: string | null = jwtService.getExpiration();

  if (token && expiration) {

    const now: number = new Date().getTime();


    if (now > Number(expiration)) {
      sessionStorage.removeItem("jwt_token");
      sessionStorage.removeItem("jwt_expiration")
      return next(req);
    }

    const cloned:HttpRequest<any> = req.clone({

      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    return next(cloned);
  }


  return next(req);
};
