import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFooTable, FooTable } from '../foo-table.model';

import { FooTableService } from './foo-table.service';

describe('Service Tests', () => {
  describe('FooTable Service', () => {
    let service: FooTableService;
    let httpMock: HttpTestingController;
    let elemDefault: IFooTable;
    let expectedResult: IFooTable | IFooTable[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FooTableService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        source: 'AAAAAAA',
        codeListCode: 'AAAAAAA',
        code: 0,
        displayValue: 'AAAAAAA',
        longDescription: 'AAAAAAA',
        fromDate: 'AAAAAAA',
        toDate: 'AAAAAAA',
        sortingPriority: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FooTable', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FooTable()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FooTable', () => {
        const returnedFromService = Object.assign(
          {
            source: 'BBBBBB',
            codeListCode: 'BBBBBB',
            code: 1,
            displayValue: 'BBBBBB',
            longDescription: 'BBBBBB',
            fromDate: 'BBBBBB',
            toDate: 'BBBBBB',
            sortingPriority: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a FooTable', () => {
        const patchObject = Object.assign(
          {
            source: 'BBBBBB',
            codeListCode: 'BBBBBB',
            sortingPriority: 1,
          },
          new FooTable()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FooTable', () => {
        const returnedFromService = Object.assign(
          {
            source: 'BBBBBB',
            codeListCode: 'BBBBBB',
            code: 1,
            displayValue: 'BBBBBB',
            longDescription: 'BBBBBB',
            fromDate: 'BBBBBB',
            toDate: 'BBBBBB',
            sortingPriority: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FooTable', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFooTableToCollectionIfMissing', () => {
        it('should add a FooTable to an empty array', () => {
          const fooTable: IFooTable = { code: 123 };
          expectedResult = service.addFooTableToCollectionIfMissing([], fooTable);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fooTable);
        });

        it('should not add a FooTable to an array that contains it', () => {
          const fooTable: IFooTable = { code: 123 };
          const fooTableCollection: IFooTable[] = [
            {
              ...fooTable,
            },
            { code: 456 },
          ];
          expectedResult = service.addFooTableToCollectionIfMissing(fooTableCollection, fooTable);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FooTable to an array that doesn't contain it", () => {
          const fooTable: IFooTable = { code: 123 };
          const fooTableCollection: IFooTable[] = [{ code: 456 }];
          expectedResult = service.addFooTableToCollectionIfMissing(fooTableCollection, fooTable);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fooTable);
        });

        it('should add only unique FooTable to an array', () => {
          const fooTableArray: IFooTable[] = [{ code: 123 }, { code: 456 }, { code: 81605 }];
          const fooTableCollection: IFooTable[] = [{ code: 123 }];
          expectedResult = service.addFooTableToCollectionIfMissing(fooTableCollection, ...fooTableArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fooTable: IFooTable = { code: 123 };
          const fooTable2: IFooTable = { code: 456 };
          expectedResult = service.addFooTableToCollectionIfMissing([], fooTable, fooTable2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fooTable);
          expect(expectedResult).toContain(fooTable2);
        });

        it('should accept null and undefined values', () => {
          const fooTable: IFooTable = { code: 123 };
          expectedResult = service.addFooTableToCollectionIfMissing([], null, fooTable, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fooTable);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
