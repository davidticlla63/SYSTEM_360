package com.erp360.util;

import com.erp360.model.Cliente;

public class EDMovimientoClientes {
	
	private Cliente cliente;
	private int cantidadCuotasPendientes;
	private double montoTotalDeudaNac;
	private double montoTotalDeudaExt;
	private double montoTotalIntNac;
	private double montoTotalIntExt;
	private double montoTotalMultaNac;
	private double montoTotalMultaExt;
	private String moneda;
	
	public EDMovimientoClientes(){
		
	}

	public EDMovimientoClientes(Cliente cliente, int cantidadCuotasPendientes,
			double montoTotalDeudaNac, double montoTotalDeudaExt,
			double montoTotalIntNac, double montoTotalIntExt,
			double montoTotalMultaNac, double montoTotalMultaExt, String moneda) {
		super();
		this.cliente = cliente;
		this.cantidadCuotasPendientes = cantidadCuotasPendientes;
		this.montoTotalDeudaNac = montoTotalDeudaNac;
		this.montoTotalDeudaExt = montoTotalDeudaExt;
		this.montoTotalIntNac = montoTotalIntNac;
		this.montoTotalIntExt = montoTotalIntExt;
		this.montoTotalMultaNac = montoTotalMultaNac;
		this.montoTotalMultaExt = montoTotalMultaExt;
		this.moneda = moneda;
	}

	@Override
	public String toString() {
		return "EDMovimientoClientes [cliente=" + cliente + ", cantidadCuotasPendientes="
				+ cantidadCuotasPendientes + ", montoTotalDeudaNac=" + montoTotalDeudaNac
				+ ", montoTotalDeudaExt=" + montoTotalDeudaExt
				+ ", montoTotalIntNac=" + montoTotalIntNac
				+ ", montoTotalIntExt=" + montoTotalIntExt
				+ ", montoTotalMultaNac=" + montoTotalMultaNac
				+ ", montoTotalMultaExt=" + montoTotalMultaExt + ", moneda="
				+ moneda + "]";
	}

	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public int getCantidadCuotasPendientes() {
		return cantidadCuotasPendientes;
	}

	public void setCantidadCuotasPendientes(int cantidadCuotasPendientes) {
		this.cantidadCuotasPendientes = cantidadCuotasPendientes;
	}

	public double getMontoTotalDeudaNac() {
		return montoTotalDeudaNac;
	}

	public void setMontoTotalDeudaNac(double montoTotalDeudaNac) {
		this.montoTotalDeudaNac = montoTotalDeudaNac;
	}

	public double getMontoTotalDeudaExt() {
		return montoTotalDeudaExt;
	}

	public void setMontoTotalDeudaExt(double montoTotalDeudaExt) {
		this.montoTotalDeudaExt = montoTotalDeudaExt;
	}

	public double getMontoTotalIntNac() {
		return montoTotalIntNac;
	}

	public void setMontoTotalIntNac(double montoTotalIntNac) {
		this.montoTotalIntNac = montoTotalIntNac;
	}

	public double getMontoTotalIntExt() {
		return montoTotalIntExt;
	}

	public void setMontoTotalIntExt(double montoTotalIntExt) {
		this.montoTotalIntExt = montoTotalIntExt;
	}

	public double getMontoTotalMultaNac() {
		return montoTotalMultaNac;
	}

	public void setMontoTotalMultaNac(double montoTotalMultaNac) {
		this.montoTotalMultaNac = montoTotalMultaNac;
	}

	public double getMontoTotalMultaExt() {
		return montoTotalMultaExt;
	}

	public void setMontoTotalMultaExt(double montoTotalMultaExt) {
		this.montoTotalMultaExt = montoTotalMultaExt;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	
	

}
