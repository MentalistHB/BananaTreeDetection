package com.btd;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import com.btd.model.Image;
import com.btd.model.User;

public class ItBase {

	protected User buildUser(boolean admin, User creator) {
		User item = new User();

		item.setId(UUID.randomUUID().toString());
		item.setEmail("email-" + UUID.randomUUID());
		item.setFirstname("firstname-" + UUID.randomUUID());
		item.setLastname("lastname-" + UUID.randomUUID());
		item.setCreateAt(new Date());
		item.setAdmin(admin);
		item.setCreator(creator);

		return item;
	}

	protected User buildUser(User creator) {
		return buildUser(false, creator);
	}

	protected User buildUser(boolean admin) {
		return buildUser(admin, null);
	}

	protected User buildUser() {
		return buildUser(false, null);
	}

	protected Image buildImage(User user) {

		Random r = new Random();
		DateTracker dateTracker = new DateTracker();
		Image item = new Image();

		item.setId(UUID.randomUUID().toString());
		item.setCenter(true);
		item.setX(r.nextInt());
		item.setY(r.nextInt());
		item.setMarkedDate(dateTracker.getCurrentDate());
		item.setX_parent(r.nextInt());
		item.setY_parent(r.nextInt());
		item.setWidth(r.nextInt());
		item.setHeight(r.nextInt());
		item.setStride(r.nextInt());
		item.setParent_local("parent-local-" + UUID.randomUUID().toString());
		item.setParent_remote("parent-remote-" + UUID.randomUUID().toString());
		item.setWidth_parent(r.nextInt());
		item.setHeight_parent(r.nextInt());
		item.setName("name-" + UUID.randomUUID().toString());
		item.setPath_local("path-local-" + UUID.randomUUID().toString());
		item.setPath_remote("path-remote-" + UUID.randomUUID().toString());
		item.setMarkedDate(new Date());
		item.setUser(user);

		return item;
	}

	protected Image buildImage() {
		return buildImage(null);
	}
}
