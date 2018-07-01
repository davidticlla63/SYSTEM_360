package com.erp360.util;

import java.util.Date;

import com.erp360.model.Almacen;
import com.erp360.model.Producto;
/**
 * 
 * @author mauriciobejaranorivera
 *
 */

public class EDKardexProducto {

	private Integer id;

	private String transaccion;// VENTA,COMPRA,TRASPASO,BAJAS

	private String estado;
	
	private Date fecha;

	private double cantidad;

	private double precioUnitario;

	private double totalEntrada;
	
	private double totalSalida;
	
	private double totalSaldo;
	
	//cantidad entrante
	private double stockEntrante;
	
	private double stockAnterior;

	//Saldo
	private double stockActual;

	private String tipoMovimiento;

	private String numeroTransaccion;

	private Date fechaRegistro;

	private Producto producto;
	
	private Almacen almacen;
	
	public EDKardexProducto(){
		super();
		this.id = 0;
		this.precioUnitario = 0;
		this.totalEntrada = 0;
		this.totalSalida = 0;
		this.totalSaldo = 0;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public double getStockActual() {
		return stockActual;
	}

	public void setStockActual(double precioActual) {
		this.stockActual = precioActual;
	}

	public double getStockAnterior() {
		return stockAnterior;
	}

	public void setStockAnterior(double precioAnterior) {
		this.stockAnterior = precioAnterior;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}

	public String getNumeroTransaccion() {
		return numeroTransaccion;
	}

	public void setNumeroTransaccion(String numeroTransaccion) {
		this.numeroTransaccion = numeroTransaccion;
	}

	public String getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public double getTotalEntrada() {
		return totalEntrada;
	}

	public void setTotalEntrada(double totalEntrada) {
		this.totalEntrada = totalEntrada;
	}

	public double getTotalSalida() {
		return totalSalida;
	}

	public void setTotalSalida(double totalSalida) {
		this.totalSalida = totalSalida;
	}

	public double getTotalSaldo() {
		return totalSaldo;
	}

	public void setTotalSaldo(double totalSaldo) {
		this.totalSaldo = totalSaldo;
	}

	public double getStockEntrante() {
		return stockEntrante;
	}

	public void setStockEntrante(double stockEntrante) {
		this.stockEntrante = stockEntrante;
	}

}