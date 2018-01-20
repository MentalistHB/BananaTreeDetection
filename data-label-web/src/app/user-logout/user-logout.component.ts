import {Component, OnInit} from '@angular/core';
import {LockComponent} from '../lock/lock.component';
import {ActivatedRoute, Router} from '@angular/router';
import {Auth} from '../auth/Auth';
import {UserService} from '../user.service';
import {User} from '../model/user';

@Component({
  selector: 'app-user-logout',
  templateUrl: './user-logout.component.html',
  styleUrls: ['./user-logout.component.css']
})
export class UserLogoutComponent extends LockComponent implements OnInit {

  user: User;

  constructor(public route: ActivatedRoute,
              public router: Router, public _userService: UserService) {
    super(route, router);
    this.user = Auth.getUser();
    this.close();

    this._userService.logout(this.user.token).subscribe();
  }

  ngOnInit() {

  }

}
