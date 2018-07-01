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
import com.erp360.dao.ProductoDao;
import com.erp360.model.GrupoProducto;
import com.erp360.model.LineaProducto;
import com.erp360.model.Producto;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

@Named(value = "lineaProductoController")
@ConversationScoped
public class LineaProductoController implements Serializable {

	private static final long serialVersionUID = 4656764987882579263L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject LineaProductoDao lineaProductoDao;
	private @Inject GrupoProductoDao grupoProductoDao;
	private @Inject ProductoDao productoDao;

	//STATE
	private boolean modificar;
	private boolean registrar;
	private boolean crear;

	//OBJECT
	private LineaProducto selectedLineaProducto;
	private LineaProducto newLineaProducto;

	//LIST
	private List<LineaProducto> listaLineaProducto;
	private List<GrupoProducto> listGrupoProducto;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;

	@PostConstruct
	public void init() {
		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		loadDefault();	
	}

	public void loadDefault(){

		listGrupoProducto = grupoProductoDao.obtenerTodosOrdenDescripcion();
		newLineaProducto = new LineaProducto();
		newLineaProducto.setEstado("AC");
		newLineaProducto.setCodigo("LP"+String.format("%06d",lineaProductoDao.correlativoLineaProducto()));
		newLineaProducto.setFechaRegistro(new Date());
		newLineaProducto.setUsuarioRegistro(usuarioSession);
		newLineaProducto.setGrupoProducto(listGrupoProducto.size()==0?null:listGrupoProducto.get(0));

		selectedLineaProducto = new LineaProducto();

		// traer todos las Productoes ordenados por ID Desc
		listaLineaProducto = lineaProductoDao.obtenerTodosOrdenFecha();

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
		newLineaProducto = selectedLineaProducto;
		crear = false;
		registrar = false;
		modificar = true;
	}

	public void actionButtonNuevo(){
		modificar = false;
		registrar = true;
		crear = false;

		//		newLineaProducto = new LineaProducto();
		//		newLineaProducto.setEstado("AC");
		//		newLineaProducto.setFechaRegistro(new Date());
		//		newLineaProducto.setUsuarioRegistro(usuarioSession);
	}

	public void registrarLineaProducto() {
		System.out.println("Ingreso a registrarProducto: ");
		if(newLineaProducto.getNombre().isEmpty() || newLineaProducto.getCodigo().isEmpty() || newLineaProducto.getDescripcion().isEmpty() ){
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		//quitar espacios al inicio y al final
		newLineaProducto.setNombre(newLineaProducto.getNombre().trim());
		newLineaProducto.setDescripcion(newLineaProducto.getDescripcion().trim());
		//-------------------------------------
		newLineaProducto.setUsuarioRegistro(usuarioSession);
		newLineaProducto.setFechaRegistro(new Date());
		LineaProducto data = lineaProductoDao.registrar(newLineaProducto);
		if(data != null){
			loadDefault();
		}
	}

	public void modificarLineaProducto() {
		if(newLineaProducto.getNombre().isEmpty() || newLineaProducto.getCodigo().isEmpty() || newLineaProducto.getDescripcion().isEmpty() ){
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		boolean sw = lineaProductoDao.modificar(newLineaProducto);
		if(sw){
			loadDefault();
		}
	}

	public void eliminarLineaProducto() {
		List<Producto> list = productoDao.obtenerTodosPorLineaProducto(newLineaProducto);
		if(list.size()>=0){
			FacesUtil.infoMessage("No se Puede Eliminar", "Esta Linea esta asociada a un Producto");
			return;
		}
		boolean sw = lineaProductoDao.eliminar(newLineaProducto);
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

	public LineaProducto getSelectedLineaProducto() {
		return selectedLineaProducto;
	}

	public void setSelectedLineaProducto(LineaProducto selectedLineaProducto) {
		this.selectedLineaProducto = selectedLineaProducto;
	}

	public LineaProducto getNewLineaProducto() {
		return newLineaProducto;
	}

	public void setNewLineaProducto(LineaProducto newLineaProducto) {
		this.newLineaProducto = newLineaProducto;
	}

	public List<LineaProducto> getListaLineaProducto() {
		return listaLineaProducto;
	}

	public void setListaLineaProducto(List<LineaProducto> listaLineaProducto) {
		this.listaLineaProducto = listaLineaProducto;
	}

	public List<GrupoProducto> getListGrupoProducto() {
		return listGrupoProducto;
	}

	public void setListGrupoProducto(List<GrupoProducto> listGrupoProducto) {
		this.listGrupoProducto = listGrupoProducto;
	}

}
