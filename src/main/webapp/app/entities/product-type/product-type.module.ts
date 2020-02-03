import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SunSharedModule } from 'app/shared/shared.module';
import { ProductTypeComponent } from './product-type.component';
import { ProductTypeDetailComponent } from './product-type-detail.component';
import { ProductTypeUpdateComponent } from './product-type-update.component';
import { ProductTypeDeleteDialogComponent } from './product-type-delete-dialog.component';
import { productTypeRoute } from './product-type.route';

@NgModule({
  imports: [SunSharedModule, RouterModule.forChild(productTypeRoute)],
  declarations: [ProductTypeComponent, ProductTypeDetailComponent, ProductTypeUpdateComponent, ProductTypeDeleteDialogComponent],
  entryComponents: [ProductTypeDeleteDialogComponent]
})
export class SunProductTypeModule {}
