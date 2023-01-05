package com.fronchak.locadora.dtos.movie;

import java.io.Serializable;

import com.fronchak.locadora.entities.Movie;

public class MovieOutputAllDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String title;
	private Double note;
	
	public MovieOutputAllDTO() {}

	public MovieOutputAllDTO(Long id, String title, Double note) {
		this.id = id;
		this.title = title;
		this.note = note;
	}

	public MovieOutputAllDTO(Movie entity) {
		this(entity.getId(), entity.getTitle(), entity.getNote());
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Double getNote() {
		return note;
	}
	
	public void setNote(Double note) {
		this.note = note;
	}
}
