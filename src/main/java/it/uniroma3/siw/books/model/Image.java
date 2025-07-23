package it.uniroma3.siw.books.model;



import java.util.Arrays;
import java.util.Objects;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
@Entity
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    private String nomeFile;

    @Lob
    @Basic(fetch = FetchType.LAZY) 
    private byte[] dati;



 
    public Image() {
    }

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
	 * @return the nomeFile
	 */
	public String getNomeFile() {
		return nomeFile;
	}

	/**
	 * @param nomeFile the nomeFile to set
	 */
	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	

	/**
	 * @return the dati
	 */
	public byte[] getDati() {
		return dati;
	}



	/**
	 * @param dati the dati to set
	 */
	public void setDati(byte[] dati) {
		this.dati = dati;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(dati);

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Image other = (Image) obj;
		return Arrays.equals(dati, other.dati)
				&& Objects.equals(nomeFile, other.nomeFile);
	}

	


    
    
    

}
