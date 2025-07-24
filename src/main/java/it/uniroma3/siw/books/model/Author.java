package it.uniroma3.siw.books.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
public class Author {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@NotBlank
	private String name;
	@NotBlank
	private String surname;
	
	
	@NotNull @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfDeath;
	@NotNull
	@ManyToOne
	private Nationality nationality; 
	
	@OneToOne(cascade = CascadeType.ALL)  
	private Image foto;

	@ManyToMany(mappedBy = "authors")
	private List<Book> books;
	/**
	 * @return the foto
	 */
	public Image getFoto() {
		return foto;
	}

	/**
	 * @param foto the foto to set
	 */
	public void setFoto(Image foto) {
		this.foto = foto;
	}



	public Author() {
		this.books= new LinkedList<>();

	}

	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(Nationality nationality) {
		this.nationality = nationality;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public LocalDate getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(LocalDate dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}



	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public void addBooks(Book book) {
		if (!this.books.contains(book)) {
	        this.books.add(book);
	        book.getAuthors().add(this); 
	    }
	}



	@Override
	public int hashCode() {
		return Objects.hash(dateOfBirth, name, nationality, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		return Objects.equals(dateOfBirth, other.dateOfBirth) && Objects.equals(name, other.name)
				&& Objects.equals(nationality, other.nationality) && Objects.equals(surname, other.surname);
	}

	/**
	 * @return the nationality
	 */
	public Nationality getNationality() {
		return nationality;
	}
	
	
	
	
}
