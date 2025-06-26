import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError, of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { environment } from '../../../../../environments/environment';
import { TokenService } from '../../../../core/services/token.service';

@Injectable({
  providedIn: 'root'
})
export class GradeService {
  api: String = environment.apiUrl;
  userId = this.tokenService.getUserId();

  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) { }

  /**
   * Obtém as turmas associadas ao professor logado.
   * A função alterna entre buscar dados do backend ou retornar dados mockados,
   * dependendo da configuração 'environment.mockData'.
   * @returns Um Observable de um array de objetos de turma.
   */
  getGrades(): Observable<any[]> {
    // Verifica se o mocking de dados está ativado no ambiente
    if (environment.mockData) {
      // Se sim, retorna dados mockados
      return this.getMockGrades();
    } else {
      // Se não, procede com a chamada HTTP real para o backend
      const id = this.userId;
      // IMPORTANTE: Verifica se o ID do usuário está disponível antes de fazer a requisição
      if (!id) {
        // Se não houver ID de usuário (ex: sem token), lança um erro imediatamente como um Observable
        return throwError(() => new Error('ID de usuário não disponível. Por favor, faça login novamente.'));
      }

      // CORREÇÃO: Tipar o retorno do get para any[]
      return this.http.get<any[]>(`${this.api}/grades/list`, { // <--- Adicionado <any[]> aqui
        params: { "id": id }
      })
      .pipe(
        tap((data) => console.log('Dados das turmas (backend):', data)), // Agora 'data' é inferido como any[]
        catchError((err: HttpErrorResponse) => {
          console.error('Erro no GradeService (Frontend) getGrades (backend):', err);

          let errorMessage = 'Erro desconhecido ao carregar turmas.';
          if (err.error instanceof ErrorEvent) {
            errorMessage = `Um erro de rede ocorreu: ${err.error.message}`;
          } else if (typeof err.error === 'string') {
            errorMessage = err.error;
          } else if (err.error && typeof err.error === 'object' && err.error.message) {
            errorMessage = err.error.message;
          } else if (err.status) {
            errorMessage = `Erro ${err.status}: ${err.statusText || 'Falha na comunicação com o servidor'}`;
            if (err.status === 403) {
              errorMessage = 'Acesso negado. Você não tem permissão para ver as turmas.';
            } else if (err.status === 401) {
              errorMessage = 'Sessão expirada ou não autenticada. Por favor, faça login novamente.';
            }
          }
          return throwError(() => new Error(errorMessage));
        })
      );
    }
  }

  /**
   * Gera dados mockados para as turmas.
   * Simula uma pequena latência de rede usando `delay`.
   * @returns Um Observable de um array de objetos de turma mockados.
   */
  private getMockGrades(): Observable<any[]> {
    console.log('Usando dados mockados para turmas.');
    const mockGrades = [
      { id: 1, name: 'Turma A - Banco de Dados I', cod: 'ABC123' },
      { id: 2, name: 'Turma B - Programação Web', cod: 'DEF456' },
      { id: 3, name: 'Turma C - Estrutura de Dados', cod: 'GHI789' },
    ];
    return of(mockGrades).pipe(delay(300));
  }

  /**
   * Cria uma nova turma.
   * Este método, por enquanto, sempre fará uma requisição HTTP real.
   * Se necessário, você pode adicionar uma lógica de mocking aqui também.
   * @param data Os dados da turma a serem criados.
   * @returns Um Observable da resposta da criação da turma.
   */
  create(data: any) {
    return this.http.post(`${this.api}/grades/create`, data, {
      params: { "id": this.userId }
    }).pipe(
      tap(),
      catchError((err: HttpErrorResponse) => {
        return throwError(() => new Error(err.error));
      })
    )
  }
}