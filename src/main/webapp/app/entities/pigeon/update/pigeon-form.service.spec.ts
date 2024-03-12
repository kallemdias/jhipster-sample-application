import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pigeon.test-samples';

import { PigeonFormService } from './pigeon-form.service';

describe('Pigeon Form Service', () => {
  let service: PigeonFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PigeonFormService);
  });

  describe('Service methods', () => {
    describe('createPigeonFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPigeonFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ringNumber: expect.any(Object),
            name: expect.any(Object),
            breeder: expect.any(Object),
            gender: expect.any(Object),
            birthYear: expect.any(Object),
            colorPattern: expect.any(Object),
            longDescription: expect.any(Object),
            mediumDescription: expect.any(Object),
            shortDescription: expect.any(Object),
            mother: expect.any(Object),
            father: expect.any(Object),
          }),
        );
      });

      it('passing IPigeon should create a new form with FormGroup', () => {
        const formGroup = service.createPigeonFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ringNumber: expect.any(Object),
            name: expect.any(Object),
            breeder: expect.any(Object),
            gender: expect.any(Object),
            birthYear: expect.any(Object),
            colorPattern: expect.any(Object),
            longDescription: expect.any(Object),
            mediumDescription: expect.any(Object),
            shortDescription: expect.any(Object),
            mother: expect.any(Object),
            father: expect.any(Object),
          }),
        );
      });
    });

    describe('getPigeon', () => {
      it('should return NewPigeon for default Pigeon initial value', () => {
        const formGroup = service.createPigeonFormGroup(sampleWithNewData);

        const pigeon = service.getPigeon(formGroup) as any;

        expect(pigeon).toMatchObject(sampleWithNewData);
      });

      it('should return NewPigeon for empty Pigeon initial value', () => {
        const formGroup = service.createPigeonFormGroup();

        const pigeon = service.getPigeon(formGroup) as any;

        expect(pigeon).toMatchObject({});
      });

      it('should return IPigeon', () => {
        const formGroup = service.createPigeonFormGroup(sampleWithRequiredData);

        const pigeon = service.getPigeon(formGroup) as any;

        expect(pigeon).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPigeon should not enable id FormControl', () => {
        const formGroup = service.createPigeonFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPigeon should disable id FormControl', () => {
        const formGroup = service.createPigeonFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
