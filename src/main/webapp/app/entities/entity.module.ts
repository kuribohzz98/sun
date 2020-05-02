import { UserRouteAccessService } from './../core/auth/user-route-access-service';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'user-delivery-info',
        data: {
          authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./user-delivery-info/user-delivery-info.module').then(m => m.SunUserDeliveryInfoModule)
      },
      {
        path: 'provider',
        data: {
          authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./provider/provider.module').then(m => m.SunProviderModule)
      },
      {
        path: 'product-type',
        data: {
          authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./product-type/product-type.module').then(m => m.SunProductTypeModule)
      },
      {
        path: 'product',
        data: {
          authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./product/product.module').then(m => m.SunProductModule)
      },
      {
        path: 'specifications',
        data: {
          authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./specifications/specifications.module').then(m => m.SunSpecificationsModule)
      },
      {
        path: 'product-history',
        data: {
          authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./product-history/product-history.module').then(m => m.SunProductHistoryModule)
      },
      {
        path: 'payment',
        data: {
          authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./payment/payment.module').then(m => m.SunPaymentModule)
      },
      {
        path: 'product-evaluate',
        data: {
          authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./product-evaluate/product-evaluate.module').then(m => m.SunProductEvaluateModule)
      },
      {
        path: 'photo',
        data: {
          authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./photo/photo.module').then(m => m.SunPhotoModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class SunEntityModule {}
