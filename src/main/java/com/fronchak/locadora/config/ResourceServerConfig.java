package com.fronchak.locadora.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenStore tokenStore;
	
	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" };
	
	private static final String[] AT_LEAST_CLIENT = { "/movies/**" };
	
	private static final String[] OPERATOR_OR_ADMIN = { "/users/**" };
 	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// H2
		if(isTestEnvironment()) {
			http.headers().frameOptions().disable();
		}
		
		http.authorizeRequests()
			.antMatchers(PUBLIC).permitAll()
			.antMatchers(HttpMethod.GET, AT_LEAST_CLIENT).hasAnyRole("CLIENT", "OPERATOR", "ADMIN")
			.antMatchers(HttpMethod.POST, AT_LEAST_CLIENT).hasAnyRole("OPERATOR", "ADMIN")
			.antMatchers(HttpMethod.PUT, AT_LEAST_CLIENT).hasAnyRole("OPERATOR", "ADMIN")
			.antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")
			.antMatchers(HttpMethod.POST, OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")
			.antMatchers(HttpMethod.DELETE, OPERATOR_OR_ADMIN).hasRole("ADMIN")
			.antMatchers(HttpMethod.DELETE, AT_LEAST_CLIENT).hasRole("ADMIN")
			.anyRequest().authenticated();
	}

	private boolean isTestEnvironment() {
		return Arrays.asList(env.getActiveProfiles()).contains("test");
	}
}
