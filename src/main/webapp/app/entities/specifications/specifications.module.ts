import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SunSharedModule } from 'app/shared/shared.module';
import { SpecificationsComponent } from './specifications.component';
import { SpecificationsDetailComponent } from './specifications-detail.component';
import { SpecificationsUpdateComponent } from './specifications-update.component';
import { SpecificationsDeleteDialogComponent } from './specifications-delete-dialog.component';
import { specificationsRoute } from './specifications.route';

@NgModule({
  imports: [SunSharedModule, RouterModule.forChild(specificationsRoute)],
  declarations: [
    SpecificationsComponent,
    SpecificationsDetailComponent,
    SpecificationsUpdateComponent,
    SpecificationsDeleteDialogComponent
  ],
  entryComponents: [SpecificationsDeleteDialogComponent]
})
export class SunSpecificationsModule {}
