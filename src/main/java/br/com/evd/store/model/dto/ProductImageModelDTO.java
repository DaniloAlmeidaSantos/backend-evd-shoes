package br.com.evd.store.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public final class ProductImageModelDTO implements Serializable {
	private static final long serialVersionUID = 764596638721870912L;
	private long idImage;
	private String file;
	private String fileDefault;
	private String mimeType;
	private String name;
	private long idProduct;
}
