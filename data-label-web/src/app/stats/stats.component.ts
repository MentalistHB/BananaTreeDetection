import {Component, OnInit} from '@angular/core';
import {LockComponent} from '../lock/lock.component';
import {ActivatedRoute, Router} from '@angular/router';
import {ImageService} from '../image.service';
import {Object2Image} from '../utils/Object2Image';
import {Auth} from '../auth/Auth';
import {User} from '../model/user';
import {Image} from '../model/image';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.css']
})
export class StatsComponent extends LockComponent implements OnInit {

  numberOfMarked: number;
  numberOfMarkedCenter: number;
  numberOfMarkedNonCenter: number;
  user: User;
  images: any;

  constructor(public route: ActivatedRoute,
              public router: Router, public _imageService: ImageService) {
    super(route, router);
    this.user = Auth.getUser();

    this.getNumberOfMarked();
    this.getNumberOfMarkedCenter();
    this.getNumberOfMarkedNonCenter();
  }

  ngOnInit() {
  }

  getNumberOfMarked() {
    this._imageService.list(this.user.token, '2').subscribe(responseListMark => {
        this.images = responseListMark;
        this.numberOfMarked = this.images.length;
      },
      responseLoginErrCode => {
        console.log(responseLoginErrCode);
      });
  }

  getNumberOfMarkedCenter() {
    this._imageService.list(this.user.token, '1').subscribe(responseListMark => {
        this.images = responseListMark;
        this.numberOfMarkedCenter = this.images.length;
      },
      responseLoginErrCode => {
        console.log(responseLoginErrCode);
      });
  }

  getNumberOfMarkedNonCenter() {
    this._imageService.list(this.user.token, '0').subscribe(responseListMark => {
        this.images = responseListMark;
        this.numberOfMarkedNonCenter = this.images.length;
      },
      responseLoginErrCode => {
        console.log(responseLoginErrCode);
      });
  }

}
