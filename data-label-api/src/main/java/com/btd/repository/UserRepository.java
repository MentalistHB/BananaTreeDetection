package com.btd.repository;

import javax.inject.Named;

import org.springframework.data.jpa.repository.JpaRepository;

import com.btd.model.User;

@Named
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);

    public int countByEmail(String email);

    public User findByEmailAndPassword(String email, String password);

    public User findByToken(String token);
}
