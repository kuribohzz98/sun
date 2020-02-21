import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';
import { Payment } from './../../shared/model/payment.model';
import { PaymentService } from './../../entities/payment/payment.service';
import { AccountService } from './../../core/auth/account.service';
import { NotifierService } from 'angular-notifier';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { faPlus, faMinus, faTrashAlt, faAngleDoubleLeft } from '@fortawesome/free-solid-svg-icons';
import { Location } from '@angular/common';
import { Account } from 'app/core/user/account.model';
import * as moment from 'moment';
import { Router } from '@angular/router';
// import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
// import { Observable, Subject, merge } from 'rxjs';
// import { debounceTime, distinctUntilChanged, filter, map } from 'rxjs/operators';

// const states = ['Alabama', 'Alaska', 'American Samoa', 'Arizona', 'Arkansas', 'California', 'Colorado',
//     'Connecticut', 'Delaware', 'District Of Columbia', 'Federated States Of Micronesia', 'Florida', 'Georgia',
//     'Guam', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine',
//     'Marshall Islands', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana',
//     'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota',
//     'Northern Mariana Islands', 'Ohio', 'Oklahoma', 'Oregon', 'Palau', 'Pennsylvania', 'Puerto Rico', 'Rhode Island',
//     'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virgin Islands', 'Virginia',
//     'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'];

@Component({
  selector: 'jhi-cart',
  templateUrl: './cart.component.html'
})
export class CartComponent implements OnInit {
  private faPlus = faPlus;
  private faMinus = faMinus;
  private faTrash = faTrashAlt;
  private faAngleDoubleLeft = faAngleDoubleLeft;
  private formHouse: any;
  private formSuperMarket: any;
  private products: any[] = [];
  private totalPrice: number = 0;
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
    private router: Router
  ) {}

  ngOnInit(): void {
    this.formHouse = this.fb.group({
      name: ['', Validators.required],
      phone: ['', Validators.required],
      city: ['', Validators.required],
      district: ['', Validators.required],
      ward: ['', Validators.required],
      address: ['', Validators.required],
      bill: [false, Validators.required]
    });
    this.formSuperMarket = this.fb.group({
      name: ['', Validators.required],
      phone: ['', Validators.required]
    });

    this.products = JSON.parse(localStorage.getItem('cart') || '[]');
    this.products.map((product: any, index) => {
      if (product.salePrice) return (this.totalPrice += product.salePrice);
      this.totalPrice += product.sellPrice;
    });
  }

  backPage() {
    this._location.back();
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
    // let product_temp = [] as any[];
    // this.products.map(product => {
    //   for (let i = 0; i < product.amount; i++) {
    //     product_temp.push(product);
    //   }
    // })
    payment.price = this.totalPrice;
    payment.products = this.products;
    payment.recipientPhone = value.phone;
    payment.deliveryAddress = value.city + ', ' + value.district + ', ' + value.ward + value.address ? ', ' + value.address : '';
    payment.userId = (this.accountService.getUserIdentity() || ({} as Account)).id;
    payment.createdAt = moment();
    payment.updatedAt = moment();
    payment.status = PaymentStatus.DELIVERED;
    this.paymentService.create(payment).subscribe(res => {
      this.notifierService.show({
        id: 'cart-payment-success',
        type: 'success',
        message: 'Mua hàng thành công'
      });
      localStorage.clear();
      this.router.navigate(['']);
    });
  }

  isAdmin(): boolean {
    return this.accountService.isAdmin();
  }

  submitSuperMarket() {
    if (!this.products || !this.products.length) {
      this.notifierService.show({
        id: 'cart-market-faild-product',
        type: 'error',
        message: 'Bạn chưa có sản phẩm nào trong giỏ hàng'
      });
      return;
    }
    if (!this.accountService.isAuthenticated() || !this.accountService.getUserIdentity()) {
      this.notifierService.show({
        id: 'cart-market-faild-account',
        type: 'error',
        message: 'Bạn cần đăng nhập để có thể thanh toán'
      });
      return;
    }
    if (this.formSuperMarket && this.formSuperMarket.invalid) {
      this.notifierService.show({
        id: 'cart-payment-faild-form',
        type: 'error',
        message: 'Bạn cần nhập đầy đủ thông tin để đặt hàng'
      });
      return;
    }

    const value = (this.formSuperMarket || ({} as any)).value || ({} as any);
    const payment = new Payment();
    // let product_temp = [] as any[];
    // this.products.map(product => {
    //   for (let i = 0; i < product.amount; i++) {
    //     product_temp.push(product);
    //   }
    // })
    payment.price = this.totalPrice;
    payment.products = this.products;
    payment.recipientPhone = value.phone;
    payment.deliveryAddress = '';
    payment.userId = (this.accountService.getUserIdentity() || ({} as Account)).id;
    payment.createdAt = moment();
    payment.updatedAt = moment();
    payment.status = PaymentStatus.PAID;
    this.paymentService.create(payment).subscribe(res => {
      this.notifierService.show({
        id: 'cart-market-success',
        type: 'success',
        message: 'Đặt hàng thành công'
      });
      localStorage.clear();
      this.router.navigate(['']);
    });
  }
}
