import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPigeon, NewPigeon } from '../pigeon.model';

export type PartialUpdatePigeon = Partial<IPigeon> & Pick<IPigeon, 'id'>;

export type EntityResponseType = HttpResponse<IPigeon>;
export type EntityArrayResponseType = HttpResponse<IPigeon[]>;

@Injectable({ providedIn: 'root' })
export class PigeonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pigeons');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(pigeon: NewPigeon): Observable<EntityResponseType> {
    return this.http.post<IPigeon>(this.resourceUrl, pigeon, { observe: 'response' });
  }

  update(pigeon: IPigeon): Observable<EntityResponseType> {
    return this.http.put<IPigeon>(`${this.resourceUrl}/${this.getPigeonIdentifier(pigeon)}`, pigeon, { observe: 'response' });
  }

  partialUpdate(pigeon: PartialUpdatePigeon): Observable<EntityResponseType> {
    return this.http.patch<IPigeon>(`${this.resourceUrl}/${this.getPigeonIdentifier(pigeon)}`, pigeon, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPigeon>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPigeon[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPigeonIdentifier(pigeon: Pick<IPigeon, 'id'>): number {
    return pigeon.id;
  }

  comparePigeon(o1: Pick<IPigeon, 'id'> | null, o2: Pick<IPigeon, 'id'> | null): boolean {
    return o1 && o2 ? this.getPigeonIdentifier(o1) === this.getPigeonIdentifier(o2) : o1 === o2;
  }

  addPigeonToCollectionIfMissing<Type extends Pick<IPigeon, 'id'>>(
    pigeonCollection: Type[],
    ...pigeonsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pigeons: Type[] = pigeonsToCheck.filter(isPresent);
    if (pigeons.length > 0) {
      const pigeonCollectionIdentifiers = pigeonCollection.map(pigeonItem => this.getPigeonIdentifier(pigeonItem)!);
      const pigeonsToAdd = pigeons.filter(pigeonItem => {
        const pigeonIdentifier = this.getPigeonIdentifier(pigeonItem);
        if (pigeonCollectionIdentifiers.includes(pigeonIdentifier)) {
          return false;
        }
        pigeonCollectionIdentifiers.push(pigeonIdentifier);
        return true;
      });
      return [...pigeonsToAdd, ...pigeonCollection];
    }
    return pigeonCollection;
  }
}
