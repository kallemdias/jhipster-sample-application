import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPigeon } from '../pigeon.model';
import { PigeonService } from '../service/pigeon.service';

export const pigeonResolve = (route: ActivatedRouteSnapshot): Observable<null | IPigeon> => {
  const id = route.params['id'];
  if (id) {
    return inject(PigeonService)
      .find(id)
      .pipe(
        mergeMap((pigeon: HttpResponse<IPigeon>) => {
          if (pigeon.body) {
            return of(pigeon.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default pigeonResolve;
