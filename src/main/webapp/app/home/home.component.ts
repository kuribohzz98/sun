import { IProduct } from './../shared/model/product.model';
import { ProductService } from './../service/product.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription, Subject } from 'rxjs';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  products: IProduct[] | null = [];
  destroy$: Subject<boolean> = new Subject<boolean>();
  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.productService
      .query({ page: 0, size: 9, salePrice: 0 })
      .pipe(takeUntil(this.destroy$))
      .subscribe(products => {
        this.products = products.body;
      });
  }

  isAdmin(): boolean {
    return this.accountService.isAdmin();
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }
}
