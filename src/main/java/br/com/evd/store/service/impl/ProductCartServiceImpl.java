package br.com.evd.store.service.impl;

import static br.com.evd.store.cache.CacheConstants.SERVICE_ON_MEMORY_CACHE;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import br.com.evd.store.model.dto.CartProductRequestDTO;
import br.com.evd.store.model.dto.OrdersResponseDTO;
import br.com.evd.store.model.dto.ProductCustomerViewDTO;
import br.com.evd.store.model.dto.SalesToUserDTO;
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
	public long sellProduct(List<SellConfirmRequestDTO> request) {
		try {
			long idObtained = sellProductRepository.confimrSell(request.get(0));
			
			for (SellConfirmRequestDTO dto : request) {
				try {
					log.info("[SELL PRODUCT] Selling product {} to user {} ", dto.getIdProduct(), dto.getIdUser());
					sellProductRepository.sellProduct(dto, idObtained);
				} catch (Exception e) {
					log.error("[ERROR SELL PRODUTCT] Error to selling product {} in datbase: {} ", dto.getIdProduct(), e.getMessage());
				}
			}
			return idObtained;
		} catch (Exception e) {
			log.error("[ERROR SELL PRODUCT] Error to selling product to TB_SALE {} ", e.getMessage());
		}
		
		return -1;
	}

	@Override
	public List<OrdersResponseDTO> getSalesToUser(Long id) {
		try {
			log.info("[ORDERS] Getting orders to user {} ", id);
			return sellProductRepository.getSalesToUser(id);
		} catch (Exception e) {
			log.error("[ERROR TO GET SALES] Error to get sales to user {} : {}", id, e.getMessage());
		}
		
		return null;
	}

	@Override
	public List<SalesToUserDTO> getSummaryOrder(long id) {
		
		try {
			log.info("[SUMMARY] Getting summary to order num : {} ", id);
			List<SalesToUserDTO> response = sellProductRepository.getSummary(id);
			return response;
		} catch (Exception e) {
			log.error("[SUMMARY] Error to get summary: {} ", e.getMessage());
		}
		
		return null;
	}

	@Override
	public boolean updateStatusOrder(long id, SellConfirmRequestDTO request) {
		
		boolean isUpdate = sellProductRepository.updateStatus(request, id);
		
		if (isUpdate) {
			log.info("[UPDATE ORDER] Order updated {} .", id);
			return true;
		}
		
		return false;
	} 	
	
	
}
	