package br.com.evd.store.enums;

public enum UserTypeEnum {
	ADMINISTRATOR(1, "ADMIN"),
	STOCKIST(2, "ESTOQUISTA"),
	CUSTOMER(3, "CLIENTE");
	
	private int id;
	private String descType;
	
	private UserTypeEnum(int id, String desc) {
		this.setId(id);
		this.setDescType(desc);
	}

	public String getDescType() {
		return descType;
	}

	public void setDescType(String descType) {
		this.descType = descType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
