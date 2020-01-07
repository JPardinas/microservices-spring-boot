package com.jpardinas.microservicios.springboot.zuul.oauth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Value("${config.security.oauth.jwt.key}")
	private String jwtKey;

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
				.hasRole("ADMIN")
			
			// Cualquier otra ruta requiere autenficacion
			.anyRequest().authenticated()
			
			// Volver a httpsecurity con and
			.and()
			// Configuracion cors
				.cors().configurationSource(corsConfigurationSource());
				
				
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
	
	
	

	// Acceso a aplicaciones cliente
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration corsConfig = new CorsConfiguration();
		
		//corsConfig.addAllowedOrigin("*");
		// * cualquier origen
		corsConfig.setAllowedOrigins(Arrays.asList("*"));
		// OPTIONS es importante ya que lo usa oauth2
		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// ** a todas las rutas de spring security
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}
	
	// Para clientes externos (angular, react...)
	// Hay que crear componente para registrar un filtro de cors global en un filtro de spring a toda la aplicacion en general
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		// Le damos prioridad alta
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		// Mismo codigo que en el servicio de seguridad
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(jwtKey);
		return tokenConverter;
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

}
