package com.btd.service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.btd.model.User;
import com.btd.repository.UserRepository;
import com.btd.rest.ApiConstant;
import com.btd.transfer.UserLoginTO;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Inject
	public ImageService imageService;

	/**
	 * list all users
	 *
	 * @param
	 *
	 * @return
	 * 
	 */
	public List<User> list(String token) {

		return userRepository.findAll();
	}

	/**
	 * get user
	 *
	 * @param userId
	 *
	 * @return
	 * 
	 * 		throws NotFoundException if the user name is not found
	 */
	public User get(String userId) {

		User user = userRepository.findOne(userId);

		if (user == null) {
			throw new NotFoundException();
		}

		return user;
	}

	public User findOne(String userId, String token) {

		lock(token);

		return get(userId);

	}

	/**
	 * get user by token
	 *
	 * @param userId
	 *
	 * @return
	 * 
	 * 		throws NotFoundException if the user name is not found
	 */
	public User getByToken(String token) {

		User user = userRepository.findByToken(token);

		if (user == null) {
			throw new NotFoundException();
		}

		return user;
	}

	/**
	 * create user
	 *
	 * @param user
	 *
	 * @return
	 * 
	 */
	public User create(User create, String token) {

		lock(token);

		User creator = userRepository.findByToken(token);

		User actual = userRepository.findByEmail(create.getEmail().toLowerCase());

		if (actual != null) {
			throw new BadRequestException();
		}

		create.setId(UUID.randomUUID().toString());
		create.setEmail(create.getEmail().toLowerCase());
		create.setPassword(generateString(ApiConstant.PWD_LENGTH));
		create.setCreator(creator);

		return userRepository.save(create);
	}

	/**
	 * update user
	 *
	 * @param user
	 *
	 * @return
	 * 
	 */
	public User update(User update, String userId, String token) {

		lock(token);

		int count = userRepository.countByEmail(update.getEmail().toLowerCase());
		User existing = userRepository.findByEmail(update.getEmail());

		if ((count > 0) && (!existing.getEmail().toLowerCase().equals(update.getEmail().toLowerCase()))) {
			throw new BadRequestException();
		}

		update.setId(existing.getId());
		update.setEmail(update.getEmail().toLowerCase());
		update.setToken(existing.getToken());
		update.setCreateAt(existing.getCreateAt());
		update.setCreator(existing.getCreator());
		update.setPassword(existing.getPassword());

		return userRepository.save(update);
	}

	/**
	 * delete user
	 *
	 * @param userId
	 *
	 * @return
	 * 
	 * 		throws NotFoundException if the user is not found
	 */
	public void delete(String userId, String token) {

		lock(token);

		User admin = userRepository.findByToken(token);

		if (admin.getId().equals(userId)) {
			throw new BadRequestException();
		}

		User user = get(userId);

		userRepository.delete(user.getId());
	}

	/**
	 * connect a user
	 *
	 * @param user
	 *
	 * @return
	 * 
	 * 		throws ForbiddenException if the email and password do not match
	 * 
	 */
	public User login(UserLoginTO userLoginTO) {

		User user = userRepository.findByEmailAndPassword(userLoginTO.getEmail(), userLoginTO.getPassword());

		if (user == null) {
			throw new ForbiddenException();
		}

		user.setToken(UUID.randomUUID().toString());

		return userRepository.save(user);
	}

	/**
	 * disconnect a user
	 *
	 * @param userId
	 *
	 * @return
	 * 
	 * 		throws NotFoundException if the user is not found
	 */
	public void logout(String token) {

		User user = userRepository.findByToken(token);

		if (user == null) {
			throw new NotFoundException();
		}

		user.setToken(null);

		userRepository.save(user);
	}

	public boolean isAdmin(String token) {

		User user = userRepository.findByToken(token);
		if ((user == null) || (!user.isAdmin())) {
			return false;
		}
		return true;
	}

	public void lock(String token) {
		if (!isAdmin(token)) {
			throw new ForbiddenException();
		}
	}

	public String generateString(int lengthChar) {
		int i, randomNum;
		Random r = new Random();
		String newId = "";
		int ascii_min_char_maj = 65;
		int ascii_max_char_maj = 90;
		int ascii_min_char_min = 97;
		int ascii_max_char_min = 122;
		int min_int = 48;
		int max_int = 57;
		int rd;
		for (i = 0; i < lengthChar; i++) {
			rd = r.nextInt((2 - 0) + 1) + 0;
			randomNum = 0;
			if (rd == 0)
				randomNum = r.nextInt((ascii_max_char_maj - ascii_min_char_maj) + 1) + ascii_min_char_maj;
			if (rd == 1)
				randomNum = r.nextInt((ascii_max_char_min - ascii_min_char_min) + 1) + ascii_min_char_min;
			if (rd == 2)
				randomNum = r.nextInt((max_int - min_int) + 1) + min_int;
			newId += (char) randomNum;
		}

		return newId;
	}
}
