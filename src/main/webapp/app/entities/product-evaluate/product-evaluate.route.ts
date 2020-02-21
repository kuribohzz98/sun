import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProductEvaluate, ProductEvaluate } from 'app/shared/model/product-evaluate.model';
import { ProductEvaluateService } from './product-evaluate.service';
import { ProductEvaluateComponent } from './product-evaluate.component';
import { ProductEvaluateDetailComponent } from './product-evaluate-detail.component';
import { ProductEvaluateUpdateComponent } from './product-evaluate-update.component';

@Injectable({ providedIn: 'root' })
export class ProductEvaluateResolve implements Resolve<IProductEvaluate> {
  constructor(private service: ProductEvaluateService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductEvaluate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((productEvaluate: HttpResponse<ProductEvaluate>) => {
          if (productEvaluate.body) {
            return of(productEvaluate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProductEvaluate());
  }
}

export const productEvaluateRoute: Routes = [
  {
    path: '',
    component: ProductEvaluateComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'ProductEvaluates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProductEvaluateDetailComponent,
    resolve: {
      productEvaluate: ProductEvaluateResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'ProductEvaluates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProductEvaluateUpdateComponent,
    resolve: {
      productEvaluate: ProductEvaluateResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'ProductEvaluates'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProductEvaluateUpdateComponent,
    resolve: {
      productEvaluate: ProductEvaluateResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'ProductEvaluates'
    },
    canActivate: [UserRouteAccessService]
  }
];
