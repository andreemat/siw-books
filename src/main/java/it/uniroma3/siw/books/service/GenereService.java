package it.uniroma3.siw.books.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.books.model.Genere;
import it.uniroma3.siw.books.repository.GenereRepository;

@Service
public class GenereService {
	
	@Autowired GenereRepository genereRepository;

	public Iterable<Genere> findAll() {
		
		return this.genereRepository.findAll();
	}
	
public List<Genere> findTheMostPopular() {
		
		return this.genereRepository.mostPopular(PageRequest.of(0,8));
	}

	public Genere findGenreById(Long id) {
		
		return this.genereRepository.findById(id).orElse(null);
	}

	public void save(Genere genere) {
		this.genereRepository.save(genere);
		
	}

	public void deleteById(Long id) {
		this.genereRepository.deleteById(id);
		
	}

	public boolean existsByNome(String lowerCase) {
		
		return this.genereRepository.existsByNomeIgnoreCase(lowerCase);
	}




}
