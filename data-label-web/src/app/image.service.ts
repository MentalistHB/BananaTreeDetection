import { Injectable } from '@angular/core';
import {UserLogin} from './model/UserLogin';
import {HttpClient} from '@angular/common/http';
import {AppConstant} from './AppConstant';
import {UserService} from './user.service';
import {Image} from './model/image';

@Injectable()
export class ImageService {

  constructor(private _http: HttpClient, private _userService: UserService) { }

  pick(token: string) {
    return this._http.get(this._userService.user_url + '/' + token + '/images');
  }

  pickToReview(userId: string, token: string) {
    return this._http.get(this._userService.user_url + '/' + token + '/images/reviews/' + userId + '/images');
  }

  mark(image: Image, token: string) {
    return this._http.post(this._userService.user_url + '/' + token + '/images', image);
  }

  review(userId: string, imageId: string, token: string, image: Image) {
    return this._http.post(this._userService.user_url + '/' + token + '/images/reviews/' + userId + '/images/' + imageId, image);
  }

  list(token: string, center: string) {
    return this._http.get(this._userService.user_url + '/' + token + '/images/all?center=' + center);
  }
}
