package com.ogzcm.dto;

import java.util.ArrayList;
import java.util.List;

public class AllBooksResponse extends SimpleResponse {

	private List<Object> books;

	public AllBooksResponse() {
		super();
		this.books = new ArrayList<>();
	}

	public List<Object> getBooks() {
		return books;
	}

	public void setBooks(List<Object> books) {
		this.books = books;
	}
	
	
}
