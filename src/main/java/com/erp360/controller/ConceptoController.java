package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import com.erp360.interfaces.IConceptoDao;
import com.erp360.interfaces.ITipoConceptoDao;
import com.erp360.model.Concepto;
import com.erp360.model.TipoConcepto;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author david
 *
 */
@ManagedBean(name = "conceptoController")
@ViewScoped
public class ConceptoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3756541979319996136L;

	// DAO
	private @Inject IConceptoDao conceptoDao;

	// STATE
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;

	// VAR

	// OBJECT
	private Concepto selectedConcepto;
	private Concepto newConcepto;

	// LIST
	private List<Concepto> listaConcepto;

	public static List<TipoConcepto> tipoConceptos = new ArrayList<TipoConcepto>();

	// SESSION
	private @Inject SessionMain sessionMain; // variable del login
	private @Inject ITipoConceptoDao tipoConceptoDao;
	private String usuarioSession;

	
	public List<Concepto> getListaConcepto() {
		return listaConcepto;
	}

	@PostConstruct
	public void init() {
		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		loadDefault();
	}

	public void loadDefault() {
		newConcepto = new Concepto();
		selectedConcepto = new Concepto();

		// traer todos las Concepto ordenados por ID Desc
		listaConcepto = conceptoDao.obtenerPorEmpresa(sessionMain
				.getEmpresaLogin());

		modificar = false;
		registrar = false;
		crear = true;
	}

	public void actionButtonNuevo() {
		modificar = false;
		registrar = true;
		crear = false;
	}

	// SELECT PRESENTACION CLICK
	public void onRowSelectConceptoClick(SelectEvent event) {
		newConcepto = selectedConcepto;
		crear = false;
		registrar = false;
		modificar = true;
	}

	public void registrarConcepto() {
		System.out.println("Ingreso a registrarConcepto: ");
		if (newConcepto.getConcepto().isEmpty()) {
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		newConcepto.setEstado("AC");
		newConcepto.setFechaRegistro(new Date());
		newConcepto.setUsuarioRegistro(usuarioSession);
		newConcepto.setEmpresa(sessionMain.getEmpresaLogin());
		Concepto data = conceptoDao.registrar(newConcepto);
		if (data != null) {
			loadDefault();
		}

	}

	public void modificarConcepto() {
		System.out.println("Ingreso a modificarConcepto: ");
		if (newConcepto.getConcepto().isEmpty()) {
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		newConcepto.setEmpresa(sessionMain.getEmpresaLogin());
		boolean data = conceptoDao.modificar(newConcepto);
		if (data == true) {
			loadDefault();
		}
	}

	public void eliminarConcepto() {
		System.out.println("Ingreso a modificarConcepto: ");
		if (newConcepto.getConcepto().isEmpty()) {
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		boolean data = conceptoDao.eliminar(newConcepto);
		if (data == true) {
			loadDefault();
		}
	}

	public List<TipoConcepto> onCompleteTipoConcepto(String query) {
		// ystem.out.println("Entro en Oncomplete Caja"+ query);
		tipoConceptos = tipoConceptoDao.RetornarOnCompletePorEmpresa(
				sessionMain.getEmpresaLogin(), query.toUpperCase());
		return tipoConceptos;
	}

	// ACTION

	public void onSelectTipoConcepto(SelectEvent event) {
		newConcepto.setTipoConcepto((TipoConcepto) event.getObject());
	}

	// get and set
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

	public Concepto getNewConcepto() {
		return newConcepto;
	}

	public void setNewConcepto(Concepto newConcepto) {
		this.newConcepto = newConcepto;
	}

	public Concepto getSelectedConcepto() {
		return selectedConcepto;
	}

	public void setSelectedConcepto(Concepto selectedConcepto) {
		this.selectedConcepto = selectedConcepto;
	}

	public static List<TipoConcepto> getTipoConceptos() {
		return tipoConceptos;
	}

	public static void setTipoConceptos(List<TipoConcepto> tipoConceptos) {
		ConceptoController.tipoConceptos = tipoConceptos;
	}

}
