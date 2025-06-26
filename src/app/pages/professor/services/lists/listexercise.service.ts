import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap, throwError, Observable, of } from 'rxjs'; // Adicionado 'of'
import { delay } from 'rxjs/operators';
import { list } from '../../../../models/list'; // Certifique-se de que este caminho está correto

@Injectable({
  providedIn: 'root'
})
export class ListexerciseService {
  api: String = environment.apiUrl;

  constructor(
    private http: HttpClient
  ) { }

  /**
   * Obtém as listas de exercícios para uma determinada turma.
   * Alterna entre dados mockados e o backend real.
   * @param gradeId O ID da turma.
   */
  getListExercises(gradeId: string): Observable<any[]> {
    if (environment.mockData) {
      return this.getMockListExercises(gradeId);
    } else {
      return this.http.get<any[]>(`${this.api}/grades/listexercises`, { params: { id: gradeId } })
        .pipe(
          tap((data) => console.log('Lists data (backend):', data)),
          catchError((err: HttpErrorResponse) => {
            console.error('Error fetching lists (backend):', err);
            return throwError(() => new Error('Failed to load lists. Please try again later.'));
          })
        );
    }
  }

  /**
   * Obtém uma lista de exercícios específica por ID.
   * @param idList O ID da lista.
   */
  getListExerciseById(idList: string): Observable<list> {
    if (environment.mockData) {
      return this.getMockListExerciseById(idList);
    } else {
      return this.http.get<list>(`${this.api}/api/listexercise/${idList}`)
        .pipe(
          tap((data) => console.log('List by ID data (backend):', data)),
          catchError((err: HttpErrorResponse) => {
            console.error('Error fetching list by ID (backend):', err);
            return throwError(() => new Error('Failed to load list by ID. Please try again later.'));
          })
        );
    }
  }

  private getMockListExercises(gradeId: string): Observable<any[]> {
    console.log(`Using mock data for lists for gradeId: ${gradeId}`);
    let mockLists: any[] = [];

    if (gradeId === '1') { // Turma A
      mockLists = [
        { id: 10, title: 'Lista SQL Básico', description: 'Exercícios introdutórios de SQL.' },
        { id: 11, title: 'Lista Joins e Subqueries', description: 'Exercícios avançados de Joins.' },
      ];
    } else if (gradeId === '2') { // Turma B
      mockLists = [
        { id: 20, title: 'Lista HTML/CSS', description: 'Fundamentos de Web.' },
        { id: 21, title: 'Lista JavaScript Básico', description: 'Primeiros passos em JS.' },
      ];
    } else {
      mockLists = [];
    }
    return of(mockLists).pipe(delay(300));
  }

  private getMockListExerciseById(idList: string): Observable<list> {
    console.log(`Using mock data for list with ID: ${idList}`);
    let mockList: list | undefined;

    if (idList === '10') {
      mockList = {
        id: 10,
        title: 'Lista SQL Básico',
        description: 'Exercícios introdutórios de SQL para iniciantes.',
        exerciseIds: [1, 2, 3] // Exemplo de IDs de exercícios associados
      };
    } else if (idList === '11') {
      mockList = {
        id: 11,
        title: 'Lista Joins e Subqueries',
        description: 'Desafios com JOINs e subconsultas.',
        exerciseIds: [4, 5]
      };
    } else {
      mockList = { id: 0, title: '', description: '', exerciseIds: [] }; // Lista vazia ou padrão
    }
    return of(mockList).pipe(delay(300));
  }

  // Seus outros métodos (createListExercise, etc.) permanecem inalterados
  createListExercise(gradeId: string, listData: list) {
    // Implemente a lógica de mock para criação se necessário, ou chame o backend
    return this.http.post(`${this.api}/api/listexercise/create?id=${gradeId}`, listData)
      .pipe(
        tap((data) => console.log('List created (backend):', data)),
        catchError((err: HttpErrorResponse) => {
          console.error('Error creating list (backend):', err);
          return throwError(() => new Error('Failed to create list. Please try again later.'));
        })
      );
  }
}