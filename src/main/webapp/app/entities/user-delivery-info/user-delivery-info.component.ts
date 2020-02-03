import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserDeliveryInfo } from 'app/shared/model/user-delivery-info.model';
import { UserDeliveryInfoService } from './user-delivery-info.service';
import { UserDeliveryInfoDeleteDialogComponent } from './user-delivery-info-delete-dialog.component';

@Component({
  selector: 'jhi-user-delivery-info',
  templateUrl: './user-delivery-info.component.html'
})
export class UserDeliveryInfoComponent implements OnInit, OnDestroy {
  userDeliveryInfos?: IUserDeliveryInfo[];
  eventSubscriber?: Subscription;

  constructor(
    protected userDeliveryInfoService: UserDeliveryInfoService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.userDeliveryInfoService.query().subscribe((res: HttpResponse<IUserDeliveryInfo[]>) => {
      this.userDeliveryInfos = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInUserDeliveryInfos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IUserDeliveryInfo): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInUserDeliveryInfos(): void {
    this.eventSubscriber = this.eventManager.subscribe('userDeliveryInfoListModification', () => this.loadAll());
  }

  delete(userDeliveryInfo: IUserDeliveryInfo): void {
    const modalRef = this.modalService.open(UserDeliveryInfoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userDeliveryInfo = userDeliveryInfo;
  }
}
