import { Routes } from '@angular/router';
import { AdminLayout } from './admin-layout';
import { Dashboard } from './dashboard';
import { Charts } from './charts';
import { Tables } from './tables';
import { Login } from './login';
import { Register } from './register';
import { Password } from './password';
import { Error401 } from './error401';
import { Error404 } from './error404';
import { Error500 } from './error500';
import { AdminContacts } from './admin-contacts';
import { AdminFormations } from './admin-formations';
import { AdminBookings } from './admin-bookings';
import { AdminCustomRequests } from './admin-custom-requests';
import { AdminCalendar } from './admin-calendar';
import { AdminReservations } from './admin-reservations';

export const adminRoutes: Routes = [
  {
    path: '',
    component: AdminLayout,
    children: [
      { path: '', component: Dashboard },
      { path: 'charts', component: Charts },
      { path: 'tables', component: Tables },
      { path: 'contacts', component: AdminContacts },
      { path: 'formations', component: AdminFormations },
      { path: 'bookings', component: AdminBookings },
      { path: 'custom-requests', component: AdminCustomRequests },
      { path: 'calendar', component: AdminCalendar },
      { path: 'reservations', component: AdminReservations },
      { path: 'login', component: Login },
      { path: 'register', component: Register },
      { path: 'password', component: Password },
      { path: '401', component: Error401 },
      { path: '404', component: Error404 },
      { path: '500', component: Error500 },
      { path: '**', component: Error404 },
    ]
  }
  // Removed non-existent pages: login, register, password, 401, 404, 500
]; 