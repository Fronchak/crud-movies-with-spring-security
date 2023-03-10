package com.fronchak.locadora.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.locadora.dtos.movie.MovieInsertDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputAllDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputDTO;
import com.fronchak.locadora.entities.Movie;
import com.fronchak.locadora.mocks.MovieMocksFactory;
import com.fronchak.locadora.util.CustomizeAsserts;

@ExtendWith(SpringExtension.class)
public class MovieMapperTest {

	private MovieMapper mapper;
	
	@BeforeEach
	public void setUp() {
		mapper = new MovieMapper();
	}
	
	@Test
	public void convertEntityPageToOutputAllDTOPageShouldConvertCorrectly() {
		Page<Movie> page = MovieMocksFactory.mockMovieEntityPage();
		Page<MovieOutputAllDTO> resultPage = mapper.convertEntityPageToOutputAllDTOPage(page);
		CustomizeAsserts.assertMovieOutputAllDTOPage(resultPage);
	}
	
	@Test
	public void convertEntityToOutputDTOShouldConvertCorrectly() {
		Movie entity = MovieMocksFactory.mockMovieEntity();
		MovieOutputDTO result = mapper.convertEntityToOutputDTO(entity);
		CustomizeAsserts.assertMovieOutputDTO(result);
	}
	
	@Test
	public void copyDTOToEntityShouldCopyValuesCorrectlyWhenUsingInsertDTO() {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		Movie result = new Movie();
		mapper.copyDTOToEntity(dto, result);
		assertNull(result.getId());
		assertEquals("Mock movie title 0", result.getTitle());
		assertEquals("Mock movie synopsis 0", result.getSynopsis());
		assertEquals(100, result.getDurationInMinutes());
		assertEquals(1.0, result.getNote());
	}
	
	@Test
	public void copyDTOToEntityShouldCopyValuesCorrectlyWhenUsingUpdateDTO() {
		MovieInsertDTO dto = MovieMocksFactory.mockMovieInsertDTO();
		Movie result = new Movie();
		result.setId(100L);
		mapper.copyDTOToEntity(dto, result);
		assertEquals(100L, result.getId());
		assertEquals("Mock movie title 0", result.getTitle());
		assertEquals("Mock movie synopsis 0", result.getSynopsis());
		assertEquals(100, result.getDurationInMinutes());
		assertEquals(1.0, result.getNote());
	}
}
