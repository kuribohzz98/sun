import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProductEvaluate } from 'app/shared/model/product-evaluate.model';
import { ProductEvaluateService } from './product-evaluate.service';

@Component({
  templateUrl: './product-evaluate-delete-dialog.component.html'
})
export class ProductEvaluateDeleteDialogComponent {
  productEvaluate?: IProductEvaluate;

  constructor(
    protected productEvaluateService: ProductEvaluateService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productEvaluateService.delete(id).subscribe(() => {
      this.eventManager.broadcast('productEvaluateListModification');
      this.activeModal.close();
    });
  }
}
