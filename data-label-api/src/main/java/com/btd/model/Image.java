package com.btd.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Image implements Serializable {
	private static final long serialVersionUID = -6090769902064343427L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "uuid")
	private UUID id;
	private boolean center;
	private int x_parent;
	private int y_parent;
	private int width;
	private int height;
	private int stride;
	private String parent_local;
	private String parent_remote;
	private int width_parent;
	private int height_parent;
	private String name;
	private String path_local;
	private String path_remote;
	private Date markedDate;
	@OneToMany(mappedBy = "image", fetch = FetchType.EAGER)
	private Set<Annotation> annotations = new HashSet<>();

	@ManyToOne
	private User user;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public boolean isCenter() {
		return center;
	}

	public void setCenter(boolean center) {
		this.center = center;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Date getMarkedDate() {
		return markedDate;
	}

	public void setMarkedDate(Date markedDate) {
		this.markedDate = markedDate;
	}

	public Set<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Set<Annotation> annotations) {
		this.annotations = annotations;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX_parent() {
		return x_parent;
	}

	public void setX_parent(int x_parent) {
		this.x_parent = x_parent;
	}

	public int getY_parent() {
		return y_parent;
	}

	public void setY_parent(int y_parent) {
		this.y_parent = y_parent;
	}

	public int getStride() {
		return stride;
	}

	public void setStride(int stride) {
		this.stride = stride;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getParent_local() {
		return parent_local;
	}

	public void setParent_local(String parent_local) {
		this.parent_local = parent_local;
	}

	public String getParent_remote() {
		return parent_remote;
	}

	public void setParent_remote(String parent_remote) {
		this.parent_remote = parent_remote;
	}

	public String getPath_local() {
		return path_local;
	}

	public void setPath_local(String path_local) {
		this.path_local = path_local;
	}

	public String getPath_remote() {
		return path_remote;
	}

	public void setPath_remote(String path_remote) {
		this.path_remote = path_remote;
	}

	public int getWidth_parent() {
		return width_parent;
	}

	public void setWidth_parent(int width_parent) {
		this.width_parent = width_parent;
	}

	public int getHeight_parent() {
		return height_parent;
	}

	public void setHeight_parent(int height_parent) {
		this.height_parent = height_parent;
	}

	public String toString() {
		return "Center: " + center + "\n" + "x_parent: " + x_parent + "\n" + "y_parent: " + y_parent + "\n" + "width: "
				+ width + "\n" + "height: " + height + "\n" + "stride: " + stride + "\n" + "name: " + name + "\n"
				+ "parent: " + parent_local + "\n" + "old_url: " + path_local + "\n" + "new_url: " + path_remote + "\n"
				+ "Marked date: " + markedDate + "\n";
	}
}
