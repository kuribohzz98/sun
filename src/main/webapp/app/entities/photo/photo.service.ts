import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPhoto } from 'app/shared/model/photo.model';

type EntityResponseType = HttpResponse<IPhoto>;
type EntityArrayResponseType = HttpResponse<IPhoto[]>;

@Injectable({ providedIn: 'root' })
export class PhotoService {
  public resourceUrl = SERVER_API_URL + 'api/photos';

  constructor(protected http: HttpClient) {}

  create(photo: IPhoto): Observable<EntityResponseType> {
    return this.http.post<IPhoto>(this.resourceUrl, photo, { observe: 'response' });
  }

  update(photo: IPhoto): Observable<EntityResponseType> {
    return this.http.put<IPhoto>(this.resourceUrl, photo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPhoto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPhoto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
