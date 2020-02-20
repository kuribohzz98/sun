import { SERVER_API_URL } from './../app.constants';
import { createRequestOption } from './../shared/util/request-util';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import * as moment from 'moment';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export abstract class BaseService<T> {
  abstract resourceUrl: string;

  constructor(protected http: HttpClient) {}

  protected getUrl(): string {
    return SERVER_API_URL + this.resourceUrl;
  }

  create(entity: T): Observable<HttpResponse<T>> {
    const copy = this.convertDateFromClient(entity);
    return this.http
      .post<T>(this.getUrl(), copy, { observe: 'response' })
      .pipe(map((res: HttpResponse<T>) => this.convertDateFromServer(res)));
  }

  update(entity: T): Observable<HttpResponse<T>> {
    const copy = this.convertDateFromClient(entity);
    return this.http
      .put<T>(this.getUrl(), copy, { observe: 'response' })
      .pipe(map((res: HttpResponse<T>) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<HttpResponse<T>> {
    return this.http
      .get<T>(`${this.getUrl()}/${id}`, { observe: 'response' })
      .pipe(map((res: HttpResponse<T>) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<T[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<T[]>(this.getUrl(), { params: options, observe: 'response' })
      .pipe(map((res: HttpResponse<T[]>) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.getUrl()}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(entity: T): T {
    const objTemp = {} as any;
    if (entity['createdAt'] && entity['createdAt'].isValid()) {
      objTemp.createdAt = entity['createdAt'].toJSON();
    }
    if (entity['updatedAt'] && entity['updatedAt'].isValid()) {
      objTemp.updatedAt = entity['updatedAt'].toJSON();
    }
    const copy: T = Object.assign({}, entity, objTemp);
    return copy;
  }

  protected convertDateFromServer(res: HttpResponse<T>): HttpResponse<T> {
    if (res.body) {
      res.body['createdAt'] = res.body['createdAt'] ? moment(res.body['createdAt']) : undefined;
      res.body['updatedAt'] = res.body['updatedAt'] ? moment(res.body['updatedAt']) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: HttpResponse<T[]>): HttpResponse<T[]> {
    if (res.body) {
      res.body.forEach((entity: T) => {
        entity['createdAt'] = entity['createdAt'] ? moment(entity['createdAt']) : undefined;
        entity['updatedAt'] = entity['updatedAt'] ? moment(entity['updatedAt']) : undefined;
      });
    }
    return res;
  }
}
