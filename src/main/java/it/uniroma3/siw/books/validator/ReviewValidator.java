package it.uniroma3.siw.books.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.books.model.Review;
import it.uniroma3.siw.books.model.User;
import it.uniroma3.siw.books.service.ReviewService;

@Component
public class ReviewValidator implements Validator{

	@Autowired ReviewService reviewService;
	
	
	@Override
	public void validate(Object o,Errors errors) {
		Review review = (Review) o;
		User user= review.getUser();
	      if (user == null || review.getBook() == null) {
	            errors.reject("review.invalid");
	            return;
	        }
		if(this.reviewService.findByUserAndBook(user,review.getBook())!=null)
			errors.reject("review.duplicate");
	}
    @Override
    public boolean supports(Class<?> clazz) {
        return Review.class.isAssignableFrom(clazz);
    }
	
}
