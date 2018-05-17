import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../user.service';
import {ToastData, ToastOptions, ToastyService} from 'ng2-toasty';
import {Auth} from '../auth/Auth';
import {User} from '../model/user';
import {Object2User} from '../utils/Object2User';
import {LockComponent} from '../lock/lock.component';
import {Observable, Subscription} from '../../../node_modules/rxjs';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent extends LockComponent implements OnInit {

  userEditForm: FormGroup;
  user: User;
  userId: string;
  userToEdit: User;

  constructor(public _formBuilder: FormBuilder, public _userService: UserService, public route: ActivatedRoute,
              public router: Router, private toastyService: ToastyService) {
    super(route, router);
    this.user = Auth.getUser();
  }

  ngOnInit() {
    this.userId = this.route.snapshot.params['id'];


    this.userEditForm = this._formBuilder.group({
      email: ['', [Validators.required, Validators.minLength(5)]],
      firstname: [''],
      lastname: [''],
      admin: [false]
    });

    this._userService.get(this.userId, this.user.token).subscribe(responseGetUser => {
        if (responseGetUser.status === 403 || responseGetUser.status === 500) {
          this.lock();
          this.addToast('Error', responseGetUser.entity, 'error');
        }
        if (responseGetUser.status !== 200) {
          this.addToast('Error', responseGetUser.entity, 'error');
        } else {
          this.userToEdit = Object2User.apply(responseGetUser.entity);

          this.userEditForm = this._formBuilder.group({
            email: [this.userToEdit.email, [Validators.required, Validators.pattern('^[a-zA-Z0-9.!#$%&\'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9]' +
              '(?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$')]],
            firstname: [this.userToEdit.firstname],
            lastname: [this.userToEdit.lastname],
            admin: [this.userToEdit.admin]
          });
        }

      },
      responseLoginErrCode => {
        console.log(responseLoginErrCode);
      });
  }

  submit() {
    const user = {
      email: this.userEditForm.value.email,
      firstname: this.userEditForm.value.firstname,
      lastname: this.userEditForm.value.lastname,
      admin: this.userEditForm.value.admin
    };

    this._userService.edit(user, this.userToEdit.id, this.user.token).subscribe(responseEditUser => {
        if (responseEditUser.status === 403 || responseEditUser.status === 500) {
          this.lock();
          this.addToast('Error', responseEditUser.entity, 'error');
        }
        if (responseEditUser.status !== 200) {
          this.addToast('Error', responseEditUser.entity, 'error');
        } else {
          this.userToEdit = Object2User.apply(responseEditUser.entity);

          this.addToast('Success!', 'The user ' + this.userToEdit.email + ' has been updated.', 'success');

          this.userEditForm = this._formBuilder.group({
            email: [this.userToEdit.email, [Validators.required, Validators.pattern('^[a-zA-Z0-9.!#$%&\'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9]' +
              '(?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$')]],
            firstname: [this.userToEdit.firstname],
            lastname: [this.userToEdit.lastname],
            admin: [this.userToEdit.admin]
          });
        }
      },
      responseLoginErrCode => {
        this.addToast('Error!', 'The user ' + this.userEditForm.value.email + ' couldn\'t been updated.', 'error');
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

  review(userId) {
    this.router.navigate(['/users/review', userId]);
  }

}
