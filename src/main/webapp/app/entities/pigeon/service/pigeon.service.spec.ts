import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPigeon } from '../pigeon.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pigeon.test-samples';

import { PigeonService } from './pigeon.service';

const requireRestSample: IPigeon = {
  ...sampleWithRequiredData,
};

describe('Pigeon Service', () => {
  let service: PigeonService;
  let httpMock: HttpTestingController;
  let expectedResult: IPigeon | IPigeon[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PigeonService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Pigeon', () => {
      const pigeon = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pigeon).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pigeon', () => {
      const pigeon = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pigeon).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pigeon', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pigeon', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pigeon', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPigeonToCollectionIfMissing', () => {
      it('should add a Pigeon to an empty array', () => {
        const pigeon: IPigeon = sampleWithRequiredData;
        expectedResult = service.addPigeonToCollectionIfMissing([], pigeon);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pigeon);
      });

      it('should not add a Pigeon to an array that contains it', () => {
        const pigeon: IPigeon = sampleWithRequiredData;
        const pigeonCollection: IPigeon[] = [
          {
            ...pigeon,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPigeonToCollectionIfMissing(pigeonCollection, pigeon);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pigeon to an array that doesn't contain it", () => {
        const pigeon: IPigeon = sampleWithRequiredData;
        const pigeonCollection: IPigeon[] = [sampleWithPartialData];
        expectedResult = service.addPigeonToCollectionIfMissing(pigeonCollection, pigeon);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pigeon);
      });

      it('should add only unique Pigeon to an array', () => {
        const pigeonArray: IPigeon[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pigeonCollection: IPigeon[] = [sampleWithRequiredData];
        expectedResult = service.addPigeonToCollectionIfMissing(pigeonCollection, ...pigeonArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pigeon: IPigeon = sampleWithRequiredData;
        const pigeon2: IPigeon = sampleWithPartialData;
        expectedResult = service.addPigeonToCollectionIfMissing([], pigeon, pigeon2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pigeon);
        expect(expectedResult).toContain(pigeon2);
      });

      it('should accept null and undefined values', () => {
        const pigeon: IPigeon = sampleWithRequiredData;
        expectedResult = service.addPigeonToCollectionIfMissing([], null, pigeon, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pigeon);
      });

      it('should return initial array if no Pigeon is added', () => {
        const pigeonCollection: IPigeon[] = [sampleWithRequiredData];
        expectedResult = service.addPigeonToCollectionIfMissing(pigeonCollection, undefined, null);
        expect(expectedResult).toEqual(pigeonCollection);
      });
    });

    describe('comparePigeon', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePigeon(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePigeon(entity1, entity2);
        const compareResult2 = service.comparePigeon(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePigeon(entity1, entity2);
        const compareResult2 = service.comparePigeon(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePigeon(entity1, entity2);
        const compareResult2 = service.comparePigeon(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
