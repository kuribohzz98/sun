import { flatMap } from 'rxjs/operators';
import { PaymentService } from './../../service/payment.service';
import { IPayment, Payment } from './../../shared/model/payment.model';
import { Injectable } from '@angular/core';
import { BillPropertyComponent } from './bill-property/bill-property.component';
import { Route, Resolve, Router, ActivatedRouteSnapshot } from '@angular/router';
import { BillComponent } from './bill.component';
import { Observable, of, EMPTY } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PaymentPropertyResolve implements Resolve<IPayment> {
  constructor(private paymentService: PaymentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPayment> | Observable<never> {
    const transactionId = route.params['transactionId'];
    if (transactionId) {
      return this.paymentService.getOneByTransactionId(transactionId).pipe(
        flatMap(payment => {
          if (payment.body) {
            return of(payment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Payment());
  }
}

export const routes: Route[] = [
  {
    path: '',
    component: BillComponent
  },
  {
    path: ':transactionId',
    component: BillPropertyComponent,
    resolve: {
      payment: PaymentPropertyResolve
    }
  }
];
