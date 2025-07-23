package it.uniroma3.siw.books.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.books.model.Book;
import it.uniroma3.siw.books.service.BookService;

@Component
public class BookValidator implements Validator {
	@Autowired BookService bookService;
	@Override
	public boolean supports(Class<?> clazz) {
		 return Book.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Book book = (Book) o;
		if(this.bookService.existsByGenreIdAndTitle(book.getGenere().getId(),book.getTitle())) {
			errors.reject("book.duplicate", "Esiste già un libro con questo titolo per lo stesso genere");

		}
		
	}

}
