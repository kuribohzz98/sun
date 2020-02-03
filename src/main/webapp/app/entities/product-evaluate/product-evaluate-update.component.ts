import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IProductEvaluate, ProductEvaluate } from 'app/shared/model/product-evaluate.model';
import { ProductEvaluateService } from './product-evaluate.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';

type SelectableEntity = IUser | IProduct;

@Component({
  selector: 'jhi-product-evaluate-update',
  templateUrl: './product-evaluate-update.component.html'
})
export class ProductEvaluateUpdateComponent implements OnInit {
  isSaving = false;

  users: IUser[] = [];

  products: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    point: [null, [Validators.required, Validators.min(0), Validators.max(5)]],
    evaluate: [],
    createdAt: [null, [Validators.required]],
    userId: [null, Validators.required],
    productId: [null, Validators.required]
  });

  constructor(
    protected productEvaluateService: ProductEvaluateService,
    protected userService: UserService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productEvaluate }) => {
      this.updateForm(productEvaluate);

      this.userService
        .query()
        .pipe(
          map((res: HttpResponse<IUser[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUser[]) => (this.users = resBody));

      this.productService
        .query({ filter: 'productevaluate-is-null' })
        .pipe(
          map((res: HttpResponse<IProduct[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IProduct[]) => {
          if (!productEvaluate.productId) {
            this.products = resBody;
          } else {
            this.productService
              .find(productEvaluate.productId)
              .pipe(
                map((subRes: HttpResponse<IProduct>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IProduct[]) => {
                this.products = concatRes;
              });
          }
        });
    });
  }

  updateForm(productEvaluate: IProductEvaluate): void {
    this.editForm.patchValue({
      id: productEvaluate.id,
      point: productEvaluate.point,
      evaluate: productEvaluate.evaluate,
      createdAt: productEvaluate.createdAt != null ? productEvaluate.createdAt.format(DATE_TIME_FORMAT) : null,
      userId: productEvaluate.userId,
      productId: productEvaluate.productId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productEvaluate = this.createFromForm();
    if (productEvaluate.id !== undefined) {
      this.subscribeToSaveResponse(this.productEvaluateService.update(productEvaluate));
    } else {
      this.subscribeToSaveResponse(this.productEvaluateService.create(productEvaluate));
    }
  }

  private createFromForm(): IProductEvaluate {
    return {
      ...new ProductEvaluate(),
      id: this.editForm.get(['id'])!.value,
      point: this.editForm.get(['point'])!.value,
      evaluate: this.editForm.get(['evaluate'])!.value,
      createdAt:
        this.editForm.get(['createdAt'])!.value != null ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      userId: this.editForm.get(['userId'])!.value,
      productId: this.editForm.get(['productId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductEvaluate>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
