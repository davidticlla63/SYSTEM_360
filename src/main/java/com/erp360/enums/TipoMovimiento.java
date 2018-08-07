package com.erp360.enums;

public enum TipoMovimiento {

	VEN("Venta"), COB("Cobro"), DEP("Deposito"), APE("Apertura Caja"), COM(
			"Compra"), COMI(
			"Comisiones"), RET("Retiro"), PAG("Pago"), CIE("Cierre Caja");

	private String label;

	TipoMovimiento(String nome) {
		this.label = nome;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
