package it.uniroma3.siw.books.service;

import java.util.List;
import it.uniroma3.siw.books.repository.NationalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.books.controller.NationalityController;
import it.uniroma3.siw.books.model.Nationality;

@Service
public class NationalityService {

   @Autowired private NationalityRepository nationalityRepository;


  
	
	public List<Nationality> findAll(){
		return  (List<Nationality>) this.nationalityRepository.findAll();
	}

}
