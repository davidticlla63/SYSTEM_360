package com.erp360.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import com.erp360.interfaces.ITipoConceptoDao;
import com.erp360.model.TipoConcepto;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author david
 *
 */
@ManagedBean(name = "tipoConceptoController")
@ViewScoped
public class TipoConceptoController implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7176528419934680867L;

	// DAO
	private @Inject ITipoConceptoDao tipoTipoConceptoDao;

	// STATE
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;

	// VAR

	// OBJECT
	private TipoConcepto selectedTipoConcepto;
	private TipoConcepto newTipoConcepto;

	// LIST
	private List<TipoConcepto> listaTipoConcepto;

	// SESSION
	private @Inject SessionMain sessionMain; // variable del login
	private String usuarioSession;

	@Produces
	@Named
	public List<TipoConcepto> getListaTipoConcepto() {
		return listaTipoConcepto;
	}

	@PostConstruct
	public void init() {
		usuarioSession = sessionMain.getUsuarioLogin().getLogin();

		loadDefault();
	}

	public void loadDefault() {
		newTipoConcepto = new TipoConcepto();
		selectedTipoConcepto = new TipoConcepto();

		// traer todos las TipoConcepto ordenados por ID Desc
		listaTipoConcepto = tipoTipoConceptoDao.obtenerPorEmpresa(sessionMain
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
	public void onRowSelectTipoConceptoClick(SelectEvent event) {
		newTipoConcepto = selectedTipoConcepto;
		crear = false;
		registrar = false;
		modificar = true;
	}

	public void registrarTipoConcepto() {
		System.out.println("Ingreso a registrarTipoConcepto: ");
		if (newTipoConcepto.getNombre().isEmpty()) {
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		newTipoConcepto.setEstado("AC");
		newTipoConcepto.setFechaRegistro(new Date());
		newTipoConcepto.setUsuarioRegistro(usuarioSession);
		newTipoConcepto.setEmpresa(sessionMain
				.getEmpresaLogin());
		TipoConcepto data = tipoTipoConceptoDao.registrar(newTipoConcepto);
		if (data != null) {
			loadDefault();
		}

	}

	public void modificarTipoConcepto() {
		System.out.println("Ingreso a modificarTipoConcepto: ");
		if (newTipoConcepto.getNombre().isEmpty()) {
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		newTipoConcepto.setEmpresa(sessionMain
				.getEmpresaLogin());
		boolean data = tipoTipoConceptoDao.modificar(newTipoConcepto);
		if (data == true) {
			loadDefault();
		}
	}

	public void eliminarTipoConcepto() {
		System.out.println("Ingreso a modificarTipoConcepto: ");
		if (newTipoConcepto.getNombre().isEmpty()) {
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		boolean data = tipoTipoConceptoDao.eliminar(newTipoConcepto);
		if (data == true) {
			loadDefault();
		}
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

	public TipoConcepto getNewTipoConcepto() {
		return newTipoConcepto;
	}

	public void setNewTipoConcepto(TipoConcepto newTipoConcepto) {
		this.newTipoConcepto = newTipoConcepto;
	}

	public TipoConcepto getSelectedTipoConcepto() {
		return selectedTipoConcepto;
	}

	public void setSelectedTipoConcepto(TipoConcepto selectedTipoConcepto) {
		this.selectedTipoConcepto = selectedTipoConcepto;
	}

}
