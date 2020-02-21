import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IProductHistory, ProductHistory } from 'app/shared/model/product-history.model';
import { ProductHistoryService } from './product-history.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';

@Component({
  selector: 'jhi-product-history-update',
  templateUrl: './product-history-update.component.html'
})
export class ProductHistoryUpdateComponent implements OnInit {
  isSaving = false;

  products: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    data: [null, [Validators.required]],
    oldValue: [],
    newValue: [null, [Validators.required]],
    description: [],
    createdAt: [null, [Validators.required]],
    updatedAt: [null, [Validators.required]],
    productId: [null, Validators.required]
  });

  constructor(
    protected productHistoryService: ProductHistoryService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productHistory }) => {
      this.updateForm(productHistory);

      this.productService
        .query({ filter: 'producthistory-is-null' })
        .pipe(
          map((res: HttpResponse<IProduct[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IProduct[]) => {
          if (!productHistory.productId) {
            this.products = resBody;
          } else {
            this.productService
              .find(productHistory.productId)
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

  updateForm(productHistory: IProductHistory): void {
    this.editForm.patchValue({
      id: productHistory.id,
      data: productHistory.data,
      oldValue: productHistory.oldValue,
      newValue: productHistory.newValue,
      description: productHistory.description,
      createdAt: productHistory.createdAt != null ? productHistory.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: productHistory.updatedAt != null ? productHistory.updatedAt.format(DATE_TIME_FORMAT) : null,
      productId: productHistory.productId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productHistory = this.createFromForm();
    if (productHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.productHistoryService.update(productHistory));
    } else {
      this.subscribeToSaveResponse(this.productHistoryService.create(productHistory));
    }
  }

  private createFromForm(): IProductHistory {
    return {
      ...new ProductHistory(),
      id: this.editForm.get(['id'])!.value,
      data: this.editForm.get(['data'])!.value,
      oldValue: this.editForm.get(['oldValue'])!.value,
      newValue: this.editForm.get(['newValue'])!.value,
      description: this.editForm.get(['description'])!.value,
      createdAt:
        this.editForm.get(['createdAt'])!.value != null ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt:
        this.editForm.get(['updatedAt'])!.value != null ? moment(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      productId: this.editForm.get(['productId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductHistory>>): void {
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

  trackById(index: number, item: IProduct): any {
    return item.id;
  }
}
