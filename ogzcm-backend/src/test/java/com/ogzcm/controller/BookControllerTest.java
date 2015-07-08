package com.ogzcm.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.ogzcm.dto.AllBooksResponse;
import com.ogzcm.dto.SimpleResponse;
import com.ogzcm.enums.ResponseType;
import com.ogzcm.template.beans.Book;
import com.ogzcm.template.beans.service.BookService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
public class BookControllerTest {
	@Mock
	private RedisTemplate<String, Object> redisTemplate; 
	@Autowired
	@InjectMocks
	private BookController bookCtrl;
	@Spy
	private BookService bookService;
	@Mock
	private HttpServletResponse resp;
	@Mock
	private HashOperations<String, Object, Object> hashOps;
	@Mock
	private CaptchaVerifier verifier;
	
	private final String key = "Book";
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		doReturn(redisTemplate).when(bookService).getRedisTemplate();
		when(redisTemplate.opsForHash()).thenReturn(hashOps);
	}

	@Test
	public void testGetAllBooksWhenSizeZero() {
		AllBooksResponse response = bookCtrl.getAllBooks(resp);
		
		InOrder order = inOrder(bookService, hashOps);
		order.verify(bookService).getAll();
		order.verify(hashOps).size(key);
		assertTrue(response.getBooks() == null);
	}
	
	@Test
	public void testGetAllBooksWhenSizeOne() {
		List<Object> list = new ArrayList<>();
		list.add(new Book(1, "Mocking Jay", "Suzanne Collins"));
		when(hashOps.size(key)).thenReturn(1l);
		when(hashOps.values(key)).thenReturn(list);
		
		AllBooksResponse response = bookCtrl.getAllBooks(resp);
		
		InOrder order = inOrder(bookService, hashOps);
		order.verify(bookService).getAll();
		order.verify(hashOps).size(key);
		order.verify(hashOps).values(key);
		assertTrue(response.getBooks().size() == 1);
		Book book = (Book) response.getBooks().get(0);
		assertEquals(book.getId(), 1l);
		assertEquals(book.getName(), "Mocking Jay");
		assertEquals(book.getAuthor(), "Suzanne Collins");
	}
	
	@Test
	public void testSaveBookCreate() {
		String bookJson = "{id:0,name:'Dummy',author:'Dummy'}";
		Book book = new Gson().fromJson(bookJson, Book.class);
		when(verifier.verifyCaptcha(null)).thenReturn(true);
		doReturn(0l).when(bookService).getNextId();
		
		SimpleResponse response = bookCtrl.saveBook(bookJson, null, resp);
		
		verify(bookService).add(book);
		assertEquals(ResponseType.SUCCESS.getText(), response.getType());
		assertEquals("Dummy has been created.", response.getMessage());
	}
	
	@Test
	public void testSaveBookUpdate() {
		String bookJson = "{id:2,name:'Dummy',author:'Dummy'}";
		Book book = new Gson().fromJson(bookJson, Book.class);
		when(verifier.verifyCaptcha(null)).thenReturn(true);
		
		SimpleResponse response = bookCtrl.saveBook(bookJson, null, resp);
		
		verify(bookService).update(book);
		assertEquals(ResponseType.SUCCESS.getText(), response.getType());
		assertEquals("Dummy has been updated.", response.getMessage());
	}
	
	@Test()
	public void testSaveBookWrongJson() {
		String bookJson = "{id:2name:'Dummy'author:'Dummy'}";
		
		SimpleResponse response = bookCtrl.saveBook(bookJson, null, resp);
		assertEquals(ResponseType.FAIL.getText(), response.getType());
		assertEquals("An unexpected error has been occurred, try again later.", response.getMessage());
	}
	
	@Test
	public void testDeleteBook() {
		bookCtrl.deleteBook(0, resp);
		
		InOrder order = inOrder(bookService, hashOps);
		order.verify(bookService).delete(0);
		order.verify(hashOps).delete(key, "0");
	}
}
