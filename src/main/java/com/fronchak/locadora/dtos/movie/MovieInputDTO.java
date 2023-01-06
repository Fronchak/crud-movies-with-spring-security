package com.fronchak.locadora.dtos.movie;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class MovieInputDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Movie's title cannot be empty")
	private String title;
	
	@NotBlank(message = "Movie's synopsis cannot be empty")
	private String synopsis;
	
	@NotNull(message = "Movie's duration must be specified")
	@Positive(message = "Movie's duration must have at least one minute")
	@Max(value = 240, message = "Movie's duration cannot be bigger than 240 minutes")
	private Integer durationInMinutes;
	
	@NotNull(message = "Movie's note must be specified")
	@PositiveOrZero(message = "Movie's note cannot be negative")
	@Max(value = 5, message = "Movie's note cannot be bigger than five")
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
