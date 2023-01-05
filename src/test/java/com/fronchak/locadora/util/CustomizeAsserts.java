package com.fronchak.locadora.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fronchak.locadora.dtos.movie.MovieOutputAllDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputDTO;

public class CustomizeAsserts {

	public static void assertMovieOutputAllDTOPage(Page<MovieOutputAllDTO> page) {
		List<MovieOutputAllDTO> resultList = page.getContent();
		
		MovieOutputAllDTO result = resultList.get(0);
		assertEquals(10L, result.getId());
		assertEquals("Mock movie title 0", result.getTitle());
		assertEquals(1.0, result.getNote());
		
		result = resultList.get(1);
		assertEquals(11L, result.getId());
		assertEquals("Mock movie title 1", result.getTitle());
		assertEquals(2.0, result.getNote());
	}
	
	public static void assertMovieOutputDTO(MovieOutputDTO result) {
		assertEquals(10L, result.getId());
		assertEquals("Mock movie title 0", result.getTitle());
		assertEquals("Mock movie synopsis 0", result.getSynopsis());
		assertEquals(100, result.getDurationInMinutes());
		assertEquals(1.0, result.getNote());
	}
}
