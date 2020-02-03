export interface IUserDeliveryInfo {
  id?: number;
  userId?: number;
  phone?: string;
  city?: string;
  district?: string;
  ward?: string;
  address?: string;
  userLogin?: string;
}

export class UserDeliveryInfo implements IUserDeliveryInfo {
  constructor(
    public id?: number,
    public userId?: number,
    public phone?: string,
    public city?: string,
    public district?: string,
    public ward?: string,
    public address?: string,
    public userLogin?: string
  ) {}
}
