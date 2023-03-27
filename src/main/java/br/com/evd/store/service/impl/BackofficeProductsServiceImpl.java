package br.com.evd.store.service.impl;

import static br.com.evd.store.cache.CacheConstants.SERVICE_ON_MEMORY_CACHE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import br.com.evd.store.model.dto.ProductImageModelDTO;
import br.com.evd.store.model.dto.ProductsModelDTO;
import br.com.evd.store.model.dto.ProductsStatusRequestModelDTO;
import br.com.evd.store.repository.ProductsRepository;
import br.com.evd.store.service.ProductsService;
import br.com.evd.store.utils.Base64Utils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Qualifier("BackofficeProducts")
public class BackofficeProductsServiceImpl implements ProductsService {

	@Autowired
	private ProductsRepository productsRepository;

	public boolean addProduct(ProductsModelDTO request) {
		Long idObtained = productsRepository.addProduct(request);

		if (idObtained != null && idObtained > 0) {
			for (ProductImageModelDTO image : request.getProductImages()) {
				try {
					String[] file = Base64Utils.replaceBase64(image.getFile());
					image.setMimeType(file[0]);
					image.setIdProduct(idObtained);
					productsRepository.addImage(image);
				} catch (Exception e) {
					log.error("[ERROR] Error to register image.");
				}
			}

			log.info("[INFO] Product {} created success ", request.getNameProduct());
			return true;

		}

		return false;
	}

	@Override
	public List<ProductsModelDTO> getAllProducts(String nameProduct) {
		List<ProductsModelDTO> response = productsRepository.getAllProducts(nameProduct);

		if (response.size() > 0) {
			log.info("[INFO] Returning products to client.");
			return response;
		}

		log.info("[ERROR] Products not found, retry later.");
		return null;
	}

	@Override
	public boolean updateStatus(ProductsStatusRequestModelDTO request) {
		boolean isUpdated = productsRepository.updateStatus(request);
		
		if (isUpdated) {
			log.info("[INFO] Product status updated");
			return true;
		}
		
		return false;
	}

	@Override
	@Cacheable(value = SERVICE_ON_MEMORY_CACHE, key = "#id", unless = "#result == null")
	public ProductsModelDTO getProduct(long id) {
		
		ProductsModelDTO product = productsRepository.getProduct(id);
		
		if (product != null) {
			List<ProductImageModelDTO> images = productsRepository.getImages(id);
			product.setProductImages(images);
			return product;
		}
		
		return null;
	}
}
