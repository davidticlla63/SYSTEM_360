/**
 * @author WILSON
 */
package com.erp360.util;


import com.erp360.enums.TipoPago;


/**
 * @author WILSON
 *
 */
public class CajaIngreso {

	private Double suma;
	private TipoPago tipoPago;
	
	/**
	 * 
	 */
	public CajaIngreso() {
		//super();
	}
	public Double getSuma() {
		return suma;
	}
	public void setSuma(Double suma) {
		this.suma = suma;
	}
	public TipoPago getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	
	
	
	
  
}
