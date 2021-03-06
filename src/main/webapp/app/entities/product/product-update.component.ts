import { FileUploadService } from './../../service/fileUpload.service';
import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IProduct, Product } from 'app/shared/model/product.model';
import { ProductService } from './product.service';
import { IProductType } from 'app/shared/model/product-type.model';
import { ProductTypeService } from 'app/entities/product-type/product-type.service';
import { IProvider } from 'app/shared/model/provider.model';
import { ProviderService } from 'app/entities/provider/provider.service';
import * as uuid from 'uuid/v4';

type SelectableEntity = IProductType | IProvider;

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html'
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;

  producttypes: IProductType[] = [];

  providers: IProvider[] = [];

  fileToUpload: any;

  editForm = this.fb.group({
    id: [],
    productTypeId: [null, [Validators.required]],
    providerId: [null, [Validators.required]],
    name: [null, [Validators.required]],
    code: [],
    sellPrice: [null, [Validators.required]],
    importPrice: [null, [Validators.required]],
    quantity: [null, [Validators.required]],
    productLine: [],
    image: [],
    salePrice: []
  });

  constructor(
    protected productService: ProductService,
    protected productTypeService: ProductTypeService,
    protected providerService: ProviderService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private fileUploadService: FileUploadService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.updateForm(product);

      this.productTypeService
        .query({ filter: 'product-is-null' })
        .pipe(
          map((res: HttpResponse<IProductType[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IProductType[]) => {
          if (!product.productTypeId) {
            this.producttypes = resBody;
          } else {
            this.productTypeService
              .find(product.productTypeId)
              .pipe(
                map((subRes: HttpResponse<IProductType>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IProductType[]) => {
                this.producttypes = concatRes;
              });
          }
        });

      this.providerService
        .query({ filter: 'product-is-null' })
        .pipe(
          map((res: HttpResponse<IProvider[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IProvider[]) => {
          if (!product.providerId) {
            this.providers = resBody;
          } else {
            this.providerService
              .find(product.providerId)
              .pipe(
                map((subRes: HttpResponse<IProvider>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IProvider[]) => {
                this.providers = concatRes;
              });
          }
        });
    });
  }

  updateForm(product: IProduct): void {
    this.editForm.patchValue({
      id: product.id,
      productTypeId: product.productTypeId,
      providerId: product.providerId,
      name: product.name,
      code: product.code,
      sellPrice: product.sellPrice,
      importPrice: product.importPrice,
      quantity: product.quantity,
      productLine: product.productLine,
      image: product.image,
      salePrice: product.salePrice
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeFile = this.fileToUpload ? this.fileToUpload.name.split('.') : null;
    const nameFile = typeFile ? uuid() + '.' + typeFile[typeFile.length - 1] : '';
    const product = this.createFromForm(nameFile);
    if (this.fileToUpload) {
      this.fileUploadService.uploadOneFile(this.fileToUpload, nameFile).subscribe(console.log);
    }
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  handleFileInput(files: FileList): void {
    this.fileToUpload = files.item(0);
    console.log(files.item(0));
  }

  private createFromForm(nameFile: string): IProduct {
    return {
      ...new Product(),
      id: this.editForm.get(['id'])!.value,
      productTypeId: this.editForm.get(['productTypeId'])!.value,
      providerId: this.editForm.get(['providerId'])!.value,
      name: this.editForm.get(['name'])!.value,
      code: this.editForm.get(['code'])!.value,
      sellPrice: this.editForm.get(['sellPrice'])!.value,
      importPrice: this.editForm.get(['importPrice'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      productLine: this.editForm.get(['productLine'])!.value,
      image: nameFile,
      salePrice: this.editForm.get(['salePrice'])!.value,
      createdAt: moment(),
      updatedAt: moment()
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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
