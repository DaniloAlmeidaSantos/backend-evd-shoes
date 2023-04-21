package br.com.evd.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UpdateStatusModelDTO {
	private long userId;
	private long idAddress;
	private String status;
}
