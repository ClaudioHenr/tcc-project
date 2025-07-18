import { AfterViewInit, Component, OnInit } from '@angular/core';
import * as ace from 'ace-builds';
import { ExerciseService } from '../services/exercise/exercise.service';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { TokenService } from '../../../core/services/token.service';

interface Exercise {
  id: string;
  title: string;
  description: string;
  dialect: string;
  type: string
}

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
export class StudentExerciceComponent implements OnInit, AfterViewInit {
  protected studentId: string | null = '';
  protected exerciseId: string | null = '';
  protected exerciseInfo!: Exercise;
  protected queryExercise!: QueryExercise;
  responseExercise!: ResponseExercise;

  imageUrl = './../../../../assets/images/der_exercises_tables.png'; // Se quiser usar uma imagem, coloque a URL aqui
  private editor: ace.Ace.Editor | undefined;

  errorMessage: string = '';

  constructor(
    private tokenExercise: TokenService,
    private exerciseService: ExerciseService,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.studentId = this.tokenExercise.getUserId();

    this.exerciseId = this.route.snapshot.paramMap.get("id");
    if (this.exerciseId) {
      this.getExerciseInfo(this.exerciseId);
    }
  }

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

  getExerciseInfo(id: string) {
    this.exerciseService.getExercise(id).subscribe({
      next: (data: any) => {
        this.exerciseInfo = data;
        console.log(data)
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  sendAnswer(): void {
    const respostaSQL = this.editor?.getValue() || '';

    if (!respostaSQL.trim()) {
      alert('Por favor, escreva uma resposta antes de enviar.');
      return;
    }

    if (this.exerciseId && this.studentId) {
      const queryExercise: QueryExercise = {
        studentId: this.studentId,
        exerciseId: this.exerciseId,
        query: respostaSQL
      };

      console.log("QUERYEXERCISE: " + queryExercise.studentId)

      this.exerciseService.sentAnswer(queryExercise).subscribe({
        next: (data: ResponseExercise) => {
          this.responseExercise = data;
        },
        error: (err: any) => {
          this.errorMessage = err;
        }
      });
    } else {
      console.error("Não é possível enviar resposta por falta de parametros obrigatórios");
    }
  }

  back() {
    this.location.back();
  }

}