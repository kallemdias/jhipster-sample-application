import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PigeonService } from '../service/pigeon.service';
import { IPigeon } from '../pigeon.model';
import { PigeonFormService } from './pigeon-form.service';

import { PigeonUpdateComponent } from './pigeon-update.component';

describe('Pigeon Management Update Component', () => {
  let comp: PigeonUpdateComponent;
  let fixture: ComponentFixture<PigeonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pigeonFormService: PigeonFormService;
  let pigeonService: PigeonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PigeonUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PigeonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PigeonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pigeonFormService = TestBed.inject(PigeonFormService);
    pigeonService = TestBed.inject(PigeonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Pigeon query and add missing value', () => {
      const pigeon: IPigeon = { id: 456 };
      const mother: IPigeon = { id: 16555 };
      pigeon.mother = mother;
      const father: IPigeon = { id: 8301 };
      pigeon.father = father;

      const pigeonCollection: IPigeon[] = [{ id: 5747 }];
      jest.spyOn(pigeonService, 'query').mockReturnValue(of(new HttpResponse({ body: pigeonCollection })));
      const additionalPigeons = [mother, father];
      const expectedCollection: IPigeon[] = [...additionalPigeons, ...pigeonCollection];
      jest.spyOn(pigeonService, 'addPigeonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pigeon });
      comp.ngOnInit();

      expect(pigeonService.query).toHaveBeenCalled();
      expect(pigeonService.addPigeonToCollectionIfMissing).toHaveBeenCalledWith(
        pigeonCollection,
        ...additionalPigeons.map(expect.objectContaining),
      );
      expect(comp.pigeonsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pigeon: IPigeon = { id: 456 };
      const mother: IPigeon = { id: 18450 };
      pigeon.mother = mother;
      const father: IPigeon = { id: 20954 };
      pigeon.father = father;

      activatedRoute.data = of({ pigeon });
      comp.ngOnInit();

      expect(comp.pigeonsSharedCollection).toContain(mother);
      expect(comp.pigeonsSharedCollection).toContain(father);
      expect(comp.pigeon).toEqual(pigeon);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPigeon>>();
      const pigeon = { id: 123 };
      jest.spyOn(pigeonFormService, 'getPigeon').mockReturnValue(pigeon);
      jest.spyOn(pigeonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pigeon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pigeon }));
      saveSubject.complete();

      // THEN
      expect(pigeonFormService.getPigeon).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pigeonService.update).toHaveBeenCalledWith(expect.objectContaining(pigeon));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPigeon>>();
      const pigeon = { id: 123 };
      jest.spyOn(pigeonFormService, 'getPigeon').mockReturnValue({ id: null });
      jest.spyOn(pigeonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pigeon: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pigeon }));
      saveSubject.complete();

      // THEN
      expect(pigeonFormService.getPigeon).toHaveBeenCalled();
      expect(pigeonService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPigeon>>();
      const pigeon = { id: 123 };
      jest.spyOn(pigeonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pigeon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pigeonService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePigeon', () => {
      it('Should forward to pigeonService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pigeonService, 'comparePigeon');
        comp.comparePigeon(entity, entity2);
        expect(pigeonService.comparePigeon).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
