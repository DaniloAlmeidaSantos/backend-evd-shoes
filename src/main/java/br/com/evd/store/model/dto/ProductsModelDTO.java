package br.com.evd.store.model.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class ProductsModelDTO implements Serializable {
	private static final long serialVersionUID = -8529395799080922599L;
	private long idProduct;
	private String brand;
	private String nameProduct;
	private String description;
	private double cost;
	private String status;
	private int quantity;
	private double ratio;
	private List<ProductImageModelDTO> productImages;
}
