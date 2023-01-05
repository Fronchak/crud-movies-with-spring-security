package com.fronchak.locadora.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fronchak.locadora.entities.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {}