import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ExerciseService } from '../services/exercise/exercise.service';

type exercise = {
  id: string;
  description: string;
  dialect: string;
  type: string
}

@Component({
  selector: 'app-student-catalog-exercises',
  standalone: true,
  imports: [],
  templateUrl: './student-catalog-exercises.component.html',
  styleUrl: './student-catalog-exercises.component.css'
})
export class StudentCatalogExercisesComponent implements OnInit {
  items: exercise[] = []

  constructor(
    private ExerciseService: ExerciseService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get("id");
    if (id) {
      this.fetch(id);
    }
  }

  fetch(id: string) {
    this.ExerciseService.getExercisesByListExerciseId(id).subscribe({
      next: (data: any) => {
        this.items = data;
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  solveExercise(id: string) {
    this.router.navigate(['student/exercise', id])
  }

}
