package com.btd.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Image implements Serializable {
	private static final long serialVersionUID = -6090769902064343427L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private boolean center;
	private int xcoordParent;
	private int ycoordParent;
	private int stride;
	private String pathParent;
	private int widthParent;
	private int heightParent;
	private String name;
	private String folder;
	private Date markedDate;

	@ManyToOne
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isCenter() {
		return center;
	}

	public void setCenter(boolean center) {
		this.center = center;
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

	public int getXcoordParent() {
		return xcoordParent;
	}

	public void setXcoordParent(int xcoordParent) {
		this.xcoordParent = xcoordParent;
	}

	public int getYcoordParent() {
		return ycoordParent;
	}

	public void setYcoordParent(int ycoordParent) {
		this.ycoordParent = ycoordParent;
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

	public String getPathParent() {
		return pathParent;
	}

	public void setPathParent(String pathParent) {
		this.pathParent = pathParent;
	}

	public int getWidthParent() {
		return widthParent;
	}

	public void setWidthParent(int widthParent) {
		this.widthParent = widthParent;
	}

	public int getHeightParent() {
		return heightParent;
	}

	public void setHeightParent(int heightParent) {
		this.heightParent = heightParent;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

}
