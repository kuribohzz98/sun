import { BillPropertyComponent } from './bill-property/bill-property.component';
import { BillComponent } from './bill.component';
import { SunSharedModule } from '../../shared/shared.module';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { routes } from './bill.route';

@NgModule({
  imports: [RouterModule.forChild(routes), SunSharedModule],
  declarations: [BillComponent, BillPropertyComponent]
})
export class BillModule {}
