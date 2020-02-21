import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AddressService {
  resourceUrl: string = 'https://thongtindoanhnghiep.co/api';
  constructor(private http: HttpClient) {}

  getCity(): Observable<any> {
    return this.http.get(this.resourceUrl + '/city');
  }

  getDistrict(cityId: number): Observable<any> {
    return this.http.get(this.resourceUrl + '/city/' + cityId + '/district');
  }

  getWard(districtId: number): Observable<any> {
    return this.http.get(this.resourceUrl + '/disitrct/' + districtId + '/ward');
  }
}
