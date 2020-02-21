import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'user-delivery-info',
        loadChildren: () => import('./user-delivery-info/user-delivery-info.module').then(m => m.SunUserDeliveryInfoModule)
      },
      {
        path: 'provider',
        loadChildren: () => import('./provider/provider.module').then(m => m.SunProviderModule)
      },
      {
        path: 'product-type',
        loadChildren: () => import('./product-type/product-type.module').then(m => m.SunProductTypeModule)
      },
      {
        path: 'product',
        loadChildren: () => import('./product/product.module').then(m => m.SunProductModule)
      },
      {
        path: 'specifications',
        loadChildren: () => import('./specifications/specifications.module').then(m => m.SunSpecificationsModule)
      },
      {
        path: 'product-history',
        loadChildren: () => import('./product-history/product-history.module').then(m => m.SunProductHistoryModule)
      },
      {
        path: 'payment',
        loadChildren: () => import('./payment/payment.module').then(m => m.SunPaymentModule)
      },
      {
        path: 'product-evaluate',
        loadChildren: () => import('./product-evaluate/product-evaluate.module').then(m => m.SunProductEvaluateModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class SunEntityModule {}
