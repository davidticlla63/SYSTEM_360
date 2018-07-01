package com.erp360.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import com.erp360.dao.CobranzaDao;
import com.erp360.dao.NotaVentaDao;
import com.erp360.util.DateUtility;

@ManagedBean
public class DashboardAdministradorController implements Serializable {

	private static final long serialVersionUID = 1L;

	//DAO
	private @Inject NotaVentaDao notaVentaDao;
	private @Inject CobranzaDao cobranzaDao;

	private double totalVentasMes;
	private double totalVentasDia;
	private int cantidadVentasDia;
	
	private int cantidadVentasContadoMes;
	private int cantidadVentasCreditoMes;
	
	private double totalVentasContadoMes;
	private double totalVentasCreditoMes;
	
	private double totalCobrosMes;
	private double totalCobrosDia;
	
	@PostConstruct
	public void init() {
		//System.out.println("DashboardAdministradorController init()");
		loadData();
	}

	private void loadData(){
		Date fecha = new Date();
		Integer dia = DateUtility.getDayOfMonth(fecha);
		Integer mes = DateUtility.obtenerMesNumeral(fecha);
		Integer anio = DateUtility.obtenerYearNumeral(fecha);
		//aqui se hacen las consultas y se hacen calculos
		totalVentasMes = notaVentaDao.totalVentasMes( mes, anio);
		totalVentasDia = notaVentaDao.totalVentasDia(dia, mes, anio);
		cantidadVentasDia = notaVentaDao.cantidadVentasDia(dia, mes, anio);
		
		cantidadVentasContadoMes = notaVentaDao.cantidadVentasContadoMes(mes, anio);
		cantidadVentasCreditoMes = notaVentaDao.cantidadVentasCreditoMes(mes, anio);
		
		totalVentasContadoMes = notaVentaDao.totalVentasContadoMes(mes, anio);
		totalVentasCreditoMes = notaVentaDao.totalVentasCreditoMes(mes, anio);
		
		totalCobrosMes = cobranzaDao.totalCobranzaMes( mes, anio);
		totalCobrosDia = cobranzaDao.totalCobranzaDia(dia, mes, anio);
	}

	public double getTotalVentasMes() {
		return totalVentasMes;
	}

	public void setTotalVentasMes(double totalVentasMes) {
		this.totalVentasMes = totalVentasMes;
	}

	public double getTotalVentasDia() {
		return totalVentasDia;
	}

	public void setTotalVentasDia(double totalVentasDia) {
		this.totalVentasDia = totalVentasDia;
	}

	public int getCantidadVentasDia() {
		return cantidadVentasDia;
	}

	public void setCantidadVentasDia(int cantidadVentasDia) {
		this.cantidadVentasDia = cantidadVentasDia;
	}

	public int getCantidadVentasContadoMes() {
		return cantidadVentasContadoMes;
	}

	public void setCantidadVentasContadoMes(int cantidadVentasContadoMes) {
		this.cantidadVentasContadoMes = cantidadVentasContadoMes;
	}

	public int getCantidadVentasCreditoMes() {
		return cantidadVentasCreditoMes;
	}

	public void setCantidadVentasCreditoMes(int cantidadVentasCreditoMes) {
		this.cantidadVentasCreditoMes = cantidadVentasCreditoMes;
	}

	public double getTotalVentasContadoMes() {
		return totalVentasContadoMes;
	}

	public void setTotalVentasContadoMes(double totalVentasContadoMes) {
		this.totalVentasContadoMes = totalVentasContadoMes;
	}

	public double getTotalVentasCreditoMes() {
		return totalVentasCreditoMes;
	}

	public void setTotalVentasCreditoMes(double totalVentasCreditoMes) {
		this.totalVentasCreditoMes = totalVentasCreditoMes;
	}

	public double getTotalCobrosMes() {
		return totalCobrosMes;
	}

	public void setTotalCobrosMes(double totalCobrosMes) {
		this.totalCobrosMes = totalCobrosMes;
	}

	public double getTotalCobrosDia() {
		return totalCobrosDia;
	}

	public void setTotalCobrosDia(double totalCobrosDia) {
		this.totalCobrosDia = totalCobrosDia;
	}

}
