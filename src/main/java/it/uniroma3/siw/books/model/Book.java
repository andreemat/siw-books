package it.uniroma3.siw.books.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Image copertina;
    @NotBlank
    @Pattern(regexp = "\\d{13}", message = "{isbn.invalid}")
    private String ISBN;
    @Size(max = 1200)
    private String descrizione;
    
   @OneToMany(cascade = CascadeType.ALL)
   private List<Image> images;
   
	@ManyToMany
	private List<Author> authors;


	@OneToMany(mappedBy="book", cascade = CascadeType.ALL)
	private List<Review> reviews;
    
	/**
 * @return the images
 */
public List<Image> getImages() {
	return images;
}

/**
 * @param images the images to set
 */
public void setImages(List<Image> images) {
	this.images = images;
}



	public Book() {
		this.authors= new LinkedList<>();
		this.images=new LinkedList<>();
	}

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

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	/**
	 * @return the iSBN
	 */
	public String getISBN() {
		return ISBN;
	}

	/**
	 * @param iSBN the iSBN to set
	 */
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	/**
	 * @return the copertina
	 */
	public Image getCopertina() {
		return copertina;
	}

	/**
	 * @param copertina the copertina to set
	 */
	public void setCopertina(Image copertina) {
		this.copertina = copertina;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	@Override
	public int hashCode() {
		return Objects.hash(authors, publicationYear);
	}
	public void removeAuthors() {
	    for (Author author : this.authors) {
	        author.getBooks().remove(this);
	    }
	    this.authors.clear();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Book other = (Book) obj;
		return Objects.equals(authors, other.authors) && publicationYear == other.publicationYear;
	}

	public void addAuthor(Author author) {
		 if (!this.authors.contains(author)) {
		        this.authors.add(author);
		    }
		    if (!author.getBooks().contains(this)) {
		        author.getBooks().add(this);
		    }
	}

	public void removeAuthor(Author author) {
		this.authors.remove(author);
		
	}

	/**
	 * @return the genere
	 */
	public Genere getGenere() {
		return genere;
	}

	/**
	 * @param genere the genere to set
	 */
	public void setGenere(Genere genere) {
		this.genere = genere;
	}
	
	

	

}

