package com.btd.rest;

public class ApiConstant {

	public static final String ADMIN_EMAIL = "nganyana@th-brandenburg.de";
	public static final String ADMIN_PWD = "admin";
	public static final String ADMIN_FIRSTNAME = "Herval";
	public static final String ADMIN_LASTNAME = "Nganya";

	public static final String ROOT_PATH = "/api";
	public static final String USER_COLLECTION_PATH = ROOT_PATH + "/users/{token}";
	public static final String USER_ITEM_PATH = USER_COLLECTION_PATH + "/{userId}";

	public static final String USER_LOGIN_PATH = ROOT_PATH + "/users/login";
	public static final String USER_LOGOUT_PATH = ROOT_PATH + "/users/logout/{token}";

	public static final String IMAGE_COLLECTION_PATH = USER_COLLECTION_PATH + "/images";
	public static final String IMAGE_ITEM_PATH = IMAGE_COLLECTION_PATH + "/{imageId}";

	public static final String SORT_ASC = "ASC";
	public static final String SORT_DESC = "DESC";

	public static final int PAGE_SIZE = 25;

	public static final int PWD_LENGTH = 10;

	public static final String DIR_LOCAL = "/opt/lampp/htdocs/";

	public static final String BASE_DIR = "data_api/";
	public static final String CLASSIFICATOR_DIR_REMOTE = BASE_DIR + "classificator/";
	public static final String CLASSIFICATOR_TRAINING_DIR_REMOTE = CLASSIFICATOR_DIR_REMOTE + "training/";
	public static final String CLASSIFICATOR_TRAINING_MARKED_DIR_REMOTE = CLASSIFICATOR_TRAINING_DIR_REMOTE + "marked/";
	public static final String CLASSIFICATOR_TRAINING_NOT_MARKED_DIR_REMOTE = CLASSIFICATOR_TRAINING_DIR_REMOTE + "not_marked/";
	
	public static final String PARENT_EXTENSION = ".jpeg";

	public static final int IMAGE_SEARCH_TRY = 10;

	public static final long IMAGE_SEARCH_WAITING_TIME = 10000L;
}
