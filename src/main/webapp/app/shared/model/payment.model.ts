import { Moment } from 'moment';
import { IProduct } from 'app/shared/model/product.model';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';

export interface IPayment {
  id?: number;
  price?: number;
  status?: PaymentStatus;
  recipientPhone?: string;
  deliveryAddress?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  products?: IProduct[];
  userLogin?: string;
  userId?: number;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public price?: number,
    public status?: PaymentStatus,
    public recipientPhone?: string,
    public deliveryAddress?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public products?: IProduct[],
    public userLogin?: string,
    public userId?: number
  ) {}
}
