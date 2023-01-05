package com.fronchak.locadora.dtos.movie;

import java.io.Serializable;

public class MovieInputDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String title;
	private String synopsis;
	private Integer durationInMinutes;
	private Double note;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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
	
	public Double getNote() {
		return note;
	}
	
	public void setNote(Double note) {
		this.note = note;
	}
}
