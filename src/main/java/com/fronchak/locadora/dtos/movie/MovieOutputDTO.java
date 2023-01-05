package com.fronchak.locadora.dtos.movie;

import com.fronchak.locadora.entities.Movie;

public class MovieOutputDTO extends MovieOutputAllDTO {

	private static final long serialVersionUID = 1L;
	
	private String synopsis;
	private Integer durationInMinutes;
	
	public MovieOutputDTO() {}

	public MovieOutputDTO(Long id, String title, Double note, String synopsis, Integer durationInMinutes) {
		super(id, title, note);
		this.synopsis = synopsis;
		this.durationInMinutes = durationInMinutes;
	}

	public MovieOutputDTO(Movie entity) {
		this(entity.getId(), entity.getTitle(), entity.getNote(), entity.getSynopsis(), entity.getDurationInMinutes());
	}

	public String getSynopsis() {
		return synopsis;
	}
	
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	
	public Integer getDurationInMinutes() {
		return durationInMinutes;
	}
	
	public void setDurationInMinutes(Integer durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}
}
