package com.btd.rest.user;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.btd.rest.ApiConstant;
import com.btd.rest.model.User;
import com.btd.service.UserService;

@RestController
@RequestMapping(ApiConstant.USER_ITEM_PATH)
public class UserResource {

	@Inject
	UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public User get(@PathVariable("userId") String userId) {

		return userService.get(userId);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@PathVariable("userId") String userId, @PathVariable("token") String token) {

		userService.delete(userId, token);
	}
}
