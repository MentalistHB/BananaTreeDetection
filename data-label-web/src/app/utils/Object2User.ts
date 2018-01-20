import {User} from '../model/user';

export class Object2User {

  public static user: User;

  constructor() {

  }

  public static apply(object): User {

    this.user = {
      id: object.id,
      email: object.email,
      password: '',
      firstname: object.firstname,
      lastname: object.lastname,
      admin: object.admin,
      token: object.token,
    };
    return this.user;
  }
}
