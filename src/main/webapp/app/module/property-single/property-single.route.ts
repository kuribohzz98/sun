import { Product } from 'app/shared/model/product.model';
import { HttpResponse } from '@angular/common/http';
import { flatMap } from 'rxjs/operators';
import { Observable, of, EMPTY } from 'rxjs';
import { ProductService } from '../../service/product.service';
import { IProduct } from '../../shared/model/product.model';
import { Resolve, Router, ActivatedRouteSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ProductResolve implements Resolve<IProduct> {
  constructor(private service: ProductService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProduct> | Observable<never> {
    const code = route.params['code'];
    if (code) {
      return this.service.findOneByCode(code).pipe(
        flatMap((product: HttpResponse<Product>) => {
          if (product.body) {
            return of(product.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Product());
  }
}
