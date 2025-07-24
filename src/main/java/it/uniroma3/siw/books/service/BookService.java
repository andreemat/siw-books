package it.uniroma3.siw.books.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.books.controller.ReviewController;
import it.uniroma3.siw.books.model.Author;
import it.uniroma3.siw.books.model.Book;
import it.uniroma3.siw.books.model.Genere;
import it.uniroma3.siw.books.model.Image;
import it.uniroma3.siw.books.repository.BookRepository;
import it.uniroma3.siw.books.repository.ReviewRepository;

import jakarta.validation.Valid;

@Service
public class BookService {


    @Autowired ReviewRepository reviewRepository;


	@Autowired BookRepository bookRepository;

	public Page<Book> getAllBooks(Pageable pageable) {
		return bookRepository.findAll(pageable);

	}

	public void save(@Valid Book book) {
		this.bookRepository.save(book);

	}
	@Transactional(readOnly = true)
	public Book findById(Long id) {

		return this.bookRepository.findById(id).orElse(null);
	}

	public void deleteBook(Book book) {
		this.bookRepository.delete(book);
		
	}

	public List<Book> cercaLibro(String filtroCercaLibro) {
		
		return (List<Book>) bookRepository.findByTitleStartingWithIgnoreCase(filtroCercaLibro);
	}
	


@Transactional 
public void deleteBookById(Long id) {
    Book book = this.bookRepository.findById(id).orElse(null);
    if (book != null) {

        for (Author author : new ArrayList<>(book.getAuthors())) {
            author.getBooks().remove(book);
        }

        this.bookRepository.delete(book);
    }
}




public List<Book> getLibriDelMomento() {
    LocalDate unaSettimanaFa = LocalDate.now().minusDays(7);
    
    return this.reviewRepository.findLibriPiuRecensiti(
        unaSettimanaFa, 3, PageRequest.of(0, 10));
}

public List<Book> getUltimiLibriAggiunti(){
	return this.bookRepository.findAllByOrderByIdDesc(PageRequest.of(0, 10));
}

public List<Book> getAllBooksAdmin() {
	return this.bookRepository.findAll();
}

public Page<Book> cercaLibro(String title, Pageable pageable) {
	return this.bookRepository.findByTitleStartingWithIgnoreCase(title,pageable);
}



public Page<Book> findByGenreId(Long genre, Pageable pageable) {
	
	return this.bookRepository.findByGenereId(genre,pageable);
}

public Page<Book> findByGenreIdAndTitle(Long genre, String title, Pageable pageable) {
	
	return this.bookRepository.findByGenereIdAndTitleStartingWithIgnoreCase(genre,title, pageable);
}

public boolean existsByGenreIdAndTitle(Long idGenere, String title ) {
	
	return this.bookRepository.existsByGenereIdAndTitleStartingWithIgnoreCase(idGenere,title);
}

public List<Book> cercaLibroContenente(String filtroCercaLibro) {
	return this.bookRepository.findByTitleContainingIgnoreCase(filtroCercaLibro);

}

public Book findByGenreIdAndTitle(Long id, String title) {
	
	return this.bookRepository.findByGenereIdAndTitle(id,title);
}





}
