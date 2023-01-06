package com.fronchak.locadora.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fronchak.locadora.dtos.movie.MovieInsertDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputAllDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputDTO;
import com.fronchak.locadora.dtos.movie.MovieUpdateDTO;
import com.fronchak.locadora.entities.Movie;
import com.fronchak.locadora.exceptions.DatabaseException;
import com.fronchak.locadora.exceptions.ResourceNotFoundException;
import com.fronchak.locadora.mappers.MovieMapper;
import com.fronchak.locadora.mocks.MovieMocksFactory;
import com.fronchak.locadora.repositories.MovieRepository;
import com.fronchak.locadora.util.CustomizeAsserts;

@ExtendWith(SpringExtension.class)
public class MovieServiceTest {

	private static final Long VALID_ID = 1L;
	private static final Long INVALID_ID = 2L;
	private static final Long DEPENDENT_ID = 3L;
	
	@Mock
	private MovieRepository repository;
	
	@Mock
	private MovieMapper mapper;
	
	@InjectMocks
	private MovieService service;
	
	@BeforeEach
	public void setUp() {
		Movie entity = MovieMocksFactory.mockMovieEntity();
		Page<Movie> entityPage = MovieMocksFactory.mockMovieEntityPage();
		MovieOutputDTO outputDTO = MovieMocksFactory.mockMovieOutputDTO();
		Page<MovieOutputAllDTO> outputAllDTOPage = MovieMocksFactory.mockMovieOutputAllDTOPage();
		
		when(repository.findById(VALID_ID)).thenReturn(Optional.of(entity));
		when(repository.findById(INVALID_ID)).thenReturn(Optional.empty());
		when(repository.findAll(any(Pageable.class))).thenReturn(entityPage);
		when(repository.save(any(Movie.class))).thenReturn(entity);
		when(repository.getReferenceById(VALID_ID)).thenReturn(entity);
		when(repository.getReferenceById(INVALID_ID)).thenThrow(EntityNotFoundException.class);
		when(mapper.convertEntityToOutputDTO(entity)).thenReturn(outputDTO);
		when(mapper.convertEntityPageToOutputAllDTOPage(entityPage)).thenReturn(outputAllDTOPage);	
	}
	
	@Test
	public void findByIdShouldReturnOutoutDTOWhenIdExists() {		
		MovieOutputDTO result = service.findById(VALID_ID);
		CustomizeAsserts.assertMovieOutputDTO(result);
		verify(repository, times(1)).findById(VALID_ID);
	}
	
	@Test
	public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(INVALID_ID));
		verify(repository, times(1)).findById(INVALID_ID);
	}
	
	@Test
	public void findAllPagedShouldReturnOutputAllDTOPage() {
		Page<MovieOutputAllDTO> resultPage = service.findAllPaged(mock(Pageable.class));
		CustomizeAsserts.assertMovieOutputAllDTOPage(resultPage);
	}
	
	@Test
	public void saveShouldReturnOutputDTOAfterSaveEntity() {
		MovieInsertDTO insertDTO = MovieMocksFactory.mockMovieInsertDTO();
		ArgumentCaptor<Movie> argumentCaptor = ArgumentCaptor.forClass(Movie.class);
		
		MovieOutputDTO result = service.save(insertDTO);

		verify(repository).save(argumentCaptor.capture());
		Movie entity = argumentCaptor.getValue();
		assertNull(entity.getId());
		
		CustomizeAsserts.assertMovieOutputDTO(result);
		verify(mapper).copyDTOToEntity(insertDTO, entity);
	}
	
	@Test
	public void updateShouldReturnOutputDTOWhenIdExists() {
		MovieUpdateDTO updateDTO = MovieMocksFactory.mockMovieUpdateDTO();
		Movie entity = MovieMocksFactory.mockMovieEntity();
		Movie entityAuxiliar = MovieMocksFactory.mockMovieEntity(1);
		MovieOutputDTO outputDTO = MovieMocksFactory.mockMovieOutputDTO(1);
		
		when(repository.getReferenceById(VALID_ID)).thenReturn(entity);
		when(repository.save(entity)).thenReturn(entityAuxiliar);
		when(mapper.convertEntityToOutputDTO(entityAuxiliar)).thenReturn(outputDTO);
		
		ArgumentCaptor<Movie> argumentCaptor = ArgumentCaptor.forClass(Movie.class);
		
		MovieOutputDTO result = service.update(updateDTO, VALID_ID);
		CustomizeAsserts.assertMovieOutputDTOAuxiliar(result);
		
		verify(repository).save(argumentCaptor.capture());
		Movie resultEntity = argumentCaptor.getValue();
		CustomizeAsserts.assertMovieEntity(resultEntity);
		verify(mapper, times(1)).copyDTOToEntity(updateDTO, resultEntity);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		assertThrows(ResourceNotFoundException.class, () -> service.update(mock(MovieUpdateDTO.class), INVALID_ID));
	}
	
	@Test
	public void deleteShouldDeleteEntityWhenIdExists() {
		assertDoesNotThrow(() -> service.delete(VALID_ID));
		verify(repository, times(1)).deleteById(VALID_ID);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(INVALID_ID);
		assertThrows(ResourceNotFoundException.class, () -> service.delete(INVALID_ID));
		verify(repository, times(1)).deleteById(INVALID_ID);
	}
	
	@Test
	public void deleteShouldReturnDatabaseExceptionWhenIdIsDependent() {
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(DEPENDENT_ID);
		assertThrows(DatabaseException.class, () -> service.delete(DEPENDENT_ID));
		verify(repository, times(1)).deleteById(DEPENDENT_ID);
	}
}
