import { createRequestOption } from 'app/shared/util/request-util';
import { IProductEvaluate } from './../shared/model/product-evaluate.model';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { BaseService } from './base.service';

@Injectable({ providedIn: 'root' })
export class ProductEvaluateService extends BaseService<IProductEvaluate> {
  resourceUrl: string = 'api/product-evaluates';

  findAllByProductId(productId: string, pageable?: { page: number; size: number }): Observable<HttpResponse<IProductEvaluate>> {
    const params = createRequestOption({ productId, ...pageable });
    return this.http
      .get<IProductEvaluate>(this.getUrl() + '/get', { params, observe: 'response' })
      .pipe(map((res: HttpResponse<IProductEvaluate>) => this.convertDateFromServer(res)));
  }
}
