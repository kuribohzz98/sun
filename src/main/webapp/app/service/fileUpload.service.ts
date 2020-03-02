import { IPhoto } from './../shared/model/photo.model';
import { SERVER_API_URL } from './../app.constants';
import { map } from 'rxjs/operators';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class FileUploadService {
  resourceUrl: string = SERVER_API_URL + 'api/upload';
  constructor(private http: HttpClient) {}

  uploadOneFile(fileToUpload: File, uuidName: string): Observable<IPhoto> {
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, uuidName);
    return this.http.post<IPhoto>(this.resourceUrl + '/uploadOneFile', formData);
  }

  getImages(paths: string[]) {
    return this.http.get(SERVER_API_URL + 'api/getFile/images', { params: { paths } });
  }

  // uploadMultiFile(code: string): Observable<HttpResponse<IProduct>> {
  //     return this.http
  //         .get<IProduct>(this.resourceUrl + "/uploadMultiFile", { params: { code }, observe: 'response' })
  //         .pipe(map((res: HttpResponse<IProduct>) => this.convertDateFromServer(res)));
  // }
}
