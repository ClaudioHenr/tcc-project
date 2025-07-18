import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CardComponent } from "../../../components/card/card.component";

type CardInfo = {
  title: string;
  description: string;
  route: string;
}

@Component({
  selector: 'app-student-home',
  standalone: true,
  imports: [
    CardComponent
],
  templateUrl: './student-home.component.html',
  styleUrl: './student-home.component.css'
})
export class StudentHomeComponent {
  cardsInfo: CardInfo[] = [
    { title: "Turmas", description: "Navegue entre suas turmas", route: "/student/grades" },
    { title: "Listas", description: "Procure lista de exercícios para realizar!", route: "/student/lists" }
  ]

}
