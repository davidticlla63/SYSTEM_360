package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import com.erp360.dao.RolDao;
import com.erp360.dao.SucursalDao;
import com.erp360.dao.UsuarioDao;
import com.erp360.dao.UsuarioRolDao;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.Sucursal;
import com.erp360.model.Usuario;
import com.erp360.model.Roles;
import com.erp360.model.UsuarioRol;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

@Named(value = "usuarioController")
@ConversationScoped
public class UsuarioController implements Serializable {

	private static final long serialVersionUID = 6211210765749674269L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject RolDao rolesDao;
	private @Inject UsuarioRolDao usuarioRolDao;
	private @Inject SucursalDao sucursalDao;
	private @Inject UsuarioDao usuarioDao;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String nombreUsuario;	
	private Empresa empresaLogin;
	private Gestion gestionLogin;

	//VAR
	private String nombreRol;
	private String nombreSucursal;
	private String nombreEstado="ACTIVO";

	@Produces
	@Named
	private Usuario newUsuario;
	private Usuario selectedUsuario;
	private UsuarioRol selectedUsuarioRol;
	private Roles selectedRol;
	private Sucursal selectedSucursal;

	//LIST
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<Usuario> listFilterUsuario = new ArrayList<Usuario>();
	private List<Roles> listRol = new ArrayList<Roles>();
	private String[] listEstado = {"ACTIVO","INACTIVO"};
	private List<UsuarioRol> listUsuarioRol = new ArrayList<UsuarioRol>();
	private List<Sucursal> listSucursal = new ArrayList<Sucursal>();

	//STATE
	private boolean crear = true;
	private boolean registrar = false;
	private boolean modificar = false;
	private boolean stateInicial = true;

	@PostConstruct
	public void init() {

		System.out.println(" init new initNewUsuario");
		nombreUsuario = sessionMain.getUsuarioLogin().getLogin();
		empresaLogin = sessionMain.getEmpresaLogin();
		gestionLogin = sessionMain.getGestionLogin();

		listRol = rolesDao.obtenerOrdenAscPorId();
		listSucursal = sucursalDao. obtenerPorEmpresa(empresaLogin);

		loadDefault();
	}

	public void loadDefault(){

		crear = true;
		registrar = false;
		modificar = false;
		stateInicial = true;
		newUsuario = new Usuario();
		selectedUsuario = new Usuario();
		selectedUsuarioRol = new UsuarioRol();
		listUsuario = usuarioDao.obtenerUsuarioOrdenAscPorId();

		listUsuarioRol = usuarioRolDao.obtenerOrdenAscPorId();

		nombreRol = listRol.get(0).getNombre();
		selectedRol = listRol.get(0);

		selectedSucursal = listSucursal.get(0);
		nombreSucursal = selectedRol.getNombre();

		modificar = false;
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
		return "usuario.xhtml?faces-redirect=true";
	}

	public void registrarUsuario() {
		System.out.println("Ingreso a registrarUsuario: ");
		Date fechaActual = new Date();
		newUsuario.setFechaRegistro(new Date());
		newUsuario.setUsuarioRegistro(nombreUsuario);
		newUsuario.setState(nombreEstado.equals("ACTIVO")?"AC":"IN");
		newUsuario.setSucursal(selectedSucursal);

		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setRol(selectedRol);
		usuarioRol.setEstado("AC");
		usuarioRol.setFechaRegistro(fechaActual);
		usuarioRol.setUsuarioRegistro(nombreUsuario);
		boolean sw = usuarioDao.registrar(newUsuario,usuarioRol);
		if(sw){
			loadDefault();
			FacesUtil.updateComponent("form001");
		}
	}

	public void modificarUsuario() {
		System.out.println("Ingreso a modificarUsuario: "
				+ newUsuario.getId());
		Date fechaActual = new Date();
		newUsuario.setFechaModificacion(new Date());
		newUsuario.setState(nombreEstado.equals("ACTIVO")?"AC":"IN");
		newUsuario.setSucursal(selectedSucursal);

		UsuarioRol usuarioRol = usuarioRolDao.obtenerPorUsuario(newUsuario);
		usuarioRol.setRol(selectedRol);
		usuarioRol.setFechaModificacion(fechaActual);

		boolean sw = usuarioDao.modificar(newUsuario,usuarioRol);
		if(sw){
			loadDefault();
			FacesUtil.updateComponent("form001");
		}
	}

	public void eliminarUsuario() {
		Date fechaActual = new Date();
		System.out.println("Ingreso a eliminarUsuario "
				+ newUsuario.getId());
		newUsuario.setFechaModificacion(fechaActual);
		//usuarioRegistration.update(newUsuario);

		//eliminar usuariorol
		selectedUsuarioRol.setFechaModificacion(fechaActual);
		//usuarioRolRegistration.update(selectedUsuarioRol);

		boolean sw = usuarioDao.eliminar(newUsuario,selectedUsuarioRol);
		if(sw){
			loadDefault();
			FacesUtil.updateComponent("form001");
		}
	}

	public void onRowSelect(SelectEvent event) {
		//selectedRol = usuarioRolesRepository.findByUsuario(selectedUsuario).getRol();
		selectedSucursal = selectedUsuarioRol.getUsuario().getSucursal();
		selectedRol = selectedUsuarioRol.getRol();
		nombreRol = selectedRol.getNombre();
		nombreSucursal = selectedSucursal.getNombre();
		newUsuario = selectedUsuario;
		newUsuario = selectedUsuarioRol.getUsuario();
		nombreEstado = newUsuario.getState().equals("AC")?"ACTIVO":"INACTIVO";
		crear = false;
		registrar = false;
		modificar = true;
	}

	public void actualizarFormReg(){
		crear = true;
		registrar = false;
		modificar = false;
		newUsuario = new Usuario();
		selectedUsuario = null;
	}

	public void actionButtonNuevo(){
		crear = false;
		registrar = true;
		modificar = false;
		selectedUsuario = new Usuario();
		newUsuario = new Usuario();
	}

	//validaciones

	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		if (((String)arg2).length()<1) {
			throw new ValidatorException(new FacesMessage("Al menos 1 caracteres "));
		}
	}

	private Roles obtenerRolByLocal(String nombreRol){
		for(Roles r: listRol){
			if(r.getNombre().equals(nombreRol)){
				return r;
			}
		}
		return null;
	}

	private Sucursal obtenerSucursalByLocal(String nombreSucursal){
		for(Sucursal r: listSucursal){
			if(r.getNombre().equals(nombreSucursal)){
				return r;
			}
		}
		return null;
	}

	// ----------  get and set -------------

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public Usuario getSelectedUsuario() {
		return selectedUsuario;
	}

	public void setSelectedUsuario(Usuario selectedUsuario) {
		this.selectedUsuario = selectedUsuario;
	}

	public List<Usuario> getListUsuario() {
		return listUsuario;
	}

	public void setListUsuario(List<Usuario> listUsuario) {
		this.listUsuario = listUsuario;
	}

	public String getNombreRol() {
		return nombreRol;
	}

	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
		selectedRol = obtenerRolByLocal( nombreRol);
	}

	public List<Usuario> getListFilterUsuario() {
		return listFilterUsuario;
	}

	public void setListFilterUsuario(List<Usuario> listFilterUsuario) {
		this.listFilterUsuario = listFilterUsuario;
	}

	public String getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	public String[] getListEstado() {
		return listEstado;
	}

	public void setListEstado(String[] listEstado) {
		this.listEstado = listEstado;
	}

	public boolean isStateInicial() {
		return stateInicial;
	}

	public void setStateInicial(boolean stateInicial) {
		this.stateInicial = stateInicial;
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

	public List<UsuarioRol> getListUsuarioRol() {
		return listUsuarioRol;
	}

	public void setListUsuarioRol(List<UsuarioRol> listUsuarioRol) {
		this.listUsuarioRol = listUsuarioRol;
	}

	public UsuarioRol getSelectedUsuarioRol() {
		return selectedUsuarioRol;
	}

	public void setSelectedUsuarioRol(UsuarioRol selectedUsuarioRol) {
		this.selectedUsuarioRol = selectedUsuarioRol;
	}
	
	public Sucursal getSelectedSucursal() {
		return selectedSucursal;
	}

	public void setSelectedSucursal(Sucursal selectedSucursal) {
		this.selectedSucursal = selectedSucursal;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
		selectedSucursal = obtenerSucursalByLocal( nombreSucursal);
	}

	public List<Sucursal> getListSucursal() {
		return listSucursal;
	}

	public void setListSucursal(List<Sucursal> listSucursal) {
		this.listSucursal = listSucursal;
	}

}
