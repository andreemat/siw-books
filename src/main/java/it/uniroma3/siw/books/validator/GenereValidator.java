package it.uniroma3.siw.books.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.books.model.Genere;
import it.uniroma3.siw.books.service.GenereService;
@Component
public class GenereValidator implements Validator {

	@Autowired private GenereService genereService;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object o, Errors errors) {
		Genere genere = (Genere) o;
		if(genereService.existsByNome(genere.getNome().toLowerCase()))
			 errors.reject("genere.invalid");
        
		
	}

}
