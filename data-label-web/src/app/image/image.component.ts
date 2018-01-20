import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {LockComponent} from '../lock/lock.component';
import {ActivatedRoute, Router} from '@angular/router';
import {Auth} from '../auth/Auth';
import {ImageService} from '../image.service';
import {Image} from '../model/image';
import {Object2Image} from '../utils/Object2Image';
import {User} from '../model/user';
import {AppConstant} from '../AppConstant';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ImgMapComponent} from 'ng2-img-map';
import {ToastData, ToastOptions, ToastyConfig, ToastyService} from 'ng2-toasty';
import {Observable, Subscription} from 'rxjs/Rx';

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.css']
})
export class ImageComponent extends LockComponent implements OnInit {


  @ViewChild('imgMap')
  imgMap: ImgMapComponent;
  x: number;
  y: number;
  radius: number;

  width_parent: number;
  height_parent: number;


  image: Image;
  markedImage: Image;
  user: User;
  server: string;
  url_image: string;
  url_parent: string;
  event: MouseEvent;
  clientX = 0;
  clientY = 0;

  Math: any;

  markForm: FormGroup;

  constructor(public _formBuilder: FormBuilder, public _imageService: ImageService, public route: ActivatedRoute,
              public router: Router, private toastyService: ToastyService, private toastyConfig: ToastyConfig) {
    super(route, router);

    this.server = AppConstant.server_file;
    this.user = Auth.getUser();
  }

  ngOnInit() {
    this.pick(this.user.token);
    this.markForm = this._formBuilder.group({
      x: [''],
      y: [''],
      center: [false]
    });
  }

  pick(token: string) {
    this._imageService.pick(token).subscribe(responsePick => {
        this.image = Object2Image.apply(responsePick);
        this.url_image = this.server + this.image.path_remote;
        this.url_parent = this.server + this.image.parent_remote;
        this.x = this.image.x_parent + this.image.width / 2;
        this.y = this.image.y_parent + this.image.height / 2;
        this.radius = this.image.width / 2;

        this.width_parent = this.image.width_parent;
        this.height_parent = this.image.height_parent;

        console.log(this.x, this.y, this.radius, this.width_parent, this.height_parent);
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

  pickPixel(ref: ElementRef) {
    this.markForm.controls['x'].setValue(this.clientX);
    this.markForm.controls['y'].setValue(this.clientY);
    this.markForm.controls['center'].setValue(true);
  }


  submit() {
    const markedImage = {
      id: '',
      center: this.markForm.value.center,
      x: this.markForm.value.x,
      y: this.markForm.value.y,
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

    this._imageService.mark(markedImage, this.user.token).subscribe(responseMark => {
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
}
