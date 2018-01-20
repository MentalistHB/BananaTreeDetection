import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {LockComponent} from '../lock/lock.component';

import { ImgMapComponent } from 'ng2-img-map';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent extends LockComponent implements OnInit {

  constructor(public route: ActivatedRoute,
              public router: Router) {
    super(route, router);
  }

  ngOnInit() {
  }

}
