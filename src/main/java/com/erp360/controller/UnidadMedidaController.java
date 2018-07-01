package com.erp360.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import com.erp360.dao.UnidadMedidaDao;
import com.erp360.model.UnidadMedida;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Named(value = "unidadMedidaController")
@ConversationScoped
public class UnidadMedidaController implements Serializable {

	private static final long serialVersionUID = 651748306747641264L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	@Inject
	Conversation conversation;

	//DAO
	private @Inject UnidadMedidaDao unidadMedidaDao;

	//STATE
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;

	//VAR

	//OBJECT
	private UnidadMedida selectedUnidadMedida;
	private UnidadMedida newUnidadMedida;

	//LIST
	private List<UnidadMedida> listaUnidadMedida;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;

	@Produces
	@Named
	public List<UnidadMedida> getListaUnidadMedida() {
		return listaUnidadMedida;
	}

	@PostConstruct
	public void init() {
		usuarioSession = sessionMain.getUsuarioLogin().getLogin();

		loadDefault();
	}

	public void loadDefault(){
		newUnidadMedida = new UnidadMedida();
		selectedUnidadMedida = new UnidadMedida();

		// traer todos las UnidadMedida ordenados por ID Desc
		listaUnidadMedida = unidadMedidaDao.findAllActivosOrderedByID();

		modificar = false;
		registrar = false;
		crear = true;
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

	public void actionButtonNuevo(){
		modificar = false;
		registrar = true;
		crear = false;
	}
	
	// SELECT PRESENTACION CLICK
	public void onRowSelectUnidadMedidaClick(SelectEvent event) {
		newUnidadMedida = selectedUnidadMedida;
		crear = false;
		registrar = false;
		modificar = true;
	}

	public void registrarUnidadMedida() {
		System.out.println("Ingreso a registrarUnidadMedida: ");
		if(newUnidadMedida.getNombre().isEmpty() || newUnidadMedida.getDescripcion().isEmpty()){
			FacesUtil.infoMessage("VALIDACION","No puede haber campos vacios");
			return;
		}
		newUnidadMedida.setEstado("AC");
		newUnidadMedida.setFechaRegistro(new Date());
		newUnidadMedida.setUsuarioRegistro(usuarioSession);
		UnidadMedida data = unidadMedidaDao.registrar(newUnidadMedida);
		if(data != null){
			loadDefault();
		}

	}

	public void modificarUnidadMedida() {
		System.out.println("Ingreso a modificarUnidadMedida: ");
		if(newUnidadMedida.getNombre().isEmpty() || newUnidadMedida.getDescripcion().isEmpty()){
			FacesUtil.infoMessage("VALIDACION","No puede haber campos vacios");
			return;
		}
		UnidadMedida data = unidadMedidaDao.modificar(newUnidadMedida);
		if(data != null){
			loadDefault();
		}
	}

	public void eliminarUnidadMedida() {
		System.out.println("Ingreso a modificarUnidadMedida: ");
		if(newUnidadMedida.getNombre().isEmpty() || newUnidadMedida.getDescripcion().isEmpty()){
			FacesUtil.infoMessage("VALIDACION","No puede haber campos vacios");
			return;
		}
		UnidadMedida data = unidadMedidaDao.eliminar(newUnidadMedida);
		if(data != null){
			loadDefault();
		}
	}

	//get and set
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

	public UnidadMedida getNewUnidadMedida() {
		return newUnidadMedida;
	}

	public void setNewUnidadMedida(UnidadMedida newUnidadMedida) {
		this.newUnidadMedida = newUnidadMedida;
	}

	public UnidadMedida getSelectedUnidadMedida() {
		return selectedUnidadMedida;
	}

	public void setSelectedUnidadMedida(UnidadMedida selectedUnidadMedida) {
		this.selectedUnidadMedida = selectedUnidadMedida;
	}

}
