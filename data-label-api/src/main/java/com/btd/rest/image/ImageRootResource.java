package com.btd.rest.image;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

@RestController
@CrossOrigin(origins = "*")
public class ImageRootResource {

	@Inject
	ImageService imageService;

	@RequestMapping(path = ApiConstant.IMAGE_COLLECTION_PATH, method = RequestMethod.POST)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Image mark(@PathVariable("token") String token, @RequestBody Image image) throws IOException {

		return imageService.mark(image, token);
	}

	@RequestMapping(path = ApiConstant.IMAGE_COLLECTION_PATH, method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public Image pick(@PathVariable("token") String token) {

		return imageService.pick_image(token);
	}

	@RequestMapping(path = ApiConstant.IMAGE_COLLECTION_PATH + "/all", method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Image> listByCenter(@RequestParam("center") String center, @PathVariable("token") String token) {

		return imageService.listByCenter(center, token);
	}
}
