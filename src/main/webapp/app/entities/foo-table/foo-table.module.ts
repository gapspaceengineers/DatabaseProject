import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FooTableComponent } from './list/foo-table.component';
import { FooTableDetailComponent } from './detail/foo-table-detail.component';
import { FooTableUpdateComponent } from './update/foo-table-update.component';
import { FooTableDeleteDialogComponent } from './delete/foo-table-delete-dialog.component';
import { FooTableRoutingModule } from './route/foo-table-routing.module';

@NgModule({
  imports: [SharedModule, FooTableRoutingModule],
  declarations: [FooTableComponent, FooTableDetailComponent, FooTableUpdateComponent, FooTableDeleteDialogComponent],
  entryComponents: [FooTableDeleteDialogComponent],
})
export class FooTableModule {}
