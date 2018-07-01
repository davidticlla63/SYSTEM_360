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

import com.erp360.dao.OrdenTraspasoDao;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.OrdenTraspaso;
import com.erp360.model.Usuario;
import com.erp360.util.PageUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Named(value = "listOrdenTraspasoController")
@ConversationScoped
public class ListOrdenTraspasoController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject OrdenTraspasoDao ordenTraspasoDao;

	//STATE
	private boolean modificar ;

	//OBJECT
	private OrdenTraspaso selectedOrdenTraspaso;

	//LIST
	private List<OrdenTraspaso> listaOrdenTraspaso;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Usuario usuarioSession;
	private Gestion gestionSesion;
	private Empresa empresaLogin;

	@PostConstruct
	public void init() {
		System.out.println(" ... initNewOrdenSalida ...");
		sessionMain.setAttributeSession("pIdOrdenTraspaso", null);//clear atribute
		usuarioSession = sessionMain.getUsuarioLogin();
		gestionSesion = sessionMain.getGestionLogin();
		empresaLogin = sessionMain.getEmpresaLogin();
		loadDefault();
	}

	public void loadDefault(){

		selectedOrdenTraspaso = new OrdenTraspaso();
		modificar = false;
		
		listaOrdenTraspaso = ordenTraspasoDao.findAllOrderedByIDGestion(gestionSesion);
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
		sessionMain.setAttributeSession("pIdOrdenTraspaso", String.valueOf(selectedOrdenTraspaso.getId()));
		String page = "pages/proceso/orden_ingreso.xhtml";
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

	public OrdenTraspaso getSelectedOrdenTraspaso() {
		return selectedOrdenTraspaso;
	}

	public void setSelectedOrdenTraspaso(OrdenTraspaso selectedOrdenTraspaso) {
		this.selectedOrdenTraspaso = selectedOrdenTraspaso;
	}

	public List<OrdenTraspaso> getListaOrdenTraspaso() {
		return listaOrdenTraspaso;
	}

	public void setListaOrdenTraspaso(List<OrdenTraspaso> listaOrdenTraspaso) {
		this.listaOrdenTraspaso = listaOrdenTraspaso;
	}

}
