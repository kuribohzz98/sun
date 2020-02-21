import { IProvider } from './provider.model';
export interface IProductType {
  id?: number;
  name?: string;
  code?: string;
  providers?: IProvider[];
}

export class ProductType implements IProductType {
  constructor(public id?: number, public name?: string, public code?: string) {}
}
