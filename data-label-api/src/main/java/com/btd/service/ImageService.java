package com.btd.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.btd.model.Annotation;
import com.btd.model.Image;
import com.btd.model.User;
import com.btd.repository.ImageRepository;
import com.btd.repository.UserRepository;
import com.btd.rest.ApiConstant;
import com.btd.transfer.SaveImageTO;
import com.btd.transfer.SearchImageTO;

@Service
@Transactional
public class ImageService {

	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private UserRepository userRepository;
	@Inject
	private AnnotationService annotationService;

	/**
	 * list all images
	 *
	 * @param
	 *
	 * @return
	 * 
	 */
	public SearchImageTO list(Date date, int page, String sort) {

		Page<Image> images;

		if (sort == null) {
			sort = ApiConstant.SORT_DESC;
		}

		if (sort.equals(ApiConstant.SORT_ASC)) {
			images = imageRepository.findByMarkedDateOrderByMarkedDateAsc(date,
					new PageRequest(page, ApiConstant.PAGE_SIZE));
		} else {
			images = imageRepository.findByMarkedDateOrderByMarkedDateDesc(date,
					new PageRequest(page, ApiConstant.PAGE_SIZE));
		}

		SearchImageTO result = new SearchImageTO();
		result.setPage(images.getNumber());
		result.setPage(images.getTotalPages());
		result.setSize(images.getSize());
		result.setSort(sort);
		result.setImages(images.getContent());

		return result;
	}

	/**
	 * get an image from the database
	 *
	 * @param
	 *
	 * @return
	 * 
	 */
	public Image get(Long imageId) {

		Image image = imageRepository.findOne(imageId);

		if (image == null) {
			throw new NotFoundException("The image cannot be found");
		}

		return image;
	}

	public Image mark(SaveImageTO saveImageTO, String token) throws IOException {

		User user = userRepository.findByToken(token);

		if (user == null) {
			throw new ForbiddenException("Access denied");
		}

		Image image = saveImageTO.getImage();
		Set<Annotation> annotations = saveImageTO.getAnnotations();
		
		// check if the image has already been marked by the user
		if (hasAlreadyMarked(image.getName(), user.getId())) {
			throw new BadRequestException("The image has already been marked");
		}

		String oldFolder = image.getFolder();

		image.setMarkedDate(new Date());
		image.setUser(user);
		image.setFolder(ApiConstant.MARKED_CROPED_DIR);
		image = imageRepository.save(image);
		
		annotationService.mark(annotations, image.getId(), token);

		move(new ClassPathResource(ApiConstant.IMAGE_DIR + oldFolder + image.getName()).getFile().getAbsolutePath(),
				new ClassPathResource(ApiConstant.IMAGE_DIR + ApiConstant.MARKED_CROPED_README).getFile()
						.getAbsolutePath().replace("readme", "") + image.getName());

		return image;
	}

	/**
	 * get an image to mark from the server
	 *
	 * @param
	 *
	 * @return
	 * @throws IOException
	 * 
	 */
	public Image pickImage(Long userId) throws IOException {

		User user = userRepository.findOne(userId);

		// check if the user is connected
		if (user == null) {
			throw new NotFoundException("The user cannot be found");
		}

		// get the folder of non marked images
		File notMarkedDir = new ClassPathResource(ApiConstant.IMAGE_DIR + ApiConstant.NOT_MARKED_CROPED_DIR).getFile();

		// get the list of all sub folders assuming that there are only
		// folder there
		List<File> notMarkedDirFiles = Arrays.asList(notMarkedDir.listFiles());

		if (notMarkedDirFiles.isEmpty()) {
			throw new NotFoundException("There is no image no annonate");
		}

		// get the first file in the list
		File imageFile = notMarkedDirFiles.get(0);

		return file2Image(imageFile, false);
	}

	/**
	 * list images
	 *
	 * @param
	 *
	 * @return
	 * 
	 */
	public List<Image> listByCenter(String center, String token) {

		User user = userRepository.findByToken(token);

		// check if the user is connected
		if (user == null) {
			throw new ForbiddenException("Access denied");
		}

		if (center.equals("2")) {
			return imageRepository.findByUserId(user.getId());
		}

		boolean centerValue;
		if (center.equals("0")) {
			centerValue = false;
		} else {
			centerValue = true;
		}

		return imageRepository.findByCenterAndUserIdOrderByMarkedDateAsc(centerValue, user.getId());
	}

	public int generateIntRandom(int min, int max) {

		Random r = new Random();
		int number = r.nextInt((max - min) + 1) + min;

		return number;
	}

	public static Image file2Image(File file, boolean marked) {

		String imageName = FilenameUtils.getBaseName(file.getAbsolutePath());
		String[] imageInfos = imageName.split(",");

		Image image = new Image();

		image.setId(null);
		image.setCenter(false);
		image.setName(file.getName());
		image.setPathParent(ApiConstant.PARENT_DIR + imageInfos[1] + ApiConstant.IMAGE_EXTENSION);
		image.setXcoordParent(Integer.parseInt(imageInfos[2]));
		image.setYcoordParent(Integer.parseInt(imageInfos[3]));
		image.setWidthParent(Integer.parseInt(imageInfos[4]));
		image.setHeightParent(Integer.parseInt(imageInfos[5]));
		image.setStride(Integer.parseInt(imageInfos[6]));
		image.setName(file.getName());
		image.setUser(null);

		if (marked) {
			image.setFolder(ApiConstant.MARKED_CROPED_DIR);
		} else {
			image.setFolder(ApiConstant.NOT_MARKED_CROPED_DIR);
		}

		return image;
	}

	public boolean hasAlreadyMarked(String name, Long userId) {
		List<Image> actual = imageRepository.findByNameAndUserId(name, userId);

		if (actual.size() == 0) {
			return false;
		}

		return true;
	}

	public static File getCropParentImage(File sub_file) {
		File parent = new File(sub_file.getParent());
		File parent_parent = new File(parent.getParent());

		for (File file : parent_parent.listFiles()) {
			if (!file.isDirectory() && file.getName().endsWith(".jpeg")) {
				return file;
			}
		}
		return null;
	}

	public void move(String src, String dest) throws IOException {

		String srcTarget = src;

		String newSrc = src.replace("target/classes", "src/main/resources");
		String newDest = dest.replace("target/classes", "src/main/resources");

		FileUtils.moveFile(FileUtils.getFile(newSrc), FileUtils.getFile(newDest));

		FileUtils.deleteQuietly(FileUtils.getFile(srcTarget));
	}
}
