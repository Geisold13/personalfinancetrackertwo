import { TestBed } from '@angular/core/testing';

import { TransactionreportService } from './transactionreport.service';

describe('TransactionreportService', () => {
  let service: TransactionreportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransactionreportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
