package br.com.evd.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import br.com.evd.store.model.dto.ProductCustomerViewDTO;
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

	public boolean updateProduct(ProductsModelDTO request) {
		log.info("[INFO] Updating product {} ", request.getNameProduct());
		boolean isUpdated = productsRepository.updateProduct(request);

		if (isUpdated) {
			List<ProductImageModelDTO> existsImages = productsRepository.getImages(request.getIdProduct());
			List<ProductImageModelDTO> actualImages = request.getProductImages();

			if (existsImages.size() == 0) {
				for (ProductImageModelDTO image : request.getProductImages()) {
					try {
						log.info("[INFO] Inserting new images");
						String[] file = Base64Utils.replaceBase64(image.getFile());
						image.setMimeType(file[0]);
						image.setIdProduct(request.getIdProduct());
						productsRepository.addImage(image);
					} catch (Exception e) {
						log.error("[ERROR] Error to register image.");
					}
				}
			} else {
				for (int i = 0; i < actualImages.size(); i++) {
					try {
						log.info("[INFO] Inserting new images");
						String[] file = Base64Utils.replaceBase64(actualImages.get(i).getFile());
						actualImages.get(i).setMimeType(file[0]);
						actualImages.get(i).setIdProduct(request.getIdProduct());

						if (i >= existsImages.size()) {
							productsRepository.addImage(actualImages.get(i));
						} else if (existsImages.get(i).getIdProduct() != actualImages.get(i).getIdProduct()) {
							productsRepository.addImage(actualImages.get(i));
						} else {
							productsRepository.updateImage(actualImages.get(i));
						}
					} catch (Exception e) {
						log.error("[ERROR] Error to register image {} .", e.getMessage());
					}
				}
			}

		}

		return isUpdated;

	}

	@Override
	public ProductsModelDTO getProduct(long id) {

		ProductsModelDTO product = productsRepository.getProduct(id);

		if (product != null) {
			List<ProductImageModelDTO> images = productsRepository.getImages(id);
			product.setProductImages(images);
			return product;
		}

		return null;
	}

	@Override
	public List<ProductCustomerViewDTO> getProductView() {
		log.info("[PRODUCT] Getting products to home.");
		List<ProductCustomerViewDTO> productView = productsRepository.getProductView(0);
		
		if(productView != null) {
			log.info("[PRODUTCT] Success to find products to home.");
			return productView;
		}
		
		log.info("[PRODUCT] Products not found, or error to find products in databse.");
		return null;
	}
}
