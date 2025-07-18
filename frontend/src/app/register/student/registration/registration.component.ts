import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { StudentService } from './services/student.service';  

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent implements OnInit {

  name = '';
  email = '';
  confirmEmail = '';
  registrationNumber = '';
  confirmPassword = '';
  password = '';
  errorMessages: string[] = [];
  successMessage: string = '';
  showModal: boolean = false; 

  constructor(private studentService: StudentService, private router: Router) {}  

  ngOnInit(): void {
    console.log('RegistrationComponent inicializado');
  }

  onSubmit() {
    if (this.password !== this.confirmPassword) {
      this.errorMessages = ['As senhas não coincidem!'];
      return;
    }

    if (this.email !== this.confirmEmail) {
      this.errorMessages = ['Os e-mails não coincidem!'];
      return;
    }

    const studentData = {
      name: this.name,
      email: this.email,
      confirmEmail: this.confirmEmail,
      registrationNumber: this.registrationNumber,
      password: this.password,
      confirmPassword: this.confirmPassword
    };

    this.studentService.registerStudent(studentData).subscribe({
      next: (res: any) => {
        console.log('Cadastro realizado com sucesso:', res);
        this.successMessage = 'Cadastro realizado com sucesso!';
        this.errorMessages = []; 
        this.showModal = true; 
        setTimeout(() => {
          this.router.navigate(['/auth/signin']); 
        }, 5000);
      },
      error: (err: any) => {
        console.error('Erro ao cadastrar estudante:', err);
        if (Array.isArray(err)) {
          this.errorMessages = err;  
        } else if (typeof err === 'string') {
          this.errorMessages = [err];
        } else {
          this.errorMessages = [err.message || 'Erro desconhecido'];
        }
        this.successMessage = ''; 
      }
    });
  }

  redirectNow() {
    this.router.navigate(['/auth/signin']);
  }
}
