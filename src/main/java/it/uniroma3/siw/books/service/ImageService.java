package it.uniroma3.siw.books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.books.model.Image;
import it.uniroma3.siw.books.repository.ImageRepository;
import jakarta.transaction.Transactional;

@Service
public class ImageService {

	@Autowired private ImageRepository imageRepository;
	@Transactional
	public void save(Image img) {
		this.imageRepository.save(img);
	}
	public Image findById(Long id) {
		
		return this.imageRepository.findById(id).orElse(null);
	}
	public Image findByNomeFile(String string) {
		
		return imageRepository.findByNomeFile(string);
	}
	public void deleteImage(Long id) {
		
		this.imageRepository.deleteById(id);
		
	}

}
