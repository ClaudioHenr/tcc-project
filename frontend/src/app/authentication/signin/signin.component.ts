import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { CommonModule } from '@angular/common';
import { TokenService } from '../../core/services/token.service';
import { AppRoles } from '../../core/constants/roles.const.enum';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css'
})

export class SigninComponent {
  //email: string = '';
  //senha: string = '';
  emailRecuperacao: string = '';
  captcha: string = '';
  mostrarModal: boolean = false;
  mostrarConfirmacao: boolean = false;

  email = signal('');
  senha = signal('');
  isLoading = signal(false);
  errorMessage = signal<string | null>(null);

  captchaNum1: number = 0;
  captchaNum2: number = 0;

  constructor(
    private authService: AuthService,
    private tokenService: TokenService,
    private router: Router
  ) { }

  rememberMe = signal(false);

  onLogin() {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.authService.login(this.email(), this.senha()).subscribe({
      next: (response) => {
        this.tokenService.saveToken(response.token, this.rememberMe());

        const userRole = this.tokenService.getUserRole();

        if (userRole === AppRoles.PROFESSOR) {
          // this.router.navigate(['/professor/home']);
          this.router.navigate(['/professor/grades']);
        } else if (userRole === AppRoles.STUDENT) {
          this.router.navigate(['/student/grades']);
        } else {
          this.router.navigate(['/']);
        }
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.message || 'Email ou senha incorretos');
      }
    });
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
