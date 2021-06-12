import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FooTableDetailComponent } from './foo-table-detail.component';

describe('Component Tests', () => {
  describe('FooTable Management Detail Component', () => {
    let comp: FooTableDetailComponent;
    let fixture: ComponentFixture<FooTableDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FooTableDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ fooTable: { code: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FooTableDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FooTableDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fooTable on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fooTable).toEqual(jasmine.objectContaining({ code: 123 }));
      });
    });
  });
});
