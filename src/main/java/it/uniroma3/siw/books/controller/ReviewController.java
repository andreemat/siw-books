package it.uniroma3.siw.books.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.books.model.Book;
import it.uniroma3.siw.books.model.Review;
import it.uniroma3.siw.books.model.User;
import it.uniroma3.siw.books.service.BookService;
import it.uniroma3.siw.books.service.CredentialService;
import it.uniroma3.siw.books.service.ReviewService;
import it.uniroma3.siw.books.validator.ReviewValidator;
import jakarta.validation.Valid;

@Controller
public class ReviewController {

   

	@Autowired ReviewService reviewService;
	@Autowired BookService bookService;
	@Autowired CredentialService credentialService;

	@Autowired private ReviewValidator reviewValidator;


	
	@PostMapping("/books/{idB}/save-review")
	public String saveReview(Model model, @PathVariable("idB") Long idB,@Valid @ModelAttribute("review") Review review,BindingResult bindingResult,@AuthenticationPrincipal UserDetails userDetails) {
		 Book book = this.bookService.findById(idB);
		    User user = this.credentialService.getCredential(userDetails.getUsername()).getUser();


		    review.setUser(user);
		    review.setBook(book);
		    review.setDate(LocalDate.now());

		    this.reviewValidator.validate(review, bindingResult);

		    if (bindingResult.hasErrors()) {
		        model.addAttribute("book", book);
		     
		        return "book"; // il nome del template senza .html
		    }

		    this.reviewService.createNewReview(review, user, book);
		return "redirect:/books/{idB}";
	}
	
	@GetMapping("/admin/books/{idB}/manage-reviews")
	public String manageReviews(Model model, @PathVariable("idB") Long idB) {
		model.addAttribute("book",this.bookService.findById(idB));
		return "admin/manageBookReview.html";
		
	}
	
	
	@PostMapping("/admin/books/{idB}/reviews/{idR}/delete")
	public String removeReview(Model model, @PathVariable("idB") Long idB,@PathVariable("idR") Long idR,@AuthenticationPrincipal UserDetails userDetails) {
		this.reviewService.deleteReviewById(idR);
		model.addAttribute("book",this.bookService.findById(idB));
		return "redirect:/admin/books/{idB}";
	}
	
	
}
