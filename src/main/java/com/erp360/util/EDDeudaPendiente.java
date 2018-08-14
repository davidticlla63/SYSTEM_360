package com.erp360.util;

import java.util.List;

import com.erp360.model.CuentaCobrar;
import com.erp360.model.NotaVenta;
import com.erp360.model.PlanCobranza;

public class EDDeudaPendiente {
	
	private Integer id;
	private CuentaCobrar cuentaCobrar;
	private NotaVenta notaVenta;
	private boolean pagarTodo;
	private boolean pagarSiguienteCuota;
	private int cuotasPendientes;
	private int totalCuotas;
	private double montoProximaCuota;
	private double montoProximaCuotaExtranjero;
	private List<PlanCobranza> collectionPlan;

	public EDDeudaPendiente(){
		super();
	}

	public EDDeudaPendiente(CuentaCobrar cuentaCobrar, boolean pagarTodo,
			boolean pagarSiguienteCuota, int cuotasPendientes, int totalCuotas,
			double montoProximaCuota,double montoProximaCuotaExtranjero,List<PlanCobranza> collectionPlan) {
		super();
		this.cuentaCobrar= cuentaCobrar;
		this.pagarTodo = pagarTodo;
		this.pagarSiguienteCuota = pagarSiguienteCuota;
		this.cuotasPendientes = cuotasPendientes;
		this.totalCuotas = totalCuotas;
		this.montoProximaCuota = montoProximaCuota;
		this.montoProximaCuotaExtranjero = montoProximaCuotaExtranjero;
		this.collectionPlan = collectionPlan;
	}

	public boolean isPagarTodo() {
		return pagarTodo;
	}

	public void setPagarTodo(boolean pagarTodo) {
		this.pagarTodo = pagarTodo;
	}

	public boolean isPagarSiguienteCuota() {
		return pagarSiguienteCuota;
	}

	public void setPagarSiguienteCuota(boolean pagarSiguienteCuota) {
		this.pagarSiguienteCuota = pagarSiguienteCuota;
	}

	public int getCuotasPendientes() {
		return cuotasPendientes;
	}

	public void setCuotasPendientes(int cuotasPendientes) {
		this.cuotasPendientes = cuotasPendientes;
	}

	public int getTotalCuotas() {
		return totalCuotas;
	}

	public void setTotalCuotas(int totalCuotas) {
		this.totalCuotas = totalCuotas;
	}

	public double getMontoProximaCuota() {
		return montoProximaCuota;
	}

	public void setMontoProximaCuota(double montoProximaCuota) {
		this.montoProximaCuota = montoProximaCuota;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CuentaCobrar getCuentaCobrar() {
		return cuentaCobrar;
	}

	public void setCuentaCobrar(CuentaCobrar cuentaCobrar) {
		this.cuentaCobrar = cuentaCobrar;
	}

	public double getMontoProximaCuotaExtranjero() {
		return montoProximaCuotaExtranjero;
	}

	public void setMontoProximaCuotaExtranjero(double montoProximaCuotaExtranjero) {
		this.montoProximaCuotaExtranjero = montoProximaCuotaExtranjero;
	}

	public List<PlanCobranza> getCollectionPlan() {
		return collectionPlan;
	}

	public void setCollectionPlan(List<PlanCobranza> collectionPlan) {
		this.collectionPlan = collectionPlan;
	}

	public NotaVenta getNotaVenta() {
		return notaVenta;
	}

	public void setNotaVenta(NotaVenta notaVenta) {
		this.notaVenta = notaVenta;
	}
	
}
