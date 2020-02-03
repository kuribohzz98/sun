import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProductEvaluate } from 'app/shared/model/product-evaluate.model';

type EntityResponseType = HttpResponse<IProductEvaluate>;
type EntityArrayResponseType = HttpResponse<IProductEvaluate[]>;

@Injectable({ providedIn: 'root' })
export class ProductEvaluateService {
  public resourceUrl = SERVER_API_URL + 'api/product-evaluates';

  constructor(protected http: HttpClient) {}

  create(productEvaluate: IProductEvaluate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productEvaluate);
    return this.http
      .post<IProductEvaluate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(productEvaluate: IProductEvaluate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productEvaluate);
    return this.http
      .put<IProductEvaluate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProductEvaluate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProductEvaluate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(productEvaluate: IProductEvaluate): IProductEvaluate {
    const copy: IProductEvaluate = Object.assign({}, productEvaluate, {
      createdAt: productEvaluate.createdAt && productEvaluate.createdAt.isValid() ? productEvaluate.createdAt.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((productEvaluate: IProductEvaluate) => {
        productEvaluate.createdAt = productEvaluate.createdAt ? moment(productEvaluate.createdAt) : undefined;
      });
    }
    return res;
  }
}
