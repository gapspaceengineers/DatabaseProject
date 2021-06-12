import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFooTable, getFooTableIdentifier } from '../foo-table.model';

export type EntityResponseType = HttpResponse<IFooTable>;
export type EntityArrayResponseType = HttpResponse<IFooTable[]>;

@Injectable({ providedIn: 'root' })
export class FooTableService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/foo-tables');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(fooTable: IFooTable): Observable<EntityResponseType> {
    return this.http.post<IFooTable>(this.resourceUrl, fooTable, { observe: 'response' });
  }

  update(fooTable: IFooTable): Observable<EntityResponseType> {
    return this.http.put<IFooTable>(`${this.resourceUrl}/${getFooTableIdentifier(fooTable) as number}`, fooTable, { observe: 'response' });
  }

  partialUpdate(fooTable: IFooTable): Observable<EntityResponseType> {
    return this.http.patch<IFooTable>(`${this.resourceUrl}/${getFooTableIdentifier(fooTable) as number}`, fooTable, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFooTable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFooTable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFooTableToCollectionIfMissing(fooTableCollection: IFooTable[], ...fooTablesToCheck: (IFooTable | null | undefined)[]): IFooTable[] {
    const fooTables: IFooTable[] = fooTablesToCheck.filter(isPresent);
    if (fooTables.length > 0) {
      const fooTableCollectionIdentifiers = fooTableCollection.map(fooTableItem => getFooTableIdentifier(fooTableItem)!);
      const fooTablesToAdd = fooTables.filter(fooTableItem => {
        const fooTableIdentifier = getFooTableIdentifier(fooTableItem);
        if (fooTableIdentifier == null || fooTableCollectionIdentifiers.includes(fooTableIdentifier)) {
          return false;
        }
        fooTableCollectionIdentifiers.push(fooTableIdentifier);
        return true;
      });
      return [...fooTablesToAdd, ...fooTableCollection];
    }
    return fooTableCollection;
  }
}
