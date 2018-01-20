import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Auth} from '../auth/Auth';

@Component({
  selector: 'app-lock',
  templateUrl: './lock.component.html',
  styleUrls: ['./lock.component.css']
})
export class LockComponent implements OnInit {

  constructor(public _route: ActivatedRoute,
              public _router: Router) {
    this.lock();
  }

  ngOnInit() {
  }

  lock() {
    if (!Auth.isAuthenticated()) {
      this.close();
    }
  }

  close() {
    Auth.reset();
    this._router.navigate(['/users/login']);
  }
}
