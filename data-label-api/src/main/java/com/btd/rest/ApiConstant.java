package com.btd.rest;

public class ApiConstant {

	public static final String ROOT_PATH = "/api";
	public static final String USER_COLLECTION_PATH = ROOT_PATH + "/users/{token}";
	public static final String USER_ITEM_PATH = USER_COLLECTION_PATH + "/{userId}";

	public static final String USER_LOGIN_PATH = ROOT_PATH + "/login";
	public static final String USER_LOGOUT_PATH = ROOT_PATH + "/logout/{token}";

	public static final String IMAGE_COLLECTION_PATH = USER_ITEM_PATH + "/users";
	public static final String IMAGE_ITEM_PATH = IMAGE_COLLECTION_PATH + "/{imageId}";

	public static final int PWD_LENGTH = 10;
}
