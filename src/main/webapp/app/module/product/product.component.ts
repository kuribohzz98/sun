import { AccountService } from './../../core/auth/account.service';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { takeUntil, mergeMap, flatMap, concatMap, filter } from 'rxjs/operators';
import { IProvider } from './../../shared/model/provider.model';
import { ProductCodeToName } from './../../shared/constants/product.constants';
import { IProduct } from './../../shared/model/product.model';
import { ProductService } from './../../service/product.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router, UrlSegment, Params, ActivationEnd } from '@angular/router';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { Subject, from, BehaviorSubject } from 'rxjs';

@Component({
  selector: 'jhi-product',
  templateUrl: './product.component.html',
  styles: []
})
export class ProductComponent implements OnInit, OnDestroy {
  private products: IProduct[] = [];
  private productType: string = '';
  private faBars = faBars;
  private providers: IProvider[] = [];
  private destroy$: Subject<boolean> = new Subject();
  private formPrice?: FormGroup;
  private url: UrlSegment[] = [];
  private queryParam: Params = {};
  private action$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private productSerivce: ProductService,
    private fb: FormBuilder,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.action$
      .pipe(
        mergeMap(data =>
          this.activatedRoute.url.pipe(
            mergeMap(url => {
              this.url = url;
              this.productType = ProductCodeToName[url[url.length - 1].path];
              return this.activatedRoute.queryParams;
            }),
            concatMap(data => {
              this.queryParam = data;
              return this.activatedRoute.data;
            }),
            concatMap(({ productType }) => {
              this.providers = productType.providers;
              const query = {} as any;
              if (this.queryParam.fromPrice) query.minSellPrice = this.queryParam.fromPrice;
              if (this.queryParam.toPrice) query.maxSellPrice = this.queryParam.toPrice;
              if (this.queryParam.provider)
                query.providerId = (this.providers.find(provider => provider.code == this.queryParam.provider) || {}).id;
              query.productTypeId = productType.id;
              return this.productSerivce.query({ page: 0, size: 10, ...query });
            }),
            takeUntil(this.destroy$)
          )
        )
      )
      .subscribe(data => {
        this.products = data.body as IProduct[];
      });
    this.router.events.pipe(filter(event => event instanceof ActivationEnd && event.snapshot.url.length != 0)).subscribe(event => {
      this.action$.next(true);
    });
    this.formPrice = this.fb.group({
      from: ['', Validators.required],
      to: ['', Validators.required]
    });
    // this.productSerivce.query({ page: 0, size: 15 })
  }

  isAdmin(): boolean {
    return this.accountService.isAdmin();
  }

  submitFilterPrice(): void {
    const paramsTemp = { ...this.queryParam };
    paramsTemp.fromPrice = this.formPrice ? this.formPrice.value.from : null;
    paramsTemp.toPrice = this.formPrice ? this.formPrice.value.to : null;
    this.router.navigate(['/' + this.url[0].path + '/' + this.url[1].path], { queryParams: paramsTemp });
  }

  addPramsProvider(index: number): void {
    const paramsTemp = {} as any;
    paramsTemp.provider = this.providers[index].code;
    if (this.formPrice) this.formPrice.patchValue({ from: '', to: '' });
    this.router.navigate(['/' + this.url[0].path + '/' + this.url[1].path], { queryParams: paramsTemp });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
