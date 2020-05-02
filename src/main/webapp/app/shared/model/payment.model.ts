import { Moment } from 'moment';
import { IProduct } from 'app/shared/model/product.model';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';

export interface IPayment {
  id?: number;
  price?: number;
  status?: String;
  recipientPhone?: string;
  deliveryAddress?: string;
  createdAt?: any;
  updatedAt?: Moment;
  products?: IProduct[];
  userLogin?: string;
  userId?: number;
  transactionId?: string;
  qrcode?: any;
  description?: string;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public price?: number,
    public status?: String,
    public recipientPhone?: string,
    public deliveryAddress?: string,
    public createdAt?: any,
    public updatedAt?: Moment,
    public products?: IProduct[],
    public userLogin?: string,
    public userId?: number,
    public transactionId?: string,
    public qrcode?: any
  ) {}
}
