import {Injectable} from '@angular/core';
import {UserLogin} from './model/UserLogin';
import {HttpClient} from '@angular/common/http';
import {AppConstant} from './AppConstant';
import {UserService} from './user.service';
import {Image} from './model/image';
import {MarkedImageTO} from './model/markedImageTO';

@Injectable()
export class ImageService {

  constructor(private _http: HttpClient, private _userService: UserService) {
  }

  pick(userId: number) {
    return this._http.get(this._userService.user_url + '/' + userId + '/images');
  }

  save(markedImageTO: MarkedImageTO, userId: number, token: string) {
    return this._http.post(this._userService.user_url + '/' + userId + '/images?token=' + token, markedImageTO);
  }

  list(userId: number, token: string, center: string) {
    return this._http.get(this._userService.user_url + '/' + userId + '/images/list?token=' + token + '&center=' + center);
  }
}
