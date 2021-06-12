import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFooTable, FooTable } from '../foo-table.model';
import { FooTableService } from '../service/foo-table.service';

@Injectable({ providedIn: 'root' })
export class FooTableRoutingResolveService implements Resolve<IFooTable> {
  constructor(protected service: FooTableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFooTable> | Observable<never> {
    const id = route.params['code'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fooTable: HttpResponse<FooTable>) => {
          if (fooTable.body) {
            return of(fooTable.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FooTable());
  }
}
