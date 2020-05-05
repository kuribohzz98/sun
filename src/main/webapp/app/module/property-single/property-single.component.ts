import { ProductEvaluate } from 'app/shared/model/product-evaluate.model';
import { AccountService } from 'app/core/auth/account.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IProduct } from './../../shared/model/product.model';
import { IProductEvaluate } from './../../shared/model/product-evaluate.model';
import { ProductEvaluateService } from './../../service/product-evaluate.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, of, forkJoin, BehaviorSubject } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { faShoppingCart, faPlus, faMinus, faStar, faUserCircle } from '@fortawesome/free-solid-svg-icons';
import { takeUntil, mergeMap, concatMap } from 'rxjs/operators';
import * as moment from 'moment';
import { NotifierService } from 'angular-notifier';
import { Account } from 'app/core/user/account.model';
@Component({
  selector: 'jhi-property-single',
  templateUrl: './property-single.component.html',
  styles: [
    `
      .star {
        font-size: 1.5rem;
        color: #b0c4de;
      }
      .filled {
        color: #decb00;
      }
    `
  ]
})
export class PropertySingleComponent implements OnInit, OnDestroy {
  private faShoppingCart = faShoppingCart;
  private faPlus = faPlus;
  private faMinus = faMinus;
  private faStar = faStar;
  private faUser = faUserCircle;
  private ratting: number = 0;
  private destroy$: Subject<boolean> = new Subject();
  private product: IProduct = {};
  private evaluates: any[] = [];
  private progressRating: any[] = [];
  private advancePointEvaluate: number = 0;
  private isAvailableOnCart: boolean = false;
  private amount: number = 1;
  private formEvaluate?: FormGroup;
  private account?: Account | null = {} as Account;
  private evaluates$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);

  constructor(
    private activatedRoute: ActivatedRoute,
    private productEvaluateService: ProductEvaluateService,
    private router: Router,
    private notifierService: NotifierService,
    private fb: FormBuilder,
    private accountService: AccountService
  ) {}

  ngOnInit() {
    this.progressRating.length = 5;
    this.evaluates$
      .pipe(
        mergeMap(data =>
          this.activatedRoute.data.pipe(
            mergeMap(({ product }) =>
              forkJoin(this.productEvaluateService.findAllByProductId(product.id, { page: 0, size: 10 }), of(product))
            ),
            takeUntil(this.destroy$)
          )
        )
      )
      .subscribe(result => {
        console.log('__________', result);
        this.product = result[1];
        if (!this.product.quantity) this.amount = 0;
        this.evaluates = result[0].body as IProductEvaluate[];
        this.advancePointEvaluate = 0;
        this.evaluates.map(evaluate => {
          evaluate.createdAt = moment(evaluate.createdAt).format('LLLL');
          this.caculateProgressRating(evaluate);
        });
        let cart = JSON.parse(localStorage.getItem('cart') || '[]') as any[];
        const isCart = cart.find(product => product.id == this.product.id);
        if (isCart) {
          this.isAvailableOnCart = true;
        }
      });

    this.formEvaluate = this.fb.group({
      point: ['', Validators.required],
      evaluate: ['', Validators.required]
    });
  }

  isAdmin(): boolean {
    return this.accountService.isAdmin();
  }

  addAmount(amount: number) {
    this.amount = amount;
  }

  caculateProgressRating(evaluate: any): void {
    this.advancePointEvaluate += +(+evaluate.point / ((this.evaluates || []).length || 1)).toFixed(1);
    if (this.progressRating[+evaluate.point - 1]) {
      this.progressRating[+evaluate.point - 1] += +evaluate.point;
      return;
    }
    this.progressRating[+evaluate.point - 1] = +evaluate.point;
  }

  notification() {
    this.notifierService.show({
      type: 'success',
      message: 'Sản phẩm đã được thêm vào giỏ hàng',
      id: 'property-single-add-cart-success'
    });
  }

  buyNow() {
    this.handlerAddToCart();
    this.router.navigate(['/gio-hang/']);
  }

  handlerAddToCart() {
    if (this.isAvailableOnCart) {
      return;
    }
    let cart = JSON.parse(localStorage.getItem('cart') || '[]') as any[];
    let cartTemp: IProduct[] = [];
    cart.push({
      id: this.product.id,
      name: this.product.name,
      code: this.product.code,
      photo: this.product.photo,
      sellPrice: this.product.sellPrice,
      salePrice: this.product.salePrice,
      quantity: this.amount,
      maxquantity: this.product.quantity
    });
    if (!cart.length) {
      cart = cartTemp;
      localStorage.setItem('cart', JSON.stringify(cart));
      return;
    }
    localStorage.setItem('cart', JSON.stringify([...cart, ...cartTemp]));
  }

  addToCart() {
    this.handlerAddToCart();
    this.notification();
    this.isAvailableOnCart = true;
  }

  submitEvaluate() {
    if (!this.accountService.getUserIdentity() || !this.accountService.isAuthenticated()) {
      this.notifierService.show({
        type: 'error',
        message: 'Cần đăng nhập để đánh giá',
        id: 'property-single-evaluate-faild-login'
      });
      return;
    }
    if (!this.ratting || !((this.formEvaluate || ({} as any)).value || ({} as any)).evaluate) {
      this.notifierService.show({
        type: 'error',
        message: 'Cần nhập đầy đủ thông tin',
        id: 'property-single-evaluate-faild-validate'
      });
      return;
    }
    if (this.evaluates.find(evaluate => evaluate.userId == (this.accountService.getUserIdentity() || ({} as any)).id)) {
      this.notifierService.show({
        type: 'error',
        message: 'Bạn đã đánh giá sản phẩm này rồi',
        id: 'property-single-evaluate-faild'
      });
      return;
    }
    const productEvaluate = new ProductEvaluate();
    productEvaluate.point = this.ratting;
    productEvaluate.evaluate = ((this.formEvaluate || ({} as any)).value || ({} as any)).evaluate;
    productEvaluate.userId = (this.accountService.getUserIdentity() || ({} as any)).id;
    productEvaluate.productId = this.product.id;
    productEvaluate.createdAt = moment();
    this.productEvaluateService.create(productEvaluate).subscribe(res => {
      this.notifierService.show({
        type: 'success',
        message: 'Đánh giá thành công',
        id: 'property-single-evaluate-success'
      });
      this.evaluates$.next(true);
      this.formEvaluate && this.formEvaluate.setValue({ evaluate: '', point: '' });
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
