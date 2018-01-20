import {Component, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Auth} from '../auth/Auth';
import {Object2User} from '../utils/Object2User';
import {User} from '../model/user';
import {LockComponent} from '../lock/lock.component';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent extends LockComponent implements OnInit {

  userLoginForm: FormGroup;
  user: User;

  constructor(public _formBuilder: FormBuilder, public _userService: UserService, public route: ActivatedRoute,
              public router: Router) {
    super(route, router);
  }

  ngOnInit() {
    this.userLoginForm = this._formBuilder.group({
      email: ['', [Validators.required]],
      password: ['', Validators.required]
    });
  }

  submit() {
    const user = {
      email: this.userLoginForm.value.email,
      password: this.userLoginForm.value.password
    };

    this._userService.login(user).subscribe(responseLogin => {
        this.user = Object2User.apply(responseLogin);
        Auth.storeUser(this.user);
        this._router.navigate(['']);
      },
      responseLoginErrCode => {
        console.log(responseLoginErrCode);
      });

  }
}
