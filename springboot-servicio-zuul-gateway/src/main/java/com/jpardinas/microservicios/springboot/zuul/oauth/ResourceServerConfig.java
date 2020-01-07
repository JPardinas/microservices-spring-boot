package com.jpardinas.microservicios.springboot.zuul.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
	}

	// Proteger endpoints del zuul server
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			// Ruta authentificar token
			.antMatchers("/api/security/oauth/**")
				// Cualquier usuario
				.permitAll()
			// Rutas Get listar
			.antMatchers(HttpMethod.GET, 
					"/api/productos/listar", 
					"/api/items/listar", 
					"/api/usuarios/usuarios")
				// Cualquier usuario
				.permitAll()
			// Rutas GET 
			.antMatchers(HttpMethod.GET, 
					"/api/productos/ver/{id}", 
					"/api/items/ver/{id}/cantidad/{cantidad}", 
					"/api/usuarios/usuarios/{id}")
				// Roles listados
				.hasAnyRole("ADMIN", "USER")
			// Rutas POST, PUT DELETE
			.antMatchers( 
					"/api/productos/**", 
					"/api/items/**",
					"/api/usuarios/**")
				// Role ADMIN;
				.hasRole("ADMIN");
				
				
			/*
			// Rutas Post
			.antMatchers(HttpMethod.POST, 
					"/api/productos/crear", 
					"/api/items/crear",
					"/api/usuarios/usuarios")
				// Role ADMIN;
				.hasRole("ADMIN")
			// Rutas Put
			.antMatchers(HttpMethod.PUT, 
					"/api/productos/editar/{id}", 
					"/api/items/editar/{id}",
					"/api/usuarios/usuarios/{id}")
				// Role ADMIN;
				.hasRole("ADMIN")
			// Rutas Delete
			.antMatchers(HttpMethod.DELETE, 
					"/api/productos/editar/{id}", 
					"/api/items/editar/{id}",
					"/api/usuarios/usuarios/{id}")
				// Role ADMIN;
				.hasRole("ADMIN");*/
	}
	
	
	

	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		// Mismo codigo que en el servicio de seguridad
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey("algun_codigo_secreto_aeiou");
		return tokenConverter;
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

}
