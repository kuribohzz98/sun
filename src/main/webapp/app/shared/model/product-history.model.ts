import { Moment } from 'moment';

export interface IProductHistory {
  id?: number;
  data?: string;
  oldValue?: string;
  newValue?: string;
  description?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  productId?: number;
}

export class ProductHistory implements IProductHistory {
  constructor(
    public id?: number,
    public data?: string,
    public oldValue?: string,
    public newValue?: string,
    public description?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public productId?: number
  ) {}
}
