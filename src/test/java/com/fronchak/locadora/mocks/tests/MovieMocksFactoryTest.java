package com.fronchak.locadora.mocks.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.locadora.dtos.movie.MovieOutputAllDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputDTO;
import com.fronchak.locadora.entities.Movie;
import com.fronchak.locadora.mocks.MovieMocksFactory;
import com.fronchak.locadora.util.CustomizeAsserts;

@ExtendWith(SpringExtension.class)
public class MovieMocksFactoryTest {

	@Test
	public void mockMovieEntityShouldSetCorrectValuesWhenNoNumberWasPassed() {
		Movie result = MovieMocksFactory.mockMovieEntity();
		assertEquals(10L, result.getId());
		assertEquals("Mock movie title 0", result.getTitle());
		assertEquals("Mock movie synopsis 0", result.getSynopsis());
		assertEquals(100, result.getDurationInMinutes());
		assertEquals(1.0, result.getNote());
	}
	
	@Test
	public void mockMovieEntityShouldSetCorrectValuesWhenNumberWasPassed() {
		Movie result = MovieMocksFactory.mockMovieEntity(1);
		assertEquals(11L, result.getId());
		assertEquals("Mock movie title 1", result.getTitle());
		assertEquals("Mock movie synopsis 1", result.getSynopsis());
		assertEquals(101, result.getDurationInMinutes());
		assertEquals(2.0, result.getNote());
	}
	
	@Test
	public void mockMovieEntityPageShouldMockCorrectyPage() {
		Page<Movie> resultPage = MovieMocksFactory.mockMovieEntityPage();
		List<Movie> resultList = resultPage.getContent();
		
		Movie result = resultList.get(0);
		assertEquals(10L, result.getId());
		assertEquals("Mock movie title 0", result.getTitle());
		assertEquals("Mock movie synopsis 0", result.getSynopsis());
		assertEquals(100, result.getDurationInMinutes());
		assertEquals(1.0, result.getNote());
		
		result = resultList.get(1);
		assertEquals(11L, result.getId());
		assertEquals("Mock movie title 1", result.getTitle());
		assertEquals("Mock movie synopsis 1", result.getSynopsis());
		assertEquals(101, result.getDurationInMinutes());
		assertEquals(2.0, result.getNote());
	}
	
	@Test
	public void mockMovieOutputAllDTOPageShouldMockCorrectlyPage() {
		Page<MovieOutputAllDTO> resultPage = MovieMocksFactory.mockMovieOutputAllDTOPage();
		CustomizeAsserts.assertMovieOutputAllDTOPage(resultPage);
	}
	
	@Test
	public void mockMovieOutputDTOShouldMockValuesCorrectly() {
		MovieOutputDTO result = MovieMocksFactory.mockMovieOutputDTO();
		CustomizeAsserts.assertMovieOutputDTO(result);
	}
}
