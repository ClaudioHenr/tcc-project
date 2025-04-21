import { Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

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
  registrationNumber = '';

 
  ngOnInit(): void {
    console.log('SelfRegistrationComponent inicializado');
  }
    
  onSubmit() {
    if (this.password !== this.confirmPassword) {
      alert('As senhas não coincidem!');
      return;
    }

    console.log({
      name: this.name,
      email: this.email,
      password: this.password,
      registrationNumber: this.registrationNumber,
    });

    alert('Cadastro enviado com sucesso!');
  }

}