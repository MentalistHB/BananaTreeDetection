package com.btd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.btd.rest.model.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

}
