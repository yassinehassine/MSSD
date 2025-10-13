import { Routes } from '@angular/router';
import { AppFlexstartLayout } from './app.flexstart-layout';
import { Home } from './pages/home/home';
import { About } from './pages/about/about';
import { Services } from './pages/services/services';
import { PortfolioComponent } from './pages/portfolio/portfolio';
import { Blog } from './pages/blog/blog';
import { Contact } from './pages/contact/contact';
import { ServiceDetails } from './pages/service-details/service-details';
import { StarterPage } from './pages/starter-page/starter-page';
import { Calendar } from './pages/calendar/calendar';
import { Annexes } from './pages/annexes/annexes';
import { AnnexesRequest } from './pages/annexes-request/annexes-request';

export const routes: Routes = [
  {
    path: '',
    component: AppFlexstartLayout,
    children: [
      // Make Home the landing page
      { path: '', redirectTo: '/home', pathMatch: 'full' },
      { path: 'home', component: Home },
      { path: 'about', component: About },
      { path: 'services', component: Services },
      { path: 'annexes', component: Annexes },
      { path: 'annexes/request', component: AnnexesRequest },
      { path: 'portfolio', component: PortfolioComponent },
      { path: 'blog', component: Blog },
      { path: 'blog/:id', loadComponent: () => import('./pages/blog-detail/blog-detail').then(m => m.BlogDetail) },
      { path: 'annexes/:slug', loadComponent: () => import('./pages/annexes-theme/annexes-theme').then(m => m.AnnexesThemeDetail) },
      { path: 'contact', component: Contact },
      { path: 'service-details', component: ServiceDetails },
      { path: 'starter-page', component: StarterPage },
      { path: 'calendar', component: Calendar },
      { path: 'formation/:id/reviews', loadComponent: () => import('./pages/Reviews/reviews-page').then(m => m.ReviewsPage) }

    ]
  },
  {
    path: 'admin',
    loadChildren: () => import('./admin/admin.routes').then(m => m.adminRoutes)

  }

];
