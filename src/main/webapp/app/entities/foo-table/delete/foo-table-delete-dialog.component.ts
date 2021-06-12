import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFooTable } from '../foo-table.model';
import { FooTableService } from '../service/foo-table.service';

@Component({
  templateUrl: './foo-table-delete-dialog.component.html',
})
export class FooTableDeleteDialogComponent {
  fooTable?: IFooTable;

  constructor(protected fooTableService: FooTableService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fooTableService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
