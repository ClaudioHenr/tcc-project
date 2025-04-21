import { AfterViewInit, Component } from '@angular/core';
import * as ace from 'ace-builds';
import { ExerciseService } from '../services/exercise.service';
import { CommonModule } from '@angular/common';

interface QueryExercise {
  studentId: string;
  exerciseId: string;
  query: string;
};

interface ResponseExercise {
  isCorrect: boolean;
  resultQuery: Array<Record<string, any>>
};

@Component({
  selector: 'app-student-exercice',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './student-exercice.component.html',
  styleUrl: './student-exercice.component.css'
})
export class StudentExerciceComponent implements AfterViewInit {
  imageUrl = ''; // Se quiser usar uma imagem, coloque a URL aqui
  private editor: ace.Ace.Editor | undefined;
  private queryExercise: QueryExercise = {"studentId": "1", "exerciseId": "3", "query": ""};
  responseExercise!: ResponseExercise;

  errorMessage: string = '';

  constructor(
    private exerciseService: ExerciseService
  ) {}

  ngAfterViewInit(): void {
    ace.config.set('basePath', 'https://unpkg.com/ace-builds@1.4.12/src-noconflict');
    const aceEditor = ace.edit('editor');
    aceEditor.setTheme('ace/theme/monokai');
    aceEditor.session.setMode('ace/mode/sql');
    aceEditor.setOptions({
      fontSize: '14px',
      wrap: true,
      showPrintMargin: false
    });

    aceEditor.setValue("SELECT * FROM users WHERE age > 30;");

    this.editor = aceEditor;
  }

  getEditorValue(): string {
    return this.editor?.getValue() || '';
  }

  getTableKeys(): string[] {
    if (this.responseExercise && this.responseExercise.resultQuery.length > 0) {
      return Object.keys(this.responseExercise.resultQuery[0]);
    }
    return [];
  }

  enviarResposta(): void {
    const respostaSQL = this.editor?.getValue() || '';
    console.log('Resposta enviada:', respostaSQL);

    if (!respostaSQL.trim()) {
      alert('Por favor, escreva uma resposta antes de enviar.');
      return;
    }
    this.queryExercise.query = respostaSQL
    this.exerciseService.sentAnswer(this.queryExercise).subscribe({
      next: (data: ResponseExercise) => {
        this.responseExercise = data;
      },
      error: (err: any) => {
        this.errorMessage = err;
      }
    });
  }

}