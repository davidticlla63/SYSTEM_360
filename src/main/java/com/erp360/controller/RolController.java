package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import com.erp360.dao.RolDao;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.Usuario;
import com.erp360.model.Roles;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

@Named(value = "rolController")
@ConversationScoped
public class RolController implements Serializable {

	private static final long serialVersionUID = 1730442750062837853L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject RolDao rolDao;

	private @Inject SessionMain sessionMain; //variable del login
	private String nombreUsuario;	
	private Empresa empresaLogin;
	private Gestion gestionLogin;

	private boolean crear = true;
	private boolean registrar = false;
	private boolean modificar = false;

	private String tituloPanel = "Registrar Roles";
	private String tipoColumnTable; //8
	private String nombreEstado="ACTIVO";

	private Roles newRol;
	private Roles selectedRol;
	private List<Roles> listRol = new ArrayList<Roles>();
	private List<Roles> listFilterRol = new ArrayList<Roles>();

	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private String[] listEstado = {"ACTIVO","INACTIVO"};

	@PostConstruct
	public void initNewRoles() {
		beginConversation();
		nombreUsuario = sessionMain.getUsuarioLogin().getLogin();
		empresaLogin = sessionMain.getEmpresaLogin();
		gestionLogin = sessionMain.getGestionLogin();

		loadDefault();
	}

	private void loadDefault(){
		tipoColumnTable = "col-md-12";
		newRol = new Roles();
		selectedRol = new Roles();
		listRol = rolDao.obtenerOrdenAscPorId();
		modificar = false;
		crear = true;
		registrar = false;
	}

	public void beginConversation() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	public void endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
	}
	public void resetearFitrosTabla(String id) {
		DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
		table.setSelection(null);
		table.reset();
	}

	public void registrarRol() {
		if(newRol.getNombre().isEmpty() || newRol.getDescripcion().isEmpty() ){
			FacesUtil.infoMessage("VALIDACION", "No pueden haber campos vacios.");
			resetearFitrosTabla("formTableRoles:dataTableRoles");
			return;
		}
		newRol.setEstado(nombreEstado.equals("ACTIVO")?"AC":"IN");
		newRol.setFechaRegistro(new Date());
		newRol.setUsuarioRegistro(nombreUsuario);
		boolean sw = rolDao.registar(newRol);
		if(sw){
			resetearFitrosTabla("formTableRoles:dataTableRoles");
			loadDefault();
		}
	}

	public void modificarRol() {
		newRol.setEstado(nombreEstado.equals("ACTIVO")?"AC":"IN");
		newRol.setFechaModificacion(new Date());
		boolean sw = rolDao.modificar(newRol);
		if(sw){
			resetearFitrosTabla("formTableRoles:dataTableRoles");
			loadDefault();
		}
	}

	public void eliminarRol() {
		newRol.setEstado(nombreEstado.equals("ACTIVO")?"AC":"IN");
		newRol.setFechaModificacion(new Date());
		boolean sw = rolDao.modificar(newRol);
		if(sw){
			resetearFitrosTabla("formTableRoles:dataTableRoles");
			loadDefault();
		}
	}

	public void actualizarForm(){
		crear = true;
		registrar = false;
		modificar = false;
		tipoColumnTable = "col-md-12";
		selectedRol= new Roles();
		newRol = new Roles();
		resetearFitrosTabla("formTableRoles:dataTableRoles");
	}

	public void onRowSelect(SelectEvent event) {
		newRol = selectedRol;
		crear = false;
		registrar = false;
		modificar = true;
		tipoColumnTable = "col-md-8";
		//nombreEstado = newRol.getEstado().equals("AC")?"ACTIVO":"INACTIVO";
		resetearFitrosTabla("formTableRoles:dataTableRoles");
	}

	public void cambiarAspecto(){
		crear = false;
		registrar = true;
		modificar = false;
		tipoColumnTable = "col-md-8";
		selectedRol= new Roles();
		newRol = new Roles();
	}

	// -------------------- get and set -------------------
	public String getTituloPanel() {
		return tituloPanel;
	}

	public void setTituloPanel(String tituloPanel) {
		this.tituloPanel = tituloPanel;
	}

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public List<Usuario> getListUsuario() {
		return listUsuario;
	}

	public void setListUsuario(List<Usuario> listUsuario) {
		this.listUsuario = listUsuario;
	}

	public String getTest(){
		return "test";
	}

	public Roles getNewRol() {
		return newRol;
	}

	public void setNewRol(Roles newRol) {
		this.newRol = newRol;
	}

	public Roles getSelectedRol() {
		return selectedRol;
	}

	public void setSelectedRol(Roles selectedRol) {
		this.selectedRol = selectedRol;
	}

	public List<Roles> getListRol() {
		return listRol;
	}

	public void setListRol(List<Roles> listRol) {
		this.listRol = listRol;
	}

	public List<Roles> getListFilterRol() {
		return listFilterRol;
	}

	public void setListFilterRol(List<Roles> listFilterRol) {
		this.listFilterRol = listFilterRol;
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

	public String getTipoColumnTable() {
		return tipoColumnTable;
	}

	public void setTipoColumnTable(String tipoColumnTable) {
		this.tipoColumnTable = tipoColumnTable;
	}

	public String[] getListEstado() {
		return listEstado;
	}

	public void setListEstado(String[] listEstado) {
		this.listEstado = listEstado;
	}

	public String getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}


}
