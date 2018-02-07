import {Component, OnInit, ViewChild} from '@angular/core';
import {Observable, Subscription} from '../../../node_modules/rxjs';
import {AppConstant} from '../AppConstant';
import {ToastData, ToastOptions, ToastyService} from 'ng2-toasty';
import {Image} from '../model/image';
import {Object2Image} from '../utils/Object2Image';
import {ImageService} from '../image.service';
import {Auth} from '../auth/Auth';
import {ImgMapComponent} from 'ng2-img-map';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../model/user';
import {LockComponent} from '../lock/lock.component';
import {UserService} from '../user.service';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent extends LockComponent implements OnInit {

  @ViewChild('imgMap')
  imgMap: ImgMapComponent;

  image: Image;
  markedImage: Image;
  user: User;
  userId: string;
  server: string;
  url_image: string;
  url_parent: string;
  event: MouseEvent;
  clientX = 0;
  clientY = 0;

  width_parent: number;
  height_parent: number;
  x: number;
  y: number;
  width: number;
  height: number;

  Math: any;

  fill = 'none';
  x_template = 0;
  y_template = 0;
  center = false;


  constructor(public _imageService: ImageService, public _userService: UserService, public route: ActivatedRoute,
              public router: Router, private toastyService: ToastyService) {
    super(route, router);

    this.server = AppConstant.server_file;
    this.user = Auth.getUser();
  }

  ngOnInit() {

    this.userId = this.route.snapshot.params['id'];
    this.pickToReview(this.userId, this.user.token);
  }

  pickToReview(userId: string, token: string) {
    this._imageService.pickToReview(userId, token).subscribe(responsePick => {

        console.log(responsePick);
        this.image = Object2Image.apply(responsePick);
        this.url_image = this.server + this.image.path_remote;
        this.url_parent = this.server + this.image.parent_remote;
        this.width_parent = this.image.width_parent;
        this.height_parent = this.image.height_parent;
        this.x = this.image.x_parent;
        this.y = this.image.y_parent;
        this.width = this.image.width;
        this.height = this.image.height;
        this.x_template = this.image.x;
        this.y_template = this.image.y;
        this.center = this.image.center;
        if (this.center) {
          this.fill = 'red';
        } else {
          this.fill = 'none';
        }
      },
      responseLoginErrCode => {
        this.addToast('Error', responseLoginErrCode.error, 'error');
        this.close();
      });
  }

  onEvent(event: MouseEvent): void {
    this.event = event;
  }

  coordinates(event: MouseEvent): void {
    this.clientX = event.clientX;
    this.clientY = event.clientY;
  }

  pickPixel(xPicked: number, yPicked: number) {
    this.fill = 'red';
    this.x_template = xPicked;
    this.y_template = yPicked;
    this.center = true;
  }


  submit() {
    const markedImage = {
      id: this.image.id,
      center: this.center,
      x: this.x_template,
      y: this.y_template,
      x_parent: this.image.x_parent,
      y_parent: this.image.y_parent,
      width: this.image.width,
      height: this.image.height,
      stride: this.image.stride,
      parent_local: this.image.parent_local,
      parent_remote: this.image.parent_remote,
      width_parent: this.image.width_parent,
      height_parent: this.image.height_parent,
      name: this.image.name,
      path_local: this.image.path_local,
      path_remote: this.image.path_remote,
      markedDate: this.image.markedDate,
      user: null
    };

    this._imageService.review(this.userId, this.image.id, this.user.token, markedImage).subscribe(responseMark => {
        this.markedImage = Object2Image.apply(responseMark);

        this.addToast('Success', 'Class registered', 'success');
        this.ngOnInit();
      },
      responseLoginErrCode => {
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

  resetTemplate() {
    this.x_template = this.image.x;
    this.y_template = this.image.y;
    this.center = this.image.center;
    if (this.center) {
      this.fill = 'red';
    } else {
      this.fill = 'none';
    }
  }

  noCenter() {
    this.x_template = -1;
    this.y_template = -1;
    this.center = false;
    this.fill = 'none';
  }

}
