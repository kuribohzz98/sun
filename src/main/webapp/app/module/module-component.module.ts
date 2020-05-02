import { StatisticalComponent } from './statistical/statistical.component';
import { CartComponent } from './cart/cart.component';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { routes } from './module.route';
import { SunSharedModule } from './../shared/shared.module';
import { PropertySingleComponent } from './property-single/property-single.component';
import { ProductComponent } from './product/product.component';
import { ChartsModule } from 'ng2-charts';

@NgModule({
  imports: [RouterModule.forChild(routes), SunSharedModule, ChartsModule],
  declarations: [PropertySingleComponent, ProductComponent, CartComponent, StatisticalComponent]
})
export class ModuleComponentModule {}
