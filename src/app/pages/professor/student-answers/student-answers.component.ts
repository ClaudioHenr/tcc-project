import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';

@Component({
  selector: 'app-student-answers',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './student-answers.component.html',
  styleUrls: ['./student-answers.component.css']
})
export class StudentAnswersComponent implements OnInit {

  student: any;
  exercises: any[] = [];

  constructor(
    private location: Location
  ) {}

  ngOnInit(): void {
    const mockData = {
      student: {
        id: 101,
        name: 'Maria Silva',
        email: 'maria.silva@example.com',
        grade: 'Turma A'
      },
      exercises: [
        {
          id: 1,
          title: 'SQL Básico - SELECT',
          description: "Faça uma pesquisa pelo usuário de id 2, trazendo todos os campos da tabela",
          type_exercise: 'SELECT',
          student_answers: [
            { is_correct: true, answer: 'SELECT * FROM users WHERE id=2;' },
            { is_correct: false, answer: 'SELECT users;' }
          ]
        },
        {
          id: 2,
          title: 'SQL Básico - UPDATE',
          description: "Atualize a idade de um usuário chamado Carlos para 28 anos. Certifique-se de que apenas o registro com o nome exato “Carlos” seja alterado.",
          type_exercise: 'UPDATE',
          student_answers: [
            { is_correct: true, answer: "UPDATE users SET age = 28 WHERE name = 'Carlos';" },
            { is_correct: false, answer: 'SELECT users;' }
          ]
        },
        {
          id: 3,
          title: 'SQL Básico - SELECT',
          description: "Escreva uma consulta SQL para selecionar todas as colunas da tabela 'Users'.",
          type_exercise: 'SELECT',
          student_answers: [
            { is_correct: true, answer: 'SELECT * FROM users;' },
            { is_correct: false, answer: 'SELECT users;' }
          ]
        },
        {
          id: 4,
          title: 'SQL Básico - INSERT',
          description: "Como você insere um novo registro na tabela 'Products' com 'name' = 'Laptop' e 'price' = 1200?",
          type_exercise: 'INSERT',
          student_answers: [
            { is_correct: true, answer: "INSERT INTO products (name, price) VALUES ('Laptop', 1200);" },
            { is_correct: false, answer: "ADD products ('Laptop', 1200);" }
          ]
        }
      ]
    };

    this.student = mockData.student;
    this.exercises = mockData.exercises;
  }

  back(): void {
    this.location.back();
  }
}