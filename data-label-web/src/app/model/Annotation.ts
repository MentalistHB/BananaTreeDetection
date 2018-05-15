import {Image} from './image';

export class Annotation {
  public id: number;
  public principal: boolean;
  public x: number;
  public y: number;
  public image: Image;

  constructor(x: number, y: number, principal: boolean) {
    this.x = x;
    this.y = y;
    this.principal = principal;
  }

  isMarked() {
    return ((this.x !== -1) && (this.y !== -1));
  }
}
