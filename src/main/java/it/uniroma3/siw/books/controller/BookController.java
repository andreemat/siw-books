package it.uniroma3.siw.books.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import it.uniroma3.siw.books.model.Author;
import it.uniroma3.siw.books.model.Book;
import it.uniroma3.siw.books.model.Genere;
import it.uniroma3.siw.books.model.Image;
import it.uniroma3.siw.books.model.Review;
import it.uniroma3.siw.books.repository.BookRepository;
import it.uniroma3.siw.books.service.*;
import it.uniroma3.siw.books.validator.BookValidator;

import jakarta.validation.Valid;
import it.uniroma3.siw.books.model.User;

@Controller
public class BookController {

   @Autowired private  ReviewService reviewService;

   

    @Autowired private AuthorService authorService;


	@Autowired private BookService bookService;


	@Autowired private ImageService imageService;


	@Autowired private GenereService genereService;



	@Autowired private BookValidator bookValidator;



	@Autowired private CredentialService credentialService;

    @GetMapping("/books")
    public String books(Model model,
                        @RequestParam(required = false) String title,
                        @RequestParam(required = false) Long genre,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage;

        if (title != null && !title.isBlank() && genre != null) {
 
            bookPage = bookService.findByGenreIdAndTitle(genre, title, pageable);
        } else if (title != null && !title.isBlank()) {

            bookPage = bookService.cercaLibro(title, pageable);
        } else if (genre != null) {

            bookPage = bookService.findByGenreId(genre, pageable);
        } else {

            bookPage = bookService.getAllBooks(pageable);
        }


        model.addAttribute("bookPage", bookPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("title", title);
        model.addAttribute("genreId", genre);
        model.addAttribute("allGenres", genereService.findAll());
        
        return "books";
    }
    
    
    
    
    
    
    
    
    

	@GetMapping("/books/{id}")
	public String viewBook(@PathVariable("id") Long id,@AuthenticationPrincipal UserDetails userDetails, Model model) {


		Book book =this.bookService.findById(id);
		List<Review> reviews=book.getReviews();
		if (userDetails != null) {
	        User user = credentialService.getCredential(userDetails.getUsername()).getUser();
			Review userReview=this.reviewService.findByUserAndBook(user, book);
			if (userReview != null) {
			    reviews.remove(userReview);
			    model.addAttribute("userReview", userReview);
			}
	    }
		model.addAttribute("book",book );
		model.addAttribute("review",new Review());
		model.addAttribute("authors",book.getAuthors());
		
		model.addAttribute("reviews",reviews);
		Double media=this.reviewService.getAverageVote(id);
		if(media!=null)
			model.addAttribute("avgVote",String.format("%.2f",media));
		else model.addAttribute("avgVote","0.00");
		List<Image> galleryImages = book.getImages()
			    .stream()
			    .filter(img -> !img.equals(book.getCopertina()))
			    .collect(Collectors.toList());
			model.addAttribute("galleryImages", galleryImages);
		return "book.html";
	}

	@GetMapping("/admin/books/new")
	public String addbook(Model model) {
	    model.addAttribute("book", new Book()); 
	    model.addAttribute("genres",this.genereService.findAll());
	    model.addAttribute("authors",this.authorService.findAll());
	    return "admin/FormNewBook.html";
	}
	
	@PostMapping("/admin/books/save")
	public String saveNewBook(@Valid @ModelAttribute("book") Book book,BindingResult bindingResult,
	                          @RequestParam(name = "authorIds", required = false) List<Long> authorIds,
	                          
	                          @RequestParam("copertinaFile") MultipartFile copertinaFile,
	                          Model model) throws IOException {
		
	    this.bookValidator.validate(book, bindingResult);
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("genres", this.genereService.findAll());
	        model.addAttribute("authors", this.authorService.findAll());
	        return "admin/FormNewBook.html";
	    }

	    
	    if (authorIds != null && !authorIds.isEmpty()) {
	        List<Author> selectedAuthors = this.authorService.findAllById(authorIds);
	        book.setAuthors(selectedAuthors); 
	    }

	    if (copertinaFile != null && !copertinaFile.isEmpty()) {
	     
	        Image image = new Image();
	        image.setNomeFile(copertinaFile.getOriginalFilename());
	        image.setDati(copertinaFile.getBytes());
	        book.setCopertina(image);
	    }

	    this.bookService.save(book);

	    return "redirect:/admin/books/" + book.getId();
	}
	
	@GetMapping("/admin/books/{id}")
	public String showEditBookForm(@PathVariable("id") Long id, Model model) {
		List<Author> otherAuthors = new ArrayList<>(authorService.findAll());
	    Book book = (Book)this.bookService.findById(id); 
	    List<Author> bookAuthors = book.getAuthors();
	    model.addAttribute("book", book); 
	    model.addAttribute("authors", book.getAuthors());
	    model.addAttribute("avgVote", this.reviewService.getAverageVote(id)); 
	    model.addAttribute("bookAuthors",bookAuthors);	 
	    List<Image> galleryImages = book.getImages()
			    .stream()
			    .filter(img -> !img.equals(book.getCopertina()))
			    .collect(Collectors.toList());
			model.addAttribute("galleryImages", galleryImages);
	    return "admin/bookAdmin.html"; 
	}
	
	
	@GetMapping("/admin/books/{id}/update-actors")
	public String updateActors(Model model, 
	                           @PathVariable("id") Long id, 
	                           @RequestParam(name = "filterSearchAuthor", required = false) String filterSearchAuthor) {

	    Book book = this.bookService.findById(id);
	    List<Author> bookAuthors = book.getAuthors();

	    List<Author> allAuthors;

	    if (filterSearchAuthor != null && !filterSearchAuthor.isBlank()) {
	        allAuthors = this.authorService.searchAuthorBySurname(filterSearchAuthor);
	    } else {
	        allAuthors = this.authorService.findAll();
	    }
	    allAuthors.removeAll(bookAuthors);

	    model.addAttribute("book", book);
	    model.addAttribute("bookAuthors", bookAuthors);
	    model.addAttribute("otherAuthors", allAuthors);

	    return "admin/updateBooksAuthor.html";
	}
	
	
	@GetMapping("/admin/books/{id}/update-details")
	public String editBookDetails(@PathVariable("id") Long id, Model model) {
		List<Author> otherAuthors = new ArrayList<>(authorService.findAll());
	    Book book = (Book)this.bookService.findById(id); 
	    model.addAttribute("book", book); 	   
	    model.addAttribute("genres", this.genereService.findAll()); 	   
	    return "admin/updateBookDetails.html"; 
	}

	 @PostMapping("/admin/books/{id}/edit")
	    public String updateBook(@PathVariable("id") Long id,
	                             @Valid @ModelAttribute("book") Book bookFromForm,
	                             BindingResult bindingResult,
	                             Model model
	                             ) {
		 
			this.bookValidator.validate(bookFromForm,bindingResult);

	        if (bindingResult.hasErrors()) {
	            model.addAttribute("genres", this.genereService.findAll()); 
	            return "admin/updateBookDetails.html";
	        }

	        
	        Book bookToUpdate = this.bookService.findById(id);
	        
	        if (bookToUpdate != null) {
	            bookToUpdate.setTitle(bookFromForm.getTitle());
	            bookToUpdate.setPublicationYear(bookFromForm.getPublicationYear());
	            bookToUpdate.setGenere(bookFromForm.getGenere());
	            bookToUpdate.setDescrizione(bookFromForm.getDescrizione());
	            this.bookService.save(bookToUpdate);

	           
	        }

	        
	        return "redirect:/admin/books/"+id;
	    }
	 

	 
	 
	
	@PostMapping("/admin/books/{id}/delete")
	public String removeBook(@PathVariable("id") Long id,Model model) {
		this.bookService.deleteBookById(id);
		return "redirect:/admin/books";
	}


	
	@GetMapping("/admin/books")
	public String booksAdmin(@RequestParam(name = "filtroCercaLibro", required = false) String filtroCercaLibro,Model model) {
		if(filtroCercaLibro!=null) {
			model.addAttribute("books",this.bookService.cercaLibroContenente(filtroCercaLibro));
			return "admin/booksAdmin.html";
		}
		model.addAttribute("books",bookService.getAllBooksAdmin());
		return "admin/booksAdmin.html";
	}
	
	@GetMapping("/admin/books/{idB}/removeAuthor/{idA}")
	public String removeAuthor(@PathVariable("idB") Long idB,@PathVariable("idA")Long idA,Model model) {
		Author author = this.authorService.findByid(idA);
		Book book = (Book) this.bookService.findById(idB);
        if (author != null && book != null) {
        	author.removeBook(book);
        	book.removeAuthor(author);
        	bookService.save(book);    
        	authorService.save(author); 
	}
        return "redirect:/admin/books/"+idB+"/update-actors";
	}
	@GetMapping("/admin/books/{idB}/addAuthor/{idA}")
	public String addAuthor(@PathVariable("idB") Long idB,@PathVariable("idA")Long idA,Model model) {
		Author author = this.authorService.findByid(idA);
		Book book = (Book) this.bookService.findById(idB);
        if (author != null  && book != null) {
        	author.addBooks(book);   
            book.getAuthors().add(author);
        	bookService.save(book);    
        	authorService.save(author); 
	}
        return "redirect:/admin/books/"+idB+"/update-actors";
        
	
	
	
	}
	
	
	@Transactional
	@GetMapping("/admin/books/{id}/images")
	public String mostraFormImmagini(@PathVariable Long id, Model model) {
	    Book book = bookService.findById(id);
	    model.addAttribute("hasImages", book.getImages() != null && !book.getImages().isEmpty());
	    model.addAttribute("book", book);
	    return "admin/book-immagini";
	}
	@Transactional
	@PostMapping("/admin/books/{id}/immagini")
	public String uploadImmaginiNormali(@PathVariable Long id,
	                                   @RequestParam("files") MultipartFile[] files) throws IOException {
	    
	    Book book = bookService.findById(id);
	    
	    for (MultipartFile file : files) {
	        if (!file.isEmpty()) {
	            Image img = new Image();
	            img.setNomeFile(file.getOriginalFilename());
	            img.setDati(file.getBytes());
	            book.getImages().add(img);
	        }
	    }
	    
	    bookService.save(book);
	    
	    return "redirect:/admin/books/" + id;
	}
	@Transactional
	@PostMapping("/admin/books/{id}/copertina") 
	public String uploadCopertina(@PathVariable Long id,
	                             @RequestParam("file") MultipartFile file) throws IOException {
	    
	    Book book = bookService.findById(id);
	    
	
	     
	    	 if (!file.isEmpty()) {
	             Image oldCopertina = book.getCopertina();
	             
	             Image img = new Image();
	             img.setNomeFile(file.getOriginalFilename());
	             img.setDati(file.getBytes());
	             imageService.save(img);

	             book.setCopertina(img);
	             bookService.save(book);

	             // Elimina la vecchia copertina solo dopo aver salvato la nuova
	             if (oldCopertina != null && !oldCopertina.getId().equals(img.getId())) {
	                 imageService.deleteImage(oldCopertina.getId());
	             }
	         }
	
	    
	    return "redirect:/admin/books/" + id;
	}

	
	

	
	


	}
	

