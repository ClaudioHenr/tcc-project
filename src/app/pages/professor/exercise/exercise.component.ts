import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common'; // ✅ Importa CommonModule
import { exercise } from '../../../models/exercise';
import { answer } from '../../../models/answer';

@Component({
  selector: 'app-exercise',
  templateUrl: './exercise.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule],  // ✅ Adiciona CommonModule aqui
  styleUrls: ['./exercise.component.css']
})
export class ExerciseComponent {
  tiposSQL: string[] = ['SELECT', 'INSERT', 'UPDATE', 'DELETE', 'CREATE', 'DROP', 'ALTER'];  

  exercise: exercise = {
    id: 0,
    description: '',
    image: '',
    sort: false,
    public: false,
    id_professor: 0,
    id_list: 0,
    dialect: '',
    type_exercise: ''
  };

  respostasAlternativas: answer[] = [
    { id: 0, answer: '', id_exercise: this.exercise.id, id_professor: this.exercise.id_professor }
  ];

  adicionarResposta() {
    this.respostasAlternativas.push({
      id: 0,
      answer: '',
      id_exercise: this.exercise.id,
      id_professor: this.exercise.id_professor
    });
  }

  removerResposta(index: number) {
    this.respostasAlternativas.splice(index, 1);
  }

  onImageUpload(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.exercise.image = file.name;
    }
  }

  temRespostaValida(): boolean {
    return this.respostasAlternativas.some(resposta => resposta.answer.trim() !== '');
  }
  
  salvarExercicio() {
    // Validação manual (caso o ngForm falhe por algum motivo)
    if (!this.exercise.type_exercise || !this.exercise.description.trim() || !this.temRespostaValida()) {
      console.warn('Formulário inválido.');
      return;
    }
  
    console.log('Exercício salvo:', this.exercise);
    console.log('Respostas alternativas:', this.respostasAlternativas);
  }
}