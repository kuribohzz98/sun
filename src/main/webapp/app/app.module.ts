import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { SunSharedModule } from 'app/shared/shared.module';
import { SunCoreModule } from 'app/core/core.module';
import { SunAppRoutingModule } from './app-routing.module';
import { SunHomeModule } from './home/home.module';
import { SunEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    SunSharedModule,
    SunCoreModule,
    SunHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    SunEntityModule,
    SunAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class SunAppModule {}
