import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SunSharedModule } from 'app/shared/shared.module';
import { ProductEvaluateComponent } from './product-evaluate.component';
import { ProductEvaluateDetailComponent } from './product-evaluate-detail.component';
import { ProductEvaluateUpdateComponent } from './product-evaluate-update.component';
import { ProductEvaluateDeleteDialogComponent } from './product-evaluate-delete-dialog.component';
import { productEvaluateRoute } from './product-evaluate.route';

@NgModule({
  imports: [SunSharedModule, RouterModule.forChild(productEvaluateRoute)],
  declarations: [
    ProductEvaluateComponent,
    ProductEvaluateDetailComponent,
    ProductEvaluateUpdateComponent,
    ProductEvaluateDeleteDialogComponent
  ],
  entryComponents: [ProductEvaluateDeleteDialogComponent]
})
export class SunProductEvaluateModule {}
