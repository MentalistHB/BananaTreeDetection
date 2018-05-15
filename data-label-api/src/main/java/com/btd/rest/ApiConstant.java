package com.btd.rest;

public class ApiConstant {

	public static final String ADMIN_EMAIL = "nganyana@th-brandenburg.de";
	public static final String ADMIN_PWD = "admin";
	public static final String ADMIN_FIRSTNAME = "Herval";
	public static final String ADMIN_LASTNAME = "Nganya";

	public static final String REGISTRATION_SUBJECT = "New registration on the plattform BTD-2018";
	public static final String REGISTRATION_TEXT = "Thank you for accepting to be help on this project.\n"
			+ "On the bottom of this message, you will find your credentials to sign in to the plattform.\n"
			+ "Feel free to contact the administrator per email under " + ADMIN_EMAIL + " if you get any problem.\n\n"
			+ "Kind regards,\n" + ADMIN_FIRSTNAME + ADMIN_LASTNAME + "\n\n";

	public static final String ROOT_PATH = "/api";
	public static final String USER_COLLECTION_PATH = ROOT_PATH + "/users";
	public static final String USER_ITEM_PATH = USER_COLLECTION_PATH + "/{userId}";

	public static final String USER_LOGIN_PATH = ROOT_PATH + "/users/login";
	public static final String USER_LOGOUT_PATH = ROOT_PATH + "/users/logout";

	public static final String IMAGE_COLLECTION_PATH = USER_ITEM_PATH + "/images";
	public static final String IMAGE_ITEM_PATH = IMAGE_COLLECTION_PATH + "/{imageId}";

	public static final String ANNOTATION_COLLECTION_PATH = IMAGE_ITEM_PATH + "/annotations";

	public static final String SORT_ASC = "ASC";
	public static final String SORT_DESC = "DESC";

	public static final int PAGE_SIZE = 25;

	public static final int PWD_LENGTH = 10;

	public static final String STATIC_DIR = "static/";
	public static final String IMAGE_DIR = STATIC_DIR + "images/";
	public static final String CROPED_DIR = "croped/";
	public static final String MARKED_CROPED_DIR = CROPED_DIR + "marked/";
	public static final String NOT_MARKED_CROPED_DIR = CROPED_DIR + "not_marked/";
	public static final String PARENT_DIR = "parents/";
	public static final String MARKED_CROPED_README = MARKED_CROPED_DIR + "readme";

	public static final String IMAGE_EXTENSION = ".jpg";
}
