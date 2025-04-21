import { Component } from '@angular/core';
import { FormComponent } from "../../components/form/form.component";
import { Validators } from '@angular/forms';

interface FieldConfig {
  name: string;
  label: string;
  type: string;
  options?: { value: string | number; label: string }[];
  required: boolean;
  validators?: any[]
}

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [FormComponent],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  mode: 'create' | 'edit' = 'create'; 
  fields: FieldConfig[] = [
    {name: "fullName", label: "Nome", type: "text", required: true, validators: [Validators.required]},
    {name: "login", label: "Email", type: "text", required: true, validators: [Validators.required, Validators.email]},
    {name: "registration", label: "Matricula", type: "text", required: true, validators: [Validators.required]},
    {name: "password", label: "Senha", type: "password", required: true, validators: [Validators.required]},
    {name: "repeat_password", label: "Confirmação de senha", type: "password", required: true, validators: [Validators.required]}
  ];


}
