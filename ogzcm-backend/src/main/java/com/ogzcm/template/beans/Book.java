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
		this.setId(id);
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
		return "Id = " + this.getId() + " name = " + this.getName() + " author = " + this.getAuthor(); 
	}
    

}
