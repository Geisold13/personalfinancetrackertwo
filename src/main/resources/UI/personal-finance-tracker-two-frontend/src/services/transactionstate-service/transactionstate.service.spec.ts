import { TestBed } from '@angular/core/testing';

import { TransactionstateService } from './transactionstate.service';

describe('TransactionstateService', () => {
  let service: TransactionstateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransactionstateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
