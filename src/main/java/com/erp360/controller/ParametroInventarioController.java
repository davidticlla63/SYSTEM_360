package com.erp360.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.cdi.push.Push;

import com.erp360.dao.ParametroInventarioDao;
import com.erp360.model.ParametroInventario;
import com.erp360.model.Usuario;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@Named(value = "parametroInventarioController")
@ConversationScoped
public class ParametroInventarioController implements Serializable {

	private static final long serialVersionUID = -1012155707040198783L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject ParametroInventarioDao parametroInventarioDao;
	
	//STATE
	private boolean modificar = false;

	//OBJECT
	private ParametroInventario selectedParametroInventario;

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
		selectedParametroInventario = parametroInventarioDao.obtenerParametroInventario();
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

	public void actionModificar(){
		modificar = true;
	}

	// ------- procesos transaccional ------
	
	public void modificarParametroInventario(){
		selectedParametroInventario.setFechaModificacion(new Date());
		selectedParametroInventario.setUsuarioRegistro(usuarioLogin.getLogin());
		boolean sw = parametroInventarioDao.modificar(selectedParametroInventario);
		if(sw){
			loadDefault();
		}
	}
	
	// -------- get and set -------

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public ParametroInventario getSelectedParametroInventario() {
		return selectedParametroInventario;
	}

	public void setSelectedParametroInventario(ParametroInventario selectedParametroInventario) {
		this.selectedParametroInventario = selectedParametroInventario;
	}


}
