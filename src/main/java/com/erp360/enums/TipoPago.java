package com.erp360.enums;

public enum TipoPago {	
	EFE("Efectivo"), 
	CRED("Credito"), 
	TAR("Tarjeta"), 
	CHE("Cheque");

//	EFE("Efectivo"), 
//	CRED("Credito"), 
//	TAR("Tarjeta"), 
//	CHE("Cheque"), 
//	VAL("Vale"), 
//	MIX("Mixto");
	private String label;

	
	TipoPago(String nome){
		this.label = nome;
	}
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
