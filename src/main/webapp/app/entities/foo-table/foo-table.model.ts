export interface IFooTable {
  source?: string | null;
  codeListCode?: string | null;
  code?: number;
  displayValue?: string | null;
  longDescription?: string | null;
  fromDate?: string | null;
  toDate?: string | null;
  sortingPriority?: number | null;
}

export class FooTable implements IFooTable {
  constructor(
    public source?: string | null,
    public codeListCode?: string | null,
    public code?: number,
    public displayValue?: string | null,
    public longDescription?: string | null,
    public fromDate?: string | null,
    public toDate?: string | null,
    public sortingPriority?: number | null
  ) {}
}

export function getFooTableIdentifier(fooTable: IFooTable): number | undefined {
  return fooTable.code;
}
