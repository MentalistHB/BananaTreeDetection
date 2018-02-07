package com.btd.rest.image.review;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.btd.model.Image;
import com.btd.rest.ApiConstant;
import com.btd.service.ImageService;

@RestController
@CrossOrigin(origins = "*")
public class ImageReviewResource {

	@Inject
	ImageService imageService;

	@RequestMapping(path = ApiConstant.IMAGE_REVIEW_ITEM_PATH, method = RequestMethod.POST)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Image review(@PathVariable("imageId") String imageId,
			@PathVariable("token") String token, @RequestBody Image image) {

		return imageService.review(imageId, token, image);
	}
}
