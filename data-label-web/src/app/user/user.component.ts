import {Component, OnInit} from '@angular/core';
import {Auth} from '../auth/Auth';
import {LockComponent} from '../lock/lock.component';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {User} from '../model/user';
import {UserService} from '../user.service';
import {Object2User} from '../utils/Object2User';
import {Observable, Subscription} from '../../../node_modules/rxjs';
import {ToastData, ToastOptions, ToastyService} from 'ng2-toasty';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent extends LockComponent implements OnInit {

  userCreateForm: FormGroup;
  user: User;
  users: any[];
  admin = false;
  createdUser: User;

  constructor(public _formBuilder: FormBuilder, public _userService: UserService, public route: ActivatedRoute,
              public router: Router, private toastyService: ToastyService) {
    super(route, router);
    this.user = Auth.getUser();
  }

  ngOnInit() {

    this.userCreateForm = this._formBuilder.group({
      email: ['', [Validators.required, Validators.pattern('^[a-zA-Z0-9.!#$%&\'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9]' +
        '(?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$')]],
      firstname: [''],
      lastname: [''],
      admin: [false]
    });

    this._userService.list(this.user.token).subscribe(responseListUser => {
        if (responseListUser.status === 403 || responseListUser.status === 500) {
          this.addToast('Error', responseListUser.entity, 'error');
        }
        if (responseListUser.status !== 200) {
          this.addToast('Error', responseListUser.entity, 'error');
        } else {
          this.users = responseListUser.entity;
        }
      },
      responseLoginErrCode => {
        console.log(responseLoginErrCode);
      });
  }

  submit() {
    const user = {
      email: this.userCreateForm.value.email,
      firstname: this.userCreateForm.value.firstname,
      lastname: this.userCreateForm.value.lastname,
      admin: this.userCreateForm.value.admin
    };

    this._userService.create(user, this.user.token).subscribe(responseCreateUser => {

        if (responseCreateUser.status === 403 || responseCreateUser.status === 500) {
          this.lock();
          this.addToast('Error', responseCreateUser.entity, 'error');
        }
        if (responseCreateUser.status !== 200) {
          this.addToast('Error', responseCreateUser.entity, 'error');
        } else {
          this.createdUser = Object2User.apply(responseCreateUser.entity);

          this.addToast('Success!', 'The user ' + this.createdUser.email + ' has been created.', 'success');

          this.userCreateForm = this._formBuilder.group({
            email: ['', [Validators.required, Validators.minLength(5)]],
            firstname: [''],
            lastname: [''],
            admin: [false]
          });
        }
      },
      responseLoginErrCode => {
        this.addToast('Error!', 'The user ' + this.userCreateForm.value.email + ' couldn\'t been created.', 'error');
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

  onSelect(userId: string) {
    this.router.navigate(['/users', userId]);
  }
}
