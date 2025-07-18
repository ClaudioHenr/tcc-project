import { Component, OnInit, signal, WritableSignal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TokenService } from '../../core/services/token.service';
import { AppRoles } from '../../core/constants/roles.const.enum';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit {
  currentRole: WritableSignal<AppRoles | null> = signal(null);

  constructor(
    private tokenService: TokenService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.getRole();
  }

  getRole() {
    const role = this.tokenService.getUserRole();
    if (role) {
      this.currentRole.set(role);
    }
    console.log(this.currentRole());
  }

  logout() {
    this.authService.logout();
  }

}
