import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ButtonPrimaryComponent } from "../button-primary/button-primary.component";
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

interface FieldConfig {
  name: string;
  label: string;
  type: string;
  options?: { value: string | number; label: string }[];
  required: boolean;
  validators?: any[]
}

@Component({
  selector: 'app-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ButtonPrimaryComponent
  ],
  templateUrl: './form.component.html',
  styleUrl: './form.component.css'
})
export class FormComponent implements OnInit {
  @Input() mode: 'create' | 'edit' = 'create';
  @Input() initialData: any;
  @Input() fields: FieldConfig[] = [];
  @Output() formSubmit = new EventEmitter<any>();

  form!: FormGroup;
  isEditMode: boolean = false;

  ngOnInit(): void {
    this.form = new FormGroup({});
    this.fields.forEach(field => {
      this.form.addControl(field.name, new FormControl('', field.validators || []));
    });
    console.log(this.initialData);
    if (this.mode === 'edit' && this.initialData) {
      console.log("OnInit Form");
      this.form.patchValue(this.initialData);
      this.isEditMode = true;
      console.log(this.form.value);
    }
  }
  
  submitForm() {
    if (this.form.invalid) {
      this.form.markAllAsTouched(); // Marca todos os campos como "tocados" para exibir erros
      return;
    }
    console.log(this.form.value);
    this.formSubmit.emit(this.form.value);
  }

  getLabel(fieldName: string): string {
    const field = this.fields.find(f => f.name === fieldName);
    return field?.label || fieldName;
  }

  getFieldErrors(fieldName: string): string[] {
    const control = this.form.get(fieldName);
    const messages: string[] = [];

    if (!control || !control.errors || !control.touched) return messages;

    if (control.errors['required']) {
      messages.push(`${this.getLabel(fieldName)} é obrigatório.`);
    }
  
    if (fieldName == 'cpf' && control.errors['invalidCPF']) {
      messages.push('CPF inválido.');
    }

    if (control.errors['minlength']) {
      const requiredLength = control.errors['minlength'].requiredLength;
      messages.push(`${this.getLabel(fieldName)} deve ter pelo menos ${requiredLength} caracteres.`);
    }

    if (control.errors['email']) {
      messages.push('Email inválido.');
    }

    return messages;
  }

}
