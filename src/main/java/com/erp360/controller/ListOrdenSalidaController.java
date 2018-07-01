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

import com.erp360.dao.OrdenSalidaDao;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.OrdenSalida;
import com.erp360.model.Usuario;
import com.erp360.util.PageUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Named(value = "listOrdenSalidaController")
@ConversationScoped
public class ListOrdenSalidaController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject OrdenSalidaDao ordenSalidaDao;

	//STATE
	private boolean modificar ;

	//OBJECT
	private OrdenSalida selectedOrdenSalida;

	//LIST
	private List<OrdenSalida> listaOrdenSalida;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Usuario usuarioSession;
	private Gestion gestionSesion;
	private Empresa empresaLogin;

	@PostConstruct
	public void init() {
		System.out.println(" ... initNewOrdenIngreso ...");
		sessionMain.setAttributeSession("pIdOrdenSalida", null);//clear atribute
		usuarioSession = sessionMain.getUsuarioLogin();
		gestionSesion = sessionMain.getGestionLogin();
		empresaLogin = sessionMain.getEmpresaLogin();
		loadDefault();
	}

	public void loadDefault(){

		selectedOrdenSalida = new OrdenSalida();
		modificar = false;
		
		listaOrdenSalida = ordenSalidaDao.findAllOrderedByIDGestion(gestionSesion);
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

	// ------- action & event ------
	public void actionModificar(){
		sessionMain.setAttributeSession("pIdOrdenSalida", String.valueOf(selectedOrdenSalida.getId()));
		String page = "pages/proceso/orden_salida.xhtml";
		PageUtil.cargarPagina(page);
	}

	public void onRowSelect(SelectEvent event) {
		modificar = true;
	}


	// -------- get and set -------
	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public OrdenSalida getSelectedOrdenSalida() {
		return selectedOrdenSalida;
	}

	public void setSelectedOrdenSalida(OrdenSalida selectedOrdenSalida) {
		this.selectedOrdenSalida = selectedOrdenSalida;
	}

	public List<OrdenSalida> getListaOrdenSalida() {
		return listaOrdenSalida;
	}

	public void setListaOrdenSalida(List<OrdenSalida> listaOrdenSalida) {
		this.listaOrdenSalida = listaOrdenSalida;
	}

}
