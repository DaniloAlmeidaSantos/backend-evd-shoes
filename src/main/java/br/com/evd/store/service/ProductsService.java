package br.com.evd.store.service;

import java.util.List;

import br.com.evd.store.model.dto.ProductsModelDTO;

public interface ProductsService {
	List<ProductsModelDTO> getAllProducts();
	boolean addProduct(ProductsModelDTO request); 
}
