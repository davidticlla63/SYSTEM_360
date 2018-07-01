package com.erp360.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.erp360.dao.CotizacionDao;
import com.erp360.model.Cotizacion;
import com.erp360.util.DateUtility;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "listCotizacionController")
@ViewScoped
public class ListCotizacionController implements Serializable {

	private static final long serialVersionUID = -1012155707040198783L;

	//DAO
	private @Inject CotizacionDao cotizacionDao;

	//STATE
	private boolean modificar = false;
	private boolean crear = true;

	//OBJECT
	private Cotizacion selectedCotizacion;

	//LIST
	private List<Cotizacion> listCotizacion;

	//VAR
	private Date fechaInicial;
	private Date fechaFinal;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login

	@PostConstruct
	public void init() {

		System.out.println(" ... init New ...");
		loadDefault();
	}

	public void loadDefault(){
		modificar = false;
		crear = true;
		
		//para cargar las cotizaciones del mes
		fechaInicial = DateUtility.getPrimerDiaDelMes();
		fechaFinal = DateUtility.getUltimoDiaDelMes();

		selectedCotizacion = new Cotizacion();
		cargarCotizacion();
	}

	// ------- action & event ------

	public void onRowSelect(SelectEvent event) {
		//		if(selectedCotizacion.getEstado().equals("AC")){
		//			crear = false;
		//			modificar = true;
		//		}
	}
	
	//PROCESS
	
	public void cargarCotizacion(){
		listCotizacion = cotizacionDao.obtenerTodosEntreFechas(fechaInicial,fechaFinal);
	}

	// -------- get and set -------

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public boolean isCrear() {
		return crear;
	}

	public void setCrear(boolean crear) {
		this.crear = crear;
	}

	public Cotizacion getSelectedCotizacion() {
		return selectedCotizacion;
	}

	public void setSelectedCotizacion(Cotizacion selectedCotizacion) {
		this.selectedCotizacion = selectedCotizacion;
	}

	public List<Cotizacion> getListCotizacion() {
		return listCotizacion;
	}

	public void setListCotizacion(List<Cotizacion> listCotizacion) {
		this.listCotizacion = listCotizacion;
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


}
