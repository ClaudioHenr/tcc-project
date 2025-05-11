import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-lists',
  templateUrl: './lists.component.html',
  styleUrls: ['./lists.component.css']
})
export class ListsComponent implements OnInit {
  // Simulando listas de exemplo
  lists = [
    { id: 1, name: 'Lista 1', active: true },
    { id: 2, name: 'Lista 2', active: false },
    { id: 3, name: 'Lista 3', active: true },
  ];

  constructor() {}

  ngOnInit(): void {
    // Aqui você pode buscar as listas da API, por exemplo
  }

  addList(): void {
    // Lógica para adicionar uma nova lista
    console.log('Adicionar nova lista');
  }

  editList(listId: number): void {
    // Lógica para editar a lista com o ID específico
    console.log('Editar lista', listId);
  }

  toggleListStatus(listId: number): void {
    // Aqui alternamos o status da lista entre ativo e inativo
    const list = this.lists.find(l => l.id === listId);
    if (list) {
      list.active = !list.active;
    }
  }
}