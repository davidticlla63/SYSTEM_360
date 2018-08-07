package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.erp360.dao.ClienteDao;
import com.erp360.dao.EjecutivoClienteDao;
import com.erp360.dao.EjecutivoDao;
import com.erp360.model.Cliente;
import com.erp360.model.Ejecutivo;
import com.erp360.model.EjecutivoCliente;
import com.erp360.util.FacesUtil;
import com.erp360.util.NumberUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "encargadoVentaController")
@ViewScoped
public class EjecutivoController implements Serializable {

	private static final long serialVersionUID = -7148739425514986109L;

	//DAO
	private @Inject SessionMain sessionMain; //variable del login
	private @Inject EjecutivoDao encargadoVentaDao;
	private @Inject ClienteDao clienteDao;
	private @Inject EjecutivoClienteDao ejecutivoClienteDao;

	//estados
	private boolean crear ;
	private boolean registrar ;
	private boolean modificar ;

	//VARIABLES
	private String nombreEstado="ACTIVO";

	//OBJECT
	private Ejecutivo newEncargadoVenta;
	private Ejecutivo selectedEncargadoVenta;
	private List<EjecutivoCliente> ejecutivoClientes;
	private List<EjecutivoCliente> ejecutivoClientesRemoved;
	private EjecutivoCliente ejecutivoCliente;
	private List<Cliente> listCliente;
	private Cliente selectedCliente;
	
	//LISTAS
	private List<Ejecutivo> listEncargadoVenta = new ArrayList<Ejecutivo>();
	public static List<Ejecutivo> listPatrocinador = new ArrayList<Ejecutivo>();
	private String[] listEstado = {"ACTIVO","INACTIVO"};

	@PostConstruct
	public void init() {
		System.out.println(" init new init New EncargadoVenta");
		loadDefault();
	}

	private void loadDefault(){
		ejecutivoClientes = new ArrayList<>();
		crear = true;
		registrar = false;
		modificar = false;

		newEncargadoVenta = new Ejecutivo();
		selectedEncargadoVenta = new Ejecutivo();
		ejecutivoClientes = new ArrayList<>();
		ejecutivoClientesRemoved = new ArrayList<>();
		listEncargadoVenta = encargadoVentaDao.obtenerTodosOrdenadosPorId();
	}

	// -- event action

	// --- PROCESS
	public void registrar(){
		String estado = nombreEstado.equals("ACTIVO")?"AC":"IN";
		Date fechaActual = new  Date();
		newEncargadoVenta.setEstado(estado);
		newEncargadoVenta.setFechaRegistro(fechaActual);
		newEncargadoVenta.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		newEncargadoVenta.setEmpresa(sessionMain.getEmpresaLogin());
		Ejecutivo c = encargadoVentaDao.registrar(newEncargadoVenta);
		for(EjecutivoCliente ejecutivoCliente : ejecutivoClientes){
			ejecutivoCliente.setId(0);
			ejecutivoCliente.setEjecutivo(c);
			ejecutivoCliente.setFechaRegistro(c.getFechaRegistro());
			ejecutivoClienteDao.registrar(ejecutivoCliente);
		}
		if(c!=null){
			loadDefault();
		}
	}

	public void loadDialogCuenta(){
		FacesUtil.showDialog("dlgPlanCuenta");	
	}

	///------

	public void modificar(){
		String estado = nombreEstado.equals("ACTIVO")?"AC":"IN";
		Date fechaActual = new  Date();
		newEncargadoVenta.setEstado(estado);
		newEncargadoVenta.setFechaRegistro(fechaActual);
		newEncargadoVenta.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		newEncargadoVenta.setEmpresa(sessionMain.getEmpresaLogin());
		boolean sw = encargadoVentaDao.modificar(newEncargadoVenta);
		//Verifica si se crearon nuevos
		for(EjecutivoCliente ejecutivoCliente: ejecutivoClientes){
			if(ejecutivoCliente.getId()<0){//is new
				ejecutivoCliente.setId(0);
				ejecutivoCliente.setFechaRegistro(fechaActual);
				ejecutivoCliente.setEjecutivo(newEncargadoVenta);
				ejecutivoCliente = ejecutivoClienteDao.registrar(ejecutivoCliente);
			}
		}
		//Verifica si se eliminaron
		for(EjecutivoCliente ejecutivoCliente: ejecutivoClientesRemoved){
			ejecutivoCliente.setEstado("RM");
			ejecutivoClienteDao.modificar(ejecutivoCliente);
		}
		if(sw){
			loadDefault();
		}
	}

	public void eliminar(){
		boolean sw = encargadoVentaDao.eliminar(newEncargadoVenta);
		if(sw){
			loadDefault();
		}
	}

	public void actualizarFormReg(){
		crear = true;
		registrar = false;
		modificar = false;
		newEncargadoVenta = new Ejecutivo();	
		selectedEncargadoVenta = new Ejecutivo();
	}

	public void cambiarAspecto(){
		crear = false;
		registrar = true;
		modificar = false;
	}

	public void onRowSelect(SelectEvent event) {
		newEncargadoVenta = new Ejecutivo();
		newEncargadoVenta = selectedEncargadoVenta;
		System.out.println("newEncargadoVenta: "+newEncargadoVenta.getId());
		ejecutivoClientes = ejecutivoClienteDao.getAllByIdEjecutivo(newEncargadoVenta);
		System.out.println("ejecutivoClientes: "+ejecutivoClientes.size());
		nombreEstado = newEncargadoVenta.getEstado().equals("AC")?"ACTIVO":"INACTIVO";
		crear = false;
		registrar = false;
		modificar = true;
	}

	
	public void actionLoadDialogCliente(){
		ejecutivoCliente = new EjecutivoCliente();
		ejecutivoCliente.setEstado("AC");
		ejecutivoCliente.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		FacesUtil.updateComponent("formDialogCliente");
		FacesUtil.showDialog("dlgCliente");
	}
	
	// ONCOMPLETETEXT Cliente
	public List<Cliente> completeCliente(String query) {
		listCliente = new ArrayList<Cliente>();
		boolean sw = NumberUtil.isNumeric(query);
		if(sw){
			listCliente = clienteDao.obtenerTodosPorNitCodigo(query);
		}else{
			listCliente = clienteDao.obtenerTodosPorRazonSocial(query.toUpperCase());
		}
		return listCliente;
	}

	public void onRowSelectCliente2Click(SelectEvent event) {
		Cliente customer = (Cliente) event.getObject();
		int index = listCliente.indexOf(customer);
		selectedCliente = listCliente.get(index);
		selectedCliente.setNombres(selectedCliente.getNombres());
	}
	
	public void limpiarDatosCliente(){
		ejecutivoCliente = new EjecutivoCliente();
		FacesUtil.hideDialog("dlgCliente");
	}
	
	public void agregarDetalleCliente(){
		if(selectedCliente == null){
			FacesUtil.infoMessage("Validación", "Seleccione un  cliente");
			return;
		}
		if(existeClienteAsociadoAEjecutivo(selectedCliente,ejecutivoClientes)){
			FacesUtil.infoMessage("Validación", "Este  cliente ya fué agregado");
			return;
		}
		ejecutivoCliente.setId((ejecutivoClientes.size()+1)*-1);
		ejecutivoCliente.setCliente(selectedCliente);
		ejecutivoCliente.setEjecutivo(newEncargadoVenta);
		ejecutivoCliente.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		ejecutivoCliente.setFechaRegistro(new Date());
		ejecutivoClientes.add(ejecutivoCliente);
		
		ejecutivoCliente = new EjecutivoCliente();
		FacesUtil.updateComponent("form001");
		FacesUtil.hideDialog("dlgCliente");
	}
	
	private boolean existeClienteAsociadoAEjecutivo(Cliente selectedCliente,
			List<EjecutivoCliente> ejecutivoClientes) {
		for(EjecutivoCliente ec: ejecutivoClientes){
			if(ec.getCliente().getId().equals(selectedCliente.getId())){
				return true;
			}
		}
		EjecutivoCliente ec = ejecutivoClienteDao.getEjecutivoClienteByIdCliente(selectedCliente);
		return ec != null;
	}

	public void borrarEjecutivoCliente(Integer id){
		EjecutivoCliente removed = new EjecutivoCliente();
		for(EjecutivoCliente ejecutivoCliente: ejecutivoClientes){
			if(ejecutivoCliente.getId() == id){
				removed = ejecutivoCliente;
			}
		}
		ejecutivoClientesRemoved.add(removed);
		ejecutivoClientes.remove(removed);
		
	}
	
	

	//tipo cliente
	public List<Ejecutivo> onCompletePatrocinador(String query) {
		// ystem.out.println("Entro en Oncomplete Caja"+ query);
		listPatrocinador = encargadoVentaDao.obtenerPorConsulta(query.toUpperCase());
		return listPatrocinador;
	}

	// ACTION

	public void onSelectPatrocinador(SelectEvent event) {
		newEncargadoVenta.setPatrocinador((Ejecutivo) event.getObject());
	}

	// --------------   get and set  ---------------

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
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

	public Ejecutivo getNewEncargadoVenta() {
		return newEncargadoVenta;
	}

	public void setNewEncargadoVenta(Ejecutivo newEncargadoVenta) {
		this.newEncargadoVenta = newEncargadoVenta;
	}

	public Ejecutivo getSelectedEncargadoVenta() {
		return selectedEncargadoVenta;
	}

	public void setSelectedEncargadoVenta(Ejecutivo selectedEncargadoVenta) {
		this.selectedEncargadoVenta = selectedEncargadoVenta;
	}

	public List<Ejecutivo> getListEncargadoVenta() {
		return listEncargadoVenta;
	}

	public void setListEncargadoVenta(List<Ejecutivo> listEncargadoVenta) {
		this.listEncargadoVenta = listEncargadoVenta;
	}

	public List<EjecutivoCliente> getEjecutivoClientes() {
		return ejecutivoClientes;
	}

	public void setEjecutivoClientes(List<EjecutivoCliente> ejecutivoClientes) {
		this.ejecutivoClientes = ejecutivoClientes;
	}

	public List<Cliente> getListCliente() {
		return listCliente;
	}

	public void setListCliente(List<Cliente> listCliente) {
		this.listCliente = listCliente;
	}

	public EjecutivoCliente getEjecutivoCliente() {
		return ejecutivoCliente;
	}

	public void setEjecutivoCliente(EjecutivoCliente ejecutivoCliente) {
		this.ejecutivoCliente = ejecutivoCliente;
	}

	public Cliente getSelectedCliente() {
		return selectedCliente;
	}

	public void setSelectedCliente(Cliente selectedCliente) {
		this.selectedCliente = selectedCliente;
	}

	public List<EjecutivoCliente> getEjecutivoClientesRemoved() {
		return ejecutivoClientesRemoved;
	}

	public void setEjecutivoClientesRemoved(List<EjecutivoCliente> ejecutivoClientesRemoved) {
		this.ejecutivoClientesRemoved = ejecutivoClientesRemoved;
	}

	public List<Ejecutivo> getListPatrocinador() {
		return listPatrocinador;
	}

	public void setListPatrocinador(List<Ejecutivo> listPatrocinador) {
		this.listPatrocinador = listPatrocinador;
	}
}

