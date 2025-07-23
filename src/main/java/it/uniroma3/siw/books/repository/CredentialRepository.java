package it.uniroma3.siw.books.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.books.model.Credential;



public interface CredentialRepository extends CrudRepository<Credential, Long> {
	public Optional<Credential> findByUsername(String username);

}
