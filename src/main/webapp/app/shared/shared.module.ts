import { LazyLoadImageModule, intersectionObserverPreset, scrollPreset } from 'ng-lazyload-image';
import { NgModule } from '@angular/core';
import { SunSharedLibsModule } from './shared-libs.module';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { LoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';

@NgModule({
  imports: [SunSharedLibsModule, LazyLoadImageModule.forRoot(scrollPreset)],
  declarations: [AlertComponent, AlertErrorComponent, LoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [LoginModalComponent],
  exports: [SunSharedLibsModule, AlertComponent, AlertErrorComponent, LoginModalComponent, HasAnyAuthorityDirective, LazyLoadImageModule]
})
export class SunSharedModule {}
