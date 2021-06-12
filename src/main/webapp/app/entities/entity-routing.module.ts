import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'foo-table',
        data: { pageTitle: 'FooTables' },
        loadChildren: () => import('./foo-table/foo-table.module').then(m => m.FooTableModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
