package com.btd.rest.users;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.btd.model.User;
import com.btd.rest.ApiConstant;
import com.btd.service.UserService;

@RestController
@CrossOrigin(origins = "*")
public class UserRootResource {

	@Inject
	UserService userService;

	@RequestMapping(path = ApiConstant.USER_COLLECTION_PATH, method = RequestMethod.POST)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@RequestBody User user, @PathParam("token") String token) {
		try {
			User created = userService.create(user, token);
			return Response.status(Status.OK).entity(created).build();
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

	@RequestMapping(path = ApiConstant.USER_COLLECTION_PATH, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public Response list(@PathParam("token") String token) {
		try {
			List<User> users = userService.list(token);
			return Response.status(Status.OK).entity(users).build();
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
