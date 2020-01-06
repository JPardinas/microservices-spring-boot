package com.jpardinas.microservicios.springboot.productos.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.jpardinas.microservicios.springboot.commons.models.entity.Producto;


public interface ProductoDao extends CrudRepository<Producto, Long>{

}
