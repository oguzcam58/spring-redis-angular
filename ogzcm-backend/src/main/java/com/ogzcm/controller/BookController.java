package com.ogzcm.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ogzcm.dto.AllBooksResponse;
import com.ogzcm.dto.SimpleResponse;
import com.ogzcm.enums.ResponseType;
import com.ogzcm.helpers.RestServiceHelper;
import com.ogzcm.template.beans.Book;
import com.ogzcm.template.beans.service.BookService;

@RestController
public class BookController {

	private static final Logger logger = LoggerFactory.getLogger(BookController.class);
	
	@Autowired
	private BookService bookService;
	private CaptchaVerifier verifier = new CaptchaVerifier();
	
	@RequestMapping("/getAllBooks")
	public AllBooksResponse getAllBooks(HttpServletResponse resp) {
		RestServiceHelper.setResponseHeader(resp);

		AllBooksResponse allBooksResponse = new AllBooksResponse();
		try {
			allBooksResponse.setBooks(bookService.getAll());
		} catch (Exception ex) {
			logger.error("Got exception by method getAllBooks ", ex);
			allBooksResponse.setType(ResponseType.FAIL.getText());
			allBooksResponse.setMessage("An unexpected error has been occurred, try again later.");
		}
		return allBooksResponse;
	}
	
	@RequestMapping("/saveBook")
	public SimpleResponse saveBook(@RequestParam(value="book") String bookJson, @RequestParam(value="captcha", required=false) String captchaResponse, HttpServletResponse resp) {
		RestServiceHelper.setResponseHeader(resp);

		SimpleResponse simpleResponse = new SimpleResponse();
		try {
			Gson gson = new Gson();
			Book book = gson.fromJson(bookJson, Book.class);
			
			simpleResponse = validateSaveBook(book, captchaResponse);
			if(simpleResponse.getType().equals(ResponseType.SUCCESS.getText())) {
				if(book.getId() > 0){
					bookService.update(book);
					simpleResponse.setMessage(book.getName() + " has been updated.");
				} else {
					bookService.add(book);
					simpleResponse.setMessage(book.getName() + " has been created.");
				}
			}
		} catch (Exception ex) {
			logger.error("Got exception by method saveBook ", ex);
			simpleResponse.setType(ResponseType.FAIL.getText());
			simpleResponse.setMessage("An unexpected error has been occurred, try again later.");
		}
		return simpleResponse;
	}
    
	private SimpleResponse validateSaveBook(Book book, String captchaResponse) {
		SimpleResponse simpleResponse = new SimpleResponse();
		
		// Not allowed empty string book name or author
		if(book.getName() == null || book.getName().length() < 1 ||
		   book.getAuthor() == null || book.getAuthor().length() < 1) {
			simpleResponse.setType(ResponseType.FAIL.getText());
			simpleResponse.setMessage("Book info should be provided.");
		}
		if(!verifier.verifyCaptcha(captchaResponse)){
			simpleResponse.setType(ResponseType.FAIL.getText());
			simpleResponse.setMessage("Captcha should be selected.");
		}
		return simpleResponse;
	}

	@RequestMapping("/deleteBook")
    public SimpleResponse deleteBook(@RequestParam(value="bookId") long id, HttpServletResponse resp) {
    	RestServiceHelper.setResponseHeader(resp);
    	
    	SimpleResponse simpleResponse = new SimpleResponse();
    	try {
    		bookService.delete(id);
    		simpleResponse.setMessage("It has been deleted successfully.");
    	} catch (Exception ex) {
    		logger.error("Got exception by method deleteBook ", ex);
			simpleResponse.setType(ResponseType.FAIL.getText());
			simpleResponse.setMessage("An unexpected error has been occurred, try again later.");
    	}
    	return simpleResponse;
    }
	
}