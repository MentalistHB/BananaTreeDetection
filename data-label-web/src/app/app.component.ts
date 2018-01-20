import {Component, OnInit} from '@angular/core';
import {LockComponent} from './lock/lock.component';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent extends LockComponent implements OnInit {
  constructor(private route: ActivatedRoute,
              private router: Router) {
    super(route, router);

  }

  ngOnInit() {
  }
}
