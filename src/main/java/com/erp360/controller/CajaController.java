/**
 * @author david
 */
package com.erp360.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;

import com.erp360.interfaces.ICajaDao;
import com.erp360.interfaces.IUsuarioDao;
import com.erp360.model.Caja;
import com.erp360.model.CajaUsuario;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;




/**
 * @author david
 *
 */
@ManagedBean(name="cajaController")
@ViewScoped
public class CajaController {
	private static final long serialVersionUID = 1L;

	//DAO
	private @Inject ICajaDao cajaDao;
	private @Inject SessionMain sessionDao;
	private @Inject IUsuarioDao usuarioDao;

	//OBJECT 
	private Caja caja;
	
	//private VentaServicios ventaServicio;
	
	private Usuario usuario;


	//VAR
	private String currentPage = "/pages/caja/param/list.xhtml";

	//LIST
	private List<Caja> listaCajas;
	private DualListModel<Usuario> listaUsuarios;
	private List<Usuario> listaUsuariosConsultados;
	public List<Usuario>listaUsuariosSeleccionados=new ArrayList<Usuario>();;

	//STATE
	private boolean nuevo;
	private boolean registrar;
	private boolean seleccionado;
	private String mensaje;
	private boolean anulacion;
	private boolean procesar;
	private String idTablaProductos;


	@PostConstruct
	public void init() {
		loadDefault();
	}
	public void loadDefault(){
			nuevo = true;
			seleccionado = false;
			registrar = false;
			caja=new Caja();
			listaUsuariosSeleccionados=new ArrayList<Usuario>();
			listaCajas=cajaDao.listaPorEmpresaYActivos(sessionDao.getEmpresaLogin());
	}
	//ACTION
	public void actionCancelar(){
		nuevo = true;
		seleccionado = false;
		registrar = false;
		loadDefault();
		currentPage = "/pages/caja/param/list.xhtml";
	}
	public void actionNuevo(){
		System.out.println("Nuevo");
		caja=new Caja();	
		nuevo = false;
		seleccionado = false;
		registrar = true;
		listaUsuariosConsultados=usuarioDao.obtenerTodosActivosEInactivosPorCompaniaPorOrdenAsc(sessionDao.getEmpresaLogin());
		listaUsuariosSeleccionados=new ArrayList<Usuario>();
		listaUsuarios=new DualListModel<>(listaUsuariosConsultados,listaUsuariosSeleccionados);
		currentPage = "/pages/caja/param/edit.xhtml";
	}
	public void onRowSelect(SelectEvent event) {
		nuevo = false;
		seleccionado = true;
		registrar = false;
		listaUsuariosSeleccionados=new ArrayList<Usuario>();
		System.out.println("TAMAÑO LISTA USUARIOS ASIGNADOS A CAJA"+ caja.getListaUsuarios().size());
		listaUsuariosConsultados=usuarioDao.obtenerTodosActivosEInactivosPorCompaniaPorOrdenAsc(sessionDao.getEmpresaLogin());	
		System.out.println("TAMAÑO LISTA USUARIOS CONSULTADOS DAO USUARIO"+ listaUsuariosConsultados.size());
		for (CajaUsuario cajaUsuario : caja.getListaUsuarios()) {
			listaUsuariosSeleccionados.add(cajaUsuario.getUsuario());
				listaUsuariosConsultados.remove(cajaUsuario.getUsuario());
		} 
	
			System.out.println("TAMAÑO LISTA USUARIOS TARGET "+listaUsuariosSeleccionados.size());	
		   listaUsuarios=new DualListModel<>(listaUsuariosConsultados,listaUsuariosSeleccionados);
		   
		//listaUsuarios.setSource(listaUsuariosConsultados);
		// listaUsuarios.setTarget(listaUsuariosSeleccionados);
		    currentPage = "/pages/caja/param/edit.xhtml";
	    }
	/*CRUD NOTA DE VENTA*/

	public void registrarCaja(){	
		listaUsuariosSeleccionados=listaUsuarios.getTarget();
		List<CajaUsuario> listaCajaUSuario=new ArrayList<>();
		for (Usuario us : listaUsuariosSeleccionados) {
			CajaUsuario cajaUsuario=new CajaUsuario();
			cajaUsuario.setUsuario(us);
			cajaUsuario.setEstado("AC");
			cajaUsuario.setFechaRegistro(new Date());
			cajaUsuario.setUsuarioRegistro(sessionDao.getUsuarioLogin().getNombre());
			listaCajaUSuario.add(cajaUsuario);
		}
		caja.setEstado("AC");
		caja.setEmpresa(sessionDao.getEmpresaLogin());
		caja.setFechaRegistro(new Date());
		caja.setOpcion("CE");
		caja.setGestion(sessionDao.getGestionLogin());
		caja.setListaUsuarios(listaCajaUSuario);
		Caja ca= cajaDao.registrar(caja);
		//sessionDao.setCaja(co.getCaja());
		if (ca!=null) {
			loadDefault();				
			currentPage = "/pages/caja/param/list.xhtml";
		} else {
			FacesUtil.infoMessage("Informacion", "El detalle no debe estar vacio");
		}
	}
	public void modificarCaja(){

		listaUsuariosSeleccionados=listaUsuarios.getTarget();
		List<CajaUsuario> listaCajaUSuario=new ArrayList<>();
		for (Usuario us : listaUsuariosSeleccionados) {
			CajaUsuario cajaUsuario=new CajaUsuario();
			cajaUsuario.setUsuario(us);
			cajaUsuario.setEstado("AC");
			cajaUsuario.setFechaRegistro(new Date());
			cajaUsuario.setUsuarioRegistro(sessionDao.getUsuarioLogin().getNombre());
			listaCajaUSuario.add(cajaUsuario);
		}
		caja.setFechaModificacion(new Date());
		//caja.setOpcion("CE");
		//caja.setGestion(sessionDao.getGestionLogin());
		caja.setListaUsuarios(listaCajaUSuario);
	    boolean ca= cajaDao.modificar(caja);
		//sessionDao.setCaja(co.getCaja());
		if (ca!=false) {
			loadDefault();				
			currentPage = "/pages/caja/param/list.xhtml";
		} else {
			FacesUtil.infoMessage("Informacion", "El detalle no debe estar vacio");
		}

	}
	/*DETALLE NOTA DE VENTA*/
	
	

	

	/*DETALLE NOTA DE VENTA*/


	

	
	
	/*METODOS ONCOMPLETE*/
	public List<Caja> onCompleteCaja(String query){
		String upperQuery = query.toUpperCase();
		System.out.println("Entro en Oncomplete Caja"+ query);
		return cajaDao.RetornarOnCompletePorEmpresa(sessionDao.getEmpresaLogin(),sessionDao.getUsuarioLogin(),upperQuery);

	}
	
	
	

	/*SETTERS AND GETTERS*/

	public boolean isNuevo() {
		return nuevo;
	}
	public void setNuevo(boolean nuevo) {
		this.nuevo = nuevo;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public boolean isAnulacion() {
		return anulacion;
	}

	public void setAnulacion(boolean anulacion) {
		this.anulacion = anulacion;
	}

	public boolean isProcesar() {
		return procesar;
	}

	public void setProcesar(boolean procesar) {
		this.procesar = procesar;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getIdTablaProductos() {
		return idTablaProductos;
	}

	public void setIdTablaProductos(String idTablaProductos) {
		this.idTablaProductos = idTablaProductos;
	}

	

	public List<Caja> getListaCajas() {
		return listaCajas;
	}

	public void setListaCajas(List<Caja> listaCajas) {
		this.listaCajas = listaCajas;
	}

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public boolean isRegistrar() {
		return registrar;
	}
	public void setRegistrar(boolean registrar) {
		this.registrar = registrar;
	}
	public DualListModel<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}
	public void setListaUsuarios(DualListModel<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	public List<Usuario> getListaUsuariosConsultados() {
		return listaUsuariosConsultados;
	}
	//public void setListaUsuariosConsultados(List<Usuario> listaUsuariosConsultados) {
		//this.listaUsuariosConsultados = listaUsuariosConsultados;
	//}
	public List<Usuario> getListaUsuariosSeleccionados() {
		return listaUsuariosSeleccionados;
	}
	public void setListaUsuariosSeleccionados(
			List<Usuario> listaUsuariosSeleccionados) {
		this.listaUsuariosSeleccionados = listaUsuariosSeleccionados;
	}
	


}
