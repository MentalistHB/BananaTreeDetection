import {Image} from '../model/image';

export class Object2Image {

  public static image: Image;

  constructor() {

  }

  public static apply(object): Image {

    this.image = {
      id: object.id,
      center: object.center,
      xcoordParent: object.xcoordParent,
      ycoordParent: object.ycoordParent,
      stride: object.stride,
      pathParent: object.pathParent,
      widthParent: object.widthParent,
      heightParent: object.heightParent,
      name: object.name,
      folder: object.folder,
      markedDate: object.markedDate,
      annotations: object.annotations,
      user: object.user
    };

    return this.image;
  }
}
