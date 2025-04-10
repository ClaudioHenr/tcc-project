import { AfterViewInit, Component } from '@angular/core';
import * as ace from 'ace-builds';

@Component({
  selector: 'app-student-exercice',
  standalone: true,
  imports: [],
  templateUrl: './student-exercice.component.html',
  styleUrl: './student-exercice.component.css'
})
export class StudentExerciceComponent implements AfterViewInit {
  imageUrl = ''; // Se quiser usar uma imagem, coloque a URL aqui
  private editor: ace.Ace.Editor | undefined;

  ngAfterViewInit(): void {
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
    return  this.editor?.getValue() || '';
  }

  enviarResposta(): void {
    const respostaSQL = this.editor?.getValue() || '';
    console.log('Resposta enviada:', respostaSQL);
  
    // this.http.post('/api/respostas', { resposta: respostaSQL }).subscribe(...)
    if (!respostaSQL.trim()) {
      alert('Por favor, escreva uma resposta antes de enviar.');
      return;
    }
  
    // Enviar resposta (aqui só mostramos no console)
    console.log('Resposta enviada:', respostaSQL);
    alert('Resposta enviada com sucesso!');
  }
}