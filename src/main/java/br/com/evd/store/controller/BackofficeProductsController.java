package br.com.evd.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
import br.com.evd.store.model.dto.ProductCustomerViewDTO;
import br.com.evd.store.model.dto.ProductsModelDTO;
import br.com.evd.store.model.dto.ProductsStatusRequestModelDTO;
import br.com.evd.store.service.ProductsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/backoffice")
@CrossOrigin(origins = "*")
public class BackofficeProductsController {
	
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
	public ResponseEntity<List<ProductsModelDTO>> getAllProducts(@RequestParam(required = false) String nameProduct) {
		List<ProductsModelDTO> response = productsService.getAllProducts(nameProduct);
		
		if (response != null) {
			return ResponseEntity.ok(response);
		}
		
		return ResponseEntity.badRequest().body(null);
	}
	
	@PutMapping(value = "/products/status", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ApiDefaultResponseDTO> updateStatus(@RequestBody ProductsStatusRequestModelDTO request) {
		log.info("[INFO] Updating product status.");
		boolean isCreated = productsService.updateStatus(request);
		
		if (isCreated) {
			log.info("[INFO] Product updated");
			return new ResponseEntity<ApiDefaultResponseDTO>(
					new ApiDefaultResponseDTO("200", "Product status " + request.getIdProduct() + " updated"), HttpStatus.OK);
		}

		return ResponseEntity.badRequest().body(new ApiDefaultResponseDTO("400", "Error to update product."));
	}
	
	@GetMapping(value = "/product", produces = "application/json")
	public ResponseEntity<ProductsModelDTO> getProduct(@RequestParam long id) {
		ProductsModelDTO product = productsService.getProduct(id);
		
		if (product != null) {
			return ResponseEntity.ok(product);
		}
		
		return ResponseEntity.badRequest().body(null);
	}
	
	@PutMapping(value = "/product/update", produces = "application/json", consumes = "application/json")
	public ResponseEntity<ApiDefaultResponseDTO> updateProduct(@RequestBody ProductsModelDTO request) {
		log.info("[INFO] Updating product.");
		boolean isCreated = productsService.updateProduct(request);
		
		if (isCreated) {
			log.info("[INFO] Product update");
			return new ResponseEntity<ApiDefaultResponseDTO>(
					new ApiDefaultResponseDTO("200", "Product " + request.getNameProduct() + " updated"), HttpStatus.OK);
		}

		return ResponseEntity.badRequest().body(new ApiDefaultResponseDTO("400", "Error to register product."));
	}
	
	@GetMapping(value = "/products/details", produces = "application/json")
	public ResponseEntity<List<ProductCustomerViewDTO>> getProductView() {		
		List<ProductCustomerViewDTO> response = productsService.getProductView();
		
		if(response != null) {
			log.info("[INFO] Get Product Succesfull");
			return ResponseEntity.ok(response);
		}
		
		return ResponseEntity.badRequest().body(null);
		
	}
	
}