package com.btd.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.btd.model.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

	Page<Image> findByMarkedDateOrderByMarkedDateAsc(Date markedDate, Pageable pageable);

	Page<Image> findByMarkedDateOrderByMarkedDateDesc(Date markedDate, Pageable pageable);

	Image findByName(String name);

	List<Image> findByNameAndUserId(String name, String userId);

	List<Image> findByUserId(String userId);

	List<Image> findByCenterAndUserIdOrderByMarkedDateAsc(boolean center, String userId);
}
