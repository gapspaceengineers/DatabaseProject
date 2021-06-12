jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FooTableService } from '../service/foo-table.service';
import { IFooTable, FooTable } from '../foo-table.model';

import { FooTableUpdateComponent } from './foo-table-update.component';

describe('Component Tests', () => {
  describe('FooTable Management Update Component', () => {
    let comp: FooTableUpdateComponent;
    let fixture: ComponentFixture<FooTableUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fooTableService: FooTableService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FooTableUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FooTableUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FooTableUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fooTableService = TestBed.inject(FooTableService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const fooTable: IFooTable = { code: 456 };

        activatedRoute.data = of({ fooTable });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fooTable));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fooTable = { code: 123 };
        spyOn(fooTableService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fooTable });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fooTable }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fooTableService.update).toHaveBeenCalledWith(fooTable);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fooTable = new FooTable();
        spyOn(fooTableService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fooTable });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fooTable }));
        saveSubject.complete();

        // THEN
        expect(fooTableService.create).toHaveBeenCalledWith(fooTable);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fooTable = { code: 123 };
        spyOn(fooTableService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fooTable });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fooTableService.update).toHaveBeenCalledWith(fooTable);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
