package com.jpardinas.microservicios.springboot.oauth.services;

import com.jpardinas.microservicios.springboot.usuarios.commons.models.entity.Usuario;

public interface IUsuarioService {
	
	public Usuario findByUsername (String username);
	
	public Usuario update(Usuario usuario, Long Id);

}
