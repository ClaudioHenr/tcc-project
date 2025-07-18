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
  dataUserTable: any = [
    { id: 1, name: 'Luiz Fernando', age: 30, birth_date: '1994-05-15', has_driver_license: true },
    { id: 2, name: 'Elisabeth', age: 30, birth_date: '1996-07-22', has_driver_license: false },
    { id: 3, name: 'Marcos Silva', age: 30, birth_date: '1989-03-10', has_driver_license: true },
    { id: 4, name: 'Ana Paula', age: 30, birth_date: '1998-09-05', has_driver_license: false },
    { id: 5, name: 'Fernando Costa', age: 40, birth_date: '1984-12-18', has_driver_license: true },
    { id: 6, name: 'Juliana Mendes', age: 30, birth_date: '1995-06-30', has_driver_license: true },
    { id: 7, name: 'Ricardo Almeida', age: 32, birth_date: '1992-08-14', has_driver_license: false },
    { id: 8, name: 'Tatiane Souza', age: 27, birth_date: '1997-11-03', has_driver_license: true },
    { id: 9, name: 'Gustavo Lima', age: 29, birth_date: '1993-04-25', has_driver_license: false },
    { id: 10, name: 'Beatriz Rocha', age: 33, birth_date: '1991-10-08', has_driver_license: true },
  ];
  dataOrderTable: any = [
    { userId: 1, order_date: '2024-03-01', total_amount: 150.75, status: 'Pendente' },
    { userId: 1, order_date: '2024-03-02', total_amount: 200.50, status: 'Finalizado' },
    { userId: 1, order_date: '2024-03-03', total_amount: 89.99, status: 'Cancelado' },
    { userId: 4, order_date: '2024-03-04', total_amount: 120.00, status: 'Pendente' },
    { userId: 4, order_date: '2024-03-05', total_amount: 300.25, status: 'Finalizado' },
    { userId: 6, order_date: '2024-03-06', total_amount: 50.00, status: 'Pendente' },
    { userId: 7, order_date: '2024-03-07', total_amount: 450.60, status: 'Finalizado' },
    { userId: 8, order_date: '2024-03-08', total_amount: 99.99, status: 'Cancelado' },
    { userId: 9, order_date: '2024-03-09', total_amount: 210.40, status: 'Pendente' },
    { userId: 10, order_date: '2024-03-10', total_amount: 75.30, status: 'Finalizado' },
  ];


  tiposSQL: string[] = ['SELECT', 'INSERT', 'UPDATE', 'DELETE', 'CREATE', 'DROP'];
  dialectTypes: string[] = ['POSTGRESQL', 'MYSQL'];
  tableNames: string[] = ['users', 'orders'];
  showDataTables: boolean = false;
  isEditing: boolean = false;

  exercise: createExercise = {
    title: '',
    description: '',
    dialect: '',
    type: '',
    sort: false,
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

  toggleMaisInfo() {
    this.showDataTables = !this.showDataTables;
  }

  back() {
    this.location.back();
  }

}
