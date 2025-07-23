package it.uniroma3.siw.books.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.books.model.Book;
import it.uniroma3.siw.books.model.Review;
import it.uniroma3.siw.books.model.User;
import it.uniroma3.siw.books.repository.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	@Autowired ReviewRepository reviewRepository;

	public void createNewReview(Review review,User user, Book book) {
		user.getReviews().add(review);
		book.getReviews().add(review);
		this.reviewRepository.save(review);
		
	}

	public Review findById(Long id) {
		
		return this.reviewRepository.findById(id).orElse(null);
	}

	@Transactional 
    public void deleteReviewById(Long reviewId) {

        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
      
            Book book = review.getBook();
            User user = review.getUser();

                book.getReviews().remove(review);
            
            
                user.getReviews().remove(review);
            

            reviewRepository.delete(review);
        }
    }

	public Review findByUserAndBook(User user, Book book) {
		
		return this.reviewRepository.findByUserAndBook(user,book);
	}

	public Double getAverageVote(Long id) {
		
		return this.reviewRepository.findAvgVotoByBookId(id);
	}
	

}
