import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; // caso queira navegar para outra rota futuramente
import { exercise } from '../../../models/exercise';

@Component({
  selector: 'app-exercise-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './exercise-list.component.html',
  styleUrls: ['./exercise-list.component.css']
})
export class ExerciseListComponent {
  exercicios: exercise[] = [
    { id: 1, description: 'Listar todos os alunos', image: '', sort: false, public: true, id_professor: 1, id_list: 1, dialect: '', type_exercise: 'SELECT' },
    { id: 2, description: 'Inserir novo curso', image: '', sort: false, public: false, id_professor: 1, id_list: 1, dialect: '', type_exercise: 'INSERT' }
  ];

  adicionarExercicio() {
    // Aqui você pode navegar para o componente de cadastro
    console.log('Navegar para tela de novo exercício');
  }

  editarExercicio(exercicio: exercise) {
    console.log('Editar exercício:', exercicio);
  }

  removerExercicio(exercicio: exercise) {
    this.exercicios = this.exercicios.filter(e => e.id !== exercicio.id);
  }
}