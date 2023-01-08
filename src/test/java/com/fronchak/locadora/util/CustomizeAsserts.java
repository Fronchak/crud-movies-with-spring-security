package com.fronchak.locadora.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fronchak.locadora.dtos.movie.MovieOutputAllDTO;
import com.fronchak.locadora.dtos.movie.MovieOutputDTO;
import com.fronchak.locadora.dtos.role.RoleOutputDTO;
import com.fronchak.locadora.dtos.user.UserOutputDTO;
import com.fronchak.locadora.entities.Movie;
import com.fronchak.locadora.mocks.RoleMocksFactory;

public class CustomizeAsserts {

	public static void assertMovieOutputAllDTOPage(Page<MovieOutputAllDTO> page) {
		List<MovieOutputAllDTO> resultList = page.getContent();
		
		MovieOutputAllDTO result = resultList.get(0);
		assertEquals(10L, result.getId());
		assertEquals("Mock movie title 0", result.getTitle());
		assertEquals(1.0, result.getNote());
		
		result = resultList.get(1);
		assertEquals(11L, result.getId());
		assertEquals("Mock movie title 1", result.getTitle());
		assertEquals(2.0, result.getNote());
	}
	
	public static void assertMovieOutputDTO(MovieOutputDTO result) {
		assertEquals(10L, result.getId());
		assertEquals("Mock movie title 0", result.getTitle());
		assertEquals("Mock movie synopsis 0", result.getSynopsis());
		assertEquals(100, result.getDurationInMinutes());
		assertEquals(1.0, result.getNote());
	}
	
	public static void assertMovieOutputDTOAuxiliar(MovieOutputDTO result) {
		assertEquals(11L, result.getId());
		assertEquals("Mock movie title 1", result.getTitle());
		assertEquals("Mock movie synopsis 1", result.getSynopsis());
		assertEquals(101, result.getDurationInMinutes());
		assertEquals(2.0, result.getNote());
	}
	
	public static void assertMovieEntity(Movie result) {
		assertEquals(10L, result.getId());
		assertEquals("Mock movie title 0", result.getTitle());
		assertEquals("Mock movie synopsis 0", result.getSynopsis());
		assertEquals(100, result.getDurationInMinutes());
		assertEquals(1.0, result.getNote());
	}
	
	public static void assertUserOutputDTO(UserOutputDTO result) {
		RoleOutputDTO role1 = RoleMocksFactory.mockRoleOutputDTO(0);
		RoleOutputDTO role2 = RoleMocksFactory.mockRoleOutputDTO(1);
		assertEquals(30L, result.getId());
		assertEquals("mock_email_0@gmail.com.br", result.getEmail());
		assertTrue(result.getRoles().contains(role1));
		assertTrue(result.getRoles().contains(role2));
	}
}
