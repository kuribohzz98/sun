import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SunTestModule } from '../../../test.module';
import { SpecificationsUpdateComponent } from 'app/entities/specifications/specifications-update.component';
import { SpecificationsService } from 'app/entities/specifications/specifications.service';
import { Specifications } from 'app/shared/model/specifications.model';

describe('Component Tests', () => {
  describe('Specifications Management Update Component', () => {
    let comp: SpecificationsUpdateComponent;
    let fixture: ComponentFixture<SpecificationsUpdateComponent>;
    let service: SpecificationsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SunTestModule],
        declarations: [SpecificationsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SpecificationsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpecificationsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SpecificationsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Specifications(123);
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
        const entity = new Specifications();
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
