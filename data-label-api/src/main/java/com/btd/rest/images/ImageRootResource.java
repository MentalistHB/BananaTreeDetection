package com.btd.rest.images;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.btd.model.Image;
import com.btd.rest.ApiConstant;
import com.btd.service.ImageService;
import com.btd.transfer.SaveImageTO;

@RestController
@CrossOrigin(origins = "*")
public class ImageRootResource {

	@Inject
	ImageService imageService;

	@RequestMapping(path = ApiConstant.IMAGE_COLLECTION_PATH, method = RequestMethod.POST)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response mark(@RequestBody SaveImageTO saveImageTO, @PathVariable("userId") String userId,
			@PathParam("token") String token) {
		try {
			Image markedImage = imageService.mark(saveImageTO, token);
			return Response.status(Status.OK).entity(markedImage).build();
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

	@RequestMapping(path = ApiConstant.IMAGE_COLLECTION_PATH, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pick(@PathVariable("userId") Long userId) {
		try {
			Image pickedImage = imageService.pickImage(userId);
			return Response.status(Status.OK).entity(pickedImage).build();
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

	@RequestMapping(path = ApiConstant.IMAGE_COLLECTION_PATH + "/list", method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listByCenter(@RequestParam("center") String center, @PathParam("token") String token) {
		try {
			List<Image> images = imageService.listByCenter(center, token);
			return Response.status(Status.OK).entity(images).build();
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
