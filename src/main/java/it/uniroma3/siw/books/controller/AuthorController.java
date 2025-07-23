package it.uniroma3.siw.books.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.books.model.Author;
import it.uniroma3.siw.books.model.Book;
import it.uniroma3.siw.books.model.Image;
import it.uniroma3.siw.books.model.Nationality;
import it.uniroma3.siw.books.service.AuthorService;
import it.uniroma3.siw.books.service.BookService;
import it.uniroma3.siw.books.service.ImageService;
import it.uniroma3.siw.books.service.NationalityService;
import it.uniroma3.siw.books.validator.AuthorValidator;
import jakarta.validation.Valid;

@Controller
public class AuthorController {

   @Autowired private BookService bookService;

	@Autowired private AuthorService authorService;
	@Autowired private ImageService imageService;
	@Autowired private NationalityService nationalityService;

	@Autowired private AuthorValidator authorValidator;

 
	


	@GetMapping("/author/{id}")
	public String author(@PathVariable("id")Long id,Model model) {
		model.addAttribute("author", authorService.findByid(id));
		return "author.html";

	}



	@GetMapping("authors")
	public String authors(Model model,@RequestParam(value = "autore", required = false)String autore,@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
		Pageable pageable = PageRequest.of(page, size);
        Page<Author> authorPage;
        
        if (autore != null && !autore.isBlank()) {
            authorPage = this.authorService.findAllByNameStartingWithIgnoreCaseOrSurnameStartingWithIgnoreCase(
                autore, autore, pageable);
        } else {
            authorPage = this.authorService.findAll(pageable);
        }
        
        model.addAttribute("authorPage", authorPage);
        model.addAttribute("autore", autore);
        model.addAttribute("currentPage", page);
        
        return "authors.html";
	}
	
	@GetMapping("/admin/authors/new")
	public String formNewAthor(Model model) {
		model.addAttribute("author",new Author());
		model.addAttribute("allNationalities",this.nationalityService.findAll());
		return "admin/formNewAuthor.html";
	}
	
	@PostMapping("/admin/authors/save")
	public String newAuthor(@Valid @ModelAttribute("author") Author author,@Valid @RequestParam("image") MultipartFile image,BindingResult bindingResult,Model model) throws IOException {
		this.authorValidator.validate(author,bindingResult);
		if(!bindingResult.hasErrors()) {
			if(image!=null) {
				Image image2=new Image();
				image2.setDati(image.getBytes());
				image2.setNomeFile(image.getOriginalFilename());
			author.setFoto(image2);}
			this.authorService.save(author);
			return "redirect:/admin/authors/"+author.getId();
		} else {
			model.addAttribute("allNationalities",this.nationalityService.findAll());
			model.addAttribute("image", new Image());

			return "admin/formNewAuthor.html";
		}
	}
	
	
	
	
	
	
	@GetMapping("/admin/authors/{id}")
	public String showAdminAuthor(@PathVariable("id") Long id, Model model) {
	    
	    Author author = this.authorService.findByid(id);

	    model.addAttribute("author",author);
	    model.addAttribute("books",author.getBooks());
	    return "admin/authorAdmin.html"; 
	}



	
	
	@PostMapping("/admin/authors/{id}/delete")
	public String removeAuthor(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,Model model) {
		Author author = this.authorService.findByid(id);
		if(!author.getBooks().isEmpty()) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Impossibile eliminare l'autore: ha ancora libri associati.");
			return "redirect:/admin/authors";
		}
		this.authorService.deleteAuthor(author);
		return "redirect:/admin/authors";
	}

	
	@GetMapping("/admin/authors/{id}/update-details")
	public String showEditAuthorForm(@PathVariable("id") Long id, Model model) {
	    
	    Author existingAuthor = (Author)this.authorService.findByid(id);

	    model.addAttribute("author",existingAuthor);

	    List<Nationality> nationalities = this.nationalityService.findAll();
	    model.addAttribute("nationalities", nationalities);
	    return "admin/updateAuthorDetail.html"; 
	}
	
	
	 @PostMapping("/admin/authors/{id}/edit")
	    public String updateAuthor(@PathVariable("id") Long id,
	                             @Valid @ModelAttribute("author") Author authorFromForm,  BindingResult bindingResult, @RequestParam("image") MultipartFile image,
	                            
	                             Model model
	                             ) throws IOException {
		 Author authorToUpdate = this.authorService.findByid(id);
		 	this.authorValidator.validate(authorFromForm,bindingResult);
	        if (bindingResult.hasErrors()) {
	        	 List<Nationality> nationalities = this.nationalityService.findAll();
	     	    model.addAttribute("nationalities", nationalities);
	            return "admin/updateAuthorDetail.html";
	        }

	        
	        
	        
	        if (authorToUpdate != null) {
	        	authorToUpdate.setName(authorFromForm.getName());
	        	authorToUpdate.setSurname(authorFromForm.getSurname());
	        	authorToUpdate.setNationality(authorFromForm.getNationality());
	        	authorToUpdate.setDateOfBirth(authorFromForm.getDateOfBirth());
	        	authorToUpdate.setDateOfDeath(authorFromForm.getDateOfDeath());
	        	if(!image.isEmpty()) {
	        		if (authorToUpdate.getFoto() != null) {
	                    imageService.deleteImage(authorToUpdate.getFoto().getId());
	                }
	                
	                Image newImage = new Image();
	                newImage.setDati(image.getBytes());
	                newImage.setNomeFile(image.getOriginalFilename());
	                authorToUpdate.setFoto(newImage);
	        	
	        	
	        	}
	            this.authorService.save(authorToUpdate);
	   
	        }
	        return "redirect:/admin/authors/"+id;
	    }
	 
	
	@GetMapping("/admin/authors")
	public String booksAdmin(@RequestParam(name = "filterSearchAuthor", required = false) String filterSearchAuthor,Model model) {
		if(filterSearchAuthor!=null) {
			model.addAttribute("authors",authorService.searchAuthorBySurname(filterSearchAuthor));
			return "admin/authorsAdmin.html";
		}
		model.addAttribute("authors",authorService.findAll());
		return "admin/authorsAdmin.html";
	}
	

}
