package com.btd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.btd.rest.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	public User findByEmail(String email);

	public int countByEmail(String email);

	public User findByEmailAndPassword(String email, String password);

	public User findByToken(String token);
}
