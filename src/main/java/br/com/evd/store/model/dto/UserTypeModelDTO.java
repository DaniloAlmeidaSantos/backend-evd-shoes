package br.com.evd.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserTypeModelDTO {
    private long typeId;
    private String typeName;
}
