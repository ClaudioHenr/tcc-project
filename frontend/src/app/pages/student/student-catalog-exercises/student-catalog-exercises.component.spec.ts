import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentCatalogExercisesComponent } from './student-catalog-exercises.component';

describe('StudentCatalogExercisesComponent', () => {
  let component: StudentCatalogExercisesComponent;
  let fixture: ComponentFixture<StudentCatalogExercisesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentCatalogExercisesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StudentCatalogExercisesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
