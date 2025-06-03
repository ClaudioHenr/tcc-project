import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-grade-students',
  templateUrl: './grade-students.component.html',
  standalone: true,
  imports: [CommonModule],
  styleUrl: './grade-students.component.css'
})
export class GradeStudentsComponent implements OnInit {
  gradeName = 'Turma de Banco de Dados';
  gradeCode = 'ABC123';

  acceptedStudents = [
    { id: 1, name: 'Maria Silva', email: 'maria@email.com' },
    { id: 2, name: 'João Santos', email: 'joao@email.com' },
    { id: 3, name: 'Ana Costa', email: 'ana@email.com' },
  ];

  pendingRequests = [
    { id: 4, name: 'Carlos Lima', email: 'carlos@email.com' },
    { id: 5, name: 'Fernanda Souza', email: 'fernanda@email.com' },
  ];

  constructor(private router: Router) {}

  ngOnInit(): void {}

  acceptStudent(studentId: number) {
    const student = this.pendingRequests.find(s => s.id === studentId);
    if (student) {
      this.acceptedStudents.push(student);
      this.pendingRequests = this.pendingRequests.filter(s => s.id !== studentId);
    }
  }

  rejectStudent(studentId: number) {
    this.pendingRequests = this.pendingRequests.filter(s => s.id !== studentId);
  }

  removeStudent(studentId: number) {
    this.acceptedStudents = this.acceptedStudents.filter(s => s.id !== studentId);
  }

  viewStudentAnswers(studentId: number) {
    this.router.navigate(['/professor/student-answers'], {
      queryParams: { studentId },
    });
  }
}