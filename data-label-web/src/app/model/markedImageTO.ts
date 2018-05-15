import {Image} from './image';
import {Annotation} from './Annotation';

export class MarkedImageTO {
  public image: Image;
  public annotations: Annotation[];

  constructor(image: Image, annotations: Annotation[]) {
    this.image = image;
    this.annotations = annotations;
  }
}
