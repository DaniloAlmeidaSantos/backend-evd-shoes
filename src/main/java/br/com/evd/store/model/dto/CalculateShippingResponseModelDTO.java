package br.com.evd.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateShippingResponseModelDTO {
	private double price;
	private double distance;
}
