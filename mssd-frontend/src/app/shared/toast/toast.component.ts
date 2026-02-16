import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService, Toast } from '../../services/toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-container">
      <div *ngFor="let toast of toasts"
           class="toast-item"
           [class.toast-success]="toast.type === 'success'"
           [class.toast-error]="toast.type === 'error'"
           [class.toast-info]="toast.type === 'info'"
           [class.toast-warning]="toast.type === 'warning'"
           (click)="dismiss(toast.id)">
        <div class="toast-icon">
          <i [class]="getIcon(toast.type)"></i>
        </div>
        <span class="toast-message">{{ toast.message }}</span>
        <button class="toast-close" (click)="dismiss(toast.id); $event.stopPropagation()">
          <i class="bi bi-x-lg"></i>
        </button>
      </div>
    </div>
  `,
  styles: [`
    .toast-container {
      position: fixed;
      top: 80px;
      right: 1.5rem;
      z-index: 9999;
      display: flex;
      flex-direction: column;
      gap: 0.75rem;
      max-width: 420px;
      width: 100%;
      pointer-events: none;
    }

    .toast-item {
      display: flex;
      align-items: center;
      gap: 0.75rem;
      padding: 1rem 1.25rem;
      border-radius: 12px;
      color: #fff;
      font-weight: 500;
      font-size: 0.95rem;
      box-shadow: 0 8px 32px rgba(0,0,0,0.18);
      backdrop-filter: blur(10px);
      animation: toastSlideIn 0.35s cubic-bezier(0.4,0,0.2,1) forwards;
      cursor: pointer;
      pointer-events: all;
      transition: transform 0.2s, opacity 0.2s;
    }

    .toast-item:hover {
      transform: translateX(-4px);
    }

    .toast-icon i {
      font-size: 1.25rem;
    }

    .toast-message {
      flex: 1;
      line-height: 1.4;
    }

    .toast-close {
      background: none;
      border: none;
      color: rgba(255,255,255,0.7);
      cursor: pointer;
      padding: 0.25rem;
      border-radius: 6px;
      transition: all 0.2s;
      display: flex;
      align-items: center;
    }

    .toast-close:hover {
      color: #fff;
      background: rgba(255,255,255,0.15);
    }

    .toast-success {
      background: linear-gradient(135deg, #059669, #10b981);
    }

    .toast-error {
      background: linear-gradient(135deg, #dc2626, #ef4444);
    }

    .toast-info {
      background: linear-gradient(135deg, #002b6c, #1d4ed8);
    }

    .toast-warning {
      background: linear-gradient(135deg, #d97706, #f59e0b);
    }

    @keyframes toastSlideIn {
      from {
        opacity: 0;
        transform: translateX(100%);
      }
      to {
        opacity: 1;
        transform: translateX(0);
      }
    }

    @media (max-width: 480px) {
      .toast-container {
        right: 0.75rem;
        left: 0.75rem;
        max-width: 100%;
      }
    }
  `]
})
export class ToastComponent {
  toasts: Toast[] = [];

  constructor(private toastService: ToastService) {
    this.toastService.toasts$.subscribe(toasts => {
      this.toasts = toasts;
    });
  }

  dismiss(id: number): void {
    this.toastService.dismiss(id);
  }

  getIcon(type: string): string {
    switch (type) {
      case 'success': return 'bi bi-check-circle-fill';
      case 'error': return 'bi bi-exclamation-triangle-fill';
      case 'info': return 'bi bi-info-circle-fill';
      case 'warning': return 'bi bi-exclamation-circle-fill';
      default: return 'bi bi-info-circle-fill';
    }
  }
}
