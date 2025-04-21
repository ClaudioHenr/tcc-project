import { Component } from '@angular/core';
import { Validators } from '@angular/forms';
import { FormComponent } from "../../components/form/form.component";

interface FieldConfig {
  name: string;
  label: string;
  type: string;
  required: boolean;
  validators?: any[]
}

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [FormComponent],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css'
})
export class SigninComponent {
  mode: 'create' | 'edit' = 'create'; 
  fields: FieldConfig[] = [
    {name: "login", label: "Email", type: "text", required: true, validators: [Validators.required, Validators.email]},
    {name: "password", label: "Senha", type: "password", required: true, validators: [Validators.required]}
  ];
}
