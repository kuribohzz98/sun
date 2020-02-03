import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SunTestModule } from '../../../test.module';
import { ProductEvaluateDetailComponent } from 'app/entities/product-evaluate/product-evaluate-detail.component';
import { ProductEvaluate } from 'app/shared/model/product-evaluate.model';

describe('Component Tests', () => {
  describe('ProductEvaluate Management Detail Component', () => {
    let comp: ProductEvaluateDetailComponent;
    let fixture: ComponentFixture<ProductEvaluateDetailComponent>;
    const route = ({ data: of({ productEvaluate: new ProductEvaluate(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SunTestModule],
        declarations: [ProductEvaluateDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ProductEvaluateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProductEvaluateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load productEvaluate on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.productEvaluate).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
