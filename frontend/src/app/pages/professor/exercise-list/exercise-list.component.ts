import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router'; // caso queira navegar para outra rota futuramente
import { exercise } from '../../../models/exercise';
import { ExerciseService } from '../services/exercise/exercise.service';

@Component({
  selector: 'app-exercise-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './exercise-list.component.html',
  styleUrls: ['./exercise-list.component.css']
})
export class ExerciseListComponent implements OnInit {
  listExerciseId: string = '';

  exercicios!: any

  constructor(
    private exerciseService: ExerciseService,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.listExerciseId = id;
      this.getExercises();
    }
  }

  getExercises() {
    console.log(this.listExerciseId);
    this.exerciseService.getExercisesByListId(this.listExerciseId).subscribe({
      next: (data: any) => {
        this.exercicios = data;
      }, error: (err: any) => {
        
      }
    })
  }

  adicionarExercicio() {
    this.router.navigate(['/professor/exercise/', this.listExerciseId]);
    // Aqui você pode navegar para o componente de cadastro
    console.log('Navegar para tela de novo exercício');
  }

  editarExercicio(exercicio: exercise) {
    console.log('Editar exercício:', exercicio);
  }

  removerExercicio(exerciseId: string) {
    this.exerciseService.deleteExercise(exerciseId).subscribe({
      next: () => {
        alert("Exercício excluído com sucesso");
        this.getExercises();
      }, error: (err: any) => {
        console.log(err);
        alert("Erro ao excluir exercício, tente novamente mais tarde");
      }
    })
  }

  back() {
    this.location.back();
  }
}