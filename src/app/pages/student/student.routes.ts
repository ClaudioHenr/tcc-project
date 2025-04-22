import { Routes } from "@angular/router";

export const STUDENT_ROUTES: Routes = [
    {
        path: '',
        loadComponent: () => import('../../layouts/student-layout/main-layout.component').then(m => m.MainLayoutComponent),
            children: [
                {
                    path: '',
                    redirectTo: 'home',
                    pathMatch: 'full'
                },
                {
                    path: 'home',
                    loadComponent: () => import('./student-home/student-home.component').then(m => m.StudentHomeComponent)
                },

                {
                    path: 'lists',
                    children: [
                        {
                            path: '',
                            loadComponent: () => import('./student-list/student-list.component').then(m => m.StudentListComponent)
                        }
                    ]
                },


                {
                    path: 'grades',
                    children: [
                        {
                            path: '',
                            loadComponent: () => import('./student-grade/student-grade.component').then(m => m.StudentGradeComponent)
                        }
                    ]
                },

                {
                    path: 'exercises',
                    children: [
                        {
                            path: '',
                            loadComponent: () => import('./student-exercice/student-exercice.component').then(m => m.StudentExerciceComponent)

                        }
                    ]
                }
            ]
    },
]