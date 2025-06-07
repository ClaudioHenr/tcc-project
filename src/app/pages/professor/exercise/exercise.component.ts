import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { ExerciseService } from '../services/exercise/exercise.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../../core/services/token.service';

interface createExercise {
  title: string,
  description: string,
  dialect: string,
  type: string,
  sort: boolean,
  // image: string,  // Não tem no back
  public: boolean,
  tableName: string
  listId: string,
  professorId: string,
  answerProfessor: string
}

@Component({
  selector: 'app-exercise',
  templateUrl: './exercise.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule],
  styleUrls: ['./exercise.component.css']
})
export class ExerciseComponent implements OnInit {
  tiposSQL: string[] = ['SELECT', 'INSERT', 'UPDATE', 'DELETE', 'CREATE', 'DROP', 'ALTER'];
  dialectTypes: string[] = ['POSTGRESQL', 'MYSQL'];
  tableNames: string[] = ['users', 'orders'];

  isEditing: boolean = false;

  exercise: createExercise = {
    title: '',
    description: '',
    dialect: '',
    type: '',
    sort: false,
    // image: '',  // Não tem no back
    public: false,
    tableName: '',
    listId: '',
    professorId: '',
    answerProfessor: ''
  };

  constructor(
    private exerciseService: ExerciseService,
    private tokenService: TokenService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.exercise.listId = id;
      this.exercise.professorId = this.tokenService.getUserId();
    }
  }

  // onImageUpload(event: any) {
  //   const file = event.target.files[0];
  //   if (file) {
  //     this.exercise.image = file.name;
  //   }
  // }

  save(form: NgForm) {
    if (
      !this.exercise.type ||
      !this.exercise.title.trim() ||
      !this.exercise.description.trim() ||
      !this.exercise.answerProfessor.trim()
    ) {
      console.warn('Formulário inválido.');
      return;
    }
    console.log(this.exercise);
    this.exerciseService.create(this.exercise).subscribe({
      next: (res: any) => {
        console.log(res);
        alert("Exercício cadastrado com sucesso");
        form.reset();
      }, error: (err: any) => {
        console.log(err);
      }
    })
  }

  back() {
    this.location.back();
  }

}
