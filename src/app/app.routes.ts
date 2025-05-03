import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { StudentExerciceComponent } from './pages/student/student-exercice/student-exercice.component';
import { StudentListComponent } from './pages/student/student-list/student-list.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { SigninComponent } from './authentication/signin/signin.component';
import { MainLayoutComponent } from './layouts/student-layout/main-layout.component';
import { StudentHomeComponent } from './pages/student/student-home/student-home.component';
import { SelfRegistrationComponent } from './register/professor/self-registration/self-registration.component';
import { RegistrationComponent } from './register/student/registration/registration.component';
import { ProfessorHomeComponent } from './pages/professor/professor-home/professor-home.component';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './register/register.component';
import { ExerciseComponent } from './pages/professor/exercise/exercise.component';
import { UnauthorizedComponent } from './pages/unauthorized/unauthorized.component';
import { AppRoles } from './core/constants/roles.const.enum';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'auth',
    component: AuthLayoutComponent,
    children: [
      { path: 'signin', component: SigninComponent }
    ]
  },
  {
    path: 'professor',
    component: MainLayoutComponent,
    canActivate: [authGuard, roleGuard],
    data: { role: AppRoles.PROFESSOR },
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: ProfessorHomeComponent },
      { path: 'exercise', component: ExerciseComponent }
    ]
  },
  {
    path: 'student',
    component: MainLayoutComponent,
    canActivate: [authGuard, roleGuard],
    data: { role: AppRoles.STUDENT },
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { 
        path: 'home', 
        component: StudentHomeComponent,
        children: [
          { path: 'list', component: StudentListComponent },
          { path: 'exercise', component: StudentExerciceComponent }
        ]
      },
    ]
  },
  {
    path: 'register',
    component: RegisterComponent,
    children: [
      { path: 'professor', component: SelfRegistrationComponent },
      { path: 'student', component: RegistrationComponent }
    ]
  },
  {
    path: 'unauthorized',
    component: UnauthorizedComponent
  },
  {
    path: '**',
    redirectTo: '/auth/signin'
  }
];