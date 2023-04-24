package br.com.evd.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserAddressModelDTO {
	private long idAddress;
	private long idUser;
	private String streetName;
	private String number;
	private String cep;
	private String complement;
	private String district;
	private String city;
	private String uf;
	private String invoiceAddress;
	private String deliveryAddress;
	private String status;
}
