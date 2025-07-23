package it.uniroma3.siw.books.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.books.model.Image;

public interface ImageRepository extends CrudRepository<Image, Long> {

	Image findByNomeFile(String string);

		
}
