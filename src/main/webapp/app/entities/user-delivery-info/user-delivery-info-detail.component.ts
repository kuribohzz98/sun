import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserDeliveryInfo } from 'app/shared/model/user-delivery-info.model';

@Component({
  selector: 'jhi-user-delivery-info-detail',
  templateUrl: './user-delivery-info-detail.component.html'
})
export class UserDeliveryInfoDetailComponent implements OnInit {
  userDeliveryInfo: IUserDeliveryInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userDeliveryInfo }) => {
      this.userDeliveryInfo = userDeliveryInfo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
