package com.erp360.controller;

import java.io.Serializable;
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

import com.erp360.dao.CobranzaDao;
import com.erp360.dao.GestionDao;
import com.erp360.model.Cobranza;
import com.erp360.model.Gestion;
import com.erp360.model.Usuario;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@Named(value = "gestionController")
@ConversationScoped
public class GestionController implements Serializable {

	private static final long serialVersionUID = -1012155707040198783L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject GestionDao gestionDao;
	
	//STATE
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;

	//OBJECT
	private Gestion selectedGestion;

	//LIST
	private List<Gestion> listGestion;

	//VAR

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Usuario usuarioLogin;

	@PostConstruct
	public void init() {

		System.out.println(" ... init New ...");

		usuarioLogin = sessionMain.getUsuarioLogin();
		loadDefault();
	}
	
	public void loadDefault(){
		modificar = false;
		registrar = false;
		crear = true;
		
		selectedGestion = new Gestion();
		listGestion = gestionDao.obtenerOrdenAscPorId();
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
		return "page.xhtml?faces-redirect=true";
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

	public List<Gestion> getListGestion() {
		return listGestion;
	}

	public void setListGestion(List<Gestion> listGestion) {
		this.listGestion = listGestion;
	}

	public Gestion getSelectedGestion() {
		return selectedGestion;
	}

	public void setSelectedGestion(Gestion selectedGestion) {
		this.selectedGestion = selectedGestion;
	}


}
