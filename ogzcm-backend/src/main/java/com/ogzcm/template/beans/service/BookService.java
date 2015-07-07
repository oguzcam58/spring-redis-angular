package com.ogzcm.template.beans.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.ogzcm.template.AbstractRedisTemplate;
import com.ogzcm.template.beans.Book;

@Component
public class BookService extends AbstractRedisTemplate {
	@Autowired
	private Book book;
	private final String tableName = "Book";
	
	public BookService() {
		super();
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	@Override
	public void add(Object object) throws ClassCastException {
		book = (Book) object;
		book.setId(getNextId());
		getRedisTemplate().opsForHash().put(tableName, String.valueOf(book.getId()), book);
	}

	/**
	 * Increment BookIdSequence by 1, return new sequence
	 * 
	 * @return
	 */
	private long getNextId() {
		RedisTemplate<String, Object> template = getRedisTemplate();
		
		String key = tableName + "Id";
		String keyObject = (String) template.opsForValue().get(key);
		long sequence = keyObject == null ? 0l : Long.parseLong(keyObject);
		sequence++;
		template.opsForValue().set(key, sequence);
		
		return sequence;
	}

	@Override
	public void update(Object object) throws ClassCastException {
		book = (Book) object;
		getRedisTemplate().opsForHash().put(tableName, String.valueOf(book.getId()), book);
	}

	@Override
	public void delete(long id) {
		getRedisTemplate().opsForHash().delete(tableName, String.valueOf(id));
	}

	public List<Object> getAll() {
		List<Object> values = null;
		Long size = getRedisTemplate().opsForHash().size(tableName);
		if(size > 0){
			values = getRedisTemplate().opsForHash().values(tableName);
		}
		return values;
	}
	
	// TODO Delete before going live
	public void deleteAll() {
		Set<Object> keys = null;
		Long size = getRedisTemplate().opsForHash().size(tableName);
		if(size > 0){
			keys = getRedisTemplate().opsForHash().keys(tableName);
			System.out.println(keys);
		}
		
		getRedisTemplate().delete(tableName);
	}
	
}
