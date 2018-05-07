package com.btd.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.btd.model.Image;
import com.btd.model.User;
import com.btd.repository.ImageRepository;
import com.btd.repository.UserRepository;
import com.btd.rest.ApiConstant;
import com.btd.transfer.SearchImageTO;

@Service
@Transactional
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;
    @Inject
    private UserService userService;

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
    public Image get(UUID imageId) {

        Image image = imageRepository.findOne(imageId);

        if (image == null) {
            throw new NotFoundException();
        }

        return image;
    }

    public Image mark(Image image, UUID token) throws IOException {

        User user = userService.getByToken(token);

        // check if the image has already been marked by the user
        if (asAlreadyMarked(image.getName(), user.getId())) {
            throw new BadRequestException();
        }

        image.setMarkedDate(new Date());
        image.setUser(user);

        // check if the image has a center
        if (!image.isCenter()) {
            // if not, then set the list of annotations to empty
            image.setAnnotations(new HashSet<>());
        }

        // move(image.getPath_local(), ApiConstant.DIR_LOCAL +
        // ApiConstant.CLASSIFICATOR_TRAINING_MARKED_DIR_REMOTE +
        // image.getName());

        return imageRepository.save(image);
    }

    /**
     * get an image to mark from the server
     *
     * @param
     *
     * @return
     * 
     */
    public Image pickImage(UUID token) {

        User user = userRepository.findByToken(token);

        // check if the user is connected
        if (user == null) {
            throw new NotFoundException();
        }

        // get date of start
        Date start = new Date();
        long diff = 0L;
        // look for an image in each folder randomly
        while (diff < ApiConstant.IMAGE_SEARCH_WAITING_TIME) {
            // get the folder of non marked images
            File notMarkedDir = new File(
                    ApiConstant.DIR_LOCAL + ApiConstant.CLASSIFICATOR_TRAINING_NOT_MARKED_DIR_REMOTE);

            // get the list of all sub folders assuming that there are only
            // folder there
            List<File> notMarkedDirFiles = Arrays.asList(notMarkedDir.listFiles());

            // generate a number between 0 and the number of sub folders minus
            // one to index
            int dir_index = generateIntRandom(0, notMarkedDirFiles.size() - 1);

            // get a folder randomly
            File randomDir = notMarkedDirFiles.get(dir_index);

            // get the folder of croped images
            File cropedDir = new File(randomDir.getAbsolutePath() + "/croped");

            // initialize the count of tries
            int _try = 0;

            // begin tries
            do {

                // get the list of files (images) or folders
                List<File> sub_notMarkedDir = Arrays.asList(cropedDir.listFiles());

                // initialize the list of true files
                List<File> sub_images = new ArrayList<>();

                for (File file : sub_notMarkedDir) {
                    if (!file.isDirectory()) {
                        sub_images.add(file);
                    }
                }

                // get an index randomly
                int file_index = generateIntRandom(0, sub_images.size() - 1);

                // get the corresponding file
                File sub_image = sub_images.get(file_index);

                // convert it to an image
                Image image = file2Image(sub_image);

                // check if that file was already marked by the given user
                // if not then give the file to mark to the user
                if (!asAlreadyMarked(image.getName(), user.getId())) {
                    return image;
                }

                // increase the number of tries
                _try = _try + 1;
            } while (_try < ApiConstant.IMAGE_SEARCH_TRY);
            // get current date
            Date currentDate = new Date();
            diff = Math.abs(start.getTime() - currentDate.getTime());
        }
        return new Image();
    }

    /**
     * list images
     *
     * @param
     *
     * @return
     * 
     */
    public List<Image> listByCenter(String center, UUID token) {

        User user = userRepository.findByToken(token);

        // check if the user is connected
        if (user == null) {
            throw new NotFoundException();
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

    public static Image file2Image(File file) {
        Image image = new Image();

        image.setCenter(false);
        image.setName(file.getName());
        image.setParent_local(getCropParentImage(file).getAbsolutePath());
        image.setParent_remote("/" + image.getParent_local().replace(ApiConstant.DIR_LOCAL, ""));
        image.setPath_local(file.getAbsolutePath());
        image.setPath_remote("/" + image.getPath_local().replace(ApiConstant.DIR_LOCAL, ""));

        String[] split = file.getName().split("\\_", -1);

        // get x parent
        String[] x_part = split[1].split("\\-", -1);
        int x_parent = Integer.parseInt(x_part[1]);

        // get y parent
        String[] y_part = split[2].split("\\-", -1);
        int y_parent = Integer.parseInt(y_part[1]);

        // get width
        int width = Integer.parseInt(split[3]);

        // get height
        int height = Integer.parseInt(split[5]);

        // get stride
        String[] stride_part = split[6].split("\\-", -1);
        String[] stride_sub_part = stride_part[1].split("\\.", -1);

        int stride = Integer.parseInt(stride_sub_part[0]);

        // get width parent
        String[] parent_width_part = split[7].split("\\-", -1);
        String[] parent_width_sub_part = parent_width_part[1].split("\\.", -1);

        int width_parent = Integer.parseInt(parent_width_sub_part[0]);

        // get height parent
        String[] parent_height_part = split[8].split("\\-", -1);
        String[] parent_height_sub_part = parent_height_part[1].split("\\.", -1);

        int height_parent = Integer.parseInt(parent_height_sub_part[0]);

        // get height parent

        image.setX_parent(x_parent);
        image.setY_parent(y_parent);
        image.setWidth(width);
        image.setHeight(height);
        image.setStride(stride);
        image.setWidth_parent(width_parent);
        image.setHeight_parent(height_parent);

        return image;
    }

    public boolean asAlreadyMarked(String name, UUID userId) {
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

        File f_src = new File(src);
        File f_dest = new File(dest);
        Files.copy(f_src.toPath(), f_dest.toPath());
    }
}
