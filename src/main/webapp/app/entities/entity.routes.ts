import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'pigeon',
    data: { pageTitle: 'pigeonal001App.pigeon.home.title' },
    loadChildren: () => import('./pigeon/pigeon.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
