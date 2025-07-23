package it.uniroma3.siw.books.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.books.model.Book;
import it.uniroma3.siw.books.model.Genere;
import it.uniroma3.siw.books.model.Review;


public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByTitleStartingWithIgnoreCase(String filtroCercaLibro);
	
	List<Book> findAllByOrderByIdDesc(Pageable pageable);

	Page<Book> findByTitleStartingWithIgnoreCase(String title, Pageable pageable);

	Page<Book> findByGenereId(Long genre, Pageable pageable);

	Page<Book> findByGenereIdAndTitleStartingWithIgnoreCase(Long genre, String title, Pageable pageable);

	boolean existsByGenereIdAndTitleStartingWithIgnoreCase(Long genere, String title);
	
	
 
}
