import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PigeonComponent } from './list/pigeon.component';
import { PigeonDetailComponent } from './detail/pigeon-detail.component';
import { PigeonUpdateComponent } from './update/pigeon-update.component';
import PigeonResolve from './route/pigeon-routing-resolve.service';

const pigeonRoute: Routes = [
  {
    path: '',
    component: PigeonComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PigeonDetailComponent,
    resolve: {
      pigeon: PigeonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PigeonUpdateComponent,
    resolve: {
      pigeon: PigeonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PigeonUpdateComponent,
    resolve: {
      pigeon: PigeonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pigeonRoute;
