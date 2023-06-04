package br.com.evd.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.evd.store.model.dto.ApiDefaultResponseDTO;
import br.com.evd.store.model.dto.CartProductRequestDTO;
import br.com.evd.store.model.dto.OrdersResponseDTO;
import br.com.evd.store.model.dto.ProductCustomerViewDTO;
import br.com.evd.store.model.dto.SalesToUserDTO;
import br.com.evd.store.model.dto.SellConfirmRequestDTO;
import br.com.evd.store.service.ProductCartService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/products")
public class ProductsCustomerController {

	@Autowired
	private ProductCartService cartService;

	@PostMapping(value = "/cart", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<ProductCustomerViewDTO>> getCartProducts(
			@RequestBody List<CartProductRequestDTO> request) {
		log.info("[CART] Getting products in cart.");
		List<ProductCustomerViewDTO> response = cartService.getCartProducts(request);

		if (response.size() > 0) {
			log.info("[CART] Success to get products.");
			return ResponseEntity.ok(response);
		}

		log.info("[CART] Products not indentified.");
		return ResponseEntity.badRequest().body(null);
	}

	@PostMapping(value = "/confirm/sell", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ApiDefaultResponseDTO> confirmSelling(@RequestBody List<SellConfirmRequestDTO> request) {
		long orderNum = cartService.sellProduct(request);

		if (orderNum > 0) {
			return ResponseEntity.ok().body(new ApiDefaultResponseDTO("200", String.valueOf(orderNum)));
		}

		return ResponseEntity.badRequest().body(new ApiDefaultResponseDTO("400", "Error to sell produtcts"));
	}
	
	@PutMapping(value = "/order/update", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ApiDefaultResponseDTO> updateStatusOrder(@RequestParam long id, @RequestBody SellConfirmRequestDTO request) {
		boolean isUpdate = cartService.updateStatusOrder(id, request);
		
		if (isUpdate) {
			return ResponseEntity.ok(new ApiDefaultResponseDTO("200", "Order updated success!"));
		}
		
		log.warn("[UPDATE STATUS] Error in update status to order {} ", id);
		return ResponseEntity.badRequest().body(null);
	}

	@GetMapping(value = "/orders", produces = "application/json")
	public ResponseEntity<List<OrdersResponseDTO>> getOrders(@RequestParam(required = false) Long id) {

		List<OrdersResponseDTO> orders = cartService.getSalesToUser(id);

		if (orders.size() > 0) {
			return ResponseEntity.ok().body(orders);
		}

		return ResponseEntity.badRequest().body(null);
	}

	@GetMapping(value = "/orders/summary", produces = "application/json")
	public ResponseEntity<List<SalesToUserDTO>> getSummaOrders(@RequestParam long id) {

		List<SalesToUserDTO> order = cartService.getSummaryOrder(id);

		if (order != null) {
			return ResponseEntity.ok().body(order);
		}

		return ResponseEntity.badRequest().body(null);
	}

}
