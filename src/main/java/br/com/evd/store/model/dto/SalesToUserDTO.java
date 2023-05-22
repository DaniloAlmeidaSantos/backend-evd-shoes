package br.com.evd.store.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SalesToUserDTO implements Serializable {

	private static final long serialVersionUID = 2889288572140484309L;
	private long idSale;
	private String file;
	private String nameProduct;
	private int quantity;
	private double price;
	private String date;
	private String status;

}
