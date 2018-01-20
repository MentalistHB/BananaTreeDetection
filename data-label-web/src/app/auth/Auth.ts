import {User} from '../model/user';

export class Auth {

  public static user: User;

  constructor() {

  }

  public static isAuthenticated() {

    const id = sessionStorage.getItem('user.id');
    if (id == null) {
      return false;
    } else {
      return true;
    }

  }

  public static store(key: string, value) {
    sessionStorage.setItem(key, value);
  }

  public static storeUser(user: User) {
    sessionStorage.setItem('user.id', user.id);
    sessionStorage.setItem('user.email', user.email);
    sessionStorage.setItem('user.firstname', user.firstname);
    sessionStorage.setItem('user.lastname', user.lastname);
    sessionStorage.setItem('user.admin', user.admin);
    sessionStorage.setItem('user.token', user.token);
  }

  public static getUser() {
    const id = sessionStorage.getItem('user.id');
    const email = sessionStorage.getItem('user.email');
    const firstname = sessionStorage.getItem('user.firstname');
    const lastname = sessionStorage.getItem('user.lastname');
    const admin = sessionStorage.getItem('user.admin');
    const token = sessionStorage.getItem('user.token');

    this.user = {
      id: id,
      email: email,
      password: '',
      firstname: firstname,
      lastname: lastname,
      admin: admin,
      token: token,
    };

    return this.user;
  }

  public static reset() {
    sessionStorage.clear();
  }


}
