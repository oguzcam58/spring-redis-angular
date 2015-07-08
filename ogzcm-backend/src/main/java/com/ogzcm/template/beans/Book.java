package com.ogzcm.template.beans;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.ogzcm.template.AbstractBeanTemplate;

@Component
public class Book extends AbstractBeanTemplate implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private String name;
    private String author;

    public Book() {
    	super();
    }

	public Book(long id, String name, String author) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
    
	@Override
	public String toString() {
		return "Id = " + this.getId() + ", name = " + this.getName() + ", author = " + this.getAuthor(); 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Book other = (Book) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
