package com.btd.rest.users;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.btd.model.User;
import com.btd.rest.ApiConstant;
import com.btd.service.UserService;

@RestController
@RequestMapping(ApiConstant.USER_ITEM_PATH)
@CrossOrigin(origins = "*")
public class UserResource {

	@Inject
	UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathVariable("userId") Long userId, @PathParam("token") String token) {
		try {
			User user = userService.findOne(userId, token);
			return Response.status(Status.OK).entity(user).build();
		} catch (NotFoundException e) {
			return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (ForbiddenException e) {
			return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@RequestMapping(method = RequestMethod.PUT)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathVariable("userId") Long userId, @PathParam("token") String token,
			@RequestBody User user) {
		try {
			User updated = userService.update(user, userId, token);
			return Response.status(Status.OK).entity(updated).build();
		} catch (NotFoundException e) {
			return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (ForbiddenException e) {
			return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public Response delete(@PathVariable("userId") Long userId, @PathParam("token") String token) {
		try {
			userService.delete(userId, token);
			return Response.status(Status.OK).build();
		} catch (NotFoundException e) {
			return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (ForbiddenException e) {
			return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
