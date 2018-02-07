package com.btd.rest.image;

import java.io.File;
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
import com.btd.service.ImageService;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class) // 1
@SpringBootTest(classes = BTDRestApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImagesRootResourceIT extends ItBase {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ImageRepository imageRepository;

	Image image;
	User user1;
	User user2;

	@Before
	public void setup() {

		user1 = buildUser(true);
		user1.setToken(UUID.randomUUID().toString());
		user1 = userRepository.save(user1);
	}

	@Test
	public void pick() {
		given().get(ApiConstant.IMAGE_COLLECTION_PATH, user1.getToken()).then().log().all().statusCode(200);
	}

	@Test
	public void pickUserNotExists() {
		given().get(ApiConstant.IMAGE_COLLECTION_PATH, UUID.randomUUID().toString()).then().log().all().statusCode(500);
	}

	@Test
	public void markCenter() {

		int x_marked = 10;
		int y_marked = 15;

		String path_local = given().get(ApiConstant.IMAGE_COLLECTION_PATH, user1.getToken()).then().log().all()
				.statusCode(200).extract().path("path_local");

		File file = new File(path_local);
		Image image = ImageService.file2Image(file);

		image.setX(x_marked);
		image.setY(y_marked);
		image.setCenter(true);

		given().contentType(ContentType.JSON).request().body(image)
				.post(ApiConstant.IMAGE_COLLECTION_PATH, user1.getToken().toString()).then().log().all().statusCode(200)
				.body("center", is(equalTo(true))).body("x", is(equalTo(x_marked))).body("y", is(equalTo(y_marked)))
				.body("x_parent", is(equalTo(image.getX_parent()))).body("y_parent", is(equalTo(image.getY_parent())))
				.body("width", is(equalTo(image.getWidth()))).body("height", is(equalTo(image.getHeight())))
				.body("stride", is(equalTo(image.getStride())))
				.body("parent_local", is(equalTo(image.getParent_local())))
				.body("parent_remote", is(equalTo(image.getParent_remote())))
				.body("width_parent", is(equalTo(image.getWidth_parent())))
				.body("height_parent", is(equalTo(image.getHeight_parent()))).body("name", is(equalTo(image.getName())))
				.body("path_local", is(equalTo(image.getPath_local())))
				.body("path_remote", is(equalTo(image.getPath_remote()))).body("user.id", is(equalTo(user1.getId())))
				.body("user.firstname", is(equalTo(user1.getFirstname())))
				.body("user.lastname", is(equalTo(user1.getLastname())))
				.body("user.email", is(equalTo(user1.getEmail()))).body("user.token", is(equalTo(user1.getToken())));
	}

	@Test
	public void markNoCenter() {

		String path_local = given().get(ApiConstant.IMAGE_COLLECTION_PATH, user1.getToken()).then().log().all()
				.statusCode(200).extract().path("path_local");

		File file = new File(path_local);
		Image image = ImageService.file2Image(file);

		image.setX(19);
		image.setY(35);
		image.setCenter(false);

		given().contentType(ContentType.JSON).request().body(image)
				.post(ApiConstant.IMAGE_COLLECTION_PATH, user1.getToken().toString()).then().log().all().statusCode(200)
				.log().body().body("center", is(equalTo(false))).body("x", is(equalTo(-1))).body("y", is(equalTo(-1)))
				.body("x_parent", is(equalTo(image.getX_parent()))).body("y_parent", is(equalTo(image.getY_parent())))
				.body("width", is(equalTo(image.getWidth()))).body("height", is(equalTo(image.getHeight())))
				.body("stride", is(equalTo(image.getStride())))
				.body("parent_local", is(equalTo(image.getParent_local())))
				.body("parent_remote", is(equalTo(image.getParent_remote())))
				.body("width_parent", is(equalTo(image.getWidth_parent())))
				.body("height_parent", is(equalTo(image.getHeight_parent()))).body("name", is(equalTo(image.getName())))
				.body("path_local", is(equalTo(image.getPath_local())))
				.body("path_remote", is(equalTo(image.getPath_remote()))).body("user.id", is(equalTo(user1.getId())))
				.body("user.firstname", is(equalTo(user1.getFirstname())))
				.body("user.lastname", is(equalTo(user1.getLastname())))
				.body("user.email", is(equalTo(user1.getEmail()))).body("user.token", is(equalTo(user1.getToken())));
	}

	@Test
	public void userCantPickSameImage() {

		String path_local = given().get(ApiConstant.IMAGE_COLLECTION_PATH, user1.getToken()).then().log().all()
				.statusCode(200).extract().path("path_local");

		File file = new File(path_local);
		Image image = ImageService.file2Image(file);

		image.setX(19);
		image.setY(35);
		image.setCenter(false);

		given().contentType(ContentType.JSON).request().body(image).when()
				.post(ApiConstant.IMAGE_COLLECTION_PATH, user1.getToken().toString()).then().statusCode(200);

		given().get(ApiConstant.IMAGE_COLLECTION_PATH, user1.getToken().toString()).then()
				.time(greaterThanOrEqualTo(ApiConstant.IMAGE_SEARCH_WAITING_TIME)).statusCode(200);
	}

	@Test
	public void usersCanPickSameImage() {
		
		user2 = buildUser();
		user2.setToken(UUID.randomUUID().toString());
		user2 = userRepository.save(user2);

		String path_local1 = given().get(ApiConstant.IMAGE_COLLECTION_PATH, user1.getToken()).then().log().all()
				.statusCode(200).extract().path("path_local");

		String path_local2 = given().get(ApiConstant.IMAGE_COLLECTION_PATH, user2.getToken()).then().log().all()
				.statusCode(200).extract().path("path_local");

		File file1 = new File(path_local1);
		Image image1 = ImageService.file2Image(file1);

		File file2 = new File(path_local2);
		Image image2 = ImageService.file2Image(file2);

		assertEquals(image1.getId(), image2.getId());
		assertEquals(image1.isCenter(), image2.isCenter());
		assertEquals(image1.getX(), image2.getX());
		assertEquals(image1.getY(), image2.getY());
		assertEquals(image1.getX_parent(), image2.getX_parent());
		assertEquals(image1.getY_parent(), image2.getY_parent());
		assertEquals(image1.getWidth(), image2.getWidth());
		assertEquals(image1.getHeight(), image2.getHeight());
		assertEquals(image1.getStride(), image2.getStride());
		assertEquals(image1.getParent_local(), image2.getParent_local());
		assertEquals(image1.getParent_remote(), image2.getParent_remote());
		assertEquals(image1.getWidth_parent(), image2.getWidth_parent());
		assertEquals(image1.getHeight_parent(), image2.getHeight_parent());
		assertEquals(image1.getName(), image2.getName());
		assertEquals(image1.getPath_local(), image2.getPath_local());
		assertEquals(image1.getPath_remote(), image2.getPath_remote());
		assertEquals(image1.getMarkedDate(), image2.getMarkedDate());
	}

	@Test
	public void usersCanMarkSameImage() {
		
		user2 = buildUser();
		user2.setToken(UUID.randomUUID().toString());
		user2 = userRepository.save(user2);

		String path_local1 = given().get(ApiConstant.IMAGE_COLLECTION_PATH, user1.getToken()).then().log().all()
				.statusCode(200).extract().path("path_local");

		String path_local2 = given().get(ApiConstant.IMAGE_COLLECTION_PATH, user2.getToken()).then().log().all()
				.statusCode(200).extract().path("path_local");

		File file1 = new File(path_local1);
		Image image1 = ImageService.file2Image(file1);
		image1.setCenter(false);

		File file2 = new File(path_local2);
		Image image2 = ImageService.file2Image(file2);
		image2.setCenter(true);
		image2.setX(3);
		image2.setY(15);

		String id1 = given().contentType(ContentType.JSON).request().body(image1)
		.post(ApiConstant.IMAGE_COLLECTION_PATH, user1.getToken().toString()).then().log().all().statusCode(200)
		.log().body().body("center", is(equalTo(false))).body("x", is(equalTo(-1))).body("y", is(equalTo(-1)))
		.body("x_parent", is(equalTo(image1.getX_parent())))
		.body("y_parent", is(equalTo(image1.getY_parent())))
		.body("width", is(equalTo(image1.getWidth())))
		.body("height", is(equalTo(image1.getHeight())))
		.body("stride", is(equalTo(image1.getStride())))
		.body("parent_local", is(equalTo(image1.getParent_local())))
		.body("parent_remote", is(equalTo(image1.getParent_remote())))
		.body("width_parent", is(equalTo(image1.getWidth_parent())))
		.body("height_parent", is(equalTo(image1.getHeight_parent())))
		.body("name", is(equalTo(image1.getName())))
		.body("path_local", is(equalTo(image1.getPath_local())))
		.body("path_remote", is(equalTo(image1.getPath_remote())))
		.body("user.id", is(equalTo(user1.getId())))
		.body("user.firstname", is(equalTo(user1.getFirstname())))
		.body("user.lastname", is(equalTo(user1.getLastname())))
		.body("user.email", is(equalTo(user1.getEmail())))
		.body("user.token", is(equalTo(user1.getToken())))
		.extract().path("id");

		String id2 = given().contentType(ContentType.JSON).request().body(image2)
		.post(ApiConstant.IMAGE_COLLECTION_PATH, user2.getToken().toString()).then().log().all().statusCode(200)
		.log().body().body("center", is(equalTo(true))).body("x", is(equalTo(3))).body("y", is(equalTo(15)))
		.body("x_parent", is(equalTo(image2.getX_parent())))
		.body("y_parent", is(equalTo(image2.getY_parent())))
		.body("width", is(equalTo(image2.getWidth())))
		.body("height", is(equalTo(image2.getHeight())))
		.body("stride", is(equalTo(image2.getStride())))
		.body("parent_local", is(equalTo(image2.getParent_local())))
		.body("parent_remote", is(equalTo(image2.getParent_remote())))
		.body("width_parent", is(equalTo(image2.getWidth_parent())))
		.body("height_parent", is(equalTo(image2.getHeight_parent())))
		.body("name", is(equalTo(image2.getName())))
		.body("path_local", is(equalTo(image2.getPath_local())))
		.body("path_remote", is(equalTo(image2.getPath_remote())))
		.body("user.id", is(equalTo(user2.getId())))
		.body("user.firstname", is(equalTo(user2.getFirstname())))
		.body("user.lastname", is(equalTo(user2.getLastname())))
		.body("user.email", is(equalTo(user2.getEmail())))
		.body("user.token", is(equalTo(user2.getToken())))
		.extract().path("id");
		

		image1 = imageRepository.findOne(id1);
		image2 = imageRepository.findOne(id2);

		// properties that should not be equal
		assertNotEquals(image1.getId(), image2.getId());
		assertNotEquals(image1.isCenter(), image2.isCenter());
		assertNotEquals(image1.getX(), image2.getX());
		assertNotEquals(image1.getY(), image2.getY());
		
		// properties that should be equal
		assertEquals(image1.getX_parent(), image2.getX_parent());
		assertEquals(image1.getY_parent(), image2.getY_parent());
		assertEquals(image1.getWidth(), image2.getWidth());
		assertEquals(image1.getHeight(), image2.getHeight());
		assertEquals(image1.getStride(), image2.getStride());
		assertEquals(image1.getParent_local(), image2.getParent_local());
		assertEquals(image1.getParent_remote(), image2.getParent_remote());
		assertEquals(image1.getWidth_parent(), image2.getWidth_parent());
		assertEquals(image1.getHeight_parent(), image2.getHeight_parent());
		assertEquals(image1.getName(), image2.getName());
		assertEquals(image1.getPath_local(), image2.getPath_local());
		assertEquals(image1.getPath_remote(), image2.getPath_remote());
		
		// own properties
		assertEquals(image1.getUser().getId(), user1.getId());
		
		assertEquals(image2.getUser().getId(), user2.getId());
		
		
	}
}
