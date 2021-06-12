jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFooTable, FooTable } from '../foo-table.model';
import { FooTableService } from '../service/foo-table.service';

import { FooTableRoutingResolveService } from './foo-table-routing-resolve.service';

describe('Service Tests', () => {
  describe('FooTable routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FooTableRoutingResolveService;
    let service: FooTableService;
    let resultFooTable: IFooTable | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FooTableRoutingResolveService);
      service = TestBed.inject(FooTableService);
      resultFooTable = undefined;
    });

    describe('resolve', () => {
      it('should return IFooTable returned by find', () => {
        // GIVEN
        service.find = jest.fn(code => of(new HttpResponse({ body: { code } })));
        mockActivatedRouteSnapshot.params = { code: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFooTable = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFooTable).toEqual({ code: 123 });
      });

      it('should return new IFooTable if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFooTable = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFooTable).toEqual(new FooTable());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { code: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFooTable = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFooTable).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
