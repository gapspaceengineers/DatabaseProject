import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFooTable } from '../foo-table.model';
import { FooTableService } from '../service/foo-table.service';
import { FooTableDeleteDialogComponent } from '../delete/foo-table-delete-dialog.component';

@Component({
  selector: 'jhi-foo-table',
  templateUrl: './foo-table.component.html',
})
export class FooTableComponent implements OnInit {
  fooTables?: IFooTable[];
  isLoading = false;

  constructor(protected fooTableService: FooTableService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.fooTableService.query().subscribe(
      (res: HttpResponse<IFooTable[]>) => {
        this.isLoading = false;
        this.fooTables = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackCode(index: number, item: IFooTable): number {
    return item.code!;
  }

  delete(fooTable: IFooTable): void {
    const modalRef = this.modalService.open(FooTableDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fooTable = fooTable;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
