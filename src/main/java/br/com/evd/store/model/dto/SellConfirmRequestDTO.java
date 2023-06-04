package br.com.evd.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SellConfirmRequestDTO {
	private long idUser;
	private long idProduct;
	private int quantity;
	private double totalPrice;
	private String deliveryCompany;
	private long idAddress;
	private String saleAddress;
	private long idPayment;
	private String status;
}