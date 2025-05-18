import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ListexerciseService } from '../services/lists/listexercise.service';

type listExercise = {
  id: string;
  title: string;
  description: string
}

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [

  ],
  templateUrl: './student-list.component.html',
  styleUrl: './student-list.component.css'
})
export class StudentListComponent implements OnInit {

  items: listExercise[] = []

  constructor(
    private listExerciseService: ListexerciseService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    console.log(id);
    if (id) {
      this.fetch(id);
    }
  }

  fetch(id: string) {
    this.listExerciseService.getListExercises(id).subscribe({
      next: (data: any) => {
        this.items = data;
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  viewExercises(id: string) {
    this.router.navigate(['student/catalog-exercises', id]);
  }

  back() {
    this.location.back();
  }

}