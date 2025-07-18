import { CommonModule, Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ListexerciseService } from '../services/lists/listexercise.service';

@Component({
  selector: 'app-lists',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './lists.component.html',
  styleUrls: ['./lists.component.css']
})
export class ListsComponent implements OnInit {
  gradeId!: string;

  lists: any = [];

  constructor(
    private listExerciseService: ListexerciseService,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.gradeId = id;
      this.getListExercises(this.gradeId);
    }
  }

  getListExercises(gradeId: string) {
    this.listExerciseService.getListExercises(gradeId).subscribe({
      next: (data: any) => {
        this.lists = data;
      }, error: (err: any) => {
        console.log(err);
      }
    })
  }

  addExercise(listId: string): void {
    this.router.navigate(['/professor/exercise', listId])
  }

  viewExercises(listId: string): void {
    this.router.navigate(['/professor/exercise-list', listId])
  }

  addList(): void {
    this.router.navigate(['/professor/list-register', this.gradeId], { queryParams: {isEdit: false} });
  }

  editList(listId: number): void {
    this.router.navigate(['/professor/list-register', listId], { queryParams: {isEdit: true} });
    // Lógica para editar a lista com o ID específico
    console.log('Editar lista', listId);
  }


  back(): void {
    this.location.back();
  }
}