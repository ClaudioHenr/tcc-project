import { Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {

  name = '';
  email = '';
  confirmEmail = '';
  registrationNumber = '';

  ngOnInit(): void {
    console.log('SelfRegistrationComponent inicializado');
  }

  onSubmit() {
    if (this.email !== this.confirmEmail) {
      alert('Os emails não coincidem!');
      return;
    }

    console.log({
      name: this.name,
      email: this.email,
      confirmEmail: this.confirmEmail,
      registrationNumber: this.registrationNumber,
    });

    alert('Cadastro enviado com sucesso!');
  }


}