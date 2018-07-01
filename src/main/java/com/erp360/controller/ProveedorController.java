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
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import com.erp360.dao.ProveedorDao;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.Proveedor;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Named(value = "proveedorController")
@ConversationScoped
public class ProveedorController implements Serializable {

	private static final long serialVersionUID = 6986540930723020906L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject ProveedorDao proveedorDao;

	//STATE
	private boolean crear = true;
	private boolean registrar = false;
	private boolean modificar = false;

	//VAR
	private String nombreEstado="ACTIVO";

	//LIST
	private List<Proveedor> listProveedor  = new ArrayList<Proveedor>();
	private String[] listEstado = {"ACTIVO","INACTIVO"};
	private String[] listTipoProveedor = {"NACIONAL","EXTRANJERA"};

	@Produces
	@Named
	private Proveedor newProveedor;
	private Proveedor selectedProveedor;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Empresa empresaSession;
	private String usuarioSession;
	private Gestion gestionSesion;

	@PostConstruct
	public void init() {
		System.out.println(" init new Proveedor");	
		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		empresaSession = sessionMain.getEmpresaLogin();
		gestionSesion = sessionMain.getGestionLogin();
		loadDefault();
	}

	public void loadDefault(){
		crear = true;
		registrar = false;
		modificar = false;

		newProveedor = new Proveedor();
		newProveedor.setCodigo("PV"+String.format("%06d",proveedorDao.correlativoProveedor()));
		selectedProveedor = new Proveedor();
		// traer todos por Empresa ordenados por ID Desc
		listProveedor = proveedorDao.obtenerACyINOrdenAscPorId();
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

	public void registrarProveedor() {
		//proveedor
		String estado = nombreEstado.equals("ACTIVO")?"AC":"IN";
		//quitar espacios al inicio y al final
		newProveedor.setNombre(newProveedor.getNombre().trim());
		newProveedor.setDireccion(newProveedor.getDireccion().trim());
		newProveedor.setNit(newProveedor.getNit().trim());
		newProveedor.setNombreContacto(newProveedor.getNombreContacto().trim());
		newProveedor.setCiContacto(newProveedor.getCiContacto().trim());
		newProveedor.setEmailContacto(newProveedor.getEmailContacto().trim());
		//-------------------------------------
		newProveedor.setEmpresa(empresaSession);
		newProveedor.setEstado(estado);
		newProveedor.setUsuarioRegistro(usuarioSession);
		newProveedor.setFechaRegistro(new Date());
		newProveedor.setGestion(gestionSesion);
		Proveedor data = proveedorDao.registrar(newProveedor);
		if(data != null){
			loadDefault();
			FacesUtil.updateComponent("form001");
		}
	}

	public void modificarProveedor() {
		String estado = nombreEstado.equals("ACTIVO")?"AC":"IN";
		newProveedor.setEmpresa(empresaSession);
		newProveedor.setEstado(estado);
		newProveedor.setUsuarioRegistro(usuarioSession);
		newProveedor.setFechaRegistro(new Date());
		newProveedor.setGestion(gestionSesion);
		Proveedor data = proveedorDao.modificar(newProveedor);
		if(data != null){
			loadDefault();
			FacesUtil.updateComponent("form001");
		}
	}

	public void eliminarProveedor() {
		boolean sw = proveedorDao.eliminar(newProveedor);
		if(sw){
			loadDefault();
			FacesUtil.updateComponent("form001");
		}
	}

	public void actualizarForm(){
		crear = true;
		registrar = false;
		modificar = false;
		newProveedor = new Proveedor();
		selectedProveedor = new Proveedor();
	}

	public void onRowSelect(SelectEvent event) {
		newProveedor = new Proveedor();
		newProveedor = selectedProveedor;
		nombreEstado = newProveedor.getEstado().equals("AC")?"ACTIVO":"INACTIVO";
		crear = false;
		registrar = false;
		modificar = true;
	}

	public void cambiarAspecto(){
		crear = false;
		registrar = true;
		modificar = false;
	}

	public void actualizarFormReg(){
		crear = true;
		registrar = false;
		modificar = false;
		newProveedor = new Proveedor();

		selectedProveedor = new Proveedor();
	}

	//  ---- get and set -----

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public Proveedor getSelectedProveedor() {
		return selectedProveedor;
	}

	public void setSelectedProveedor(Proveedor selectedProveedor) {
		this.selectedProveedor = selectedProveedor;
	}

	public String getTest(){
		return "test";
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

	public String[] getListTipoProveedor() {
		return listTipoProveedor;
	}

	public void setListTipoProveedor(String[] listTipoProveedor) {
		this.listTipoProveedor = listTipoProveedor;
	}
	
	public List<Proveedor> getListProveedor() {
		return listProveedor;
	}

	public void setListProveedor(List<Proveedor> listProveedor){
		this.listProveedor = listProveedor;
	}

}
