import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUserDeliveryInfo } from 'app/shared/model/user-delivery-info.model';

type EntityResponseType = HttpResponse<IUserDeliveryInfo>;
type EntityArrayResponseType = HttpResponse<IUserDeliveryInfo[]>;

@Injectable({ providedIn: 'root' })
export class UserDeliveryInfoService {
  public resourceUrl = SERVER_API_URL + 'api/user-delivery-infos';

  constructor(protected http: HttpClient) {}

  create(userDeliveryInfo: IUserDeliveryInfo): Observable<EntityResponseType> {
    return this.http.post<IUserDeliveryInfo>(this.resourceUrl, userDeliveryInfo, { observe: 'response' });
  }

  update(userDeliveryInfo: IUserDeliveryInfo): Observable<EntityResponseType> {
    return this.http.put<IUserDeliveryInfo>(this.resourceUrl, userDeliveryInfo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserDeliveryInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserDeliveryInfo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
