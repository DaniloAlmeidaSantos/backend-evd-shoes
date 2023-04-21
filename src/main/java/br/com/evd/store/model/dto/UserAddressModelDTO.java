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
	private int number;
	private long cep;
	private String complement;
	private String district;
	private String city;
	private String uf;
	private String addressDefault;
	private String status;
}
