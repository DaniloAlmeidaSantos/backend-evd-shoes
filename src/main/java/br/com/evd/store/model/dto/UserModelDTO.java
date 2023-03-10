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
public class UserModelDTO {
	private String username;
	private String cpf;
	private String email;
	private String password;
	private String group;
	private UserTypeModelDTO userType;
}
