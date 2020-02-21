import { ProductCodeToName } from './../shared/constants/product.constants';
import { ProductTypeService } from './../service/product-type.service';
import { IProduct } from './../shared/model/product.model';
import { ProductService } from './../service/product.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription, Subject, forkJoin } from 'rxjs';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { takeUntil, mergeMap } from 'rxjs/operators';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  productsSale: IProduct[] | null = [];
  destroy$: Subject<boolean> = new Subject<boolean>();
  productsLaptop: IProduct[] | null = [];
  productsTivi: IProduct[] | null = [];
  productTypeTivi: any = {};
  productTypeLaptop: any = {};

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private productService: ProductService,
    private productTypeService: ProductTypeService
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
        this.productsSale = products.body;
      });

    this.productTypeService
      .query({ page: 0, size: 100 })
      .pipe(
        mergeMap(res => {
          this.productTypeTivi = res.body ? res.body.find(productType => productType.name == ProductCodeToName.TIVI) : {};
          this.productTypeLaptop = res.body ? res.body.find(productType => productType.name == ProductCodeToName.LAPTOP) : {};
          return forkJoin(
            this.productService.query({ page: 0, size: 9, productTypeId: this.productTypeLaptop ? this.productTypeLaptop.id : null }),
            this.productService.query({ page: 0, size: 9, productTypeId: this.productTypeTivi ? this.productTypeTivi.id : null })
          );
        }),
        takeUntil(this.destroy$)
      )
      .subscribe(res => {
        this.productsLaptop = res[0].body;
        this.productsTivi = res[1].body;
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
