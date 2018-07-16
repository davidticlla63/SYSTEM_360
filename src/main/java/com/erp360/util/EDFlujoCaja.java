package com.erp360.util;


public class EDFlujoCaja {
	private int id;
	private String numeroDocumento;
	private String concepto;
	private String tipoMovimiento;
	private String tipoPago;
	private double Monto;
	
	public EDFlujoCaja(){
		//super();
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	
	
	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	public double getMonto() {
		return Monto;
	}

	public void setMonto(double monto) {
		Monto = monto;
	}

	/**
	 * 
	 * @param id
	 * @param cuenta
	 * @param glosa
	 * @param centroCosto
	 * @param haberNacional
	 * @param debeNacional
	 * @param haberExtranjero
	 * @param debeExtranjero
	 * @param idAsientoContable
	 * @param numeroFactura
	 * @param numeroCheque
	 */


	
	

}
