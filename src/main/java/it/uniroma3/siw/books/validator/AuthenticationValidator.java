package it.uniroma3.siw.books.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.books.model.Author;
import it.uniroma3.siw.books.model.Credential;
import it.uniroma3.siw.books.model.Review;
import it.uniroma3.siw.books.service.CredentialService;
import siw.uniroma3.asroma3.service.CredentialsService;

@Component
public class AuthenticationValidator implements Validator {
	
	
	@Autowired CredentialService credentialService;
	
	
	@Override
	public void validate(Object o,Errors errors) {
		Credential credential = (Credential) o;
		
	      if (credentialService.existsByUsername(credential.getUsername())) {
	          errors.rejectValue("username", "username.duplicate");
	
	        }
	}
    @Override
    public boolean supports(Class<?> clazz) {
        return Review.class.isAssignableFrom(clazz);
    }
}
