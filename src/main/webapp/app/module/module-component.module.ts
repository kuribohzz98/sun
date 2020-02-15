import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'sanpham/:type/:code',
        loadChildren: () => import('./property-single/property-single.module').then(m => m.PropertySingleModule)
      }
    ])
  ]
})
export class ModuleComponentModule {}
