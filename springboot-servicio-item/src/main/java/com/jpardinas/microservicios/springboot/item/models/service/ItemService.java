package com.jpardinas.microservicios.springboot.item.models.service;

import java.util.List;

import com.jpardinas.microservicios.springboot.item.models.Item;

public interface ItemService {
	
	public List<Item> findAll();
	
	public Item findById(Long id, Integer cantidad);

}
