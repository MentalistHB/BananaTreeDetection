package com.btd.rest.images.annotations;

import java.util.Set;

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

import com.btd.model.Annotation;
import com.btd.rest.ApiConstant;
import com.btd.service.AnnotationService;

@RestController
@CrossOrigin(origins = "*")
public class AnnotationsResource {

	@Inject
	private AnnotationService annotationService;

	@RequestMapping(path = ApiConstant.ANNOTATION_COLLECTION_PATH, method = RequestMethod.POST)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response mark(@RequestBody Set<Annotation> annotations, @PathVariable("userId") Long userId,
			@PathVariable("imageId") Long imageId, @PathParam("token") String token) {
		try {
			Set<Annotation> created = annotationService.mark(annotations, imageId, token);
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
}
