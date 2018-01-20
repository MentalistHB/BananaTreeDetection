import {User} from './user';

export class Image {
  public id: string;
  public center: boolean;
  public x: number;
  public y: number;
  public x_parent: number;
  public y_parent: number;
  public width: number;
  public height: number;
  public stride: number;
  public parent_local: string;
  public parent_remote: string;
  public width_parent: number;
  public height_parent: number;
  public name: string;
  public path_local: string;
  public path_remote: string;
  public markedDate;
  public user: User;
}
