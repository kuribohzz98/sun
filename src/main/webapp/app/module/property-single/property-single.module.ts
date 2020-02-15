import { RouterModule } from '@angular/router';
import { PropertySingleComponent } from './property-single.component';
import { NgModule } from '@angular/core';

import { SunSharedModule } from 'app/shared/shared.module';
import { routes } from './property-single-route';

@NgModule({
  imports: [SunSharedModule, RouterModule.forChild(routes)],
  declarations: [PropertySingleComponent]
})
export class PropertySingleModule {}
