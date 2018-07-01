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

import com.erp360.dao.LineaProductoDao;
import com.erp360.dao.ProductoDao;
import com.erp360.dao.UnidadMedidaDao;
import com.erp360.model.LineaProducto;
import com.erp360.model.Producto;
import com.erp360.model.UnidadMedida;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Named(value = "productoController")
@ConversationScoped
public class ProductoController implements Serializable {

	private static final long serialVersionUID = 4656764987882579263L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject UnidadMedidaDao unidadMedidaDao;
	private @Inject ProductoDao productoDao;
	private @Inject LineaProductoDao lineaProductoDao;

	//STATE
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;

	//VAR
	private Producto selectedProducto;
	private Producto newProducto= new Producto();

	//OBJECT

	//LIST
	private List<UnidadMedida> listUnidadMedida ;
	private List<Producto> listaProducto;
	
	private List<LineaProducto> listLineaProducto;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;

	@PostConstruct
	public void init() {
		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		loadDefault();	
	}

	public void loadDefault(){
		listUnidadMedida = unidadMedidaDao.findAllOrderedByID();
		listLineaProducto = lineaProductoDao.obtenerTodosByNombreOrdrByDesc();

		newProducto = new Producto();
		newProducto.setEstado("AC");
		newProducto.setCodigo("PR"+String.format("%06d",productoDao.correlativoProducto()));
		newProducto.setFechaRegistro(new Date());
		newProducto.setUsuarioRegistro(usuarioSession);
		newProducto.setUnidadMedidas(listUnidadMedida.get(0));
		newProducto.setLineaProducto(listLineaProducto.get(0));
		newProducto.setPrecioCompra(0);
		newProducto.setPrecioVentaCredito(0);
		newProducto.setPrecioVentaContado(0);

		selectedProducto = null;

		// traer todos las Productos ordenados por fecha Desc
		listaProducto = productoDao.obtenerTodosOrdenFecha();
		selectedProducto = null;
		modificar = false;
		registrar = false;
		crear = true;
	}

	public void initConversation() {
		if (!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient()) {
			conversation.begin();
		}
	}

	public String endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return "orden_ingreso.xhtml?faces-redirect=true";
	}
	
	public void calcularPorcentajePrecio(){
		double precioCompra = newProducto.getPrecioCompra();
		double porcentajeContado = newProducto.getLineaProducto().getGrupoProducto().getPorcentajeVentaContado() / 100;
		double porcentajeCredito = newProducto.getLineaProducto().getGrupoProducto().getPorcentajeVentaCredito() / 100;
		newProducto.setPrecioVentaContado(precioCompra + (precioCompra*porcentajeContado));
		newProducto.setPrecioVentaCredito(precioCompra + (precioCompra*porcentajeCredito));
	}

	//ACTION----

	public void cambiarAspecto(){
		modificar = false;
		registrar = true;
		crear = false;

//		newProducto = new Producto();
//		newProducto.setEstado("AC");
//		newProducto.setFechaRegistro(new Date());
//		newProducto.setUsuarioRegistro(usuarioSession);
	}

	public void cambiarAspectoModificar(){
		modificar = true;
		registrar = false;
		crear = false;

		newProducto = selectedProducto;
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		//newProducto = selectedProducto;
		//modificar = false;
		
		modificar = true;
		registrar = false;
		crear = false;

		newProducto = selectedProducto;
	}

	//PROICESAR----

	public void registrarProducto() {
		if(newProducto.getNombre().isEmpty() || newProducto.getCodigo().isEmpty() || newProducto.getDescripcion().isEmpty() ){
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios");
			return;
		}
		//quitar espacios al inicio y al final
		newProducto.setNombre(newProducto.getNombre().trim());
		newProducto.setDescripcion(newProducto.getDescripcion().trim());
		//-------------------------------------
		newProducto.setPrecioUnitario(0);//el precio se define mediante orden ingreso
		newProducto.setUsuarioRegistro(usuarioSession);
		newProducto.setFechaRegistro(new Date());
		Producto data = productoDao.registrar(newProducto);
		if(data != null){
			loadDefault();
		}
	}

	public void modificarProducto() {
		boolean sw = productoDao.modificar(newProducto);
		if(sw){
			loadDefault();
		}
	}

	public void eliminarProducto() {
		boolean sw = false;
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

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public Producto getNewProducto() {
		return newProducto;
	}

	public void setNewProducto(Producto newProducto) {
		this.newProducto = newProducto;
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

	public List<Producto> getListaProducto() {
		return listaProducto;
	}

	public void setListaProducto(List<Producto> listaProducto){
		this.listaProducto = listaProducto;
	}

	public List<LineaProducto> getListLineaProducto() {
		return listLineaProducto;
	}

	public void setListLineaProducto(List<LineaProducto> listLineaProducto) {
		this.listLineaProducto = listLineaProducto;
	}

	public List<UnidadMedida> getListUnidadMedida() {
		return listUnidadMedida;
	}

	public void setListUnidadMedida(List<UnidadMedida> listUnidadMedida) {
		this.listUnidadMedida = listUnidadMedida;
	}

}
