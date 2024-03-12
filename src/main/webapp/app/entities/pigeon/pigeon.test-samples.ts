import { IPigeon, NewPigeon } from './pigeon.model';

export const sampleWithRequiredData: IPigeon = {
  id: 1935,
  ringNumber: 'trespass afford',
  breeder: 'whoa even',
};

export const sampleWithPartialData: IPigeon = {
  id: 17337,
  ringNumber: 'promptly',
  breeder: 'behind though',
  longDescription: '../fake-data/blob/hipster.txt',
  mediumDescription: 'psst ramen',
};

export const sampleWithFullData: IPigeon = {
  id: 26178,
  ringNumber: 'weepy diffract cyclone',
  name: 'hopeful',
  breeder: 'since below keen',
  gender: 'UNKNOWN',
  birthYear: 9185,
  colorPattern: 'PIED',
  longDescription: '../fake-data/blob/hipster.txt',
  mediumDescription: 'and wall',
  shortDescription: 'bashfully',
};

export const sampleWithNewData: NewPigeon = {
  ringNumber: 'probe',
  breeder: 'incidentally curiously amid',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
