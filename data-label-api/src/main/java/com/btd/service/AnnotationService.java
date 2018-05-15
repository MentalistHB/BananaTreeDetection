package com.btd.service;

import java.io.IOException;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.btd.model.Annotation;
import com.btd.model.Image;
import com.btd.model.User;
import com.btd.repository.AnnotationRepository;
import com.btd.repository.UserRepository;

@Service
@Transactional
public class AnnotationService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AnnotationRepository annotationRepository;
	@Inject
	private ImageService imageService;

	public Set<Annotation> mark(Set<Annotation> annotations, Long imageId, String token) throws IOException {

		User user = userRepository.findByToken(token);

		if (user == null) {
			throw new ForbiddenException("Access denied");
		}

		Image image = imageService.get(imageId);

		annotations.forEach(annotation -> {
			annotation.setImage(image);
			annotationRepository.save(annotation);
		});

		return annotations;
	}
}
