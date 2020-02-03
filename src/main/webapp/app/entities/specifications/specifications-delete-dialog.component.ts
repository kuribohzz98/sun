import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISpecifications } from 'app/shared/model/specifications.model';
import { SpecificationsService } from './specifications.service';

@Component({
  templateUrl: './specifications-delete-dialog.component.html'
})
export class SpecificationsDeleteDialogComponent {
  specifications?: ISpecifications;

  constructor(
    protected specificationsService: SpecificationsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.specificationsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('specificationsListModification');
      this.activeModal.close();
    });
  }
}
