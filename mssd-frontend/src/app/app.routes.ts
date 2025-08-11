import { Routes } from '@angular/router';
import { AppFlexstartLayout } from './app.flexstart-layout';
import { Home } from './pages/home/home';
import { About } from './pages/about/about';
import { Services } from './pages/services/services';
import { Portfolio } from './pages/portfolio/portfolio';
import { Blog } from './pages/blog/blog';
import { Contact } from './pages/contact/contact';
import { ServiceDetails } from './pages/service-details/service-details';
import { StarterPage } from './pages/starter-page/starter-page';
import { Team } from './pages/team/team';
import { Calendar } from './pages/calendar/calendar';

export const routes: Routes = [
  {
    path: '',
    component: AppFlexstartLayout,
    children: [
      { path: '', redirectTo: '/calendar', pathMatch: 'full' }, // Redirect to calendar
      { path: 'home', component: Home },
      { path: 'about', component: About },
      { path: 'services', component: Services },
      { path: 'portfolio', component: Portfolio },
      { path: 'blog', component: Blog },
      { path: 'contact', component: Contact },
      { path: 'service-details', component: ServiceDetails },
      { path: 'starter-page', component: StarterPage },
      { path: 'team', component: Team },
      { path: 'calendar', component: Calendar },
      { path: 'formation/:id/reviews', loadComponent: () => import('./pages/Reviews/reviews-page').then(m => m.ReviewsPage) }

    ]
  },
  {
    path: 'admin',
    loadChildren: () => import('./admin/admin.routes').then(m => m.adminRoutes)

  }

];
