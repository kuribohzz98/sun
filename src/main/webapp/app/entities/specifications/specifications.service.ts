import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISpecifications } from 'app/shared/model/specifications.model';

type EntityResponseType = HttpResponse<ISpecifications>;
type EntityArrayResponseType = HttpResponse<ISpecifications[]>;

@Injectable({ providedIn: 'root' })
export class SpecificationsService {
  public resourceUrl = SERVER_API_URL + 'api/specifications';

  constructor(protected http: HttpClient) {}

  create(specifications: ISpecifications): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(specifications);
    return this.http
      .post<ISpecifications>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(specifications: ISpecifications): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(specifications);
    return this.http
      .put<ISpecifications>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISpecifications>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISpecifications[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(specifications: ISpecifications): ISpecifications {
    const copy: ISpecifications = Object.assign({}, specifications, {
      createdAt: specifications.createdAt && specifications.createdAt.isValid() ? specifications.createdAt.toJSON() : undefined,
      updatedAt: specifications.updatedAt && specifications.updatedAt.isValid() ? specifications.updatedAt.toJSON() : undefined
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
      res.body.forEach((specifications: ISpecifications) => {
        specifications.createdAt = specifications.createdAt ? moment(specifications.createdAt) : undefined;
        specifications.updatedAt = specifications.updatedAt ? moment(specifications.updatedAt) : undefined;
      });
    }
    return res;
  }
}
