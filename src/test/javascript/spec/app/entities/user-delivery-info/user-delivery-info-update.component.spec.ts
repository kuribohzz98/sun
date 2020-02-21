import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SunTestModule } from '../../../test.module';
import { UserDeliveryInfoUpdateComponent } from 'app/entities/user-delivery-info/user-delivery-info-update.component';
import { UserDeliveryInfoService } from 'app/entities/user-delivery-info/user-delivery-info.service';
import { UserDeliveryInfo } from 'app/shared/model/user-delivery-info.model';

describe('Component Tests', () => {
  describe('UserDeliveryInfo Management Update Component', () => {
    let comp: UserDeliveryInfoUpdateComponent;
    let fixture: ComponentFixture<UserDeliveryInfoUpdateComponent>;
    let service: UserDeliveryInfoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SunTestModule],
        declarations: [UserDeliveryInfoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UserDeliveryInfoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserDeliveryInfoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserDeliveryInfoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserDeliveryInfo(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserDeliveryInfo();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
