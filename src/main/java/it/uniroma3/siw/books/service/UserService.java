package it.uniroma3.siw.books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.books.model.User;
import it.uniroma3.siw.books.repository.UserRepository;
import jakarta.validation.Valid;

@Service
public class UserService {

@Autowired UserRepository userRepository;
	public void saveUser(@Valid User user) {
		this.userRepository.save(user);
	}

}
