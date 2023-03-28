package br.com.evd.store.service;

import java.util.List;

import br.com.evd.store.model.dto.ProductsModelDTO;
import br.com.evd.store.model.dto.ProductsStatusRequestModelDTO;

public interface ProductsService {
	List<ProductsModelDTO> getAllProducts(String nameProduct);
	boolean addProduct(ProductsModelDTO request); 
	boolean updateStatus(ProductsStatusRequestModelDTO request);
	ProductsModelDTO getProduct(long id);
	boolean updateProduct(ProductsModelDTO request);
}
