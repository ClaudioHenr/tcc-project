import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { list } from '../../../models/list';
import { FormsModule } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { GradeService } from '../services/grade/grade.service';
import { ExerciseService } from '../services/exercise/exercise.service';
import { ListexerciseService } from '../services/lists/listexercise.service';

@Component({
  selector: 'app-list-register',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './list-register.component.html',
  styleUrl: './list-register.component.css'
})
export class ListRegisterComponent implements OnInit  {
  list: list = {
    id: 0,
    title: '',
    description: '',
    exerciseIds: [],
  };

  gradeId: string = '';

  grades?: { id: number; name: string }[];
  exercises!: any;

  isEditMode = false;

  constructor(
    private gradeService: GradeService,
    private exerciseService: ExerciseService,
    private listExerciseService: ListexerciseService, 
    private router: Router, 
    private route: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.isEditMode = params['isEdit'] === 'true';
    })
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.gradeId = id;
      if (this.isEditMode) {
        this.loadExerciseList(id);
        this.loadExercisesByUserId();
      }
    }
  }

  loadExercisesByUserId() {
    this.exerciseService.getExercises().subscribe({
      next: (data: any) => {
        console.log(data);
        this.exercises = data;
      }, error: (err: any) => {
        console.log(err);
        // alert("Erro ao recuperar exercícios, tente novamente mais tarde")
      }
    })
  }

  loadExerciseList(idList: string) {
    this.listExerciseService.getListExerciseById(idList).subscribe({
      next: (data: any) => {
        this.list = data;
      }, error: (err: any) => {
        console.log(err)
        alert("Erro ao trazer informações da lista, tente novamente mais tarde");
      }
    })
  }

  save() {
    this.listExerciseService.createListExercise(this.gradeId, this.list).subscribe({
      next: (data: any) => {
        this.grades = data;
        alert("Lista criada com sucesso");
      }, error: (err: any) => {
        console.log(err)
        alert("Erro ao salvar lista, tente novamente mais tarde");
      }
    })
    this.location.back();
  }

  edit() {
    
  }

  cancel() {
    this.location.back();
  }

  onCheckboxChange(id: number, type: 'grade' | 'exercise') {
    const targetArray = this.list.exerciseIds;
    const index = targetArray.indexOf(id);

    if (index === -1) {
      targetArray.push(id);
    } else {
      targetArray.splice(index, 1);
    }
  }
}