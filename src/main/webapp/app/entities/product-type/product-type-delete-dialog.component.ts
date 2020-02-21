import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProductType } from 'app/shared/model/product-type.model';
import { ProductTypeService } from './product-type.service';

@Component({
  templateUrl: './product-type-delete-dialog.component.html'
})
export class ProductTypeDeleteDialogComponent {
  productType?: IProductType;

  constructor(
    protected productTypeService: ProductTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('productTypeListModification');
      this.activeModal.close();
    });
  }
}
