package it.uniroma3.siw.books.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.books.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
