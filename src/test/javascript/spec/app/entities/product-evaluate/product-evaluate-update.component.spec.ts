import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SunTestModule } from '../../../test.module';
import { ProductEvaluateUpdateComponent } from 'app/entities/product-evaluate/product-evaluate-update.component';
import { ProductEvaluateService } from 'app/entities/product-evaluate/product-evaluate.service';
import { ProductEvaluate } from 'app/shared/model/product-evaluate.model';

describe('Component Tests', () => {
  describe('ProductEvaluate Management Update Component', () => {
    let comp: ProductEvaluateUpdateComponent;
    let fixture: ComponentFixture<ProductEvaluateUpdateComponent>;
    let service: ProductEvaluateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SunTestModule],
        declarations: [ProductEvaluateUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ProductEvaluateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductEvaluateUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProductEvaluateService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ProductEvaluate(123);
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
        const entity = new ProductEvaluate();
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
