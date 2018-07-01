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

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import com.erp360.dao.AlmacenDao;
import com.erp360.dao.AlmacenEncargadoDao;
import com.erp360.dao.UsuarioDao;
import com.erp360.model.Almacen;
import com.erp360.model.AlmacenEncargado;
import com.erp360.model.Gestion;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Named(value = "almacenController")
@ConversationScoped
public class AlmacenController implements Serializable {

	private static final long serialVersionUID = -4296662022834297942L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject AlmacenDao almacenDao;
	private @Inject AlmacenEncargadoDao almacenEncargadoDao;
	private @Inject UsuarioDao usuarioDao;

	//STATE
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;

	//VAR

	//OBJECT
	private Almacen selectedAlmacen;
	private Almacen newAlmacen;
	private Usuario selectedEncargado;
	private Usuario newEncargado;

	//LIST
	private List<Usuario> listUsuario ;
	private List<Almacen> listaAlmacen;
	private List<AlmacenEncargado> listAlmacenEncargado ;
	private List<AlmacenEncargado> listAlmacenEncargadoElminados ;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private Gestion gestionSession;

	@PostConstruct
	public void init() {

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		//jalar los usuarios(ENCARGADOS LIBRES=SIN ALMACEN ASIGNADOS)
		listUsuario = usuarioDao.obtenerUsuarioOrdenAscPorId();
		loadDefault();
	}

	public void loadDefault(){
		listAlmacenEncargado = new ArrayList<>();
		listAlmacenEncargadoElminados  = new ArrayList<>();

		newEncargado  = new Usuario();
		selectedEncargado = new Usuario();
		newAlmacen = new Almacen();
		newAlmacen.setCodigo("AL"+String.format("%06d",almacenDao.correlativo()));
		newAlmacen.setEstado("AC");
		newAlmacen.setFechaRegistro(new Date());
		newAlmacen.setUsuarioRegistro(usuarioSession);
		newAlmacen.setGestion(gestionSession);

		selectedAlmacen = null;

		// traer todos las almacenes ordenados por ID Desc
		listaAlmacen = almacenDao.traerAlmacenActivas();

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
		return "page.xhtml?faces-redirect=true";
	}

	//ACTION----

	public void cambiarAspecto(){
		modificar = false;
		registrar = true;
		crear = false;
	}

	public void cambiarAspectoModificar(){
		modificar = true;
		registrar = false;
		crear = false;
	}

	public void onRowSelectAlmacenClick(SelectEvent event) {
		Almacen almacen = (Almacen) event.getObject();
		selectedAlmacen = almacen;
		newAlmacen = selectedAlmacen;
		newAlmacen.setFechaRegistro(new Date());
		newAlmacen.setUsuarioRegistro(usuarioSession);

		modificar = true;
		registrar = false;
		crear = false;

		listAlmacenEncargado = almacenEncargadoDao.obtenerPorAlmacen(selectedAlmacen);
		for(AlmacenEncargado ae: listAlmacenEncargado){
			listUsuario.remove(ae.getEncargado());
		}
		if(listUsuario.size()>0){
			selectedEncargado = listUsuario.get(0);
		}else{
			selectedEncargado = new Usuario();
		}
	}

	public void closeDialogEncargado(){
		FacesUtil.hideDialog("dlgEncargado");
	}

	public void loadDialogEncargado(){
		FacesUtil.updateComponent("formDlgEncargado");
		FacesUtil.showDialog("dlgEncargado");
	}

	public void elimnarEncargado(Integer idEncargado){
		AlmacenEncargado almacenEncargado = new AlmacenEncargado();
		for(AlmacenEncargado ae: listAlmacenEncargado){
			if(ae.getId().equals(idEncargado)){
				almacenEncargado = ae;
			}
		}
		if(almacenEncargado.getId().intValue()>0){//al agregar nuevos se inserta un valor negativo al ID
			listAlmacenEncargadoElminados.add(almacenEncargado);
		}
		listUsuario.add(almacenEncargado.getEncargado());
		listAlmacenEncargado.remove(almacenEncargado);
	}

	public void agregarModificarEncargado(){
		if(selectedEncargado.getId()==0){
			FacesUtil.infoMessage("Verificación", "No hay encargados disponibles para seguir agregando");
			return;
		}
		for( AlmacenEncargado ae: listAlmacenEncargado){
			if(ae.getEncargado().getId().equals(newEncargado.getId())){
				FacesUtil.infoMessage("Verificación", "El encargado ya existe, debe agregar uno diferente.");
				return;
			}
		}
		AlmacenEncargado almacenEncargado = new AlmacenEncargado();
		almacenEncargado.setId((listAlmacenEncargado.size()+1)*(-1));
		almacenEncargado.setAlmacen(selectedAlmacen);
		almacenEncargado.setEncargado(setearEncargado(selectedEncargado));
		almacenEncargado.setEstado("AC");
		almacenEncargado.setFechaRegistro(new Date());
		almacenEncargado.setUsuarioRegistro(usuarioSession);
		listAlmacenEncargado.add(almacenEncargado);
		closeDialogEncargado();
		listUsuario.remove(almacenEncargado.getEncargado());
	}

	private Usuario setearEncargado(Usuario selectedEncargado){
		for(Usuario u: listUsuario){
			if(u.getId().equals(selectedEncargado.getId())){
				selectedEncargado = u;
			}
		}
		return selectedEncargado;
	}

	public void cerrarDialogEncargado(){
		closeDialogEncargado();
	}

	//PROCESO----

	public void registrarAlmacen() {
		if(newAlmacen.getNombre().isEmpty() || newAlmacen.getCodigo().isEmpty() || newAlmacen.getDireccion().isEmpty() || newAlmacen.getTelefono().isEmpty() ){
			FacesUtil.infoMessage("VALIDACION", "No pueden haber campos vacios.");
			return;
		}
		Almacen data = almacenDao.registrar(newAlmacen,listAlmacenEncargado);
		if(data != null){
			loadDefault();
		}
	}

	public void modificarAlmacen() {
		if(newAlmacen.getNombre().isEmpty() || newAlmacen.getCodigo().isEmpty() || newAlmacen.getDireccion().isEmpty() || newAlmacen.getTelefono().isEmpty() ){
			FacesUtil.infoMessage("VALIDACION", "No pueden haber campos vacios.");
			return;
		}
		boolean sw = almacenDao.modificar(newAlmacen,listAlmacenEncargado,listAlmacenEncargadoElminados);
		if(sw){
			loadDefault();
		}
	}

	public void eliminarAlmacen() {
		boolean sw = almacenDao.eliminar(newAlmacen);
		if(sw){
			loadDefault();
			FacesUtil.updateComponent("form001");
		}
	}

	// ---- get and set ----

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public Almacen getSelectedAlmacen() {
		return selectedAlmacen;
	}

	public void setSelectedAlmacen(Almacen selectedAlmacen) {
		this.selectedAlmacen = selectedAlmacen;
	}

	public Almacen getNewAlmacen() {
		return newAlmacen;
	}

	public void setNewAlmacen(Almacen newAlmacen) {
		this.newAlmacen = newAlmacen;
	}

	public List<Almacen> getListaAlmacen() {
		return listaAlmacen;
	}

	public void setListaAlmacen(List<Almacen> listaAlmacen){
		this.listaAlmacen = listaAlmacen;
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

	public List<Usuario> getListUsuario() {
		return listUsuario;
	}

	public void setListUsuario(List<Usuario> listUsuario) {
		this.listUsuario = listUsuario;
	}

	public List<AlmacenEncargado> getListAlmacenEncargado() {
		return listAlmacenEncargado;
	}

	public void setListAlmacenEncargado(List<AlmacenEncargado> listAlmacenEncargado) {
		this.listAlmacenEncargado = listAlmacenEncargado;
	}

	public Usuario getSelectedEncargado() {
		return selectedEncargado;
	}

	public void setSelectedEncargado(Usuario selectedEncargado) {
		this.selectedEncargado = selectedEncargado;
	}

	public List<AlmacenEncargado> getListAlmacenEncargadoElminados() {
		return listAlmacenEncargadoElminados;
	}

	public void setListAlmacenEncargadoElminados(
			List<AlmacenEncargado> listAlmacenEncargadoElminados) {
		this.listAlmacenEncargadoElminados = listAlmacenEncargadoElminados;
	}

	public Usuario getNewEncargado() {
		return newEncargado;
	}

	public void setNewEncargado(Usuario newEncargado) {
		this.newEncargado = newEncargado;
	}

}
