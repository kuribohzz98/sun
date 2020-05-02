import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { IPayment } from './../shared/model/payment.model';
import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PaymentService extends BaseService<IPayment> {
  resourceUrl: string = 'api/payments';

  getOneByTransactionId(transactionId: string): Observable<HttpResponse<IPayment>> {
    return this.http
      .get<IPayment>(this.getUrl() + '/getByTransactionId/' + transactionId, { observe: 'response' })
      .pipe(map((res: HttpResponse<IPayment>) => this.convertDateFromServer(res)));
  }

  getAllByUserId(userId: number): Observable<HttpResponse<IPayment[]>> {
    return this.http
      .get<IPayment[]>(this.getUrl() + '/getAllByUserId/' + userId, { observe: 'response' })
      .pipe(map((res: HttpResponse<IPayment[]>) => this.convertDateArrayFromServer(res)));
  }
}
