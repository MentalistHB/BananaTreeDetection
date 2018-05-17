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
import {ToastData, ToastOptions, ToastyService} from 'ng2-toasty';
import {Observable, Subscription} from 'rxjs/Rx';
import {Annotation} from '../model/Annotation';
import has = Reflect.has;
import {AnnotationService} from '../annotation.service';
import {MarkedImageTO} from '../model/markedImageTO';

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.css']
})
export class ImageComponent extends LockComponent implements OnInit {


  @ViewChild('imgMap')
  imgMap: ImgMapComponent;

  image: Image;
  markedImage: Image;
  user: User;
  server: string;
  urlImage: string;
  pathParent: string;
  event: MouseEvent;
  clientX = 0;
  clientY = 0;

  width_parent: number;
  height_parent: number;
  x: number;
  y: number;
  width: number;
  height: number;

  fills = ['none',
    'none',
    'none',
    'none',
    'none',
    'none',
    'none',
    'none'];
  center = false;

  annotations: Annotation[] = [new Annotation(-1, -1, false),
    new Annotation(-1, -1, false),
    new Annotation(-1, -1, false),
    new Annotation(-1, -1, false),
    new Annotation(-1, -1, false),
    new Annotation(-1, -1, false),
    new Annotation(-1, -1, false),
    new Annotation(-1, -1, false)];

  constructor(public _imageService: ImageService, public _annotationService: AnnotationService, public route: ActivatedRoute,
              public router: Router, private toastyService: ToastyService) {
    super(route, router);

    this.server = AppConstant.server;
    this.user = Auth.getUser();
  }

  ngOnInit() {
    this.pick(this.user.id);
    this.center = false;
  }

  pick(userId: number) {
    this._imageService.pick(userId).subscribe(responsePick => {
        if (responsePick.status === 403 || responsePick.status === 500) {
          this.lock();
          this.addToast('Error', responsePick.entity, 'error');
        }
        if (responsePick.status !== 200) {
          this.addToast('Error', responsePick.entity, 'error');
        } else {
          this.resetTemplate();
          this.image = Object2Image.apply(responsePick.entity);
          this.urlImage = this.server + '/' + this.image.folder + this.image.name;
          this.pathParent = this.server + '/' + this.image.pathParent;
          this.width_parent = this.image.widthParent;
          this.height_parent = this.image.heightParent;
          this.x = this.image.xcoordParent;
          this.y = this.image.ycoordParent;
          this.width = AppConstant.template_width;
          this.height = AppConstant.template_height;
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
    this.clientX = event.pageX;
    this.clientY = event.pageY;
  }

  pickPixel(xPicked: number, yPicked: number) {

    const i = this.contains(xPicked, yPicked);
    const nbPoints = this.getNbPoints();

    if ((nbPoints < AppConstant.max_annotation && i !== -1) || (nbPoints >= AppConstant.max_annotation && i !== -1)) {
      for (const annotation of this.annotations) {
        this.annotations[i].x = -1;
        this.annotations[i].y = -1;
        this.annotations[i].principal = false;
        this.setPrincipal();
        this.updateColors();

        if (this.getNbPoints() === 0) {
          this.center = false;
        } else {
          this.center = true;
        }
      }
    } else {
      if (nbPoints < AppConstant.max_annotation && i === -1) {
        let j = -1;
        for (const annotation of this.annotations) {
          j += 1;
          if (annotation.x === -1 && annotation.y === -1) {
            this.annotations[j].x = xPicked;
            this.annotations[j].y = yPicked;
            this.annotations[j].principal = (nbPoints === 0);
            this.center = true;
            this.setPrincipal();
            this.updateColors();
            return;
          }
        }
      } else {
        return;
      }
    }
  }


  submit() {
    const markedImage = {
      id: 0,
      center: this.center,
      xcoordParent: this.image.xcoordParent,
      ycoordParent: this.image.ycoordParent,
      stride: this.image.stride,
      pathParent: this.image.pathParent,
      widthParent: this.image.widthParent,
      heightParent: this.image.heightParent,
      name: this.image.name,
      folder: this.image.folder,
      markedDate: this.image.markedDate,
      user: null
    };

    let annotations: Annotation[] = [];
    for (const annotation of this.annotations) {
      if (annotation.isMarked()) {
        annotations.push(annotation);
      }
    }

    let to = new MarkedImageTO(markedImage, annotations);

    this._imageService.save(to, this.user.id, this.user.token).subscribe(responseMark => {
        if (responseMark.status === 403 || responseMark.status === 500) {
          this.lock();
          this.addToast('Error', responseMark.entity, 'error');
        }
        if (responseMark.status !== 200) {
          this.addToast('Error', responseMark.entity, 'error');
          this.ngOnInit();
        } else {
          this.markedImage = Object2Image.apply(responseMark.entity);
          this.addToast('Success', 'Class registered', 'success');
          this.ngOnInit();
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

  resetTemplate() {
    this.fills = ['none',
      'none',
      'none',
      'none',
      'none',
      'none',
      'none',
      'none'];

    this.annotations = [new Annotation(-1, -1, false),
      new Annotation(-1, -1, false),
      new Annotation(-1, -1, false),
      new Annotation(-1, -1, false),
      new Annotation(-1, -1, false),
      new Annotation(-1, -1, false),
      new Annotation(-1, -1, false),
      new Annotation(-1, -1, false)];
    this.center = false;
  }

  contains(x: number, y: number): number {
    let i = -1;
    for (const annotation of this.annotations) {
      i += 1;
      if (annotation.x === x && annotation.y === y) {
        return i;
      }
    }

    return -1;
  }

  getNbPoints() {
    let nbPoints = 0;
    for (const annotation of this.annotations) {
      if (annotation.x !== -1 && annotation.y !== -1) {
        nbPoints += 1;
      }
    }
    return nbPoints;
  }

  getColor(index: number, principal: boolean): string {
    if (principal) {
      return 'red';
    } else {
      switch (index) {
        case 0: {
          return 'Blue';
        }
        case 1: {
          return 'Silver';
        }
        case 2: {
          return 'Orange';
        }
        case 3: {
          return 'Yellow';
        }
        case 4: {
          return 'Pink';
        }
        case 5: {
          return 'Cyan';
        }
        case 6: {
          return 'Magenta';
        }
        case 7: {
          return 'BlueViolet';
        }
      }
    }
  }

  isDrawable(x: number, y: number) {
    if (x === -1 || y === -1) {
      return false;
    }
    return true;
  }

  setPrincipal() {
    let hasPrincipal = false;
    for (const annotation of this.annotations) {
      if (annotation.principal) {
        hasPrincipal = true;
        break;
      }
    }

    if (!hasPrincipal) {
      let j = -1;
      for (const annotation of this.annotations) {
        j += 1;
        if (annotation.x !== -1 && annotation.y !== -1) {
          this.annotations[j].principal = true;
          break;
        }
      }
    }
  }

  setAsPrincipal(i: number) {
    if (this.annotations[i].x !== -1 && this.annotations[i].y !== -1) {
      for (const annotation of this.annotations) {
        if (annotation.principal) {
          annotation.principal = false;
          this.annotations[i].principal = true;
          this.setPrincipal();
          this.updateColors();

          if (this.getNbPoints() === 0) {
            this.center = false;
          } else {
            this.center = true;
          }
          break;
        }
      }
    }
  }

  updateColors() {
    let i = 0;
    for (const annotation of this.annotations) {
      if (annotation.x !== -1 && annotation.y !== -1) {
        this.fills[i] = this.getColor(i, annotation.principal);
      } else {
        this.fills[i] = 'none';
      }
      i += 1;
    }
  }

  remove(i: number) {
    this.annotations[i].x = -1;
    this.annotations[i].y = -1;
    this.annotations[i].principal = false;
    this.setPrincipal();
    this.updateColors();

    if (this.getNbPoints() === 0) {
      this.center = false;
    } else {
      this.center = true;
    }
  }
}
