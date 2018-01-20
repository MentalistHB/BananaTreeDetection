import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserComponent} from './user/user.component';
import {UserLoginComponent} from './user-login/user-login.component';
import {UserFormComponent} from './user-form/user-form.component';
import {HomeComponent} from './home/home.component';
import {UserLogoutComponent} from './user-logout/user-logout.component';
import {ImageComponent} from './image/image.component';
import {StatsComponent} from './stats/stats.component';
import {UserEditComponent} from './user-edit/user-edit.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'users', component: UserComponent},
  {path: 'users/login', component: UserLoginComponent},
  {path: 'users/logout', component: UserLogoutComponent},
  {path: 'users/form', component: UserFormComponent},
  {path: 'users/stats', component: StatsComponent},
  {path: 'users/:id', component: UserEditComponent},
  {path: 'images', component: ImageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

export const routingComponents =
  [
    HomeComponent,
    UserComponent,
    UserLoginComponent,
    UserLogoutComponent,
    UserFormComponent,
    UserEditComponent,
    ImageComponent,
    StatsComponent
  ];
