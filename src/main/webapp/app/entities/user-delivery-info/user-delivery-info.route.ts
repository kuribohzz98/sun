import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IUserDeliveryInfo, UserDeliveryInfo } from 'app/shared/model/user-delivery-info.model';
import { UserDeliveryInfoService } from './user-delivery-info.service';
import { UserDeliveryInfoComponent } from './user-delivery-info.component';
import { UserDeliveryInfoDetailComponent } from './user-delivery-info-detail.component';
import { UserDeliveryInfoUpdateComponent } from './user-delivery-info-update.component';

@Injectable({ providedIn: 'root' })
export class UserDeliveryInfoResolve implements Resolve<IUserDeliveryInfo> {
  constructor(private service: UserDeliveryInfoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserDeliveryInfo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((userDeliveryInfo: HttpResponse<UserDeliveryInfo>) => {
          if (userDeliveryInfo.body) {
            return of(userDeliveryInfo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserDeliveryInfo());
  }
}

export const userDeliveryInfoRoute: Routes = [
  {
    path: '',
    component: UserDeliveryInfoComponent,
    data: {
      // authorities: ['ROLE_USER'],
      pageTitle: 'UserDeliveryInfos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UserDeliveryInfoDetailComponent,
    resolve: {
      userDeliveryInfo: UserDeliveryInfoResolve
    },
    data: {
      // authorities: ['ROLE_USER'],
      pageTitle: 'UserDeliveryInfos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UserDeliveryInfoUpdateComponent,
    resolve: {
      userDeliveryInfo: UserDeliveryInfoResolve
    },
    data: {
      // authorities: ['ROLE_USER'],
      pageTitle: 'UserDeliveryInfos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UserDeliveryInfoUpdateComponent,
    resolve: {
      userDeliveryInfo: UserDeliveryInfoResolve
    },
    data: {
      // authorities: ['ROLE_USER'],
      pageTitle: 'UserDeliveryInfos'
    },
    canActivate: [UserRouteAccessService]
  }
];
