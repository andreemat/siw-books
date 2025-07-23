package it.uniroma3.siw.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.books.model.Book;
import it.uniroma3.siw.books.model.Image;
import it.uniroma3.siw.books.service.BookService;
import it.uniroma3.siw.books.service.ImageService;

@Controller
public class ImageController {
	
	
	@Autowired private ImageService imageService;
	@Autowired private BookService bookService;

	@GetMapping("/images/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
	    Image image = imageService.findById(id);
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaTypeFactory.getMediaType(image.getNomeFile()).orElse(MediaType.APPLICATION_OCTET_STREAM));
	    
	    return new ResponseEntity<>(image.getDati(), headers, HttpStatus.OK);
	}
	
	
	@GetMapping("/admin/books/{idB}/images/{idI}/delete")
	public String deleteImage(Model model,@PathVariable("idB") Long idLibro, @PathVariable("idI") Long idImage) {
		Book book = bookService.findById(idLibro);
		Image image = imageService.findById(idImage);
		 
		book.getImages().remove(image);
	       
	        bookService.save(book);
		this.imageService.deleteImage(idImage);
		
		return "redirect:/admin/books/"+idLibro +"/images";
		
		
		
		
	}

}
