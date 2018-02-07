package com.btd.rest.image.review;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.btd.BTDRestApiApplication;
import com.btd.ItBase;
import com.btd.model.Image;
import com.btd.model.User;
import com.btd.repository.ImageRepository;
import com.btd.repository.UserRepository;
import com.btd.rest.ApiConstant;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class) // 1
@SpringBootTest(classes = BTDRestApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewsResourceIT extends ItBase {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ImageRepository imageRepository;

	Image image1;
	Image image2;
	Image image3;
	User user1;
	User user2;

	@Before
	public void setup() {

		user1 = buildUser(false);
		user1.setToken(UUID.randomUUID().toString());
		user1 = userRepository.save(user1);

		user2 = buildUser(true);
		user2.setToken(UUID.randomUUID().toString());
		user2 = userRepository.save(user2);

		image1 = buildImage(user1);
		image2 = buildImage(user1);
		image3 = buildImage(user1);

		imageRepository.save(Arrays.asList(image1, image2, image3));
	}

	@Test
	public void pickToReview() {

		String imageId = given().get(ApiConstant.IMAGE_REVIEW_COLLECTION_PATH, user2.getToken(), user1.getId()).then()
				.statusCode(200).extract().path("id");

		Image image = imageRepository.findOne(imageId);

		assertEquals(image.getId(), image1.getId());
		assertEquals(image.isCenter(), image1.isCenter());
		assertEquals(image.getX(), image1.getX());
		assertEquals(image.getY(), image1.getY());
		assertEquals(image.getX_parent(), image1.getX_parent());
		assertEquals(image.getY_parent(), image1.getY_parent());
		assertEquals(image.getWidth(), image1.getWidth());
		assertEquals(image.getHeight(), image1.getHeight());
		assertEquals(image.getStride(), image1.getStride());
		assertEquals(image.getParent_local(), image1.getParent_local());
		assertEquals(image.getParent_remote(), image1.getParent_remote());
		assertEquals(image.getWidth_parent(), image1.getWidth_parent());
		assertEquals(image.getHeight_parent(), image1.getHeight_parent());
		assertEquals(image.getName(), image1.getName());
		assertEquals(image.getPath_local(), image1.getPath_local());
		assertEquals(image.getPath_remote(), image1.getPath_remote());
		assertEquals(image.getMarkedDate(), image1.getMarkedDate());
	}
}
