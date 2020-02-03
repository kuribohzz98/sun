export interface IProvider {
  id?: number;
  name?: string;
  code?: string;
}

export class Provider implements IProvider {
  constructor(public id?: number, public name?: string, public code?: string) {}
}
