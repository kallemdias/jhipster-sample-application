import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { Gender } from 'app/entities/enumerations/gender.model';
import { ColorPattern } from 'app/entities/enumerations/color-pattern.model';
import { PigeonService } from '../service/pigeon.service';
import { IPigeon } from '../pigeon.model';
import { PigeonFormService, PigeonFormGroup } from './pigeon-form.service';

@Component({
  standalone: true,
  selector: 'jhi-pigeon-update',
  templateUrl: './pigeon-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PigeonUpdateComponent implements OnInit {
  isSaving = false;
  pigeon: IPigeon | null = null;
  genderValues = Object.keys(Gender);
  colorPatternValues = Object.keys(ColorPattern);

  pigeonsSharedCollection: IPigeon[] = [];

  editForm: PigeonFormGroup = this.pigeonFormService.createPigeonFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected pigeonService: PigeonService,
    protected pigeonFormService: PigeonFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePigeon = (o1: IPigeon | null, o2: IPigeon | null): boolean => this.pigeonService.comparePigeon(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pigeon }) => {
      this.pigeon = pigeon;
      if (pigeon) {
        this.updateForm(pigeon);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('pigeonal001App.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pigeon = this.pigeonFormService.getPigeon(this.editForm);
    if (pigeon.id !== null) {
      this.subscribeToSaveResponse(this.pigeonService.update(pigeon));
    } else {
      this.subscribeToSaveResponse(this.pigeonService.create(pigeon));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPigeon>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pigeon: IPigeon): void {
    this.pigeon = pigeon;
    this.pigeonFormService.resetForm(this.editForm, pigeon);

    this.pigeonsSharedCollection = this.pigeonService.addPigeonToCollectionIfMissing<IPigeon>(
      this.pigeonsSharedCollection,
      pigeon.mother,
      pigeon.father,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pigeonService
      .query()
      .pipe(map((res: HttpResponse<IPigeon[]>) => res.body ?? []))
      .pipe(
        map((pigeons: IPigeon[]) =>
          this.pigeonService.addPigeonToCollectionIfMissing<IPigeon>(pigeons, this.pigeon?.mother, this.pigeon?.father),
        ),
      )
      .subscribe((pigeons: IPigeon[]) => (this.pigeonsSharedCollection = pigeons));
  }
}
