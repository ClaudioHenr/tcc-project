import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';  // Importar RouterModule
import { SelfRegistrationComponent } from '../register/professor/self-registration/self-registration.component';
import { RegistrationComponent } from '../register/student/registration/registration.component';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, RouterModule],  // Adicionar RouterModule aqui
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {}
