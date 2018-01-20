import { Component, OnInit } from '@angular/core';
import {LockComponent} from '../lock/lock.component';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent extends LockComponent implements OnInit {

  constructor(public route: ActivatedRoute,
              public router: Router) {
    super(route, router);
    this.lock();
  }

  ngOnInit() {
  }

}
