package com.erp360.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.erp360.dao.MovimientoClientesDao;
import com.erp360.util.EDMovimientoClientes;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "movimientoClientesController")
@ViewScoped
public class MovimientoClientesController implements Serializable {

	private static final long serialVersionUID = -7148739425514986109L;

	//DAO
	private @Inject SessionMain sessionMain; //variable del login
	private @Inject MovimientoClientesDao movimientoClientesDao; 

	//estados
	private boolean crear ;
	private boolean registrar ;
	private boolean modificar ;

	//VAR
	private Date fechaInicial;
	private Date fechaFinal;
	private String tipoFiltro;
	
	//OBJECT
	private EDMovimientoClientes deudor;

	//LIST
	private List<EDMovimientoClientes> deudores;

	@PostConstruct
	public void init() {
		System.out.println(" init new initNewCliente");
		loadDefault();
	}

	private void loadDefault(){
		crear = true;
		registrar = false;
		modificar = false;

		deudor = new EDMovimientoClientes();
		deudores = movimientoClientesDao.obtenerDelMesActual();

		fechaInicial = new Date();
		fechaFinal = new Date();
		tipoFiltro = "MES_ACTUAL";
	}

	// -- event action

	public void loadDialogImage() {
		FacesUtil.showDialog("dlgImagenCliente");
		FacesUtil.updateComponent("dlgImagenCliente");
	}

	public void onRowSelect(SelectEvent event) {
		crear = false;
		registrar = false;
		modificar = true;
	}

	// --------------   get and set  ---------------

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

	public boolean isRegistrar() {
		return registrar;
	}

	public void setRegistrar(boolean registrar) {
		this.registrar = registrar;
	}

	public EDMovimientoClientes getDeudor() {
		return deudor;
	}

	public void setDeudor(EDMovimientoClientes deudor) {
		this.deudor = deudor;
	}

	public List<EDMovimientoClientes> getDeudores() {
		return deudores;
	}

	public void setDeudores(List<EDMovimientoClientes> deudores) {
		this.deudores = deudores;
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

	public String getTipoFiltro() {
		return tipoFiltro;
	}

	public void setTipoFiltro(String tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}
}

