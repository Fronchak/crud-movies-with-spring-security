package com.fronchak.locadora.mappers;

import org.junit.jupiter.api.BeforeEach;
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
public class MovieMapperTest {

	private MovieMapper mapper;
	
	@BeforeEach
	public void setUp() {
		mapper = new MovieMapper();
	}
	
	@Test
	public void convertEntityPageToOutputAllDTOPageShouldConvertCorrectly() {
		Page<Movie> page = MovieMocksFactory.mockMovieEntityPage();
		Page<MovieOutputAllDTO> resultPage = mapper.convertEntityToOutputAllDTO(page);
		CustomizeAsserts.assertMovieOutputAllDTOPage(resultPage);
	}
	
	@Test
	public void convertEntityToOutputDTOShouldConvertCorrectly() {
		Movie entity = MovieMocksFactory.mockMovieEntity();
		MovieOutputDTO result = mapper.convertEntityToOutputDTO(entity);
		CustomizeAsserts.assertMovieOutputDTO(result);
	}
}
