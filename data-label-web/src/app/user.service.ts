import {Injectable} from '@angular/core';
import {UserLogin} from './model/UserLogin';
import {AppConstant} from './AppConstant';
import {Response} from '@angular/http';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {User} from './model/user';
import {UserCreate} from './model/UserCreate';

@Injectable()
export class UserService {

  constructor(private _http: HttpClient) {
  }

  user_url = AppConstant.base + '/users';
  login_url = this.user_url + '/login';
  logout_url = this.user_url + '/logout';

  login(user: UserLogin) {
    return this._http.post(this.login_url, user);
  }

  logout(token: string) {
    return this._http.delete(this.logout_url + '/' + token).map((response: Response) => response.json());
  }

  create(user: UserCreate, token: string) {
    return this._http.post(this.user_url, user).map((response: Response) => response.json());
  }

  delete(userId: string, token: string){
    return this._http.delete(this.user_url + '/' + token + '/' + userId).map((response: Response) => response.json());
  }

  list(token: string){
    return this._http.get(this.user_url + '/' + token).map((response: Response) => response.json());
  }
}
