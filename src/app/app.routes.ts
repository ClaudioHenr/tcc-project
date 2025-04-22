import { Routes } from '@angular/router';
import { SignupComponent } from './authentication/signup/signup.component';
import { StudentExerciceComponent } from './pages/student/student-exercice/student-exercice.component';
import { StudentListComponent } from './pages/student/student-list/student-list.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { SigninComponent } from './authentication/signin/signin.component';
import { MainLayoutComponent } from './layouts/student-layout/main-layout.component';
// import { MainLayoutComponent } from './layouts/professor-layout/main-layout.component';
import { StudentHomeComponent } from './pages/student/student-home/student-home.component';
import { SelfRegistrationComponent } from './register/professor/self-registration/self-registration.component';
import { RegistrationComponent } from './register/student/registration/registration.component';
import { ProfessorHomeComponent } from './pages/professor/professor-home/professor-home.component';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'auth/signin',
        pathMatch: 'full'
    },

    {
        path: 'auth',
        component: AuthLayoutComponent,
        children: [
            { path: 'signup', component: SignupComponent },
            { path: 'signin', component: SigninComponent }
        ]
    },

    {
        path: 'professor',
        component: MainLayoutComponent, // trocar para main de professor ou criar um genérico
        children: [
            { path: '', redirectTo: 'home', pathMatch: 'full' },
            { path: 'home', component: ProfessorHomeComponent }
        ]
    },

    // {
    //     path: 'student',
    //     component: MainLayoutComponent,
    //     // ADICIONAR GUARDS
    //     children: [
    //         { path: '', redirectTo: 'home', pathMatch: 'full' }, // CASO /student/ -> REDIRECIONA PARA /home
    //         { path: 'home', component: StudentHomeComponent,
    //             children: [
    //                 { path: 'list', component: StudentListComponent },
    //                 { path: 'exercise', component: StudentExerciceComponent }
    //             ]
    //         },
    //     ]
    // },

    {
        path: 'student',
        component: MainLayoutComponent,
        data: { role: 'student' },
        loadChildren: () => import('./pages/student/student.routes').then(m => m.STUDENT_ROUTES)
    },

    {
        path: 'register/professor',
        component: SelfRegistrationComponent
    },
    {
        path: 'register/student',
        component : RegistrationComponent
    },

    {
        path: '**',
        redirectTo: '/auth/signin' // ADICIONAR PÁGINA DE ERRO
    }
];
