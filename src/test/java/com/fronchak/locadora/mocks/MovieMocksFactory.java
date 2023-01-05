package com.fronchak.locadora.mocks;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.fronchak.locadora.dtos.movie.MovieInputDTO;
import com.fronchak.locadora.dtos.movie.MovieInsertDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputAllDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputDTO;
import com.fronchak.locadora.dtos.movie.MovieUpdateDTO;
import com.fronchak.locadora.entities.Movie;

public class MovieMocksFactory {

	public static Movie mockMovieEntity() {
		return mockMovieEntity(0);
	}
	
	public static Movie mockMovieEntity(int i) {
		Movie mock = new Movie();
		mock.setId(mockId(i));
		mock.setTitle(mockTitle(i));
		mock.setSynopsis(mockSynopsis(i));
		mock.setDurationInMinutes(mockDurationInMinutes(i));
		mock.setNote(mockNote(i));
		return mock;
	}
	
	public static Long mockId(int i) {
		return 10L + i;
	}
	
	public static String mockTitle(int i) {
		return "Mock movie title " + i;
	}
	
	public static String mockSynopsis(int i) {
		return "Mock movie synopsis " + i;
	}
	
	public static Integer mockDurationInMinutes(int i) {
		return 100 + i;
	}
	
	public static Double mockNote(int i) {
		return 1.0 + i;
	}
	
	public static Page<Movie> mockMovieEntityPage() {
		return new PageImpl<>(mockMovieEntityList());
	}
	
	public static List<Movie> mockMovieEntityList() {
		List<Movie> list = new ArrayList<>();
		list.add(mockMovieEntity(0));
		list.add(mockMovieEntity(1));
		return list;
	}
	
	public static Page<MovieOutputAllDTO> mockMovieOutputAllDTOPage() {
		return new PageImpl<>(mockMovieOutputAllDTOList());
	}
	
	public static List<MovieOutputAllDTO> mockMovieOutputAllDTOList() {
		List<MovieOutputAllDTO> list = new ArrayList<>();
		list.add(mockMovieOutputAllDTO(0));
		list.add(mockMovieOutputAllDTO(1));
		return list;
	}
	
	public static MovieOutputAllDTO mockMovieOutputAllDTO(int i) {
		MovieOutputAllDTO mock = new MovieOutputAllDTO();
		mock.setId(mockId(i));
		mock.setTitle(mockTitle(i));
		mock.setNote(mockNote(i));
		return mock;
	}
	
	public static MovieOutputDTO mockMovieOutputDTO() {
		return mockMovieOutputDTO(0);
	}
	
	public static MovieOutputDTO mockMovieOutputDTO(int i) {
		MovieOutputDTO mock = new MovieOutputDTO();
		mock.setId(mockId(i));
		mock.setTitle(mockTitle(i));
		mock.setSynopsis(mockSynopsis(i));
		mock.setDurationInMinutes(mockDurationInMinutes(i));
		mock.setNote(mockNote(i));
		return mock;
	}
	
	public static MovieInsertDTO mockMovieInsertDTO() {
		return (MovieInsertDTO) mockMovieInputDTO(new MovieInsertDTO(), 0);
	}
	
	public static MovieInputDTO mockMovieInputDTO(MovieInputDTO mock, int i) {
		mock.setTitle(mockTitle(i));
		mock.setSynopsis(mockSynopsis(i));
		mock.setDurationInMinutes(mockDurationInMinutes(i));
		mock.setNote(mockNote(i));
		return mock;
	}

	public static MovieUpdateDTO mockMovieUpdateDTO() {
		return (MovieUpdateDTO) mockMovieInputDTO(new MovieUpdateDTO(), 0);
	}
}
