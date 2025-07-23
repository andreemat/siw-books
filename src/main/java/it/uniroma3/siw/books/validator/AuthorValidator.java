package it.uniroma3.siw.books.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.books.model.Author;
import it.uniroma3.siw.books.model.Review;
import it.uniroma3.siw.books.model.User;
import it.uniroma3.siw.books.service.AuthorService;
import it.uniroma3.siw.books.service.ReviewService;


@Component
public class AuthorValidator implements Validator{
	
	
	@Autowired AuthorService authorService;
	
	
	@Override
	public void validate(Object o,Errors errors) {
		Author author = (Author) o;
        if (author.getDateOfBirth() != null) {
            if (author.getDateOfBirth().isAfter(LocalDate.now())) {
                errors.rejectValue("dateOfBirth", "author.dateOfBirth.future", 
                                 "La data di nascita non può essere nel futuro");
            }
        }
        
        if (author.getDateOfDeath() != null) {
            if (author.getDateOfDeath().isAfter(LocalDate.now())) {
                errors.rejectValue("dateOfDeath", "author.dateOfDeath.future", 
                                 "La data di morte non può essere nel futuro");
            }
            
            if (author.getDateOfBirth() != null && 
                author.getDateOfDeath().isBefore(author.getDateOfBirth())) {
                errors.rejectValue("dateOfDeath", "author.dateOfDeath.beforeBirth", 
                                 "La data di morte non può essere precedente alla data di nascita");
            }
        }
        
        if (author.getName() != null && author.getSurname() != null && 
            author.getDateOfBirth() != null && author.getNationality() != null) {
            
            Author existingAuthor = authorService.findByNameAndSurnameAndDateOfBirthAndNationality(
                author.getName(), author.getSurname(), author.getDateOfBirth(), author.getNationality());
            
            if (existingAuthor != null && !existingAuthor.getId().equals(author.getId())) {
                errors.reject("author.duplicate", "Esiste già un autore con questi dati");
            }
        }
    }
	     
	
    @Override
    public boolean supports(Class<?> clazz) {
        return Review.class.isAssignableFrom(clazz);
    }

}
