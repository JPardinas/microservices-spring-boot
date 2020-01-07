package com.jpardinas.microservicios.springboot.usuarios;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import com.jpardinas.microservicios.springboot.usuarios.commons.models.entity.Role;
import com.jpardinas.microservicios.springboot.usuarios.commons.models.entity.Usuario;

@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer {

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Usuario.class, Role.class);
	}
	
	

}
