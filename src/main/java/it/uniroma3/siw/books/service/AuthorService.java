package it.uniroma3.siw.books.service;



import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.books.model.Author;
import it.uniroma3.siw.books.model.Nationality;
import it.uniroma3.siw.books.repository.AuthorRepository;
import jakarta.validation.Valid;

@Service
public class AuthorService {
	@Autowired AuthorRepository authorRepository;

	public void save(@Valid Author author) {
		this.authorRepository.save(author);

	}

	public Author findByid(Long id) {

		return this.authorRepository.findById(id).orElse(null);
	}

	public List<Author> findAll() {
		return (List<Author>) this.authorRepository.findAll();


	}

	public void deleteAuthor(Author author) {
		this.authorRepository.delete(author);
	}
	public List<Author>searchAuthorBySurname(String surname){
		return this.authorRepository.findBySurnameStartingWithIgnoreCase(surname);
	}
	
	public List<Author> mostReviewedAuthors(){
		return this.authorRepository.findAutoriConPiuRecensioni(PageRequest.of(0, 10));
	}

	public List<Author> findAllById(List<Long> authorIds) {
		
		return (List<Author>) this.authorRepository.findAllById(authorIds);
	}

	public Author findByNameAndSurnameAndDateOfBirthAndNationality(String name, String surname, LocalDate dateOfBirth,
			Nationality nationality) {
		
		return authorRepository.findByNameAndSurnameAndDateOfBirthAndNationality( name,  surname,  dateOfBirth,
				 nationality);
	}



	public  Page<Author> findAllByNameStartingWithIgnoreCaseOrSurnameStartingWithIgnoreCase(String autore, String autore2,Pageable pageable) {
		return this.authorRepository.findAllByNameStartingWithIgnoreCaseOrSurnameStartingWithIgnoreCase(autore,autore2,pageable);
	}

	public Page<Author> findAll(Pageable pageable) {
		
		return this.authorRepository.findAll(pageable);
	}

	
}
