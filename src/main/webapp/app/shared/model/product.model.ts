import { IProductEvaluate } from './product-evaluate.model';
import { Moment } from 'moment';

export interface IProduct {
  id?: number;
  productTypeId?: number;
  providerId?: number;
  photo?: string;
  name?: string;
  code?: string;
  sellPrice?: number;
  importPrice?: number;
  salePrice?: number;
  quantity?: number;
  productLine?: string;
  point?: number;
  createdAt?: Moment;
  updatedAt?: Moment;
  productEvaluates?: IProductEvaluate[];
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public productTypeId?: number,
    public providerId?: number,
    public name?: string,
    public code?: string,
    public sellPrice?: number,
    public importPrice?: number,
    public quantity?: number,
    public productLine?: string,
    public photo?: string,
    public point?: number,
    public createdAt?: Moment,
    public updatedAt?: Moment
  ) {}
}
