package it.uniroma3.siw.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.books.model.Genere;
import it.uniroma3.siw.books.service.GenereService;
import it.uniroma3.siw.books.validator.GenereValidator;
import jakarta.validation.Valid;

@Controller
public class GenereController {
	@Autowired GenereService genereService;
	@Autowired private GenereValidator genereValidator;
	@GetMapping("/genres")
	public String getAllGenres(Model model) {
		model.addAttribute("genres", this.genereService.findAll() );
		return "genres.html";
	}
	
	@GetMapping("/genres/{id}")
	public String getBookGenre(Model model, @PathVariable("id") Long id) {
		Genere genere= this.genereService.findGenreById(id);
		model.addAttribute("genre", genere);
		model.addAttribute("books",genere.getLibri());
		return "genere.html";
	}
	
	@GetMapping("/admin/genres")
	public String getAllGenresAdmin(Model model) {
		model.addAttribute("genres", this.genereService.findAll() );
		Genere g = new Genere();
		model.addAttribute("genere", g);
		return "admin/genresAdmin.html";
	}
	
	
	@PostMapping("/admin/genres")
	public String addGenere(@Valid @ModelAttribute("genere") Genere genere,BindingResult bindingResult,Model model) {
	    this.genereValidator.validate(genere, bindingResult);

	    if (bindingResult.hasErrors()) {
	        model.addAttribute("genres", genereService.findAll()); 
	        model.addAttribute("genere", genere);
	        return "admin/genresAdmin"; 
	    }

	    this.genereService.save(genere);
	    return "redirect:/admin/genres";
	}
		

		

	
	@PostMapping("/admin/genere/{id}/delete")
	public String deleteGenere(@PathVariable Long id,RedirectAttributes redirectAttributes) {
		Genere genere = this.genereService.findGenreById(id);
	    
	    if (genere!=null) {
	        
	        if (genere.getLibri() != null && !genere.getLibri().isEmpty()) {
	            redirectAttributes.addFlashAttribute("errorMessage", "Impossibile eliminare il genere '" + genere.getNome() + "' perché ha libri associati.");
	            return "redirect:/admin/genres";
	        }
	        this.genereService.deleteById(id);
	    }

	    return "redirect:/admin/genres";
	}

}
