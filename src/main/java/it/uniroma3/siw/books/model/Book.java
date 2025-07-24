package it.uniroma3.siw.books.model;

import java.time.Year;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @NotBlank
    private String title;
    
    @NotNull
    @Min(value = 1000)
    @Max(value = 2025)
    private Integer publicationYear;

    @NotNull
    @ManyToOne
    private Genere genere;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Image copertina;
    
    @NotBlank
    @Pattern(regexp = "\\d{13}", message = "{isbn.invalid}")
    private String ISBN;
    
    @Size(max = 1200)
    private String descrizione;


    @OneToMany(cascade = CascadeType.ALL)
 
    private List<Image> images = new LinkedList<>();

  
    @ManyToMany

    private List<Author> authors = new LinkedList<>();

    @OneToMany(mappedBy="book", cascade = CascadeType.ALL)
 
    private List<Review> reviews = new LinkedList<>();

    // Constructors
    public Book() {}

    // Getters and Setters
    

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	public Genere getGenere() {
		return genere;
	}

	public void setGenere(Genere genere) {
		this.genere = genere;
	}

	public Image getCopertina() {
		return copertina;
	}

	public void setCopertina(Image copertina) {
		this.copertina = copertina;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;

	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

	public void removeAuthor(Author author) {
	    if (this.authors.remove(author)) {
	        author.getBooks().remove(this);
	    }
		
	}
}