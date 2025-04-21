import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-button-primary',
  standalone: true,
  imports: [],
  templateUrl: './button-primary.component.html',
  styleUrl: './button-primary.component.css'
})
export class ButtonPrimaryComponent {

  @Output() eventMessage = new EventEmitter<string>(); // envia do filho para o pai

  onClick() {
    this.eventMessage.emit("Botão clicado")
  }

}
