import { PaymentService } from './../../service/payment.service';
import { ICity, City } from './../../shared/model/Address/city.model';
import { AddressService } from './../../service/address.service';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';
import { Payment } from './../../shared/model/payment.model';
import { AccountService } from './../../core/auth/account.service';
import { NotifierService } from 'angular-notifier';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { faPlus, faMinus, faTrashAlt, faAngleDoubleLeft } from '@fortawesome/free-solid-svg-icons';
import { Location } from '@angular/common';
import { Account } from 'app/core/user/account.model';
import * as moment from 'moment';
import { Router } from '@angular/router';
import { map, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';

@Component({
  selector: 'jhi-cart',
  templateUrl: './cart.component.html'
})
export class CartComponent implements OnInit, OnDestroy {
  private faPlus = faPlus;
  private faMinus = faMinus;
  private faTrash = faTrashAlt;
  private faAngleDoubleLeft = faAngleDoubleLeft;
  private formHouse: FormGroup = new FormBuilder().group({});
  private formSuperMarket: FormGroup = new FormBuilder().group({});
  private products: any[] = [];
  private totalPrice: number = 0;
  private cities: ICity[] = [] as ICity[];
  private districts: ICity[] = [] as ICity[];
  private wards: ICity[] = [] as ICity[];
  private destroys$: Subject<boolean> = new Subject<boolean>();
  // @ViewChild('instance', { static: true }) instance: any;
  // focus$ = new Subject<string>();
  // click$ = new Subject<string>();

  // model: any;

  constructor(
    private fb: FormBuilder,
    private _location: Location,
    private notifierService: NotifierService,
    private accountService: AccountService,
    private paymentService: PaymentService,
    private addressService: AddressService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.formHouse = this.fb.group({
      phone: ['', Validators.required],
      city: ['', Validators.required],
      district: ['', Validators.required],
      ward: ['', Validators.required],
      address: ['']
    });
    this.formSuperMarket = this.fb.group({
      name: ['', Validators.required],
      phone: ['', Validators.required]
    });

    this.products = JSON.parse(localStorage.getItem('cart') || '[]');
    this.products.map(product => {
      if (product.salePrice) return (this.totalPrice += product.salePrice * product.quantity);
      this.totalPrice += product.sellPrice * product.quantity;
    });
    this.addressService
      .getCity()
      .pipe(map(res => res.map(data => new City(data))))
      .subscribe(data => {
        this.cities = data;
      });
    this.formHouse
      .get('city')
      .valueChanges.pipe(takeUntil(this.destroys$))
      .subscribe(value => {
        if (value) {
          this.addressService
            .getDistrict(+value)
            .pipe(map(res => res.map(data => new City(data))))
            .subscribe(data => {
              this.districts = data;
            });
          this.wards = [];
          this.formHouse.patchValue({
            district: '',
            ward: ''
          });
        }
        if (!value) this.districts = [];
      });
    this.formHouse
      .get('district')
      .valueChanges.pipe(takeUntil(this.destroys$))
      .subscribe(value => {
        if (value) {
          this.addressService
            .getWard(+value)
            .pipe(map(res => res.map(data => new City(data))))
            .subscribe(data => {
              this.wards = data;
            });

          this.formHouse.patchValue({
            ward: ''
          });
        }
      });
  }

  backPage() {
    // this._location.back();
    this.router.navigate(['']);
  }

  addAmount(index: number, quantity: number) {
    const price = this.products[index].salePrice ? +this.products[index].salePrice : +this.products[index].sellPrice;
    this.totalPrice -= this.products[index].quantity * price;
    this.products[index].quantity = quantity;
    this.totalPrice += this.products[index].quantity * price;
  }

  removeItem(index: number) {
    this.addAmount(index, 0);
    this.products.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(this.products));
  }

  submitPayment() {
    if (!this.products || !this.products.length) {
      this.notifierService.show({
        id: 'cart-payment-faild-product',
        type: 'error',
        message: 'Bạn chưa có sản phẩm nào trong giỏ hàng'
      });
      return;
    }
    if (!this.accountService.isAuthenticated() || !this.accountService.getUserIdentity()) {
      this.notifierService.show({
        id: 'cart-payment-faild-account',
        type: 'error',
        message: 'Bạn cần đăng nhập để có thể thanh toán'
      });
      return;
    }
    if (this.formHouse && this.formHouse.invalid) {
      this.notifierService.show({
        id: 'cart-payment-faild-form',
        type: 'error',
        message: 'Bạn cần nhập đầy đủ thông tin để thanh toán'
      });
      return;
    }
    const value = (this.formHouse || ({} as any)).value || ({} as any);
    const payment = new Payment();
    const city = this.cities.find(city => city.id == +value.city);
    const district = this.districts.find(district => district.id == +value.district);
    payment.price = Math.floor(this.totalPrice / 100);
    payment.products = this.products;
    payment.recipientPhone = value.phone;
    payment.deliveryAddress =
      (value.address ? value.address + ', ' : '') + value.ward + ', ' + (district || {}).name + ', ' + (city || {}).name;
    payment.userId = (this.accountService.getUserIdentity() || ({} as Account)).id;
    payment.createdAt = moment();
    payment.updatedAt = moment();
    payment.status = PaymentStatus.UNPAID;

    console.log(payment);
    this.paymentService.create(payment).subscribe(res => {
      this.notifierService.show({
        id: 'cart-payment-success',
        type: 'success',
        message: 'Mua hàng thành công'
      });
      localStorage.clear();
      console.log('_________', res.body);
      this.router.navigate(['/hoa-don/' + (res.body || {}).transactionId]);
    });
  }

  isAdmin(): boolean {
    return this.accountService.isAdmin();
  }

  // submitSuperMarket() {
  //   if (!this.products || !this.products.length) {
  //     this.notifierService.show({
  //       id: 'cart-market-faild-product',
  //       type: 'error',
  //       message: 'Bạn chưa có sản phẩm nào trong giỏ hàng'
  //     });
  //     return;
  //   }
  //   if (!this.accountService.isAuthenticated() || !this.accountService.getUserIdentity()) {
  //     this.notifierService.show({
  //       id: 'cart-market-faild-account',
  //       type: 'error',
  //       message: 'Bạn cần đăng nhập để có thể thanh toán'
  //     });
  //     return;
  //   }
  //   if (this.formSuperMarket && this.formSuperMarket.invalid) {
  //     this.notifierService.show({
  //       id: 'cart-payment-faild-form',
  //       type: 'error',
  //       message: 'Bạn cần nhập đầy đủ thông tin để đặt hàng'
  //     });
  //     return;
  //   }

  //   const value = (this.formSuperMarket || ({} as any)).value || ({} as any);
  //   const payment = new Payment();
  //   payment.price = this.totalPrice / 100;
  //   payment.products = this.products;
  //   payment.recipientPhone = value.phone;
  //   payment.deliveryAddress = '';
  //   payment.userId = (this.accountService.getUserIdentity() || ({} as Account)).id;
  //   payment.createdAt = moment();
  //   payment.updatedAt = moment();
  //   payment.status = 'UNPAID';
  //   this.paymentService.create(payment).subscribe(res => {
  //     this.notifierService.show({
  //       id: 'cart-market-success',
  //       type: 'success',
  //       message: 'Đặt hàng thành công'
  //     });
  //     localStorage.clear();
  //     this.router.navigate(['']);
  //   });
  // }

  ngOnDestroy() {
    this.destroys$.next(true);
    this.destroys$.complete();
  }
}
