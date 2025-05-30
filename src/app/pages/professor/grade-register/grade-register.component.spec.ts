import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GradeRegisterComponent } from './grade-register.component';

describe('GradeRegisterComponent', () => {
  let component: GradeRegisterComponent;
  let fixture: ComponentFixture<GradeRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GradeRegisterComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GradeRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
