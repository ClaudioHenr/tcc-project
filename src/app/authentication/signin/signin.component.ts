import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports:  [
    FormsModule,
    CommonModule
  ],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css'
})
export class SigninComponent {
  email: string = '';
  senha: string = '';
  emailRecuperacao: string = '';
  captcha: string = '';
  mostrarModal: boolean = false;
  mostrarConfirmacao: boolean = false; 

  captchaNum1: number = 0;
  captchaNum2: number = 0;

  constructor(private router: Router) {}

  onLogin() {
    console.log('Email:', this.email);
    console.log('Senha:', this.senha);
  }

  cadastrar() {
    this.router.navigate(['/register/professor']);
  }

  abrirModal() {
    this.gerarCaptcha();
    this.mostrarModal = true;
  }

  fecharModal() {
    this.mostrarModal = false;
    this.emailRecuperacao = '';
    this.captcha = '';
  }

  abrirModalConfirmacao() {
    this.mostrarConfirmacao = true; 
  }

  fecharModalConfirmacao() {
    this.mostrarConfirmacao = false; 
  }

  confirmarRecuperacao() {
    
    if (!this.emailRecuperacao) {
      alert('Por favor, preencha o e-mail para continuar.');
      return;
    }

    this.fecharModal(); 
  }

  gerarCaptcha() {
    this.captchaNum1 = Math.floor(Math.random() * 10) + 1; 
    this.captchaNum2 = Math.floor(Math.random() * 10) + 1;
  }

  confirmarCancelamento() {
    this.fecharModal(); 
    this.fecharModalConfirmacao(); 
  }
}
