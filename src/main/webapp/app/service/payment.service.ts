import { IPayment } from './../shared/model/payment.model';
import { Injectable } from '@angular/core';
import { BaseService } from './base.service';

@Injectable({ providedIn: 'root' })
export class PaymentService extends BaseService<IPayment> {
  resourceUrl: string = 'api/payments';
}
