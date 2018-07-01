package com.erp360.util;

import java.util.List;

import com.erp360.model.NotaVenta;
import com.erp360.model.PlanCobranza;

public class EDEstadoCuentaCliente {
	
	private Integer id;
	private NotaVenta notaVenta; //getCliente
	private List<PlanCobranza> listPlanCobranza;
	private int cuotasPendientes;
	
	public EDEstadoCuentaCliente(){
		super();
	}
	
	public EDEstadoCuentaCliente(Integer id,
			NotaVenta notaVenta, List<PlanCobranza> listPlanCobranza,
			int cuotasPendientes) {
		super();
		this.id = id;
		this.notaVenta = notaVenta;
		this.listPlanCobranza = listPlanCobranza;
		this.cuotasPendientes = cuotasPendientes;
	}

	public NotaVenta getNotaVenta() {
		return notaVenta;
	}

	public void setNotaVenta(NotaVenta notaVenta) {
		this.notaVenta = notaVenta;
	}

	public List<PlanCobranza> getListPlanCobranza() {
		return listPlanCobranza;
	}

	public void setListPlanCobranza(List<PlanCobranza> listPlanCobranza) {
		this.listPlanCobranza = listPlanCobranza;
	}

	public int getCuotasPendientes() {
		return cuotasPendientes;
	}

	public void setCuotasPendientes(int cuotasPendientes) {
		this.cuotasPendientes = cuotasPendientes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	

}
