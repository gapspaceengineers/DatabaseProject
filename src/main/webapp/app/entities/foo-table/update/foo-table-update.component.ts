import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFooTable, FooTable } from '../foo-table.model';
import { FooTableService } from '../service/foo-table.service';

@Component({
  selector: 'jhi-foo-table-update',
  templateUrl: './foo-table-update.component.html',
})
export class FooTableUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    source: [],
    codeListCode: [],
    code: [],
    displayValue: [],
    longDescription: [],
    fromDate: [],
    toDate: [],
    sortingPriority: [],
  });

  constructor(protected fooTableService: FooTableService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fooTable }) => {
      this.updateForm(fooTable);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fooTable = this.createFromForm();
    if (fooTable.code !== undefined) {
      this.subscribeToSaveResponse(this.fooTableService.update(fooTable));
    } else {
      this.subscribeToSaveResponse(this.fooTableService.create(fooTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFooTable>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fooTable: IFooTable): void {
    this.editForm.patchValue({
      source: fooTable.source,
      codeListCode: fooTable.codeListCode,
      code: fooTable.code,
      displayValue: fooTable.displayValue,
      longDescription: fooTable.longDescription,
      fromDate: fooTable.fromDate,
      toDate: fooTable.toDate,
      sortingPriority: fooTable.sortingPriority,
    });
  }

  protected createFromForm(): IFooTable {
    return {
      ...new FooTable(),
      source: this.editForm.get(['source'])!.value,
      codeListCode: this.editForm.get(['codeListCode'])!.value,
      code: this.editForm.get(['code'])!.value,
      displayValue: this.editForm.get(['displayValue'])!.value,
      longDescription: this.editForm.get(['longDescription'])!.value,
      fromDate: this.editForm.get(['fromDate'])!.value,
      toDate: this.editForm.get(['toDate'])!.value,
      sortingPriority: this.editForm.get(['sortingPriority'])!.value,
    };
  }
}
