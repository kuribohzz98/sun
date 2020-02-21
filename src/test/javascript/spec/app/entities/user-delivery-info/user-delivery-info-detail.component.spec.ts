import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SunTestModule } from '../../../test.module';
import { UserDeliveryInfoDetailComponent } from 'app/entities/user-delivery-info/user-delivery-info-detail.component';
import { UserDeliveryInfo } from 'app/shared/model/user-delivery-info.model';

describe('Component Tests', () => {
  describe('UserDeliveryInfo Management Detail Component', () => {
    let comp: UserDeliveryInfoDetailComponent;
    let fixture: ComponentFixture<UserDeliveryInfoDetailComponent>;
    const route = ({ data: of({ userDeliveryInfo: new UserDeliveryInfo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SunTestModule],
        declarations: [UserDeliveryInfoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(UserDeliveryInfoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserDeliveryInfoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load userDeliveryInfo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userDeliveryInfo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
