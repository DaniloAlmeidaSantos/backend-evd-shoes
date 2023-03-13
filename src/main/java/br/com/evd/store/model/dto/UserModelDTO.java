package br.com.evd.store.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
public class UserModelDTO{	
	private long idUser;
	private String username;
	private String cpf;
	private String email;
	private String password;
	private String status;
	private UserTypeModelDTO userType;
	
	private boolean isNewPassword; // Para alteração de senha
}
