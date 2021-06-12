import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FooTableComponent } from '../list/foo-table.component';
import { FooTableDetailComponent } from '../detail/foo-table-detail.component';
import { FooTableUpdateComponent } from '../update/foo-table-update.component';
import { FooTableRoutingResolveService } from './foo-table-routing-resolve.service';

const fooTableRoute: Routes = [
  {
    path: '',
    component: FooTableComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':code/view',
    component: FooTableDetailComponent,
    resolve: {
      fooTable: FooTableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FooTableUpdateComponent,
    resolve: {
      fooTable: FooTableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':code/edit',
    component: FooTableUpdateComponent,
    resolve: {
      fooTable: FooTableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fooTableRoute)],
  exports: [RouterModule],
})
export class FooTableRoutingModule {}
