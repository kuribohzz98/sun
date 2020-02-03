import { Moment } from 'moment';

export interface IProduct {
  id?: number;
  productTypeId?: number;
  providerId?: number;
  name?: string;
  code?: string;
  sellPrice?: number;
  importPrice?: number;
  quantity?: number;
  productLine?: string;
  image?: string;
  point?: number;
  createdAt?: Moment;
  updatedAt?: Moment;
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
    public image?: string,
    public point?: number,
    public createdAt?: Moment,
    public updatedAt?: Moment
  ) {}
}
