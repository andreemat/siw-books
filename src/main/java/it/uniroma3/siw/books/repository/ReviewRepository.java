package it.uniroma3.siw.books.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.books.model.Book;
import it.uniroma3.siw.books.model.Review;
import it.uniroma3.siw.books.model.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	Review findByUserAndBook(User user, Book book);

	
	@Query("SELECT r.book " +
		       "FROM Review r " +
		       "WHERE r.date >= :dataInizio " +
		       "GROUP BY r.book " +
		       "HAVING COUNT(r) >= :minimoRecensioni " +
		       "ORDER BY COUNT(r) DESC, AVG(r.vote) DESC")
		List<Book> findLibriPiuRecensiti(@Param("dataInizio") LocalDate dataInizio,
		                                @Param("minimoRecensioni") int minimoRecensioni,
		                                Pageable pageable);


	
    @Query("SELECT AVG(r.vote) FROM Review r WHERE r.book.id = :bookId")
    Double findAvgVotoByBookId(@Param("bookId") Long bookId);
}
