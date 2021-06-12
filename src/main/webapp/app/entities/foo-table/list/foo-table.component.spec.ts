import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FooTableService } from '../service/foo-table.service';

import { FooTableComponent } from './foo-table.component';

describe('Component Tests', () => {
  describe('FooTable Management Component', () => {
    let comp: FooTableComponent;
    let fixture: ComponentFixture<FooTableComponent>;
    let service: FooTableService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FooTableComponent],
      })
        .overrideTemplate(FooTableComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FooTableComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FooTableService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ code: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.fooTables?.[0]).toEqual(jasmine.objectContaining({ code: 123 }));
    });
  });
});
