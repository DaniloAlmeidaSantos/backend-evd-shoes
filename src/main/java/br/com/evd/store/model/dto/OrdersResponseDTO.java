package br.com.evd.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersResponseDTO {
	private Long idSale; 
	private String orderStatus;
	private String orderDate;
	private double totalPrice;
	private String freight; 
}
