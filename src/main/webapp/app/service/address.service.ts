import { ICity } from './../shared/model/Address/city.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AddressService {
  resourceUrl: string = 'https://dc.tintoc.net/app/api-customer/public';
  constructor(private http: HttpClient) {}

  getCity(): Observable<ICity[]> {
    return this.http.get<ICity[]>(this.resourceUrl + '/provinces', { params: { page: '0', size: '100', sort: 'name' } });
  }

  getDistrict(cityId: number): Observable<ICity[]> {
    return this.http.get<ICity[]>(this.resourceUrl + '/districts', {
      params: { page: '0', size: '1000', ['provinceId.equals']: cityId.toString() }
    });
  }

  getWard(districtId: number): Observable<ICity[]> {
    return this.http.get<ICity[]>(this.resourceUrl + '/wards/', {
      params: { page: '0', size: '1000', ['districtId.equals']: districtId.toString() }
    });
  }
}
