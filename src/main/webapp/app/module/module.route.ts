import { ProductTypeResolve } from './product/product.route';
import { ProductResolve } from './property-single/property-single.route';
import { Route } from '@angular/router';
import { CartComponent } from './cart/cart.component';
import { PropertySingleComponent } from './property-single/property-single.component';
import { ProductComponent } from './product/product.component';

export const routes: Route[] = [
  {
    path: 'gio-hang',
    component: CartComponent
  },
  {
    path: 'san-pham/:sanpham',
    component: ProductComponent,
    resolve: {
      productType: ProductTypeResolve
    }
  },
  {
    path: 'thong-tin-san-pham/:code',
    component: PropertySingleComponent,
    resolve: {
      product: ProductResolve
    }
  }
];
