import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SunTestModule } from '../../../test.module';
import { UserDeliveryInfoComponent } from 'app/entities/user-delivery-info/user-delivery-info.component';
import { UserDeliveryInfoService } from 'app/entities/user-delivery-info/user-delivery-info.service';
import { UserDeliveryInfo } from 'app/shared/model/user-delivery-info.model';

describe('Component Tests', () => {
  describe('UserDeliveryInfo Management Component', () => {
    let comp: UserDeliveryInfoComponent;
    let fixture: ComponentFixture<UserDeliveryInfoComponent>;
    let service: UserDeliveryInfoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SunTestModule],
        declarations: [UserDeliveryInfoComponent],
        providers: []
      })
        .overrideTemplate(UserDeliveryInfoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserDeliveryInfoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserDeliveryInfoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new UserDeliveryInfo(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.userDeliveryInfos && comp.userDeliveryInfos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
