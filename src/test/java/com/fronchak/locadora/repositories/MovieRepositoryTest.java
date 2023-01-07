package com.fronchak.locadora.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fronchak.locadora.entities.Movie;

@DataJpaTest
public class MovieRepositoryTest {
	
	private static final String VALID_TITLE = "Harry Potter and the Prisoner of Azkaban";
	private static final String INVALID_TITLE = "Some Title";

	@Autowired
	private MovieRepository repository;
	
	@Test
	public void findByTitleShouldReturnEntityWhenTitleExists() {
		Movie result = repository.findByTitle(VALID_TITLE);
		assertNotNull(result);
		assertEquals(3, result.getId());
		assertEquals(VALID_TITLE, result.getTitle());
	}
	
	@Test
	public void findByTitleShouldReturnNullWhenTitleDoesNotExist() {
		Movie result = repository.findByTitle(INVALID_TITLE);
		assertNull(result);
	}
}
