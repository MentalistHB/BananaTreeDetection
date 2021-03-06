import {LockComponent} from './lock/lock.component';
import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule, routingComponents} from './app-routing.module';

import {AppComponent} from './app.component';
import {UserComponent} from './user/user.component';
import {HomeComponent} from './home/home.component';
import {UserFormComponent} from './user-form/user-form.component';
import {UserLoginComponent} from './user-login/user-login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UserService} from './user.service';
import {MatFormFieldModule, MatOptionModule, MatSelectModule} from '@angular/material';
import {HttpClientModule} from '@angular/common/http';
import {UserLogoutComponent} from './user-logout/user-logout.component';
import {ImageComponent} from './image/image.component';
import {ImageService} from './image.service';
import {ImageZoomModule} from 'angular2-image-zoom';
import {CommonModule} from '@angular/common';
import {ImgMapComponent} from 'ng2-img-map';
import {StatsComponent} from './stats/stats.component';
import {ToastyModule} from 'ng2-toasty';
import {UserEditComponent} from './user-edit/user-edit.component';
import { ReviewComponent } from './review/review.component';


@NgModule({
  declarations: [
    AppComponent,
    UserComponent,
    HomeComponent,
    routingComponents,
    UserFormComponent,
    UserLoginComponent,
    UserEditComponent,
    LockComponent,
    UserLogoutComponent,
    ImageComponent,
    ImgMapComponent,
    StatsComponent,
    ReviewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatOptionModule,
    MatSelectModule,
    ImageZoomModule,
    CommonModule,
    ToastyModule.forRoot()
  ],
  providers: [UserService, ImageService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
