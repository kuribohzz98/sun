import { IProductEvaluate } from './../../shared/model/product-evaluate.model';
import { ProductEvaluateService } from './../../service/product-evaluate.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, of, forkJoin } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { faShoppingCart, faPlus, faMinus, faStar } from '@fortawesome/free-solid-svg-icons';
import { takeUntil, mergeMap } from 'rxjs/operators';
import * as moment from 'moment';
import { NotifierService } from 'angular-notifier';
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
  private ratting: number = 0;
  private destroy$: Subject<boolean> = new Subject();
  private product: any;
  private evaluates: any[] = [];
  private progressRating: any[] = [];
  private advancePointEvaluate: number = 0;
  private isAvailableOnCart: boolean = false;
  private amount: number = 1;
  constructor(
    private activatedRoute: ActivatedRoute,
    private productEvaluateService: ProductEvaluateService,
    private router: Router,
    private notifierService: NotifierService
  ) {}

  ngOnInit() {
    this.progressRating.length = 5;
    this.activatedRoute.data
      .pipe(
        mergeMap(({ product }) => forkJoin(this.productEvaluateService.findAllByProductId(product.id, { page: 0, size: 5 }), of(product))),
        takeUntil(this.destroy$)
      )
      .subscribe(result => {
        this.product = result[1];
        this.evaluates = result[0].body as IProductEvaluate[];
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
  }

  addAmount(amount: number) {
    this.amount = amount;
  }

  caculateProgressRating(evaluate: any): void {
    this.advancePointEvaluate += +(+evaluate.point / (this.product.productEvaluates.length || 1)).toFixed(1);
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
    let cartTemp: any = [];
    cart.push({
      id: this.product.id,
      name: this.product.name,
      code: this.product.code,
      image: this.product.image,
      sellPrice: this.product.sellPrice,
      salePrice: this.product.salePrice,
      amount: this.amount
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

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
