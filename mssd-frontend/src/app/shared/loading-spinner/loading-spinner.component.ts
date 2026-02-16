import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-loading-spinner',
  standalone: true,
  template: `
    <div class="spinner-wrapper" [class.overlay]="overlay">
      <div class="spinner">
        <div class="spinner-ring"></div>
      </div>
      @if (message) {
        <p class="spinner-text">{{ message }}</p>
      }
    </div>
  `,
  styles: [`
    :host { display: block; }

    .spinner-wrapper {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 2rem;
      gap: 1rem;
    }

    .spinner-wrapper.overlay {
      position: fixed;
      inset: 0;
      background: rgba(255,255,255,0.85);
      backdrop-filter: blur(4px);
      z-index: 9000;
    }

    .spinner {
      width: 48px;
      height: 48px;
      position: relative;
    }

    .spinner-ring {
      width: 100%;
      height: 100%;
      border: 4px solid #e5e7eb;
      border-top-color: #002b6c;
      border-radius: 50%;
      animation: spin 0.8s linear infinite;
    }

    .spinner-text {
      color: #6b7280;
      font-size: 0.95rem;
      font-weight: 500;
      margin: 0;
    }

    @keyframes spin {
      to { transform: rotate(360deg); }
    }
  `],
  imports: []
})
export class LoadingSpinnerComponent {
  @Input() message = '';
  @Input() overlay = false;
}
