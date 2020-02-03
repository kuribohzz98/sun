import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SunSharedModule } from 'app/shared/shared.module';
import { UserDeliveryInfoComponent } from './user-delivery-info.component';
import { UserDeliveryInfoDetailComponent } from './user-delivery-info-detail.component';
import { UserDeliveryInfoUpdateComponent } from './user-delivery-info-update.component';
import { UserDeliveryInfoDeleteDialogComponent } from './user-delivery-info-delete-dialog.component';
import { userDeliveryInfoRoute } from './user-delivery-info.route';

@NgModule({
  imports: [SunSharedModule, RouterModule.forChild(userDeliveryInfoRoute)],
  declarations: [
    UserDeliveryInfoComponent,
    UserDeliveryInfoDetailComponent,
    UserDeliveryInfoUpdateComponent,
    UserDeliveryInfoDeleteDialogComponent
  ],
  entryComponents: [UserDeliveryInfoDeleteDialogComponent]
})
export class SunUserDeliveryInfoModule {}
