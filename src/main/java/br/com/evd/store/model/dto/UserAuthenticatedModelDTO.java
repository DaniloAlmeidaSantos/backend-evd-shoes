package br.com.evd.store.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class UserAuthenticatedModelDTO {
	@JsonIgnore
	private String ecryptedPassword;
	private long idUser;
	private String userType;
	private String username;
	private String email;
	private String status;
}
