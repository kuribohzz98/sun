import { IPayment } from 'app/shared/model/payment.model';
import { PaymentService } from './../../service/payment.service';
import { AccountService } from './../../core/auth/account.service';
import { OnInit, Component } from '@angular/core';
import { Subject } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-bill',
  templateUrl: './bill.component.html'
})
export class BillComponent implements OnInit {
  public done: boolean = false;
  public payments: IPayment[] = [];
  constructor(private accountService: AccountService, private paymentService: PaymentService, private router: Router) {}

  ngOnInit() {
    const accountId = (this.accountService.getUserIdentity() || {}).id;
    if (accountId) {
      this.paymentService.getAllByUserId(accountId).subscribe(res => {
        this.payments = res.body || [];
        console.log('______________');
        console.log(this.payments);
        this.payments.forEach(payment => {
          if (payment.createdAt) payment.createdAt = payment.createdAt.format('DD-MM-YYYY');
        });
      });
    }
  }

  comeInBillProperty(transactionId: string) {
    this.router.navigate(['hoa-don/' + transactionId]);
  }

  isAdmin(): boolean {
    return this.accountService.isAdmin();
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }
}
