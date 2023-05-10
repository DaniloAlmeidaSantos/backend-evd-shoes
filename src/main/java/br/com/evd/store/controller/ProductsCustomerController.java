package br.com.evd.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.evd.store.model.dto.ApiDefaultResponseDTO;
import br.com.evd.store.model.dto.CartProductRequestDTO;
import br.com.evd.store.model.dto.ProductCustomerViewDTO;
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
		boolean isSelled = cartService.sellProduct(request);

		if (isSelled) {
			return ResponseEntity.badRequest().body(new ApiDefaultResponseDTO("200", "Products saled success"));
		}

		return ResponseEntity.badRequest().body(new ApiDefaultResponseDTO("400", "Error to sell produtcts"));
	}

}
