import { CommonModule, Location } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-student-answers-historic',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './student-answers-historic.component.html',
  styleUrl: './student-answers-historic.component.css'
})
export class StudentAnswersHistoricComponent {

  infoHistoric: any;
  tries: any;
  student: any;
  exercise: any;

  constructor(
    private location: Location
  ) {}

  ngOnInit(): void {
    const mockData = {
      infoHistoric: {
        gradeName: 'Banco de Dados I',
        listName: 'Lista de exercícios 1'
      },
      student: {
        id: 101,
        name: 'Maria Silva',
        email: 'maria.silva@example.com',
        grade: 'Turma A'
      },
      tries: [
        { is_correct: true, answer: 'SELECT * FROM users WHERE id=2;' },
        { is_correct: true, answer: 'SELECT * FROM users WHERE id=2;' },
        { is_correct: false, answer: 'SELECT * FROM users id=2;' },
        { is_correct: false, answer: 'SELECT * FROM users;' },
        { is_correct: false, answer: 'SELECT * FROM users WHERE name=2;' },
        { is_correct: false, answer: 'SELECT * FR;' },
      ],
      exercise: {
        title: 'Consulte o usuário',
        description: "Faça uma pesquisa pelo usuário de id 2, trazendo todos os campos da tabela",
        dialect: 'POSTGRESQL',
        type_exercise: 'SELECT',
      }
    };

    this.infoHistoric = mockData.infoHistoric
    this.tries = mockData.tries
    this.student = mockData.student;
    this.exercise = mockData.exercise;
  }

  back(): void {
    this.location.back();
  }

}
