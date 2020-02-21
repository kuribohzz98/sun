import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProductType, ProductType } from 'app/shared/model/product-type.model';
import { ProductTypeService } from './product-type.service';
import { ProductTypeComponent } from './product-type.component';
import { ProductTypeDetailComponent } from './product-type-detail.component';
import { ProductTypeUpdateComponent } from './product-type-update.component';

@Injectable({ providedIn: 'root' })
export class ProductTypeResolve implements Resolve<IProductType> {
  constructor(private service: ProductTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((productType: HttpResponse<ProductType>) => {
          if (productType.body) {
            return of(productType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProductType());
  }
}

export const productTypeRoute: Routes = [
  {
    path: '',
    component: ProductTypeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      defaultSort: 'id,asc',
      pageTitle: 'ProductTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProductTypeDetailComponent,
    resolve: {
      productType: ProductTypeResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'ProductTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProductTypeUpdateComponent,
    resolve: {
      productType: ProductTypeResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'ProductTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProductTypeUpdateComponent,
    resolve: {
      productType: ProductTypeResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'ProductTypes'
    },
    canActivate: [UserRouteAccessService]
  }
];
