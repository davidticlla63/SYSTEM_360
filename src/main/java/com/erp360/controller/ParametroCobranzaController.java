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

import com.erp360.dao.AlmacenDao;
import com.erp360.dao.ParametroCobranzaDao;
import com.erp360.model.Almacen;
import com.erp360.model.ParametroCobranza;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@Named(value = "parametroCobranzaController")
@ConversationScoped
public class ParametroCobranzaController implements Serializable {

	private static final long serialVersionUID = -1012155707040198783L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject ParametroCobranzaDao parametroCobranzaDao;

	//STATE
	private boolean modificar = false;

	//OBJECT
	private ParametroCobranza selectedParametroCobranza;

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
		selectedParametroCobranza = parametroCobranzaDao.obtenerParametroCobanza();
		//listAlmacen = almacenDao.obtenerTodosActivosOrdenadosPorId();
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

	public void modificarParametroCobranza(){
		selectedParametroCobranza.setFechaModificacion(new Date());
		selectedParametroCobranza.setUsuarioRegistro(usuarioLogin.getLogin());
		boolean sw = parametroCobranzaDao.modificar(selectedParametroCobranza);
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

	public ParametroCobranza getSelectedParametroCobranza() {
		return selectedParametroCobranza;
	}

	public void setSelectedParametroCobranza(ParametroCobranza selectedParametroCobranza) {
		this.selectedParametroCobranza = selectedParametroCobranza;
	}


}
