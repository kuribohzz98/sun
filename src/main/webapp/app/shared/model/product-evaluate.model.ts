import { Moment } from 'moment';

export interface IProductEvaluate {
  id?: number;
  point?: number;
  evaluate?: string;
  createdAt?: Moment;
  userLogin?: string;
  userId?: number;
  productId?: number;
}

export class ProductEvaluate implements IProductEvaluate {
  constructor(
    public id?: number,
    public point?: number,
    public evaluate?: string,
    public createdAt?: Moment,
    public userLogin?: string,
    public userId?: number,
    public productId?: number
  ) {}
}
