import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [RouterLink],
  template: `
    <section class="not-found">
      <div class="not-found-content">
        <div class="error-code">404</div>
        <h1 class="error-title">Page introuvable</h1>
        <p class="error-message">
          La page que vous recherchez n'existe pas ou a été déplacée.
        </p>
        <a routerLink="/home" class="btn-home">
          <i class="bi bi-house-door"></i> Retour à l'accueil
        </a>
      </div>
    </section>
  `,
  styles: [`
    .not-found {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #002b6c 0%, #003d99 100%);
      text-align: center;
      padding: 2rem;
    }

    .error-code {
      font-size: 8rem;
      font-weight: 900;
      color: rgba(255,255,255,0.15);
      line-height: 1;
      margin-bottom: 0.5rem;
    }

    .error-title {
      font-size: 2rem;
      font-weight: 700;
      color: #fff;
      margin-bottom: 1rem;
    }

    .error-message {
      font-size: 1.1rem;
      color: rgba(255,255,255,0.75);
      margin-bottom: 2rem;
      max-width: 400px;
    }

    .btn-home {
      display: inline-flex;
      align-items: center;
      gap: 0.5rem;
      padding: 0.85rem 2rem;
      background: #e60000;
      color: #fff;
      border-radius: 10px;
      text-decoration: none;
      font-weight: 700;
      font-size: 1rem;
      transition: all 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(230,0,0,0.35);
        color: #fff;
      }
    }
  `]
})
export class NotFound {}
