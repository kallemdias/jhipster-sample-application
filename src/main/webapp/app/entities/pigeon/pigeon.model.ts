import { Gender } from 'app/entities/enumerations/gender.model';
import { ColorPattern } from 'app/entities/enumerations/color-pattern.model';

export interface IPigeon {
  id: number;
  ringNumber?: string | null;
  name?: string | null;
  breeder?: string | null;
  gender?: keyof typeof Gender | null;
  birthYear?: number | null;
  colorPattern?: keyof typeof ColorPattern | null;
  longDescription?: string | null;
  mediumDescription?: string | null;
  shortDescription?: string | null;
  mother?: Pick<IPigeon, 'id'> | null;
  father?: Pick<IPigeon, 'id'> | null;
}

export type NewPigeon = Omit<IPigeon, 'id'> & { id: null };
