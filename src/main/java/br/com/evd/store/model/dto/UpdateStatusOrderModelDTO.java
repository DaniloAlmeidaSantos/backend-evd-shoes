package br.com.evd.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class UpdateStatusOrderModelDTO {
	private String status;
	private long idSale;
}
