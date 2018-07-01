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

import com.erp360.dao.GrupoProductoDao;
import com.erp360.dao.LineaProductoDao;
import com.erp360.model.GrupoProducto;
import com.erp360.model.LineaProducto;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

@Named(value = "grupoProductoController")
@ConversationScoped
public class GrupoProductoController implements Serializable {

	private static final long serialVersionUID = 4656764987882579263L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";
	
	@Inject Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject GrupoProductoDao grupoProductoDao;
	private @Inject LineaProductoDao lineaProductoDao;

	//STATE
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;

	//OBJECT
	private GrupoProducto selectedGrupoProducto;
	private GrupoProducto newGrupoProducto;

	//LIST
	private List<GrupoProducto> listaGrupoProducto;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;

	@PostConstruct
	public void init() {
		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		loadDefault();	
	}

	public void loadDefault(){
		newGrupoProducto = new GrupoProducto();
		newGrupoProducto.setEstado("AC");
		newGrupoProducto.setCodigo("GP"+String.format("%06d",grupoProductoDao.correlativoGrupoProducto()));
		newGrupoProducto.setFechaRegistro(new Date());
		newGrupoProducto.setUsuarioRegistro(usuarioSession);

		selectedGrupoProducto = new GrupoProducto();

		// traer todos las Productoes ordenados por Fecha Desc
		listaGrupoProducto = grupoProductoDao.obtenerTodosOrdenFecha();

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

	public void onRowSelect(SelectEvent event) {
		newGrupoProducto = selectedGrupoProducto;
		crear = false;
		registrar = false;
		modificar = true;
	}
	
	public void actionButtonNuevo(){
		modificar = false;
		registrar = true;
		crear = false;
//
//		newGrupoProducto = new GrupoProducto();
//		newGrupoProducto.setEstado("AC");
//		newGrupoProducto.setFechaRegistro(new Date());
//		newGrupoProducto.setUsuarioRegistro(usuarioSession);
	}

	public void registrarGrupoProducto() {
		System.out.println("Ingreso a registrarProducto: ");
		if(newGrupoProducto.getNombre().isEmpty() || newGrupoProducto.getCodigo().isEmpty() || newGrupoProducto.getDescripcion().isEmpty() ){
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		//quitar espacios al inicio y al final
		newGrupoProducto.setNombre(newGrupoProducto.getNombre().trim());
		newGrupoProducto.setDescripcion(newGrupoProducto.getDescripcion().trim());
		//-------------------------------------
		newGrupoProducto.setUsuarioRegistro(usuarioSession);
		newGrupoProducto.setFechaRegistro(new Date());
		GrupoProducto data = grupoProductoDao.registrar(newGrupoProducto);
		if(data != null){
			loadDefault();
		}
	}

	public void modificarGrupoProducto() {
		System.out.println("Ingreso a registrarProducto: ");
		if(newGrupoProducto.getNombre().isEmpty() || newGrupoProducto.getCodigo().isEmpty() || newGrupoProducto.getDescripcion().isEmpty() ){
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		boolean sw = grupoProductoDao.modificar(newGrupoProducto);
		if(sw){
			loadDefault();
		}
	}
	
	public void eliminarGrupoProducto() {
		System.out.println("Ingreso a eliminarGrupoProducto: ");
		if(newGrupoProducto.getNombre().isEmpty() || newGrupoProducto.getCodigo().isEmpty() || newGrupoProducto.getDescripcion().isEmpty() ){
			FacesUtil.infoMessage("Verificación", "No puede haber campos vacios");
			return;
		}
		List<LineaProducto> lp = lineaProductoDao.obtenerTodosPorGrupoProducto(newGrupoProducto);
		if(lp.size()>=1){
			FacesUtil.infoMessage("No se Puede Eliminar", "Este grupo esta asociado a una línea");
			return;
		}
		boolean sw = grupoProductoDao.eliminar(newGrupoProducto);
		if(sw){
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

	public GrupoProducto getSelectedGrupoProducto() {
		return selectedGrupoProducto;
	}

	public void setSelectedGrupoProducto(GrupoProducto selectedGrupoProducto) {
		this.selectedGrupoProducto = selectedGrupoProducto;
	}

	public GrupoProducto getNewGrupoProducto() {
		return newGrupoProducto;
	}

	public void setNewGrupoProducto(GrupoProducto newGrupoProducto) {
		this.newGrupoProducto = newGrupoProducto;
	}

	public List<GrupoProducto> getListaGrupoProducto() {
		return listaGrupoProducto;
	}

	public void setListaGrupoProducto(List<GrupoProducto> listaGrupoProducto) {
		this.listaGrupoProducto = listaGrupoProducto;
	}

}
