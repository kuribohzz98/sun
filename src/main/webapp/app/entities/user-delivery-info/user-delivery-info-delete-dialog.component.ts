import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserDeliveryInfo } from '../../shared/model/user-delivery-info.model';
import { UserDeliveryInfoService } from './user-delivery-info.service';

@Component({
  templateUrl: './user-delivery-info-delete-dialog.component.html'
})
export class UserDeliveryInfoDeleteDialogComponent {
  userDeliveryInfo?: IUserDeliveryInfo;

  constructor(
    protected userDeliveryInfoService: UserDeliveryInfoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userDeliveryInfoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('userDeliveryInfoListModification');
      this.activeModal.close();
    });
  }
}
