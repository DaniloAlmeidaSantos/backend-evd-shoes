package br.com.evd.store.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.evd.store.model.dto.CartProductRequestDTO;
import br.com.evd.store.model.dto.ProductCustomerViewDTO;
import br.com.evd.store.model.dto.SalesToUserDTO;
import br.com.evd.store.model.dto.SellConfirmRequestDTO;

@Service
public interface ProductCartService {
	List<ProductCustomerViewDTO> getCartProducts(List<CartProductRequestDTO> indentifiers);
	long sellProduct(List<SellConfirmRequestDTO> request);
	List<SalesToUserDTO> getSalesToUser(Long id);
	List<SalesToUserDTO> getSummaryOrder(long id);
}
