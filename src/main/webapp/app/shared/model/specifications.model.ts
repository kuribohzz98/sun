import { Moment } from 'moment';

export interface ISpecifications {
  id?: number;
  title?: string;
  content?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  productId?: number;
}

export class Specifications implements ISpecifications {
  constructor(
    public id?: number,
    public title?: string,
    public content?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public productId?: number
  ) {}
}
