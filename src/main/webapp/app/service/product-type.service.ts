import { IProductType } from './../shared/model/product-type.model';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { BaseService } from './base.service';

@Injectable({ providedIn: 'root' })
export class ProductTypeService extends BaseService<IProductType> {
  resourceUrl: string = 'api/product-types';
}
