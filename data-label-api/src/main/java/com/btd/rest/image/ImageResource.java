package com.btd.rest.image;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btd.rest.ApiConstant;
import com.btd.service.ImageService;

@RestController
@RequestMapping(ApiConstant.IMAGE_ITEM_PATH)
@CrossOrigin(origins = "*")
public class ImageResource {

	@Inject
	ImageService imageService;

}
