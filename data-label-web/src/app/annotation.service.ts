import { Injectable } from '@angular/core';
import {UserService} from './user.service';
import {HttpClient} from '@angular/common/http';
import {Image} from './model/image';
import {Annotation} from './model/Annotation';

@Injectable()
export class AnnotationService {

  constructor(private _http: HttpClient, private _userService: UserService) {
  }

  save(annotations: Annotation[], userId: number, imageId: number,  token: string) {
    return this._http.post(this._userService.user_url + '/' + userId + '/images/' + imageId + '/annotations?token=' + token, annotations);
  }

}
