package com.fronchak.locadora.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fronchak.locadora.dtos.movie.MovieInsertDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputAllDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputDTO;
import com.fronchak.locadora.dtos.movie.MovieUpdateDTO;
import com.fronchak.locadora.entities.Movie;
import com.fronchak.locadora.exceptions.ResourceNotFoundException;
import com.fronchak.locadora.mappers.MovieMapper;
import com.fronchak.locadora.repositories.MovieRepository;

@Service
public class MovieService {

	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private MovieMapper mapper;

	@Transactional(readOnly = true)
	public MovieOutputDTO findById(Long id) {
		Movie entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Movie", id.toString()));
		return mapper.convertEntityToOutputDTO(entity);
	}
	
	@Transactional(readOnly = true)
	public Page<MovieOutputAllDTO> findAllPaged(Pageable pageable) {
		Page<Movie> page = repository.findAll(pageable);
		return mapper.convertEntityPageToOutputAllDTOPage(page);
	}
	
	@Transactional
	public MovieOutputDTO save(MovieInsertDTO dto) {
		Movie entity = new Movie();
		mapper.copyDTOToEntity(dto, entity);
		entity = repository.save(entity);
		return mapper.convertEntityToOutputDTO(entity);
	}
	
	@Transactional
	public MovieOutputDTO update(MovieUpdateDTO dto, Long id) {
		try {
			Movie entity = repository.getReferenceById(id);
			mapper.copyDTOToEntity(dto, entity);
			entity = repository.save(entity);
			return mapper.convertEntityToOutputDTO(entity);			
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Movie", id.toString());
		}

	}
}
