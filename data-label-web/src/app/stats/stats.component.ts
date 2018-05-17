import {Component, OnInit} from '@angular/core';
import {LockComponent} from '../lock/lock.component';
import {ActivatedRoute, Router} from '@angular/router';
import {ImageService} from '../image.service';
import {Object2Image} from '../utils/Object2Image';
import {Auth} from '../auth/Auth';
import {User} from '../model/user';
import {ToastData, ToastOptions, ToastyService} from 'ng2-toasty';
import {Observable, Subscription} from '../../../node_modules/rxjs';

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
              public router: Router, public _imageService: ImageService, private toastyService: ToastyService) {
    super(route, router);
    this.user = Auth.getUser();

    this.getNumberOfMarked();
    this.getNumberOfMarkedCenter();
    this.getNumberOfMarkedNonCenter();
  }

  ngOnInit() {
  }

  getNumberOfMarked() {
    this._imageService.list(this.user.id, this.user.token, '2').subscribe(responseListMark => {
        if (responseListMark.status === 403 || responseListMark.status === 500) {
          this.lock();
          this.addToast('Error', responseListMark.entity, 'error');
        }
        if (responseListMark.status !== 200) {
          this.addToast('Error', responseListMark.entity, 'error');
        } else {
          this.markedImage = Object2Image.apply(responseListMark.entity);
          this.images = responseListMark.entity;
          this.numberOfMarked = this.images.length;
        }
      },
      responseLoginErrCode => {
        console.log(responseLoginErrCode);
      });
  }

  getNumberOfMarkedCenter() {
    this._imageService.list(this.user.id, this.user.token, '1').subscribe(responseListMark => {
        if (responseListMark.status === 403 || responseListMark.status === 500) {
          this.lock();
          this.addToast('Error', responseListMark.entity, 'error');
        }
        if (responseListMark.status !== 200) {
          this.addToast('Error', responseListMark.entity, 'error');
        } else {
          this.images = responseListMark.entity;
          this.numberOfMarkedCenter = this.images.length;
        }
      },
      responseLoginErrCode => {
        console.log(responseLoginErrCode);
      });
  }

  getNumberOfMarkedNonCenter() {
    this._imageService.list(this.user.id, this.user.token, '0').subscribe(responseListMark => {
        if (responseListMark.status === 403 || responseListMark.status === 500) {
          this.lock();
          this.addToast('Error', responseListMark.entity, 'error');
        }
        if (responseListMark.status !== 200) {
          this.addToast('Error', responseListMark.entity, 'error');
        } else {
          this.images = responseListMark.entity;
          this.numberOfMarkedNonCenter = this.images.length;
        }
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

}
