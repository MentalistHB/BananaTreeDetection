package com.btd;

import java.util.Date;
import java.util.UUID;

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
}
