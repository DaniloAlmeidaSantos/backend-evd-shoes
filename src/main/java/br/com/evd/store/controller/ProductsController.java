package br.com.evd.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.evd.store.model.dto.ApiDefaultResponseDTO;
import br.com.evd.store.model.dto.ProductsModelDTO;
import br.com.evd.store.service.ProductsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/backoffice")
@CrossOrigin(origins = "*")
public class ProductsController {
	
	@Autowired
	@Qualifier("BackofficeProducts")
	private ProductsService productsService;
	
	@PostMapping(value = "/product/add", produces = "application/json", consumes = "application/json")
	public ResponseEntity<ApiDefaultResponseDTO> addProduct(@RequestBody ProductsModelDTO request) {
		log.info("[INFO] Creating product.");
		boolean isCreated = productsService.addProduct(request);
		
		if (isCreated) {
			log.info("[INFO] Product created");
			return new ResponseEntity<ApiDefaultResponseDTO>(
					new ApiDefaultResponseDTO("201", "Product " + request.getNameProduct() + " created"), HttpStatus.CREATED);
		}

		return ResponseEntity.badRequest().body(new ApiDefaultResponseDTO("400", "Error to register product."));
	}
	
	@GetMapping(value = "/products", produces = "application/json")
	public ResponseEntity<List<ProductsModelDTO>> getAllProducts() {
		List<ProductsModelDTO> response = productsService.getAllProducts();
		
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		
		return ResponseEntity.badRequest().body(null);
	}
}