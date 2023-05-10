package br.com.evd.store.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.evd.store.model.dto.CartProductRequestDTO;
import br.com.evd.store.model.dto.ProductCustomerViewDTO;
import br.com.evd.store.model.dto.SellConfirmRequestDTO;
import br.com.evd.store.repository.ProductsRepository;
import br.com.evd.store.repository.SellProductRepository;
import br.com.evd.store.service.ProductCartService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductCartServiceImpl implements ProductCartService {

	@Autowired
	private ProductsRepository productsRepository;
	
	@Autowired
	private SellProductRepository sellProductRepository;
	
	@Override
	public List<ProductCustomerViewDTO> getCartProducts(List<CartProductRequestDTO> request) {
		List<ProductCustomerViewDTO> customerViews = new ArrayList<>();
		
		for (CartProductRequestDTO cartRequest : request) {
			try {
				log.info("[CART] Finding product {} ", cartRequest.getId());
				ProductCustomerViewDTO response = productsRepository.getProductView(cartRequest.getId()).get(0);
				response.setTotalPrice(cartRequest.getQuantity() * response.getCost());
				response.setQuantity(cartRequest.getQuantity());
				customerViews.add(response);
			} catch(Exception e) {
				log.error("[CART] Error to find product {}: {}", cartRequest.getId(), e.getMessage());
			}
			
		}
				
		return customerViews;
	}

	@Override
	public boolean sellProduct(List<SellConfirmRequestDTO> request) {
		
		for (SellConfirmRequestDTO dto : request) {
			try {
				log.info("[SELL PRODUCT] Selling product {} to user {} ", dto.getIdProduct(), dto.getIdUser());
				sellProductRepository.sellProduct(dto);
			} catch (Exception e) {
				log.error("[ERROR SELL PRODUTCT] Error to selling product {} in datbase: {} ", dto.getIdProduct(), e.getMessage());
			}
		}
		
		return true;
	} 	
	
	
}
	