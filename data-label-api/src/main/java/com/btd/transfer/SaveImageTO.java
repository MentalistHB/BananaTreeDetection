package com.btd.transfer;

import java.util.Set;

import com.btd.model.Annotation;
import com.btd.model.Image;

public class SaveImageTO {

	private Image image;
	private Set<Annotation> annotations;

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Set<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Set<Annotation> annotations) {
		this.annotations = annotations;
	}

}
