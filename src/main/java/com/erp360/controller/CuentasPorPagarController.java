package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.erp360.dao.ProveedorDao;
import com.erp360.model.CuentaPagar;
import com.erp360.model.Proveedor;
import com.erp360.util.FacesUtil;
import com.erp360.util.NumberUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "cuentasPorPagarController")
@ViewScoped
public class CuentasPorPagarController implements Serializable {

	private static final long serialVersionUID = -7148739425514986109L;

	//DAO
	private @Inject SessionMain sessionMain; //variable del login
	private @Inject ProveedorDao proveedorDao; 

	//estados
	private boolean crear ;
	private boolean registrar ;
	private boolean modificar ;

	//VARIABLES
	private String nombreEstado="ACTIVO";

	//OBJECT
	private Proveedor seleProveedor;

	//LIST
	private List<CuentaPagar> cuentaPagars;
	private List<Proveedor> proveedors;

	//MAP

	@PostConstruct
	public void init() {
		System.out.println(" init new initNewCliente");
		loadDefault();
	}

	private void loadDefault(){
		crear = true;
		registrar = false;
		modificar = false;

		seleProveedor = new Proveedor();
		cuentaPagars = new ArrayList<>();
		proveedors = new ArrayList<>();
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

	// ONCOMPLETETEXT Cliente
	public List<Proveedor> completeProveedor(String query) {
		proveedors = new ArrayList<Proveedor>();
		boolean sw = NumberUtil.isNumeric(query);
		if(sw){
			//proveedors = proveedors.obtenerTodosPorNitCi(query);
		}else{
			//proveedors = proveedors.obtenerTodosPorNombresApeliidosRazonSocial(query.toUpperCase());
		}
		return proveedors;
	}

	public void onRowSelectProveedorClick(SelectEvent event) {
		Proveedor proveedor = (Proveedor) event.getObject();
		for(Proveedor p : proveedors){
			if(p.getId().equals(proveedor.getId())){
				seleProveedor = p;
				//selectedCliente.setNombres(selectedCliente.getNombres()+" ");
				//listKardexCliente = kardexClienteDao.obtenerKardexPorCliente(selectedCliente);
				return;
			}
		}
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

	public List<CuentaPagar> getCuentaPagars() {
		return cuentaPagars;
	}

	public void setCuentaPagars(List<CuentaPagar> cuentaPagars) {
		this.cuentaPagars = cuentaPagars;
	}

	public List<Proveedor> getProveedors() {
		return proveedors;
	}

	public void setProveedors(List<Proveedor> proveedors) {
		this.proveedors = proveedors;
	}

	public Proveedor getSeleProveedor() {
		return seleProveedor;
	}

	public void setSeleProveedor(Proveedor seleProveedor) {
		this.seleProveedor = seleProveedor;
	}
}

