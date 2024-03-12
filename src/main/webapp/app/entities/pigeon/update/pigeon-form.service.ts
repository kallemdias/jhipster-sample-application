import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPigeon, NewPigeon } from '../pigeon.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPigeon for edit and NewPigeonFormGroupInput for create.
 */
type PigeonFormGroupInput = IPigeon | PartialWithRequiredKeyOf<NewPigeon>;

type PigeonFormDefaults = Pick<NewPigeon, 'id'>;

type PigeonFormGroupContent = {
  id: FormControl<IPigeon['id'] | NewPigeon['id']>;
  ringNumber: FormControl<IPigeon['ringNumber']>;
  name: FormControl<IPigeon['name']>;
  breeder: FormControl<IPigeon['breeder']>;
  gender: FormControl<IPigeon['gender']>;
  birthYear: FormControl<IPigeon['birthYear']>;
  colorPattern: FormControl<IPigeon['colorPattern']>;
  longDescription: FormControl<IPigeon['longDescription']>;
  mediumDescription: FormControl<IPigeon['mediumDescription']>;
  shortDescription: FormControl<IPigeon['shortDescription']>;
  mother: FormControl<IPigeon['mother']>;
  father: FormControl<IPigeon['father']>;
};

export type PigeonFormGroup = FormGroup<PigeonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PigeonFormService {
  createPigeonFormGroup(pigeon: PigeonFormGroupInput = { id: null }): PigeonFormGroup {
    const pigeonRawValue = {
      ...this.getFormDefaults(),
      ...pigeon,
    };
    return new FormGroup<PigeonFormGroupContent>({
      id: new FormControl(
        { value: pigeonRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      ringNumber: new FormControl(pigeonRawValue.ringNumber, {
        validators: [Validators.required],
      }),
      name: new FormControl(pigeonRawValue.name),
      breeder: new FormControl(pigeonRawValue.breeder, {
        validators: [Validators.required],
      }),
      gender: new FormControl(pigeonRawValue.gender),
      birthYear: new FormControl(pigeonRawValue.birthYear),
      colorPattern: new FormControl(pigeonRawValue.colorPattern),
      longDescription: new FormControl(pigeonRawValue.longDescription),
      mediumDescription: new FormControl(pigeonRawValue.mediumDescription),
      shortDescription: new FormControl(pigeonRawValue.shortDescription),
      mother: new FormControl(pigeonRawValue.mother),
      father: new FormControl(pigeonRawValue.father),
    });
  }

  getPigeon(form: PigeonFormGroup): IPigeon | NewPigeon {
    return form.getRawValue() as IPigeon | NewPigeon;
  }

  resetForm(form: PigeonFormGroup, pigeon: PigeonFormGroupInput): void {
    const pigeonRawValue = { ...this.getFormDefaults(), ...pigeon };
    form.reset(
      {
        ...pigeonRawValue,
        id: { value: pigeonRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PigeonFormDefaults {
    return {
      id: null,
    };
  }
}
