import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TokenService } from '../../core/services/token.service';
import { AppRoles } from '../../core/constants/roles.const.enum';

@Component({
  selector: 'app-unauthorized',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './unauthorized.component.html',
  styleUrls: ['./unauthorized.component.css']
})
export class UnauthorizedComponent implements OnInit, OnDestroy {
  countdown = 10;
  private countdownInterval: any;

  constructor(private router: Router, private tokenService: TokenService) { }

  ngOnInit(): void {
    this.startCountdown();
  }

  ngOnDestroy(): void {
    if (this.countdownInterval) {
      clearInterval(this.countdownInterval);
    }
  }

  startCountdown(): void {
    this.countdownInterval = setInterval(() => {
      this.countdown--;

      if (this.countdown <= 0) {
        this.redirectNow();
      }
    }, 1000);
  }

  redirectNow(): void {
    clearInterval(this.countdownInterval);
    const userRole = this.tokenService.getUserRole();

    if (userRole === AppRoles.PROFESSOR) {
      this.router.navigate(['/professor/home']);
    } else if (userRole === AppRoles.STUDENT) {
      this.router.navigate(['/student/home']);
    } else {
      this.router.navigate(['/']);
    }
  }
}