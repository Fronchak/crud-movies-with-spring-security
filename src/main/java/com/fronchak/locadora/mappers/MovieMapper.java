package com.fronchak.locadora.mappers;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fronchak.locadora.dtos.movie.MovieInputDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputAllDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputDTO;
import com.fronchak.locadora.entities.Movie;

@Service
public class MovieMapper {

	public Page<MovieOutputAllDTO> convertEntityToOutputAllDTO(Page<Movie> page) {
		return page.map(entity -> new MovieOutputAllDTO(entity));
	}
	
	public MovieOutputDTO convertEntityToOutputDTO(Movie entity) {
		return new MovieOutputDTO(entity);
	}
	
	public void copyDTOToEntity(MovieInputDTO dto, Movie entity) {
		entity.setTitle(dto.getTitle());
		entity.setSynopsis(dto.getSynopsis());
		entity.setDurationInMinutes(dto.getDurationInMinutes());
		entity.setNote(dto.getNote());
	}
}
