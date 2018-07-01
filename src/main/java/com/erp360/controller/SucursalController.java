package com.erp360.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import com.erp360.model.Empresa;
import com.erp360.model.Sucursal;
import com.erp360.dao.SucursalDao;
import com.erp360.util.SessionMain;

@Named(value = "sucursalController")
@ConversationScoped
public class SucursalController implements Serializable {

	private static final long serialVersionUID = -3542057490304209261L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	private @Inject SucursalDao sucursalDao;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//STATE
	private boolean crear;
	private boolean registrar;
	private boolean modificar;
	private boolean seleccionadaDosificacion;
	private boolean estadoButtonDialog;

	//VAR
	private String nombreEstado="ACTIVO";

	//LIST
	private List<Sucursal> listSucursal;

	//OBJECT
	private Sucursal newSucursal;
	private Sucursal selectedSucursal;

	//SESSION
	private String nombreUsuario;	
	private @Inject SessionMain sessionMain; //variable del login
	private Empresa empresaLogin;

	@PostConstruct
	public void initNewSucursal() {
		nombreUsuario = sessionMain.getUsuarioLogin().getLogin();
		empresaLogin = sessionMain.getEmpresaLogin();

		loadDefault();
	}

	public void loadDefault(){
		crear = true;
		registrar = false; 
		modificar = false;
		seleccionadaDosificacion = false;
		estadoButtonDialog = true;

		newSucursal = new Sucursal();
		selectedSucursal = new Sucursal();

		// traer todos las sucursales
		listSucursal = sucursalDao.obtenerPorEmpresa(empresaLogin);
	} 

	public void initConversation() {
		if (!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient()) {
			conversation.begin();
			System.out.println(">>>>>>>>>> CONVERSACION INICIADA...");
		}
	}

	public String endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
			System.out.println(">>>>>>>>>> CONVERSACION TERMINADA...");
		}
		return "orden_ingreso.xhtml?faces-redirect=true";
	}
	//-----  metodos sucursal ---------------

	public void registrarSucursal(){
		String estado = nombreEstado.equals("ACTIVO")?"AC":"IN";
		newSucursal.setEstado(estado);
		newSucursal.setEmpresa(empresaLogin);
		newSucursal.setUsuarioRegistro(nombreUsuario);
		newSucursal.setFechaRegistro(new Date());
		Sucursal s = sucursalDao.registrar(newSucursal);
		if(s!=null){
			loadDefault();
		}
	}

	public void modificarSucursal(){
		String estado = nombreEstado.equals("ACTIVO")?"AC":"IN";
		newSucursal.setEstado(estado);
		boolean sw = sucursalDao.modificar(newSucursal);
		if(sw){
			loadDefault();
		}

	}

	public void eliminarSucursal(){
		boolean sw = sucursalDao.eliminar(newSucursal);
		if(sw){
			loadDefault();
		}
	}

	public void onRowSelectSucursal(SelectEvent event) {
		crear = false;
		modificar = true;
		registrar = false ;
		newSucursal = selectedSucursal;
	}

	//--------  acciones para la vista----------

	public void cambiarAspecto(){
		crear = false;
		registrar = true;
		modificar = false;

	}

	// -------- get and set---------------------

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public Sucursal getSelectedSucursal() {
		return selectedSucursal;
	}

	public void setSelectedSucursal(Sucursal selectedSucursal) {
		this.selectedSucursal = selectedSucursal;
	}

	public String getTest(){
		return "test";
	}

	public Empresa getEmpresaLogin() {
		return empresaLogin;
	}

	public void setEmpresaLogin(Empresa empresaLogin) {
		this.empresaLogin = empresaLogin;
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

	public boolean isSeleccionadaDosificacion() {
		return seleccionadaDosificacion;
	}

	public void setSeleccionadaDosificacion(boolean seleccionadaDosificacion) {
		this.seleccionadaDosificacion = seleccionadaDosificacion;
	}

	public Sucursal getNewSucursal() {
		return newSucursal;
	}

	public void setNewSucursal(Sucursal newSucursal) {
		this.newSucursal = newSucursal;
	}

	public List<Sucursal> getListSucursal() {
		return listSucursal;
	}

	public void setListSucursal(List<Sucursal> listSucursal) {
		this.listSucursal = listSucursal;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public boolean isEstadoButtonDialog() {
		return estadoButtonDialog;
	}

	public void setEstadoButtonDialog(boolean estadoButtonDialog) {
		this.estadoButtonDialog = estadoButtonDialog;
	} 

}
