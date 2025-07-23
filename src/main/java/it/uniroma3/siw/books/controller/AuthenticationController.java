package it.uniroma3.siw.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.books.model.Credential;
import it.uniroma3.siw.books.model.User;
import it.uniroma3.siw.books.service.AuthorService;
import it.uniroma3.siw.books.service.BookService;
import it.uniroma3.siw.books.service.CredentialService;
import it.uniroma3.siw.books.service.GenereService;
import it.uniroma3.siw.books.service.UserService;
import it.uniroma3.siw.books.validator.AuthenticationValidator;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
	@Autowired
	private CredentialService credentialService;

	@Autowired
	private UserService userService;
	@Autowired private BookService bookService;
	@Autowired private AuthorService authorService;
	@Autowired private GenereService genereService;


	@Autowired private AuthenticationValidator authenticationValidator;




	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping(value = "/register")
	public String showRegisterForm (Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credential", new Credential());
		
		return "formRegisterUser";
	}

	@PostMapping(value = { "/register" })
	public String registerUser(@Valid @ModelAttribute("user") User user,
			BindingResult userBindingResult, @RequestParam("confirmPassword") String confirmPassword,
			@Valid @ModelAttribute("credential") Credential credential,
			BindingResult credentialsBindingResult,
			Model model) {
		this.authenticationValidator.validate(credential,credentialsBindingResult);
		 if (!credential.getPassword().equals(confirmPassword)) {
		        credentialsBindingResult.rejectValue("password", "password.mismatch", "Le password non coincidono");
		    }
		// se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
		if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			userService.saveUser(user);
			credential.setUser(user);
			user.setCredential(credential);
			credentialService.saveCredential(credential);
			model.addAttribute("user", user);
			return "registrationSuccessful";
		}
		return "formRegisterUser.html";
	}

	@GetMapping("/")
	public String mostraHome(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("trendingBooks",bookService.getLibriDelMomento());
		model.addAttribute("latestBooks",bookService.getUltimiLibriAggiunti());
		model.addAttribute("authors", authorService.mostReviewedAuthors());
		model.addAttribute("genres", genereService.findTheMostPopular());
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "index";
		}else{
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credential credentials = credentialService.getCredential(userDetails.getUsername());
			if (credentials.getRole().equals(Credential.ADMIN_ROLE)) {
				User user = credentialService.getCredential(userDetails.getUsername()).getUser();
				return "admin/indexAdmin";
			}
			else {
				return "index";}
		}
		
	
}
	
	
	@GetMapping("/admin/dashboard")
	public String dashboardAdmin(Model model) {
		return "admin/dashboardAdmin.html";
	}
	
	@GetMapping("/profile")
	public String profile(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		User user = credentialService.getCredential(userDetails.getUsername()).getUser();
		 model.addAttribute("user",user);
		return "registrationSuccessful";
	}
}