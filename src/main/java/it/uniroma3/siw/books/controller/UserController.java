package it.uniroma3.siw.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.books.model.User;
import it.uniroma3.siw.books.service.CredentialService;
@Controller
public class UserController {
	@Autowired private CredentialService credentialService;

	@GetMapping("/profile")
	public String profile(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User user = credentialService.getCredential(userDetails.getUsername()).getUser();
		 model.addAttribute("user",user);
		return "registrationSuccessful";
	}
}
