package it.uniroma3.siw.books.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.books.model.Author;
import it.uniroma3.siw.books.model.Book;
import it.uniroma3.siw.books.model.Nationality;


public interface AuthorRepository extends JpaRepository<Author, Long> {
	List<Author> findBySurnameStartingWithIgnoreCase(String AuthorSearchFilter);

	@Query("SELECT a, COUNT(r) as totaleRecensioni " +
		       "FROM Author a " +
		       "JOIN a.books l " +
		       "JOIN l.reviews r " +
		       "GROUP BY a " +
		       "ORDER BY COUNT(r) DESC")
		List<Author> findAutoriConPiuRecensioni(Pageable pageable);

	Author findByNameAndSurnameAndDateOfBirthAndNationality(String name, String surname, LocalDate dateOfBirth,
			Nationality nationality);


	List<Author> findAllByNameStartingWithIgnoreCaseOrSurnameStartingWithIgnoreCase(String autore, String autore2);
	
	


	Page<Author> findAllByNameStartingWithIgnoreCaseOrSurnameStartingWithIgnoreCase(String autore, String autore2,Pageable pageable);
}
