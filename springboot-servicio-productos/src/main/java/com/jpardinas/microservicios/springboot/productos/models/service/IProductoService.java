package com.jpardinas.microservicios.springboot.productos.models.service;

import java.util.List;

import com.jpardinas.microservicios.springboot.productos.models.entity.Producto;

public interface IProductoService {
	
	public List<Producto> findAll();
	public Producto findById(Long id);

}
