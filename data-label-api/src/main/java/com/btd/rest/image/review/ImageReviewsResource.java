package com.btd.rest.image.review;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.btd.model.Image;
import com.btd.rest.ApiConstant;
import com.btd.service.ImageService;

@RestController
@CrossOrigin(origins = "*")
public class ImageReviewsResource {

	@Inject
	ImageService imageService;

	@RequestMapping(path = ApiConstant.IMAGE_REVIEW_COLLECTION_PATH, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public Image pickToReview(@PathVariable("userId") String userId, @PathVariable("token") String token) {

		return imageService.pickToReview(userId, token);
	}
}
