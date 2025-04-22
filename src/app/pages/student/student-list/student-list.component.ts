import { Component } from '@angular/core';
import { CardComponent } from "../../../components/card/card.component";

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [CardComponent],
  templateUrl: './student-list.component.html',
  styleUrl: './student-list.component.css'
})
export class StudentListComponent {

  cards: any = [
    {title: "Lista 01", "description": "", "route": "/student/exercises"},
    {title: "Lista 02", "description": "", "route": ""},
    {title: "Lista 03", "description": "", "route": ""},
    {title: "Lista 04", "description": "", "route": ""},
    {title: "Lista 05", "description": "", "route": ""}
  ]

}