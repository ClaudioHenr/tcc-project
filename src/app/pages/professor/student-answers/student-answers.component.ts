import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

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
          description: "Escreva uma consulta SQL para selecionar todas as colunas da tabela 'Users'.",
          type_exercise: 'SQL',
          student_answers: [
            { is_correct: true, answer: 'SELECT * FROM Users;' },
            { is_correct: false, answer: 'SELECT Users;' }
          ]
        },
        {
          id: 2,
          title: 'SQL Básico - INSERT',
          description: "Como você insere um novo registro na tabela 'Products' com 'name' = 'Laptop' e 'price' = 1200?",
          type_exercise: 'SQL',
          student_answers: [
            { is_correct: true, answer: "INSERT INTO Products (name, price) VALUES ('Laptop', 1200);" },
            { is_correct: false, answer: "ADD Products ('Laptop', 1200);" }
          ]
        }
      ]
    };

    this.student = mockData.student;
    this.exercises = mockData.exercises;
  }
}