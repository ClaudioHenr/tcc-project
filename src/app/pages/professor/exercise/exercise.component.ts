import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { exercise } from '../../../models/exercise';  // Corrigir o caminho de importação conforme necessário
import { answer } from '../../../models/answer';  // Importando a model de resposta

@Component({
  selector: 'app-exercise',
  templateUrl: './exercise.component.html',
  standalone: true,
  imports: [FormsModule],  // Importando FormsModule para usar ngModel
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
    id_professor: 0,  // Será atribuído dinamicamente
    id_list: 0,  // Será atribuído dinamicamente
    dialect: '',
    type_exercise: ''
  };

  // Iniciando respostas alternativas como um array de objects baseados na model 'answer'
  respostasAlternativas: answer[] = [{ id: 0, answer: '', id_exercise: this.exercise.id, id_professor: this.exercise.id_professor }];
  adicionarResposta() {
    this.respostasAlternativas.push({ id: 0, answer: '', id_exercise: this.exercise.id, id_professor: this.exercise.id_professor });
  }

  removerResposta(index: number) {
    this.respostasAlternativas.splice(index, 1);
  }

  onImageUpload(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.exercise.image = file.name;  // Salva apenas o nome do arquivo para o exercício
    }
  }

  salvarExercicio() {
    // Aqui você pode chamar o serviço que salva o exercício no backend
    console.log('Exercício salvo:', this.exercise);
    console.log('Respostas alternativas:', this.respostasAlternativas);
  }
}