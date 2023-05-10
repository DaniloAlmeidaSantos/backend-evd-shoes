package br.com.evd.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartProductRequestDTO {
	private Long id;
	private int quantity;
}
