import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { routes } from './module.route';
import { SunSharedModule } from './../shared/shared.module';
import { PropertySingleComponent } from './property-single/property-single.component';
import { CartComponent } from './cart/cart.component';
import { ProductComponent } from './product/product.component';

@NgModule({
  imports: [RouterModule.forChild(routes), SunSharedModule],
  declarations: [CartComponent, PropertySingleComponent, ProductComponent]
})
export class ModuleComponentModule {}
