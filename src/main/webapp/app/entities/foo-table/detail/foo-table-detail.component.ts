import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFooTable } from '../foo-table.model';

@Component({
  selector: 'jhi-foo-table-detail',
  templateUrl: './foo-table-detail.component.html',
})
export class FooTableDetailComponent implements OnInit {
  fooTable: IFooTable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fooTable }) => {
      this.fooTable = fooTable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
