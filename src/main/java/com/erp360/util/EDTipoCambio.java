/**
 * 
 */
package com.erp360.util;

import java.util.Date;

import com.erp360.model.TipoCambio;


/**
 * @author mauriciobejaranorivera
 *
 */
public class EDTipoCambio {
	private Integer id;

	private Date fecha;
	
	private TipoCambio tipoCambio;
	
	public EDTipoCambio() {
		super();
		this.id = 0;
		this.fecha = new Date();
		this.tipoCambio = new TipoCambio();
	}
	
	public EDTipoCambio(Integer id,Date fecha, TipoCambio tipoCambio) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.tipoCambio = tipoCambio;
	}

	public TipoCambio getTipoCambio() {
		return tipoCambio;
	}
	
	public void setTipoCambio(TipoCambio tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	

}
