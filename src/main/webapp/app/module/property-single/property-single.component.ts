import { Component } from '@angular/core';
import { faShoppingCart, faPlus, faMinus } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-property-single',
  templateUrl: './property-single.component.html',
  styles: [
    `
      .star {
        font-size: 1.5rem;
        color: #b0c4de;
      }
      .filled {
        color: #decb00;
      }
    `
  ]
})
export class PropertySingleComponent {
  private faShoppingCart = faShoppingCart;
  private faPlus = faPlus;
  private faMinus = faMinus;
  private ratting: number = 0;
  constructor() {}
}
