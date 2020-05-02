import { AccountService } from './../../../core/auth/account.service';
import { NotifierService } from 'angular-notifier';
import { PaymentService } from './../../../service/payment.service';
import { IPayment } from './../../../shared/model/payment.model';
import { OnInit, Component, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-bill-property',
  templateUrl: './bill-property.component.html'
})
export class BillPropertyComponent implements OnInit, OnDestroy {
  public destroys$: Subject<boolean> = new Subject<boolean>();
  public payment: IPayment = {};
  constructor(
    private activatedRoute: ActivatedRoute,
    private paymentService: PaymentService,
    private notifierService: NotifierService,
    private accountService: AccountService
  ) {}

  ngOnInit() {
    this.activatedRoute.data.pipe(takeUntil(this.destroys$)).subscribe(res => {
      this.payment = res.payment;
      this.payment.description = (this.payment.description || '').replace(',}', '}');
      const desc = JSON.parse(this.payment.description || '{}');
      (this.payment.products || []).forEach(product => {
        if (product.id) product.quantity = +desc[product.id];
        if (product.salePrice) product.sellPrice = product.salePrice;
      });
      console.log('+_+______________', this.payment);
    });
  }

  confim() {
    this.paymentService.find(this.payment.id || 0).subscribe(res => {
      if (!res.body) {
        return;
      }
      if (res.body && res.body.status == 'PAID') {
        this.notifierService.show({
          id: 'bill-confim-success',
          type: 'success',
          message: 'Xác nhận thanh toán thành công'
        });
        this.payment.status = 'PAID';
        return;
      }
      this.notifierService.show({
        id: 'bill-confim-fail',
        type: 'error',
        message: 'Đơn của bạn chưa được thanh toán. Vui lòng thanh toán và thực hiện lại'
      });
    });
  }

  isAdmin(): boolean {
    return this.accountService.isAdmin();
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  ngOnDestroy() {
    this.destroys$.next(true);
    this.destroys$.complete();
  }
}
