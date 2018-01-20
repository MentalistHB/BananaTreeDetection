import {Image} from '../model/image';

export class Object2Image {

  public static image: Image;

  constructor() {

  }

  public static apply(object): Image {

    this.image = {
      id: object.id,
      center: object.center,
      x: object.x,
      y: object.y,
      x_parent: object.x_parent,
      y_parent: object.y_parent,
      width: object.width,
      height: object.height,
      stride: object.stride,
      parent_local: object.parent_local,
      parent_remote: object.parent_remote,
      width_parent: object.width_parent,
      height_parent: object.height_parent,
      name: object.name,
      path_local: object.path_local,
      path_remote: object.path_remote,
      markedDate: object.markedDate,
      user: object.user
    };

    return this.image;
  }
}
