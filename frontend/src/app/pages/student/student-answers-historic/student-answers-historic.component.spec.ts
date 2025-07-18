import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentAnswersHistoricComponent } from './student-answers-historic.component';

describe('StudentAnswersHistoricComponent', () => {
  let component: StudentAnswersHistoricComponent;
  let fixture: ComponentFixture<StudentAnswersHistoricComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentAnswersHistoricComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StudentAnswersHistoricComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
