package it.uniroma3.siw.books.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.books.model.Genere;

public interface GenereRepository extends CrudRepository<Genere, Long> {

	boolean existsByNomeIgnoreCase(String lowerCase);
	
	
	
	@Query("SELECT g FROM Genere g JOIN g.libri b GROUP BY g ORDER BY COUNT(b) DESC" )
	List<Genere> mostPopular(Pageable pageable);



}
