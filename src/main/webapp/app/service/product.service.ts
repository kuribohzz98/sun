import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IProduct } from './../shared/model/product.model';
import { Injectable } from '@angular/core';
import { BaseService } from './base.service';

@Injectable({ providedIn: 'root' })
export class ProductService extends BaseService<IProduct> {
  resourceUrl: string = 'api/products';

  findOneByCode(code: string): Observable<HttpResponse<IProduct>> {
    return this.http
      .get<IProduct>(this.getUrl() + '/getOne', { params: { code }, observe: 'response' })
      .pipe(map((res: HttpResponse<IProduct>) => this.convertDateFromServer(res)));
  }
}
