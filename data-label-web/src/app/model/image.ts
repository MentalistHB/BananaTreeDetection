import {User} from './user';

export class Image {
  public id: number;
  public center: boolean;
  public xcoordParent: number;
  public ycoordParent: number;
  public stride: number;
  public pathParent: string;
  public widthParent: number;
  public heightParent: number;
  public name: string;
  public folder: string;
  public markedDate;
  public user: User;
}
