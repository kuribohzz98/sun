import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SunTestModule } from '../../../test.module';
import { SpecificationsDetailComponent } from 'app/entities/specifications/specifications-detail.component';
import { Specifications } from 'app/shared/model/specifications.model';

describe('Component Tests', () => {
  describe('Specifications Management Detail Component', () => {
    let comp: SpecificationsDetailComponent;
    let fixture: ComponentFixture<SpecificationsDetailComponent>;
    const route = ({ data: of({ specifications: new Specifications(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SunTestModule],
        declarations: [SpecificationsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SpecificationsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SpecificationsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load specifications on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.specifications).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
