package com.ogzcm.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.ogzcm.controller.Application;
import com.ogzcm.template.beans.Book;
import com.ogzcm.template.beans.service.BookService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
public class IntegrationTest {

	@Autowired
	private BookService bookService;
	private final Gson gson = new Gson(); 
	
	@Before
	public void setUp() throws Exception {
		bookService.deleteAll();
	}
	
	@Test
	public void testCreateBook() throws Exception {
		assertNull(bookService.getAll());
		Book bookOne = new Book(1, "Mocking Jay", "Suzanne Collins");
		Book bookTwo = new Book(2, "Catching Fire", "Suzanne Collins");
		
		bookService.add(bookOne);
		bookService.add(bookTwo);
		
		assertEquals(2, bookService.getAll().size());
	}
	
	@Test
	public void testUpdateBook() throws Exception {
		assertNull(bookService.getAll());
		Book book = new Book(1, "Mocking Jay", "Suzanne Collins");
		bookService.add(book);
		String json = gson.toJson(bookService.getAll().get(0));
		Book bookGotFromDb = gson.fromJson(json, Book.class);
		assertEquals("Mocking Jay", bookGotFromDb.getName());
		
		book.setName("Catching Fire");
		bookService.update(book);
		
		json = gson.toJson(bookService.getAll().get(0));
		bookGotFromDb = gson.fromJson(json, Book.class);
		assertEquals("Catching Fire", bookGotFromDb.getName());
	}
	
	@Test
	public void testDeleteBook() throws Exception {
		assertNull(bookService.getAll());
		Book bookOne = new Book(1, "Mocking Jay", "Suzanne Collins");
		Book bookTwo = new Book(2, "Catching Fire", "Suzanne Collins");
		bookService.add(bookOne);
		bookService.add(bookTwo);
		assertEquals(2, bookService.getAll().size());
		
		bookService.delete(bookTwo.getId());
		
		assertEquals(1, bookService.getAll().size());
	}
}

