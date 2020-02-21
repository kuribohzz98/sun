import { ProductTypeService } from './../../service/product-type.service';
import { ProductType } from 'app/shared/model/product-type.model';
import { IProductType } from '../../shared/model/product-type.model';
import { Product } from 'app/shared/model/product.model';
import { HttpResponse } from '@angular/common/http';
import { flatMap } from 'rxjs/operators';
import { Observable, of, EMPTY } from 'rxjs';
import { ProductService } from '../../service/product.service';
import { IProduct } from '../../shared/model/product.model';
import { Resolve, Router, ActivatedRouteSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ProductTypeResolve implements Resolve<IProductType> {
  constructor(private productTypeService: ProductTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductType> | Observable<never> {
    const code = route.params['sanpham'];
    if (code) {
      return this.productTypeService.query({ page: 0, size: 100 }).pipe(
        flatMap(productType => {
          if (productType.body) {
            const path = productType.body.find(productType => productType.code == code);
            if (path) {
              return of(path);
            } else {
              this.router.navigate(['404']);
              return EMPTY;
            }
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
