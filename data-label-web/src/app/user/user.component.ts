import {Component, OnInit} from '@angular/core';
import {Auth} from '../auth/Auth';
import {LockComponent} from '../lock/lock.component';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {User} from '../model/user';
import {UserService} from '../user.service';
import {Object2User} from '../utils/Object2User';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent extends LockComponent implements OnInit {

  title: string;
  userCreateForm: FormGroup;
  user: User;

  constructor(public _formBuilder: FormBuilder, public _userService: UserService,public route: ActivatedRoute,
              public router: Router) {
    super(route, router);
    this.user = Auth.getUser();
  }

  ngOnInit() {
    this.userCreateForm = this._formBuilder.group({
      email: ['', [Validators.required, Validators.minLength(5)]],
      firstname: ['', [Validators.required]],
      lastname: ['', [Validators.required]],
      admin: ['']
    });
  }

  submit() {
    const user = {
      email: this.userCreateForm.value.email,
      firstname: this.userCreateForm.value.firstname,
      lastname: this.userCreateForm.value.lastname,
      admin: this.userCreateForm.value.admin
    };

    this._userService.create(user, this.user.token).subscribe(responseLogin => {
        this.user = Object2User.apply(responseLogin);
      },
      responseLoginErrCode => {
        console.log(responseLoginErrCode);
      });

  }

}
