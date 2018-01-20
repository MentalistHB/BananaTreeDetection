import {Component, OnInit} from '@angular/core';
import {LockComponent} from '../lock/lock.component';
import {ActivatedRoute, Router} from '@angular/router';
import {Auth} from '../auth/Auth';

@Component({
  selector: 'app-user-logout',
  templateUrl: './user-logout.component.html',
  styleUrls: ['./user-logout.component.css']
})
export class UserLogoutComponent extends LockComponent implements OnInit {

  constructor(public route: ActivatedRoute,
              public router: Router) {
    super(route, router);
    this.close();
  }

  ngOnInit() {

  }

}
