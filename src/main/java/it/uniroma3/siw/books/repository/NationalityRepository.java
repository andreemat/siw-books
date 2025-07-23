package it.uniroma3.siw.books.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.books.model.Nationality;

public interface NationalityRepository extends CrudRepository<Nationality, Long> {

}
