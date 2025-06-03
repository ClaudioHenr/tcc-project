import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { exercise } from '../../../models/exercise';
import { answer } from '../../../models/answer';

@Component({
  selector: 'app-exercise',
  templateUrl: './exercise.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule],
  styleUrls: ['./exercise.component.css']
})
export class ExerciseComponent implements OnInit {
  tiposSQL: string[] = ['SELECT', 'INSERT', 'UPDATE', 'DELETE', 'CREATE', 'DROP', 'ALTER'];

  isEditing: boolean = false;

  exercise: exercise = {
    id: 0,
    title: '',
    description: '',
    image: '',
    sort: false,
    public: false,
    id_professor: 1,
    id_list: 0,
    dialect: '',
    type_exercise: '',
    reference_table: ''
  };

  answer: answer = {
    id: 0,
    answer: '',
    id_exercise: this.exercise.id,
    id_professor: this.exercise.id_professor
  };

  ngOnInit(): void {
    // 🔧 Simulação de edição (se necessário, descomente)
    /*
    this.isEditing = true;
    this.exercise = {
      id: 5,
      title: 'Exercício de exemplo',
      description: 'Descrição de exemplo',
      image: 'imagem.png',
      sort: true,
      public: false,
      id_professor: 1,
      id_list: 2,
      dialect: '',
      type_exercise: 'SELECT'
    };
    this.answer = {
      id: 1,
      answer: 'SELECT * FROM alunos',
      id_exercise: 5,
      id_professor: 1
    };
    */
  }

  onImageUpload(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.exercise.image = file.name;
    }
  }

  salvarExercicio() {
    if (
      !this.exercise.type_exercise ||
      !this.exercise.title.trim() ||
      !this.exercise.description.trim() ||
      !this.answer.answer.trim()
    ) {
      console.warn('Formulário inválido.');
      return;
    }

    if (this.isEditing) {
      console.log('Exercício editado:', this.exercise);
    } else {
      console.log('Exercício criado:', this.exercise);
    }
    console.log('Resposta correta:', this.answer);

    // chamada ao backend futuramente
  }
}
