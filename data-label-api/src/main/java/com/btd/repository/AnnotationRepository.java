package com.btd.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.btd.model.Annotation;

public interface AnnotationRepository extends JpaRepository<Annotation, UUID> {
}
