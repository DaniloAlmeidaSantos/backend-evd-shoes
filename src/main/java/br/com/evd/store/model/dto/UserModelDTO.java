package br.com.evd.store.model.dto;

import java.io.Serializable;
import java.util.List;

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
public final class UserModelDTO implements Serializable {	
	private static final long serialVersionUID = 5217920246895673323L;
	
	private long idUser;
	private String username; 
	private String cpf;
	private String email;
	private String password;
	private String status;
	private UserTypeModelDTO userType;
	private boolean newPassword;
	private List<UserAddressModelDTO> addresses; // endereços
	private String dateOfBirth;
	private String genre;
}
