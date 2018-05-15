import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Auth} from '../auth/Auth';
import {Object2User} from '../utils/Object2User';
import {User} from '../model/user';
import {LockComponent} from '../lock/lock.component';
import {ToastData, ToastOptions, ToastyService} from 'ng2-toasty';
import {Observable, Subscription} from '../../../node_modules/rxjs';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent extends LockComponent implements OnInit {

  userLoginForm: FormGroup;
  user: User;

  constructor(public _formBuilder: FormBuilder, public _userService: UserService, public route: ActivatedRoute,
              public router: Router, private toastyService: ToastyService) {
    super(route, router);
  }

  ngOnInit() {
    this.userLoginForm = this._formBuilder.group({
      email: ['', [Validators.required, Validators.required, Validators.pattern('^[a-zA-Z0-9.!#$%&\'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9]' +
        '(?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$')]],
      password: ['', Validators.required]
    });
  }

  submit() {
    const user = {
      email: this.userLoginForm.value.email,
      password: this.userLoginForm.value.password
    };

    this._userService.login(user).subscribe(responseLogin => {
        if (responseLogin.status != 200) {
          this.addToast('Error!', responseLogin.entity, 'error');
          console.log(responseLogin);
        } else {
          this.user = Object2User.apply(responseLogin.entity);
          Auth.storeUser(this.user);
          this._router.navigate(['']);
        }
      },
      responseLoginErrCode => {
        this.addToast('Error!', 'Please check your email and/or password and try again!\n' +
          'If the problem persists, please contact the administrator!', 'error');
        console.log(responseLoginErrCode);
      });

  }

  addToast(title, message, type) {
    const interval = 1000;
    const timeout = 5000;
    const seconds = timeout / 1000;
    let subscription: Subscription;

    const toastOptions: ToastOptions = {
      title: title,
      msg: message,
      showClose: true,
      timeout: timeout,
      onAdd: (toast: ToastData) => {
        console.log('Toast ' + toast.id + ' has been added!');
        // Run the timer with 1 second iterval
        const observable = Observable.interval(interval).take(seconds);
        // Start listen seconds beat
        subscription = observable.subscribe((count: number) => {
          // Update title of toast
          toast.title = title;
          // Update message of toast
          toast.msg = message;
        });

      },
      onRemove: function (toast: ToastData) {
        console.log('Toast ' + toast.id + ' has been removed!');
        // Stop listenning
        subscription.unsubscribe();
      }
    };

    switch (type) {
      case 'default':
        this.toastyService.default(toastOptions);
        break;
      case 'info':
        this.toastyService.info(toastOptions);
        break;
      case 'success':
        this.toastyService.success(toastOptions);
        break;
      case 'wait':
        this.toastyService.wait(toastOptions);
        break;
      case 'error':
        this.toastyService.error(toastOptions);
        break;
      case 'warning':
        this.toastyService.warning(toastOptions);
        break;
    }
  }
}
