import { Component } from '@angular/core';
import { CardComponent } from '../../../components/card/card.component';

type CardInfo = {
  title: string;
  description: string;
  route: string;
}

@Component({
  selector: 'app-professor-home',
  standalone: true,
  imports: [
    CardComponent
  ],
  templateUrl: './professor-home.component.html',
  styleUrl: './professor-home.component.css'
})

export class ProfessorHomeComponent {
  cardsInfo: CardInfo[] = [
    { title: "Gerenciar Turmas", description: "Gerencie suas turmas", route: "/professor/grades" },
    { title: "Gerenciar Listas", description: "Gerencie as listas de exercícios", route: "/professor/lists" },
    { title: "Cadastrar Exercício", description: "Cadastre um novo exercício!", route: "/professor/exercise" },
    { title: "Ranking de Alunos", description: "Visualize o desempenho dos alunos", route: "/professor/student-ranking" } // New card
  ]
}