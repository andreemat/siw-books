package it.uniroma3.siw.books.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Genere {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Column(unique = true, nullable = false)
    private String nome;
    
    
    
    @OneToMany(mappedBy = "genere")
    private List<Book> libri;
    
    
    
    /**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}


	/**
	 * @return the colore
	 */


	/**
	 * @param colore the colore to set
	 */

	/**
	 * @return the libri
	 */
	public List<Book> getLibri() {
		return libri;
	}

	/**
	 * @param libri the libri to set
	 */
	public void setLibri(List<Book> libri) {
		this.libri = libri;
	}


	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Genere other = (Genere) obj;
		return Objects.equals(nome, other.nome);
	}

	public void addBook(@Valid Book book) {
		this.libri.add(book);
		
	}
}
