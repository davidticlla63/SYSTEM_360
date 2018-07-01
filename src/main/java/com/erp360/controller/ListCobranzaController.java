package com.erp360.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.erp360.dao.CobranzaDao;
import com.erp360.model.Cobranza;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@ManagedBean(name = "listPagoController")
@ViewScoped
public class ListCobranzaController implements Serializable {

	private static final long serialVersionUID = -1012155707040198783L;

	//DAO
	private @Inject CobranzaDao cobranzaDao;
	
	//STATE
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;

	//OBJECT
	private Cobranza cobranza;

	//LIST
	private List<Cobranza> listCobranza;

	//VAR

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login

	@PostConstruct
	public void init() {

		System.out.println(" ... init New Cobranzas...");
		loadDefault();
	}
	
	public void loadDefault(){
		modificar = false;
		registrar = false;
		crear = true;
		
		cobranza = new Cobranza();
		listCobranza = cobranzaDao.obtenerOrdenAscPorId();
	}

	// ------- action & event ------

	public void actionNuevo(){
		crear = false;
		modificar = false;
		registrar = true;
	}

	public void actionModificar(){
		crear = false;
		modificar = true;
		registrar = false;
	}

	public void onRowSelect(SelectEvent event) {
		crear = false;
		registrar = false;
		modificar = true;
	}

	// ------- procesos transaccional ------

	
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

	public boolean isRegistrar() {
		return registrar;
	}

	public void setRegistrar(boolean registrar) {
		this.registrar = registrar;
	}

	public List<Cobranza> getListCobranza() {
		return listCobranza;
	}

	public void setListCobranza(List<Cobranza> listCobranza) {
		this.listCobranza = listCobranza;
	}

	public Cobranza getCobranza() {
		return cobranza;
	}

	public void setCobranza(Cobranza cobranza) {
		this.cobranza = cobranza;
	}


}
