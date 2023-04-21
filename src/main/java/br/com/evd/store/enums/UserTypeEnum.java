package br.com.evd.store.enums;

public enum UserTypeEnum {
	ADMINISTRATOR(1, "ADMIN"),
	STOCKIST(2, "ESTOQUISTA"),
	CUSTOMER(3, "CLIENTE");
	
	private long id;
	private String descType;
	
	private UserTypeEnum(long id, String desc) {
		this.setId(id);
		this.setDescType(desc);
	}

	public String getDescType() {
		return descType;
	}

	public void setDescType(String descType) {
		this.descType = descType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
