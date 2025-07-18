import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentExerciceComponent } from './student-exercice.component';

describe('StudentExerciceComponent', () => {
  let component: StudentExerciceComponent;
  let fixture: ComponentFixture<StudentExerciceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentExerciceComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StudentExerciceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
