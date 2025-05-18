import { TestBed } from '@angular/core/testing';

import { ListexerciseService } from './listexercise.service';

describe('ListexerciseService', () => {
  let service: ListexerciseService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ListexerciseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
