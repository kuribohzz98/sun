import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProductHistory } from 'app/shared/model/product-history.model';

type EntityResponseType = HttpResponse<IProductHistory>;
type EntityArrayResponseType = HttpResponse<IProductHistory[]>;

@Injectable({ providedIn: 'root' })
export class ProductHistoryService {
  public resourceUrl = SERVER_API_URL + 'api/product-histories';

  constructor(protected http: HttpClient) {}

  create(productHistory: IProductHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productHistory);
    return this.http
      .post<IProductHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(productHistory: IProductHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productHistory);
    return this.http
      .put<IProductHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProductHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProductHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(productHistory: IProductHistory): IProductHistory {
    const copy: IProductHistory = Object.assign({}, productHistory, {
      createdAt: productHistory.createdAt && productHistory.createdAt.isValid() ? productHistory.createdAt.toJSON() : undefined,
      updatedAt: productHistory.updatedAt && productHistory.updatedAt.isValid() ? productHistory.updatedAt.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? moment(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((productHistory: IProductHistory) => {
        productHistory.createdAt = productHistory.createdAt ? moment(productHistory.createdAt) : undefined;
        productHistory.updatedAt = productHistory.updatedAt ? moment(productHistory.updatedAt) : undefined;
      });
    }
    return res;
  }
}
