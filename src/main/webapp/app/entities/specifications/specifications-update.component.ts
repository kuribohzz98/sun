import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ISpecifications, Specifications } from 'app/shared/model/specifications.model';
import { SpecificationsService } from './specifications.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';

@Component({
  selector: 'jhi-specifications-update',
  templateUrl: './specifications-update.component.html'
})
export class SpecificationsUpdateComponent implements OnInit {
  isSaving = false;

  products: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    content: [null, [Validators.required]],
    productId: [null, Validators.required]
  });

  constructor(
    protected specificationsService: SpecificationsService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specifications }) => {
      this.updateForm(specifications);

      this.productService
        .query({ filter: 'specifications-is-null' })
        .pipe(
          map((res: HttpResponse<IProduct[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IProduct[]) => {
          if (!specifications.productId) {
            this.products = resBody;
          } else {
            this.productService
              .find(specifications.productId)
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

  updateForm(specifications: ISpecifications): void {
    this.editForm.patchValue({
      id: specifications.id,
      title: specifications.title,
      content: specifications.content,
      productId: specifications.productId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const specifications = this.createFromForm();
    if (specifications.id !== undefined) {
      this.subscribeToSaveResponse(this.specificationsService.update(specifications));
    } else {
      this.subscribeToSaveResponse(this.specificationsService.create(specifications));
    }
  }

  private createFromForm(): ISpecifications {
    return {
      ...new Specifications(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      createdAt: moment(),
      updatedAt: moment(),
      productId: this.editForm.get(['productId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpecifications>>): void {
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
