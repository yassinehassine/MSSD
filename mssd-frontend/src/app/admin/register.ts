import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { User } from '../model/user.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
  registerData = {
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: 'ADMIN' as 'ADMIN' | 'USER'
  };
  
  errorMessage = '';
  successMessage = '';
  isLoading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    // Reset messages
    this.errorMessage = '';
    this.successMessage = '';

    // Validation
    if (!this.registerData.username || !this.registerData.email || 
        !this.registerData.password || !this.registerData.confirmPassword) {
      this.errorMessage = 'Please fill in all fields';
      return;
    }

    if (this.registerData.password !== this.registerData.confirmPassword) {
      this.errorMessage = 'Passwords do not match';
      return;
    }

    if (this.registerData.password.length < 6) {
      this.errorMessage = 'Password must be at least 6 characters long';
      return;
    }

    this.isLoading = true;

    const newUser: User = {
      username: this.registerData.username,
      email: this.registerData.email,
      role: this.registerData.role
    };

    // Add password to the user object for registration
    const userWithPassword = { ...newUser, password: this.registerData.password };

    this.authService.register(userWithPassword as any).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = 'Registration successful! You can now login.';
        // Reset form
        this.registerData = {
          username: '',
          email: '',
          password: '',
          confirmPassword: '',
          role: 'ADMIN'
        };
        // Redirect to login after 2 seconds
        setTimeout(() => {
          this.router.navigate(['/admin/login']);
        }, 2000);
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Registration failed. User may already exist.';
        console.error('Registration error:', error);
      }
    });
  }
} 