package com.fronchak.locadora.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
public class WebSecurityConfigTest {

	@Autowired
	BCryptPasswordEncoder passwordEnconder;
	
	@Test
	public void encoderShouldEncodeCorrectly() {
		String result = passwordEnconder.encode("123456");
		assertEquals("$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG", result);
	}
}
