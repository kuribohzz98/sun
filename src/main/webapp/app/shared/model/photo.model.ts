export interface IPhoto {
  id?: number;
  name?: string;
  imageContentType?: string;
  image?: any;
}

export class Photo implements IPhoto {
  constructor(public id?: number, public name?: string, public imageContentType?: string, public image?: any) {}
}
