import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { list } from '../../../models/list';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-list-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './list-register.component.html',
  styleUrl: './list-register.component.css'
})
export class ListRegisterComponent implements OnInit  {
  list: list = {
    id: 0,
    name: '',
    description: '',
    dueDate: undefined,
    gradeIds: [],
    exerciseIds: [],
  };

  grades: { id: number; name: string }[] = [];
  exercises: { id: number; description: string }[] = [];

  isEditMode = false;

  constructor(private router: Router, private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Aqui você verifica se é edição
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.loadExerciseList(parseInt(id));
    }

    // Carrega turmas e exercícios
    this.loadGrades();
    this.loadExercises();
  }

  loadGrades() {
    // Simulando dados, substituir por chamada API
    this.grades = [
      { id: 1, name: 'Turma A' },
      { id: 2, name: 'Turma B' },
      { id: 3, name: 'Turma C' },
    ];
  }

  loadExercises() {
    // Simulando dados, substituir por chamada API
    this.exercises = [
      { id: 101, description: 'Exercício SELECT' },
      { id: 102, description: 'Exercício INSERT' },
      { id: 103, description: 'Exercício JOIN' },
    ];
  }

  loadExerciseList(id: number) {
    // Simular carregamento da lista existente (para edição)
    this.list = {
      id: id,
      name: 'Lista de SQL',
      description: 'Prática de comandos básicos',
      dueDate: new Date(),
      gradeIds: [1, 2],
      exerciseIds: [101, 102],
    };
  }

  save() {
    if (this.isEditMode) {
      console.log('Atualizando lista:', this.list);
      // chamada para atualizar no backend
    } else {
      console.log('Criando nova lista:', this.list);
      // chamada para criar no backend
    }

    // Após salvar, redireciona
    this.router.navigate(['/professor/lists']);
  }

  cancel() {
    this.router.navigate(['/professor/lists']);
  }

  onCheckboxChange(id: number, type: 'grade' | 'exercise') {
    const targetArray = type === 'grade' ? this.list.gradeIds : this.list.exerciseIds;
    const index = targetArray.indexOf(id);

    if (index === -1) {
      targetArray.push(id);
    } else {
      targetArray.splice(index, 1);
    }
  }
}