import { map } from 'rxjs/operators';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IProduct } from './../shared/model/product.model';
import { Injectable } from '@angular/core';
import { BaseService } from './base.service';

@Injectable({ providedIn: 'root' })
export class FileUploadService {
  resourceUrl: string = 'api/upload';
  constructor(private http: HttpClient) {}

  uploadOneFile(fileToUpload: File): Observable<boolean> {
    const formData: FormData = new FormData();
    formData.append('fileKey', fileToUpload, fileToUpload.name);
    return this.http.post(this.resourceUrl + '/uploadOneFile', formData).pipe(
      map(() => {
        return true;
      })
    );
    // return this.http
    //     .get<IProduct>(this.resourceUrl + "/uploadOneFile", { params: { code }, observe: 'response' })
    //     .pipe(map((res: HttpResponse<IProduct>) => this.convertDateFromServer(res)));
  }

  // uploadMultiFile(code: string): Observable<HttpResponse<IProduct>> {
  //     return this.http
  //         .get<IProduct>(this.resourceUrl + "/uploadMultiFile", { params: { code }, observe: 'response' })
  //         .pipe(map((res: HttpResponse<IProduct>) => this.convertDateFromServer(res)));
  // }
}
