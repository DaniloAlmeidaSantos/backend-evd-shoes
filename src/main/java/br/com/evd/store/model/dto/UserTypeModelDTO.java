package br.com.evd.store.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public final class UserTypeModelDTO implements Serializable {
	private static final long serialVersionUID = 1395971014565609126L;
	private long typeId;
    private String groupName;
}
