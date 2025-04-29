import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { ProfessorService } from '../services/professor.service';

@Component({
  selector: 'app-self-registration',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './self-registration.component.html',
  styleUrl: './self-registration.component.css'
})
export class SelfRegistrationComponent {
  name = '';
  email = '';
  password = '';
  confirmPassword = '';

  errorMessages: string[] = []; 
  successMessage: string = '';
  showModal: boolean = false; // Variável para controlar a exibição da modal

  constructor(private professorService: ProfessorService, private router: Router) { }

  onSubmit() {
    if (this.password !== this.confirmPassword) {
      this.errorMessages = ['As senhas não coincidem!'];
      return;
    }

    const professorData = {
      name: this.name,
      email: this.email,
      password: this.password,
      confirmPassword: this.confirmPassword
    };

    this.professorService.registerProfessor(professorData).subscribe({
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
        console.error('Erro ao cadastrar professor:', err);
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
