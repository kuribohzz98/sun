import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISpecifications, Specifications } from 'app/shared/model/specifications.model';
import { SpecificationsService } from './specifications.service';
import { SpecificationsComponent } from './specifications.component';
import { SpecificationsDetailComponent } from './specifications-detail.component';
import { SpecificationsUpdateComponent } from './specifications-update.component';

@Injectable({ providedIn: 'root' })
export class SpecificationsResolve implements Resolve<ISpecifications> {
  constructor(private service: SpecificationsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpecifications> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((specifications: HttpResponse<Specifications>) => {
          if (specifications.body) {
            return of(specifications.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Specifications());
  }
}

export const specificationsRoute: Routes = [
  {
    path: '',
    component: SpecificationsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Specifications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SpecificationsDetailComponent,
    resolve: {
      specifications: SpecificationsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Specifications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SpecificationsUpdateComponent,
    resolve: {
      specifications: SpecificationsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Specifications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SpecificationsUpdateComponent,
    resolve: {
      specifications: SpecificationsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Specifications'
    },
    canActivate: [UserRouteAccessService]
  }
];
