package br.com.evd.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ProductCustomerViewDTO {
	private long idProduct;
	private String brand;
	private String nameProduct;
	private Double cost;
	private Double totalPrice; // If the request is to cart
	private String file;
	private int quantity;
}
