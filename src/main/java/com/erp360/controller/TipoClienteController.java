package com.erp360.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.erp360.interfaces.ITipoClienteDao;
import com.erp360.model.TipoCliente;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author david
 *
 */
@ManagedBean(name = "tipoClienteController")
@ViewScoped
public class TipoClienteController implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7176528419934680867L;

	// DAO
	private @Inject ITipoClienteDao tipoTipoClienteDao;

	// STATE
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;

	// VAR

	// OBJECT
	private TipoCliente selectedTipoCliente;
	private TipoCliente newTipoCliente;

	// LIST
	private List<TipoCliente> listaTipoCliente;

	// SESSION
	private @Inject SessionMain sessionMain; // variable del login
	private String usuarioSession;

	
	public List<TipoCliente> getListaTipoCliente() {
		return listaTipoCliente;
	}

	@PostConstruct
	public void init() {
		usuarioSession = sessionMain.getUsuarioLogin().getLogin();

		loadDefault();
	}

	public void loadDefault() {
		newTipoCliente = new TipoCliente();
		selectedTipoCliente = new TipoCliente();

		// traer todos las TipoCliente ordenados por ID Desc
		listaTipoCliente = tipoTipoClienteDao.obtenerPorEmpresa(sessionMain
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
	public void onRowSelectTipoClienteClick(SelectEvent event) {
		newTipoCliente = selectedTipoCliente;
		crear = false;
		registrar = false;
		modificar = true;
	}

	public void registrarTipoCliente() {
		System.out.println("Ingreso a registrarTipoCliente: ");
		if (newTipoCliente.getNombre().isEmpty()) {
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		newTipoCliente.setEstado("AC");
		newTipoCliente.setFechaRegistro(new Date());
		newTipoCliente.setUsuarioRegistro(usuarioSession);
		newTipoCliente.setEmpresa(sessionMain
				.getEmpresaLogin());
		TipoCliente data = tipoTipoClienteDao.registrar(newTipoCliente);
		if (data != null) {
			loadDefault();
		}

	}

	public void modificarTipoCliente() {
		System.out.println("Ingreso a modificarTipoCliente: ");
		if (newTipoCliente.getNombre().isEmpty()) {
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		newTipoCliente.setEmpresa(sessionMain
				.getEmpresaLogin());
		boolean data = tipoTipoClienteDao.modificar(newTipoCliente);
		if (data == true) {
			loadDefault();
		}
	}

	public void eliminarTipoCliente() {
		System.out.println("Ingreso a modificarTipoCliente: ");
		if (newTipoCliente.getNombre().isEmpty()) {
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		boolean data = tipoTipoClienteDao.eliminar(newTipoCliente);
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

	public TipoCliente getNewTipoCliente() {
		return newTipoCliente;
	}

	public void setNewTipoCliente(TipoCliente newTipoCliente) {
		this.newTipoCliente = newTipoCliente;
	}

	public TipoCliente getSelectedTipoCliente() {
		return selectedTipoCliente;
	}

	public void setSelectedTipoCliente(TipoCliente selectedTipoCliente) {
		this.selectedTipoCliente = selectedTipoCliente;
	}

}
