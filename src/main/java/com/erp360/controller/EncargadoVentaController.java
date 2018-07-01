package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.erp360.dao.EncargadoVentaDao;
import com.erp360.model.EncargadoVenta;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "encargadoVentaController")
@ViewScoped
public class EncargadoVentaController implements Serializable {

	private static final long serialVersionUID = -7148739425514986109L;

	//DAO
	private @Inject SessionMain sessionMain; //variable del login
	private @Inject EncargadoVentaDao encargadoVentaDao;

	//estados
	private boolean crear ;
	private boolean registrar ;
	private boolean modificar ;

	//VARIABLES
	private String nombreEstado="ACTIVO";

	//OBJECT
	private EncargadoVenta newEncargadoVenta;
	private EncargadoVenta selectedEncargadoVenta;

	//LISTAS
	private List<EncargadoVenta> listEncargadoVenta = new ArrayList<EncargadoVenta>();
	private String[] listEstado = {"ACTIVO","INACTIVO"};

	@PostConstruct
	public void init() {
		System.out.println(" init new init New EncargadoVenta");
		loadDefault();
	}

	private void loadDefault(){

		crear = true;
		registrar = false;
		modificar = false;

		newEncargadoVenta = new EncargadoVenta();
		selectedEncargadoVenta = new EncargadoVenta();
		listEncargadoVenta = encargadoVentaDao.obtenerTodosOrdenadosPorId();
	}

	// -- event action

	// --- PROCESS
	public void registrar(){
		String estado = nombreEstado.equals("ACTIVO")?"AC":"IN";
		Date fechaActual = new  Date();
		newEncargadoVenta.setEstado(estado);
		newEncargadoVenta.setFechaRegistro(fechaActual);
		newEncargadoVenta.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		newEncargadoVenta.setEmpresa(sessionMain.getEmpresaLogin());
		EncargadoVenta c = encargadoVentaDao.registrar(newEncargadoVenta);
		if(c!=null){
			loadDefault();
		}
	}

	public void loadDialogCuenta(){
		FacesUtil.showDialog("dlgPlanCuenta");	
	}

	///------

	public void modificar(){
		String estado = nombreEstado.equals("ACTIVO")?"AC":"IN";
		Date fechaActual = new  Date();
		newEncargadoVenta.setEstado(estado);
		newEncargadoVenta.setFechaRegistro(fechaActual);
		newEncargadoVenta.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		newEncargadoVenta.setEmpresa(sessionMain.getEmpresaLogin());
		boolean sw = encargadoVentaDao.modificar(newEncargadoVenta);
		if(sw){
			loadDefault();
		}
	}

	public void eliminar(){
		boolean sw = encargadoVentaDao.eliminar(newEncargadoVenta);
		if(sw){
			loadDefault();
		}
	}

	public void actualizarFormReg(){
		crear = true;
		registrar = false;
		modificar = false;
		newEncargadoVenta = new EncargadoVenta();	
		selectedEncargadoVenta = new EncargadoVenta();
	}

	public void cambiarAspecto(){
		crear = false;
		registrar = true;
		modificar = false;
	}

	public void onRowSelect(SelectEvent event) {
		newEncargadoVenta = new EncargadoVenta();
		newEncargadoVenta = selectedEncargadoVenta;
		
		nombreEstado = newEncargadoVenta.getEstado().equals("AC")?"ACTIVO":"INACTIVO";
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

	public String getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	public String[] getListEstado() {
		return listEstado;
	}

	public void setListEstado(String[] listEstado) {
		this.listEstado = listEstado;
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

	public EncargadoVenta getNewEncargadoVenta() {
		return newEncargadoVenta;
	}

	public void setNewEncargadoVenta(EncargadoVenta newEncargadoVenta) {
		this.newEncargadoVenta = newEncargadoVenta;
	}

	public EncargadoVenta getSelectedEncargadoVenta() {
		return selectedEncargadoVenta;
	}

	public void setSelectedEncargadoVenta(EncargadoVenta selectedEncargadoVenta) {
		this.selectedEncargadoVenta = selectedEncargadoVenta;
	}

	public List<EncargadoVenta> getListEncargadoVenta() {
		return listEncargadoVenta;
	}

	public void setListEncargadoVenta(List<EncargadoVenta> listEncargadoVenta) {
		this.listEncargadoVenta = listEncargadoVenta;
	}
}

