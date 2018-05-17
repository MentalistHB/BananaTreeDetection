import {Injectable} from '@angular/core';
import {UserLogin} from './model/UserLogin';
import {AppConstant} from './AppConstant';
import {HttpClient} from '@angular/common/http';
import {UserCreate} from './model/UserCreate';

@Injectable()
export class UserService {

  user_url = AppConstant.base + '/users';
  login_url = this.user_url + '/login';
  logout_url = this.user_url + '/logout';

  constructor(private _http: HttpClient) {
  }

  login(user: UserLogin) {
    return this._http.post(this.login_url, user);
  }

  logout(token: string) {
    return this._http.delete(this.logout_url + '?token=' + token);
  }

  create(user: UserCreate, token: string) {
    return this._http.post(this.user_url + '?token=' + token, user);
  }

  edit(user: UserCreate, userId: string, token: string) {
    return this._http.put(this.user_url + '/' + userId + '?token=' + token, user);
  }

  delete(userId: string, token: string) {
    return this._http.delete(this.user_url + '/' + userId + '?token=' + token);
  }

  list(token: string) {
    return this._http.get(this.user_url + '?token=' + token);
  }

  get(userId: string, token: string) {
    return this._http.get(this.user_url + '/' + userId + '?token=' + token);
  }
}
