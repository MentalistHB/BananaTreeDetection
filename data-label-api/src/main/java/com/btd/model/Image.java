package com.btd.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Image implements Serializable {
	private static final long serialVersionUID = -6090769902064343427L;

	@Id
	private String id;
	private boolean center;
	private int x;
	private int y;
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

	// review attributes

	private boolean reviewed;
	@ManyToOne
	private User reviewer;
	private Date reviewedDate;
	private int previous_x;
	private int previous_y;
	private boolean changed;

	@ManyToOne
	private User user;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isCenter() {
		return center;
	}

	public void setCenter(boolean center) {
		this.center = center;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
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

	public boolean isReviewed() {
		return reviewed;
	}

	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

	public User getReviewer() {
		return reviewer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	public Date getReviewedDate() {
		return reviewedDate;
	}

	public void setReviewedDate(Date reviewedDate) {
		this.reviewedDate = reviewedDate;
	}

	public int getPrevious_x() {
		return previous_x;
	}

	public void setPrevious_x(int previous_x) {
		this.previous_x = previous_x;
	}

	public int getPrevious_y() {
		return previous_y;
	}

	public void setPrevious_y(int previous_y) {
		this.previous_y = previous_y;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public String toString() {
		return "Center: " + center + "\n" + "x: " + x + "\n" + "y: " + y + "\n" + "x_parent: " + x_parent + "\n"
				+ "y_parent: " + y_parent + "\n" + "width: " + width + "\n" + "height: " + height + "\n" + "stride: "
				+ stride + "\n" + "name: " + name + "\n" + "parent: " + parent_local + "\n" + "old_url: " + path_local
				+ "\n" + "new_url: " + path_remote + "\n" + "Marked date: " + markedDate + "\n";
	}
}
