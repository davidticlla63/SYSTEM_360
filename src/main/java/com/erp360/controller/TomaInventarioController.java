package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLEncoder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import com.erp360.dao.AlmacenDao;
import com.erp360.dao.AlmacenProductoDao;
import com.erp360.dao.DetalleOrdenIngresoDao;
import com.erp360.dao.DetalleTomaInventarioDao;
import com.erp360.dao.DetalleTomaInventarioOrdenIngresoDao;
import com.erp360.dao.FachadaOrdenIngreso;
import com.erp360.dao.OrdenIngresoDao;
import com.erp360.dao.ProductoDao;
import com.erp360.dao.ProveedorDao;
import com.erp360.dao.TomaInventarioDao;
import com.erp360.model.Almacen;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.BajaProducto;
import com.erp360.model.CierreGestionAlmacen;
import com.erp360.model.DetalleOrdenIngreso;
import com.erp360.model.DetalleProducto;
import com.erp360.model.DetalleTomaInventario;
import com.erp360.model.DetalleTomaInventarioOrdenIngreso;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.OrdenIngreso;
import com.erp360.model.Producto;
import com.erp360.model.Proveedor;
import com.erp360.model.TomaInventario;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

@Named(value = "tomaInventarioController")
@ConversationScoped
public class TomaInventarioController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private @Inject FacesContext facesContext;

	//DAO
	private @Inject AlmacenDao almacenDao;
	private @Inject AlmacenProductoDao almacenProductoDao;
	private @Inject ProveedorDao proveedorDao;
	private @Inject ProductoDao productoDao;
	private @Inject TomaInventarioDao tomaInventarioDao;
	private @Inject DetalleTomaInventarioOrdenIngresoDao detalleTomaInventarioOrdenIngresoDao;
	private @Inject DetalleOrdenIngresoDao detalleOrdenIngresoDao;
	private @Inject DetalleTomaInventarioDao detalleTomaInventarioDao;
	private @Inject OrdenIngresoDao ordenIngresoDao;

	//ESTADOS
	private boolean crear = true;
	private boolean verProcesar = true;
	private boolean verReport = false;
	private boolean verButtonReport = false;
	private boolean revisarReport = false;
	private boolean verGuardar = false;
	private boolean verLista = true;//mostrar lista de tomas de inventario
	private boolean modificar = false;//verificar
	private boolean registrar = false;//mostrar maestro detalle
	private boolean buttonConciliar = false;//mostrar button conciliar
	private boolean conciliar = false;
	private boolean cierreAlmacen = false;
	private boolean buttonEditarTomaInventarioIncial;
	private boolean buttonProcesarTomaInventarioIncial;
	private boolean verButtonDetalle = true;
	private boolean editarOrdenIngreso = false;
	private boolean nuevoProducto = false;
	private boolean editarTomaInventarioIncial = false;//estado, modificar toma de inventario inicial
	private boolean hayGestionAnterior;
	private boolean stateProyecto;
	private boolean stateFuncionario;
	private boolean stateProveedor;
	private boolean stateDetalleUnidad;//area trabajo

	//VAR
	private String tituloPanel = "Registrar Almacen";
	private String urlTomaInventario = "";
	private String tipoTomaInventario;

	//OBJECT
	private Almacen selectedAlmacen;
	private TomaInventario newTomaInventario;
	private TomaInventario selectedTomaInventario;
	private Gestion gestionAnterior;

	//LIST
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<Almacen> listaAlmacen = new ArrayList<Almacen>();
	private List<Proveedor> listaProveedor = new ArrayList<Proveedor>();
	private List<DetalleTomaInventario> listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
	private List<DetalleTomaInventario> listDetalleTomaInventarioEliminadas = new ArrayList<DetalleTomaInventario>();
	private List<TomaInventario> listTomaInventario = new ArrayList<TomaInventario>();
	private List<AlmacenProducto> listAlmacenProducto = new ArrayList<AlmacenProducto>();
	private List<String> listTipo = new ArrayList<String>();//INICIAL,PARCIAL,FINAL
	private List<DetalleTomaInventario> listSelectedDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
	private List<DetalleTomaInventario>  listTemp = new ArrayList<>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Usuario usuarioSession;
	private Gestion gestionSesion;
	private Empresa empresaLogin;

	//PRODUTO
	private Producto newProducto ;
	private Producto selectedProducto;
	private DetalleOrdenIngreso selectedDetalleOrdenIngreso;
	private List<DetalleOrdenIngreso> listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>(); // ITEMS
	private List<DetalleOrdenIngreso> listDetalleOrdenIngresoEliminados = new ArrayList<DetalleOrdenIngreso>();
	private String tituloProducto = "Agregar Producto";
	private List<Proveedor> listProveedor = new ArrayList<Proveedor>();
	private Proveedor selectedProveedor;
	//ORDEN INGRESO
	private OrdenIngreso newOrdenIngreso;

	//FACADE
	private @Inject FachadaOrdenIngreso fachadaOrdenIngreso;

	@PostConstruct
	public void init() {
		System.out.println(" ... loadDefault ...");
		usuarioSession = sessionMain.getUsuarioLogin();
		gestionSesion = sessionMain.getGestionLogin();
		empresaLogin = sessionMain.getEmpresaLogin();

		loadDefault();		
	}

	public void loadDefault(){
		// tituloPanel
		tituloPanel = "Registrar Toma Inventario";
		stateProyecto = true;
		stateFuncionario = true;
		stateProveedor = true;
		stateDetalleUnidad = true;

		hayGestionAnterior = false;
		crear = true;
		verProcesar = true;
		verReport = false;
		revisarReport = false;
		verGuardar = false;
		verButtonReport = false;
		conciliar = false;
		buttonConciliar = false;
		cierreAlmacen = false;
		editarTomaInventarioIncial = false;
		buttonProcesarTomaInventarioIncial = false;
		buttonEditarTomaInventarioIncial = false;

		tipoTomaInventario = "PARCIAL";
		//---
		verLista = true;
		modificar = false;
		registrar = false;

		listTomaInventario = tomaInventarioDao.findAllOrderedByIDGestion(gestionSesion);
		//listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
		//listaAlmacen = almacenRepository.findAllOrderedByID();
		listTipo  = new ArrayList<String>();

		selectedTomaInventario = null;

		newTomaInventario = new TomaInventario();
		//newTomaInventario.setCorrelativo(cargarCorrelativo(listaOrdenIngreso.size()+1));
		newTomaInventario.setEstado("AC");
		newTomaInventario.setFecha(new Date());
		newTomaInventario.setFechaRegistro(new Date());
		newTomaInventario.setGestion(gestionSesion);
		newTomaInventario.setUsuarioRegistro(usuarioSession.getLogin());

		//PROUDCTO
		nuevoProducto = false;
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		selectedProducto= new Producto();
		newProducto= new Producto();
		listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>();
		selectedProveedor = new Proveedor();
		verButtonDetalle = true;
		selectedAlmacen = new Almacen();
		//ORDEN INGRESO
		newOrdenIngreso = new OrdenIngreso();

		//Verifica la gestion , sobre el levantamiento si ya se hizo el inicial
		if(gestionSesion.isIniciada()){
			newTomaInventario.setTipo("PARCIAL");
			listTipo.add("PARCIAL");
			listTipo.add("FINAL");
		}else{
			newTomaInventario.setTipo("INICIAL");
			listTipo.add("INICIAL");
			//listTipo.add("PARCIAL");//test , luego eliminar esta linea
		}
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

	//ACTION EVENT ------------

	public void cambiarAspectoModificar(){
		modificar = true;
		registrar = false;
		crear = false;
	}

	public void loadDialogProducto(){
		FacesUtil.updateComponent("formDialogProducto");
		FacesUtil.showDialog("dlgProducto");
	}

	public void closeDialogProducto(){
		FacesUtil.hideDialog("dlgProducto");
	}

	public void actionCerrarDialogProducto(){
		closeDialogProducto();
		selectedProducto = new Producto();
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		setVerButtonDetalle(true);
		setEditarOrdenIngreso(false);
		FacesUtil.updateComponent("form001");
	}

	public void actionAgregarProducto(){
		System.out.println("actionAgregarProducto - selectedProducto: "+selectedProducto);
		//validaciones
		if(selectedDetalleOrdenIngreso.getPrecioVentaContado()<0 || selectedProducto.getId().equals(0) || selectedProveedor.getId().equals(0)){
			FacesUtil.infoMessage("OBSERVACION", "Datos incompletos");
			return ;
		}
		//verificar si el producto ya fue agregado
		if(verificarProductoAgregado(selectedProducto)){
			FacesUtil.infoMessage("OBSERVACION", "Este producto ya fue agregado.");
			return ;
		}
		//selectedDetalleOrdenIngreso
		selectedDetalleOrdenIngreso.setProducto(selectedProducto);
		newOrdenIngreso.setProveedor(selectedProveedor);
		listaDetalleOrdenIngreso.add(0, selectedDetalleOrdenIngreso);
		actionCerrarDialogProducto();
	}

	public void registrarTomaInventarioInicialDesdeGestionAnterior(){

	}

	public void cambiarAspecto(){
		//verificar si ya hay una tomma de inventario inicial en esa gestion
		if(existeTomaInventarioIncialByGestion()){
			FacesUtil.infoMessage("VALIDACION", "Ya se registro una toma Inventario Inicial, gestion "+gestionSesion.getGestion());
			return;
		}
		if(this.newTomaInventario.getTipo().equals("INICIAL")){
			gestionAnterior = null;
			gestionAnterior = obtenerGestionAnterior();
			if(gestionAnterior!=null){
				//si hay, hay que jalar todos los datos de su cierre de almacen en la toma de inventario final
				hayGestionAnterior = true;
			}
			verLista = false;
			modificar = false;
			registrar = true;
			crear = false;
		} else{
			verLista = false;
			modificar = false;
			registrar = true;
			crear = false;
		}
	}

	public void calcularPorcentajePrecio(){
		double precioCompra = selectedDetalleOrdenIngreso.getPrecioCompra();
		//1000*0,25
		double porcentajeContado = selectedProducto.getLineaProducto().getGrupoProducto().getPorcentajeVentaContado() / 100;
		double porcentajeCredito = selectedProducto.getLineaProducto().getGrupoProducto().getPorcentajeVentaCredito() / 100;
		System.out.println(porcentajeContado);
		selectedDetalleOrdenIngreso.setPrecioVentaContado(precioCompra + (precioCompra*porcentajeContado));
		System.out.println(selectedDetalleOrdenIngreso.getPrecioVentaContado());
		selectedDetalleOrdenIngreso.setPrecioVentaCredito(precioCompra + (precioCompra*porcentajeCredito));
		calcular();
	}

	public void cargarDatosDeGestionAnterior(){
		System.out.println("cargarDatosDeGestionAnterior()");
		if(selectedAlmacen.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Seleccione un almacen");
			return;
		}
		//verificar si hay una gestion anterior
		gestionAnterior = obtenerGestionAnterior();
		if(gestionAnterior!=null){
			//si hay, hay que jalar todos los datos de su cierre de almacen en la toma de inventario final
			hayGestionAnterior = true;
			//1. obtener la toma inventario Anterior Gestion (tipo = 'FINAL')
			//TomaInventario tomaAnteriorGestion = tomaInventarioRepository.findByGestionAnteriorAndAlmacen(gestionAnterior,selectedAlmacen);
			//			if(tomaAnteriorGestion == null){
			//				FacesUtil.infoMessage("VALIDACION", "Este almacén no fué cerrado en la gestión pasada.");
			//				return;
			//			}
			//			System.out.println("tomaAnteriorGestion: "+tomaAnteriorGestion);
			//			int var1 = 1;//no cargar todavia
			//			if(tomaAnteriorGestion!=null && 1==var1){
			//DetalleTomaInventario detalle = detalleTomaInventarioRepository.findByTomaInventario(tomaAnteriorGestion);
			//List<DetalleTomaInventario>  detalleTomaInventario = detalleTomaInventarioRepository.findAllActivosByTomaInventario(tomaAnteriorGestion);
			//System.out.println("DetalleTomaInventario : "+detalleTomaInventario.size());

			//unificar producros con precio iguales
			//detalleTomaInventario = unificarProductosConPreciosIguales(detalleTomaInventario);
			//Date fechaActual = new Date();
			//2.cargar los datos para par lista de DetalleTomaInventarioOrdenIngreso
			//				for(DetalleTomaInventario var: detalleTomaInventario){
			//					DetalleOrdenIngreso detalleOI = new DetalleOrdenIngreso();
			//					detalleOI.setCantidad(var.getCantidadRegistrada());
			//					detalleOI.setProducto(var.getProducto());
			//					detalleOI.setObservacion("Levantamiento Inicial");
			//					detalleOI.setPrecioUnitario(var.getPrecioUnitario());
			//					detalleOI.setTotal(var.getTotal());
			//					detalleOI.setEstado("AC");
			//					detalleOI.setFechaRegistro(fechaActual);
			//					listaDetalleOrdenIngreso.add(detalleOI);					
			//				}
			//				listDetalleTomaInventario = detalleTomaInventario;

			//detalle toma inventario
			//editarTomaInventarioIncial = true;
			//verLista = false;
			//modificar = false;
			//registrar = true;
			//verButtonReport = false;
			//newTomaInventario = selectedTomaInventario;
			// ocultar botones
			//buttonEditarTomaInventarioIncial = false;
			//buttonProcesarTomaInventarioIncial = false;
			//}
		}
	}

	private List<DetalleTomaInventario>  unificarProductosConPreciosIguales(List<DetalleTomaInventario>  detalleTomaInventario){
		System.out.println("unificarProductosConPreciosIguales size: "+detalleTomaInventario.size());
		listTemp = new ArrayList<>();
		for(DetalleTomaInventario detalle: detalleTomaInventario){
			Producto prod = detalle.getProducto();
			double precio = detalle.getPrecioVentaContado();
			double stock = detalle.getCantidadRegistrada();

			DetalleTomaInventario det = actualizarProductoEnListDetalleTomaInventario(prod);
			if(det!=null){
				System.out.println("SI- producto: "+prod.getId());
				double precio2 = (det.getPrecioVentaContado() + precio) / 2;
				double stock2 = det.getCantidadRegistrada() + stock;
				det.setCantidadRegistrada(stock2);
				det.setPrecioVentaContado(precio2);
				det.setTotal(stock2*precio2);
			}else{
				listTemp.add(detalle);
			}
		}
		return listTemp;
	}

	private  DetalleTomaInventario actualizarProductoEnListDetalleTomaInventario(Producto producto){
		for(DetalleTomaInventario det: listTemp){	
			if(det.getProducto().equals(producto)){
				return det;
			}
		}
		return null;
	}

	private Gestion obtenerGestionAnterior(){
		//Gestion gestion = gestionRepository.findGestionAnterior(gestionSesion);
		return null;//gestion;
	}

	private boolean existeTomaInventarioIncialByGestion(){
		return false;//tomaInventarioDao.findTIInicialByGestion(gestionSesion);
	}

	//correlativo incremental por gestion
	private String cargarCorrelativo(int nroOrdenIngreso){
		//pather = "000001";
		//Date fecha = new Date(); 
		//String year = new SimpleDateFormat("yy").format(fecha);
		//String mes = new SimpleDateFormat("MM").format(fecha);
		return String.format("%06d", nroOrdenIngreso);
	}

	/**
	 * Procesar Un Inventario Inicial, que antes ya fue creado y cargado los items que tendra un almacen
	 * Actualiza el estado de la orden de ingreso a procesada
	 * Actualiza el estado de la toma de Inventario inicial a procesada
	 * Registra los productos asociando a su almacen resectivo
	 * Registra los stock del prodcto
	 */
	public void procesarTomaInventarioInicial(){
		Date fechaActual = new Date();
		// DetalleTomaInventarioOrdenIngreso
		DetalleTomaInventarioOrdenIngreso detalle = detalleTomaInventarioOrdenIngresoDao.findByTomaInventario(selectedTomaInventario);
		//almacen
		selectedAlmacen = selectedTomaInventario.getAlmacen();
		//orden ingreso
		newOrdenIngreso = detalle.getOrdenIngreso();
		//Actualizar estado de gestion
		gestionSesion.setIniciada(true);
		sessionMain.getGestionLogin().setIniciada(true);//actualaizar la gestion en la session
		detalle.setEstado("AC");
		//procesar OrdenIngreso
		newOrdenIngreso.setEstado("PR");
		newOrdenIngreso.setFechaAprobacion(fechaActual);
		newOrdenIngreso.setUsuarioAprobacion(usuarioSession);
		// actuaizar stock de AlmacenProducto
		listaDetalleOrdenIngreso = detalleOrdenIngresoDao.findAllByOrdenIngreso(newOrdenIngreso);
		Proveedor proveedor = newOrdenIngreso.getProveedor();
		List<AlmacenProducto> listAlmacenProducto = new ArrayList<>();
		List<DetalleProducto> listDetalleProducto = new ArrayList<>();
		for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
			Producto prod = d.getProducto();
			//actualiza el esstock por producto almacen(teniendo en cuenta la agrupacion de productos)
			AlmacenProducto almProd = new AlmacenProducto();
			almProd = new AlmacenProducto();
			almProd.setAlmacen(newOrdenIngreso.getAlmacen());
			almProd.setOrdenIngreso(newOrdenIngreso);
			almProd.setProducto(prod);
			almProd.setProveedor(proveedor);
			almProd.setStock(d.getCantidad());
			almProd.setGarantia(d.getGarantia());
			almProd.setSerie(d.getSerie());
			almProd.setPrecioCompra(d.getPrecioCompra());
			almProd.setPrecioVentaContado(d.getPrecioVentaContado());
			almProd.setPrecioVentaCredito(d.getPrecioVentaCredito());
			almProd.setStockMin(d.getCantidadMinima());
			almProd.setPorcentajeVentaContado(d.getPorcentajeVentaContado());
			almProd.setPorcentajeVentaCredito(d.getPrecioVentaCredito());
			almProd.setEstado("AC");
			almProd.setFechaRegistro(d.getFechaRegistro());
			almProd.setUsuarioRegistro(usuarioSession.getLogin());
			almProd.setGestion(gestionSesion);
			listAlmacenProducto.add(almProd);
			DetalleProducto detalleProducto = new DetalleProducto();
			detalleProducto.setCodigo("OI"+newOrdenIngreso.getCorrelativo()+fechaActual.toString());
			detalleProducto.setAlmacen(newOrdenIngreso.getAlmacen());
			detalleProducto.setEstado("AC");
			detalleProducto.setPrecioVentaContado(d.getPrecioVentaContado());
			detalleProducto.setPrecioVentaCredito(d.getPrecioVentaCredito());
			detalleProducto.setPrecioCompra(d.getPrecioCompra());
			detalleProducto.setStockActual(d.getCantidad());
			detalleProducto.setStockInicial(d.getCantidad());
			detalleProducto.setCorrelativoTransaccion(newOrdenIngreso.getCorrelativo());
			detalleProducto.setFecha(fechaActual);
			detalleProducto.setFechaRegistro(fechaActual);
			detalleProducto.setProducto(d.getProducto());
			detalleProducto.setUsuarioRegistro(usuarioSession.getLogin());
			detalleProducto.setGestion(gestionSesion);
			listDetalleProducto.add(detalleProducto);
		}
		//actualizar estado
		selectedTomaInventario.setFechaRevision(fechaActual);
		selectedTomaInventario.setEstado("PR");
		selectedTomaInventario.setEstadoRevision("SI");
		//newOrdenIngreso
		//selectedTomaInventario
		//gestion
		//listAlmacenProducto
		//listDetalleProducto
		boolean sw = tomaInventarioDao.procesarTomaInventarioInicial(newOrdenIngreso, selectedTomaInventario, gestionSesion, listAlmacenProducto, listDetalleProducto);
		if(sw){
			loadDefault();
		}
	}

	private void procesarOrdenIngreso(){
		try {
			Date fechaActual = new Date();
			// actualizar estado de orden ingreso
			newOrdenIngreso.setEstado("PR");
			newOrdenIngreso.setFechaAprobacion(fechaActual);
			//ordenIngresoRegistration.updated(newOrdenIngreso);
			// actuaizar stock de AlmacenProducto
			listaDetalleOrdenIngreso = detalleOrdenIngresoDao.findAllByOrdenIngreso(newOrdenIngreso);
			Proveedor proveedor = newOrdenIngreso.getProveedor();
			List<AlmacenProducto> listAlmacenProducto = new ArrayList<>();
			for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
				Producto prod = d.getProducto();
				//actualiza el esstock por producto almacen(teniendo en cuenta la agrupacion de productos)
				AlmacenProducto almProd = new AlmacenProducto();
				almProd = new AlmacenProducto();
				almProd.setAlmacen(newOrdenIngreso.getAlmacen());
				almProd.setProducto(prod);
				almProd.setProveedor(proveedor);
				almProd.setStock(d.getCantidad());
				almProd.setPrecioCompra(d.getPrecioCompra());
				almProd.setPrecioVentaContado(d.getPrecioVentaContado());
				almProd.setPrecioVentaCredito(d.getPrecioVentaCredito());
				almProd.setEstado("AC");
				almProd.setFechaRegistro(d.getFechaRegistro());
				almProd.setUsuarioRegistro(usuarioSession.getLogin());
				almProd.setGestion(gestionSesion);
				listAlmacenProducto.add(almProd);
				//almacenProductoDao.registrar(almProd);
				//registra la transaccion de entrada del producto
				//fachadaOrdenIngreso.actualizarKardexProducto(newOrdenIngreso.getCorrelativo(),newOrdenIngreso.getAlmacen(),gestionSesion, prod,fechaActual, d.getCantidad(),d.getPrecioUnitario(),usuarioSession);
				//registra los stock de los producto , para luego utilizar PEPS en ordenes de traspaso y salida
				//fachadaOrdenIngreso.cargarDetalleProducto(gestionSesion,fechaActual,newOrdenIngreso.getAlmacen(),d.getProducto(), d.getCantidad(), d.getPrecioUnitario(), d.getFechaRegistro(), newOrdenIngreso.getCorrelativo(),usuarioSession);
			}
		} catch (Exception e) {
		}
	}

	public void registrarTomaInventarioPrevio(){
		//validaciones
		if( selectedAlmacen.getId().equals(0) || newTomaInventario.getNombreInventariador().isEmpty() || newTomaInventario.getNombreResponsable().isEmpty()){
			FacesUtil.infoMessage("VALIDACION", "No pueden haber campos vacios");
			return;
		}
		if(newTomaInventario.getTipo().equals("INICIAL") && listaDetalleOrdenIngreso.size()==0){
			FacesUtil.infoMessage("VALIDACION", "Debe Agregar items..");
			return;
		}else if(! newTomaInventario.getTipo().equals("INICIAL") && listDetalleTomaInventario.size()==0){
			FacesUtil.infoMessage("VALIDACION", "Debe Agregar items.");
			return;
		}
		if(hayGestionAnterior){
			FacesUtil.showDialog("dlgValidacionProyFunc");
			FacesUtil.updateComponent("formValidacionProyFunc");
			return;
		}
		registrarTomaInventario();
	}

	public void registrarTomaInventario() {
		Date fechaActual = new Date();
		newTomaInventario.setEstado("AC");
		newTomaInventario.setFechaRegistro(fechaActual);
		newTomaInventario.setGestion(gestionSesion);
		newTomaInventario.setAlmacen(selectedAlmacen);
		//newTomaInventario = tomaInventarioRegistration.register(newTomaInventario);
		if(newTomaInventario.getTipo().equals("PARCIAL")){
			for(DetalleTomaInventario detalle : listDetalleTomaInventario){
				detalle.setTomaInventario(newTomaInventario);
				detalle.setFechaRegistro(fechaActual);
				detalle.setUsuarioRegistro(usuarioSession.getLogin());
				//detalleTomaInventarioRegistration.register(detalle);
			}
		}
		//si es de Tipo INICIAL
		List<DetalleTomaInventario> listDetalleTomaInventario = new ArrayList<>();
		DetalleTomaInventarioOrdenIngreso dtioi = new DetalleTomaInventarioOrdenIngreso();
		if(newTomaInventario.getTipo().equals("INICIAL")){
			//registrar ordenIngreso
			registrarOrdenIngreso();
			listDetalleTomaInventario = cargarDatosDetalleTomaInvetario();
			dtioi.setEstado("IN");
			dtioi.setFechaRegistro(fechaActual);
			dtioi.setOrdenIngreso(newOrdenIngreso);
			dtioi.setTomaInventario(newTomaInventario);
			dtioi.setUsuarioRegistro(usuarioSession.getLogin());
			if(hayGestionAnterior){
			}
		}
		boolean sw = tomaInventarioDao.registrar(newTomaInventario,dtioi,newOrdenIngreso,listaDetalleOrdenIngreso,listDetalleTomaInventario);
		if(sw){
			loadDefault();
		}
	}

	public void registrarOrdenIngreso() {
		calcularTotal();
		System.out.println("Ingreso a registrarOrdenIngreso: ");
		int numeroCorrelativo = ordenIngresoDao.obtenerNumeroOrdenIngreso(gestionSesion);
		newOrdenIngreso.setMotivoIngreso("TOMA INVENTARIO INICIAL");
		newOrdenIngreso.setTipoDocumento("SIN DOCUMENTO");
		newOrdenIngreso.setNumeroDocumento("0");
		newOrdenIngreso.setObservacion("Generado a partir de la Toma de Inventario Numero:"+newTomaInventario.getId()+", Inventario Inicial");
		newOrdenIngreso.setCorrelativo(cargarCorrelativo(numeroCorrelativo));
		newOrdenIngreso.setGestion(gestionSesion);
		newOrdenIngreso.setUsuarioRegistro(usuarioSession.getLogin());
		newOrdenIngreso.setEstado("IN");
		newOrdenIngreso.setAlmacen(selectedAlmacen);
		//newOrdenIngreso = ordenIngresoRegistration.register(newOrdenIngreso);
	}

	private List<DetalleTomaInventario> cargarDatosDetalleTomaInvetario(){
		List<DetalleTomaInventario> list = new ArrayList<>();
		for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
			//Registrar detalle toma inventario
			DetalleTomaInventario detalle = new DetalleTomaInventario();
			detalle.setCantidadRegistrada(d.getCantidad());
			detalle.setCantidadVerificada(d.getCantidad());
			detalle.setDiferencia(0d);
			//detalle.setProveedor(newOrdenIngreso.getProveedor());
			detalle.setEstado("AC");
			detalle.setObservacion("Ninguna");
			detalle.setProducto(d.getProducto());
			detalle.setTomaInventario(newTomaInventario);
			detalle.setUsuarioRegistro(usuarioSession.getLogin());
			list.add(detalle);
		}
		return list;
	}

	/**
	 * Modificacion de una Toma de Inventaio, Si cambia el almacen el Nombre del Inventariador o Modifica, agregar y elimina los productos- tambien
	 * si cambia los proveedores de los prodcutos, tanto como ser los precios las cantidades.
	 */
	public void modificarTomaInventario() {
		try {
			System.out.println("-- Ingreso a modificarTomaInvenario ---");
			Date fechaActual = new Date();
			newTomaInventario.setAlmacen(selectedAlmacen);
			newTomaInventario.setEstado("AC");
			tomaInventarioDao.modificar(newTomaInventario);
			System.out.println(" 1 ");
			//tomaInventarioRegistration.updated(newTomaInventario);
			//primero eliminar los items seleccionados
			System.out.println("listDetalleOrdenIngresoEliminados: "+listDetalleOrdenIngresoEliminados.size());
			for(DetalleOrdenIngreso detalleOI2: listDetalleOrdenIngresoEliminados){
				if(!detalleOI2.getId().equals(0)){
					detalleOI2.setEstado("RM");
					detalleOrdenIngresoDao.modificar(detalleOI2);
					//detalleOrdenIngresoRegistration.updated(detalleOI2);
					//actualizar DetalleTomaInventario
					DetalleTomaInventario detalleAux = obtenerDetalleTomaInventarioByProducto( detalleOI2.getProducto());
					detalleAux.setEstado("RM");
					//detalleTomaInventarioRegistration.updated(detalleAux);
					detalleTomaInventarioDao.modificar(detalleAux);
				}
			}
			System.out.println("listaDetalleOrdenIngreso: "+listaDetalleOrdenIngreso.size());
			//actualizar y registrar nuevos y modificaciones de ites
			for(DetalleOrdenIngreso detalleOI1 : listaDetalleOrdenIngreso){
				System.out.println("detalleOI1 id:"+detalleOI1.getId());
				if(detalleOI1.getId().equals(0)){
					System.out.println("por if");
					detalleOI1.setEstado("AC");
					detalleOI1.setFechaRegistro(fechaActual);
					detalleOI1.setOrdenIngreso(newOrdenIngreso);
					detalleOI1.setUsuarioRegistro(usuarioSession.getLogin());
					detalleOI1 = detalleOrdenIngresoDao.registrarBasic(detalleOI1);
					//detalleOrdenIngresoRegistration.register(detalleOI1);
					//nuevo DetalleTomaInventario
					DetalleTomaInventario detalle = new DetalleTomaInventario();
					detalle.setCantidad((int)detalleOI1.getCantidad());
					detalle.setCantidadRegistrada(detalleOI1.getCantidad());
					detalle.setCantidadVerificada(detalleOI1.getCantidad());
					detalle.setDiferencia(0d);
					detalle.setEstado("AC");
					detalle.setFechaRegistro(fechaActual);
					detalle.setProducto(detalleOI1.getProducto());
					detalle.setTomaInventario(newTomaInventario);
					detalle.setUsuarioRegistro(usuarioSession.getLogin());
					//detalle = detalleTomaInventarioRegistration.register(detalle);
					detalle = detalleTomaInventarioDao.registrarBasic(detalle);
					//listDetalleTomaInventario.add(detalle);
				}else{
					System.out.println("por else");
					System.out.println("producto: id:"+detalleOI1.getId()+" - cantidad:"+detalleOI1.getCantidad()+" - codigo:"+detalleOI1.getProducto().getCodigo());
					detalleOrdenIngresoDao.modificar(detalleOI1);
					System.out.println("p ------ 2 ----");
					//detalleOrdenIngresoRegistration.updated(detalleOI1);
					DetalleTomaInventario detalleAux = obtenerDetalleTomaInventarioByProducto( detalleOI1.getProducto());
					detalleAux.setCantidad((int)detalleOI1.getCantidad());
					detalleAux.setCantidadRegistrada(detalleOI1.getCantidad());
					detalleAux.setCantidadVerificada(detalleOI1.getCantidad());
					detalleAux.setProducto(detalleOI1.getProducto());
					detalleAux.setPrecioCompra(detalleOI1.getPrecioCompra());
					detalleAux.setPrecioVentaContado(detalleOI1.getPrecioVentaContado());
					detalleAux.setPrecioVentaCredito(detalleOI1.getPrecioVentaCredito());
					System.out.println("** producto: id:"+detalleAux.getId()+" - cantidad:"+detalleAux.getCantidad()+" - codigo:"+detalleAux.getProducto().getCodigo()+" **");
					detalleTomaInventarioDao.modificar(detalleAux);
					System.out.println("p ------ 3 ----");
					//	detalleTomaInventarioRegistration.updated(detalleAux);
				}
			}
			FacesUtil.infoMessage("Orden de Ingreso Modificada", "");
			loadDefault();
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println("Error "+e.getMessage());
			FacesUtil.errorMessage("Error al Modificar.");
		}
	}

	private DetalleTomaInventario obtenerDetalleTomaInventarioByProducto(Producto producto){
		for(DetalleTomaInventario detalle : listDetalleTomaInventario){
			if(detalle.getProducto().getId().equals(producto.getId())){
				return detalle;
			}
		}
		return null;
	}

	public void eliminarTomaInventario() {
		try {
			System.out.println("Ingreso a eliminarOrdenIngreso: ");

			FacesUtil.infoMessage("Orden de Ingreso Eliminada!", "");
			loadDefault();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Eliminar.");
		}
	}

	//cierre de almacen por gestion
	public void cerrarAlmacenPorGestion(){
		//validaciones
		if(selectedAlmacen.getId().equals(0)){
			FacesUtil.infoMessage("VALIDACION", "Seleccione un Almacém");
			return;
		}
		if(  newTomaInventario.getNombreInventariador().isEmpty() ||  newTomaInventario.getNombreResponsable().isEmpty() ){
			FacesUtil.infoMessage("VALIDACION", "No pueden haber campos vacios");
			return;
		}

		try{
			System.out.println("cerrarAlmacenPorGestion()");
			//1, registrar  toma invenario con estado= CE
			Date fechaActual = new Date();
			newTomaInventario.setAlmacen(selectedAlmacen);
			newTomaInventario.setEstado("CE");
			newTomaInventario.setFechaRegistro(fechaActual);
			newTomaInventario.setGestion(gestionSesion);
			//newTomaInventario = tomaInventarioRegistration.register(newTomaInventario);
			for(DetalleTomaInventario detalle : listDetalleTomaInventario){
				double diferencia = 0;
				String observacion = "CIERRE";
				double registrada = detalle.getCantidadRegistrada();
				detalle.setCantidadVerificada(registrada);
				detalle.setDiferencia(diferencia);
				detalle.setObservacion(observacion);
				detalle.setTomaInventario(newTomaInventario);
				detalle.setFechaRegistro(fechaActual);
				detalle.setUsuarioRegistro(usuarioSession.getLogin());
				//detalleTomaInventarioRegistration.register(detalle);
			}
			//2. registrar objeto de cierre
			CierreGestionAlmacen cierre = new CierreGestionAlmacen();
			cierre.setEstado("AC");
			cierre.setFechaRegistro(fechaActual);
			cierre.setUsuarioRegistro(usuarioSession.getLogin());
			cierre.setGestion(gestionSesion);
			//cierre.setAlmacen(newTomaInventario.getAlmacen());
			cierre.setTomaInventario(newTomaInventario);
			//cierre = CierreGestionAlmacenRegistration.register(cierre);
			FacesUtil.infoMessage("CIERRE DE ALMACEN CORRECTO", newTomaInventario.getAlmacen()+" CERRADO - GESTION "+gestionSesion.getGestion());
			loadDefault();
		}catch(Exception e){
			System.out.println("cerrarAlmacenPorGestion ERROR: "+e.getMessage());
		}
	}

	public void calcularTotal(){
		double totalImporte = 0;
		for(DetalleOrdenIngreso d : listaDetalleOrdenIngreso){
			totalImporte =totalImporte + d.getTotal();
		}
		newOrdenIngreso.setTotalImporte(totalImporte);
	}

	public void procesarConsulta(){
		try {
			if(selectedAlmacen.getId().equals(0)){
				FacesUtil.infoMessage("INFORMACION", "Seleccione un almacen ");
				return;
			}
			//verificar si el almacen-gestion ya fue cerrado
			//			if(cierreGestionAlmacenRepository.finAlmacenGestionCerrado(selectedAlmacen,gestionSesion) != null){
			//				FacesUtil.infoMessage("INFORMACION", "El lmacen "+selectedAlmacen.getNombre()+" fué cerrado");
			//				return ;
			//			}

			List<AlmacenProducto> listAlmacenProducto = almacenProductoDao.findByAlmacen(gestionSesion,selectedAlmacen);
			if(listAlmacenProducto.size()==0){//validacion de almacen
				FacesUtil.infoMessage("INFORMACION", "No se encontraron existencias en el almacen "+selectedAlmacen.getNombre());
				return ;
			}
			listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
			for(AlmacenProducto ap : listAlmacenProducto){
				DetalleTomaInventario detalle = new DetalleTomaInventario();
				detalle.setProducto(ap.getProducto());
				detalle.setCantidadRegistrada(ap.getStock());
				detalle.setPrecioCompra(ap.getPrecioCompra());
				detalle.setPrecioVentaContado(ap.getPrecioVentaContado());
				detalle.setPrecioVentaCredito(ap.getPrecioVentaCredito());
				detalle.setTotal(ap.getPrecioVentaContado()*ap.getStock());
				listDetalleTomaInventario.add(detalle);
			}
			//unificar producros con precio iguales
			listDetalleTomaInventario = unificarProductosConPreciosIguales(listDetalleTomaInventario);
			if(newTomaInventario.getTipo().equals("FINAL")){
				cierreAlmacen = true;
			}else{
				cierreAlmacen = false;
				verGuardar = listDetalleTomaInventario.size()>0?true:false;
			}
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Procesar!");
		}
	}

	public void buttonConciliarTomaInventario(){
		System.out.println("buttonConciliarTomaInventario()");
		try {
			newTomaInventario = selectedTomaInventario;
			selectedAlmacen = selectedTomaInventario.getAlmacen();
			listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
			listDetalleTomaInventario = detalleTomaInventarioDao.findAllByTomaInventario(selectedTomaInventario);

			verLista = false;
			modificar = false;
			registrar = false;
			verButtonReport = false;
			buttonConciliar = false;
			conciliar = true;
		} catch (Exception e) {
			System.out.println("ERROR "+e.getMessage());
		}
	}

	public void conciliarTomaInventario(){
		try{
			if(listSelectedDetalleTomaInventario.size()>0){
				Date fechaActual = new Date();
				selectedTomaInventario.setEstado("CN");//CONCILIADO
				//tomaInventarioRegistration.updated(selectedTomaInventario);
				BajaProducto baja = new BajaProducto();
				Almacen almacen = selectedTomaInventario.getAlmacen();
				System.out.println("conciliarTomaInventario() size:"+listSelectedDetalleTomaInventario.size());
				for(DetalleTomaInventario d: listSelectedDetalleTomaInventario){
					System.out.println("d :"+d.getProducto().getNombre());
					baja = new BajaProducto();
					baja.setDetalleTomaInventario(d);
					baja.setEstado("AC");
					baja.setFechaRegistro(fechaActual);
					baja.setObservacion(d.getObservacion());
					baja.setStockActual(d.getCantidadVerificada());
					baja.setStockAnterior(d.getCantidadRegistrada());
					baja.setUsuarioRegistro(usuarioSession.getLogin());

					//actualizar en DetalleProducto
					if(d.getCantidadRegistrada() - d.getCantidadVerificada() > 0){//si faltaron
						//actualizar en AlmacenProducto
						//fachadaOrdenTraspaso.actualizarStock(gestionSesion,almacen,d);//-1 para que no actualize el precio
						//fachadaOrdenSalida.actualizarDetalleProducto(gestionSesion,selectedTomaInventario.getAlmacen(), d.getProducto(), d.getDiferencia());
						//actualizar en kardex(NOSE) como una salida (como baja de producto)
						//fachadaOrdenSalida.actualizarKardexProducto("Por Baja de Producto", gestionSesion, selectedOrdenSalida, prod, fechaActual, cantidad, precioUnitario, usuarioSession);
						//bajaProductoRegistration.register(baja);

					}
				}
				FacesUtil.infoMessage("INFORMACION", "Toma Inventario "+selectedTomaInventario.getId()+" Conciliada.");
				loadDefault();
			}else{
				FacesUtil.infoMessage("INFORMACION", "Seleccione los items para conciliarlos");
			}
		}catch(Exception e){
			System.out.println("ERROR "+e.getMessage());
		}
	}

	public void buttonRevisar(){
		try {
			newTomaInventario = selectedTomaInventario;
			selectedAlmacen = selectedTomaInventario.getAlmacen();
			listDetalleTomaInventario = new ArrayList<DetalleTomaInventario>();
			//listDetalleTomaInventario = detalleTomaInventarioRepository.findAllByTomaInventario(selectedTomaInventario);

			verLista = false;
			modificar = true;
			registrar = false;
			verButtonReport = false;
			if(selectedTomaInventario.getTipo().equals("INICIAL")){
				cargarSelectedTomaInventario();
			}
		} catch (Exception e) {
			System.out.println("ERROR "+e.getMessage());
		}
	}

	public void verificacionTomaInventario(){
		try{
			newTomaInventario.setEstadoRevision("SI");
			newTomaInventario.setEstado("RE");
			newTomaInventario.setFechaRevision(new Date());
			//			tomaInventarioRegistration.updated(newTomaInventario);
			//			for(DetalleTomaInventario detalle : listDetalleTomaInventario){
			//				detalleTomaInventarioRegistration.updated(detalle);
			//			}
			FacesUtil.infoMessage("Toma Inventario Revisada", "");
			loadDefault();			
		}catch(Exception e){
			System.out.println("ERROR "+e.getMessage());
		}
	}

	public void cargarReporte(){
		Map<String,String> map1 = new HashMap<>();
		map1.put("pRazonSocial", empresaLogin.getRazonSocial());
		map1.put("pDireccion", empresaLogin.getDireccion());
		map1.put("pTelefono", empresaLogin.getTelefono());
		map1.put("pIdEmpresa", String.valueOf(empresaLogin.getId()));
		map1.put("pIdTomaInventario", String.valueOf(selectedTomaInventario.getId()));
		map1.put("pUsuario", usuarioSession.getLogin());
		urlTomaInventario = buildUrl("ReporteTomaInventario",map1);
		verReport = true;
		verLista = false;
		modificar = false;
		registrar = false;
		revisarReport = false;
	}

	public void cargarSelectedTomaInventario(){
		// DetalleTomaInventarioOrdenIngreso
		DetalleTomaInventarioOrdenIngreso detalle = detalleTomaInventarioOrdenIngresoDao.findByTomaInventario(selectedTomaInventario);
		//almacen
		selectedAlmacen = selectedTomaInventario.getAlmacen();
		//orden ingreso
		newOrdenIngreso = detalle.getOrdenIngreso();
		//proveedor
		//selectedProveedor = newOrdenIngreso.getProveedor();
		//detalle orden ingreso
		listaDetalleOrdenIngreso = detalleOrdenIngresoDao.findAllByOrdenIngreso(newOrdenIngreso);

		//detalle toma inventario
		editarTomaInventarioIncial = true;
		verLista = false;
		modificar = false;
		registrar = true;
		verButtonReport = false;
		newTomaInventario = selectedTomaInventario;
		// ocultar botones
		buttonEditarTomaInventarioIncial = false;
		buttonProcesarTomaInventarioIncial = false;

		//
		listDetalleTomaInventario = detalleTomaInventarioDao.findAllByTomaInventario(selectedTomaInventario);
	}

	public void onRowSelectTomaInventarioClick(SelectEvent event){
		verButtonReport = true;
		crear = false;
		if(selectedTomaInventario.getTipo().equals("FINAL") ){
			buttonConciliar = false;
			revisarReport = false;
			return;			
		}
		if(selectedTomaInventario.getTipo().equals("INICIAL")){
			if(selectedTomaInventario.getEstado().equals("AC")){
				System.out.println("TomaInventario Incial - Modificar - Procesar");
				buttonEditarTomaInventarioIncial = true;
				buttonProcesarTomaInventarioIncial = true;
			}else{
				buttonEditarTomaInventarioIncial = false;
				buttonProcesarTomaInventarioIncial = false;
			}
			return;
		}
		if(selectedTomaInventario.getEstadoRevision().equals("NO")){
			revisarReport = true;
			buttonConciliar = false;
		}else{
			revisarReport = false;
			if(selectedTomaInventario.getEstado().equals("CN")){
				buttonConciliar = false;
			}else{
				buttonConciliar = true;
			}
		}
	}

	// ONCOMPLETETEXT ALMACEN
	public List<Almacen> completeAlmacen(String query) {
		String upperQuery = query.toUpperCase();
		//listaAlmacen = almacenDao.findAllAlmacenForQueryNombre(upperQuery);
		listaAlmacen = almacenDao.findAllAlmacenForQueryNombre(upperQuery);
		return listaAlmacen;
	}

	public void onRowSelectAlmacenClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Almacen i : listaAlmacen){
			if(i.getNombre().equals(nombre)){
				selectedAlmacen = i;
				newTomaInventario.setNombreResponsable(usuarioSession.getNombre());
				return;
			}
		}
	}

	//PRODUCTO
	public void registrarProducto() {
		System.out.println("Ingreso a registrarProducto: ");
		newProducto.setEstado("AC");
		newProducto.setFechaRegistro(new Date());
		newProducto.setUsuarioRegistro(usuarioSession.getLogin());
		newProducto.setUsuarioRegistro(usuarioSession.getLogin());
		newProducto.setFechaRegistro(new Date());
		System.out.println(newProducto.toString());
		Producto p = productoDao.registrar(newProducto);
		if(p != null){
			setSelectedProducto(p);
			calcular();
			setNuevoProducto(false);
			newProducto = new Producto();
		}

	}

	//calcular totales
	public void calcular(){
		double precio = selectedDetalleOrdenIngreso.getPrecioVentaContado();//.getPrecioUnitario();
		double precioCompra = selectedDetalleOrdenIngreso.getPrecioCompra();
		double cantidad = selectedDetalleOrdenIngreso.getCantidad();
		System.out.println("calcular() p="+precio+",c="+cantidad);
		selectedDetalleOrdenIngreso.setTotal(precio * cantidad);
		selectedDetalleOrdenIngreso.setTotalCompra(precioCompra * cantidad);
	}

	private List<Producto> listProducto = new ArrayList<Producto>();
	// ONCOMPLETETEXT PRODUCTO
	public List<Producto> completeProducto(String query) {
		if(selectedProveedor.getId().equals(0)){
			FacesUtil.infoMessage("", "Antes seleccione un Proveedor");
			return new ArrayList<>();
		}
		String upperQuery = query.toUpperCase();
		//listProducto =  productoRepository.findAllProductoForQueryNombre(upperQuery);
		listProducto =  productoDao.findAllProductoForQueryNombre(upperQuery);
		return listProducto;
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		Integer id = ((Producto) event.getObject()).getId();
		for(Producto i : listProducto){
			if(i.getId().equals(id)){
				selectedProducto = i;
				calcular();
				return;
			}
		}
	}

	// ONCOMPLETETEXT PROVEEDOR
	public List<Proveedor> completeProveedor(String query) {
		String upperQuery = query.toUpperCase();
		//listProveedor = proveedorRepository.findAllProveedorForQueryNombre(upperQuery,gestionSesion);
		listProveedor = proveedorDao.findAllProveedorForQueryNombre(upperQuery,gestionSesion);
		return listProveedor;
	}

	public void onRowSelectProveedorClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Proveedor i : listProveedor){
			if(i.getNombre().equals(nombre)){
				selectedProveedor = i;
				//newOrdenIngreso.setProveedor(selectedProveedor);
				return;
			}
		}
	}

	private DetalleTomaInventario selectedDetalleTomaInventario;

	// DETALLE ORDEN INGRESO ITEMS
	public void editarDetalleOrdenIngreso(Integer id){

		for(DetalleOrdenIngreso s:listaDetalleOrdenIngreso){
			if(s.getId().equals(id)){
				selectedDetalleOrdenIngreso = s;
			}
		}
		for(DetalleTomaInventario dti : listDetalleTomaInventario ){
			if(dti.getProducto().getId().equals(selectedDetalleOrdenIngreso.getProducto().getId())){
				selectedDetalleTomaInventario = dti;
			}
		}
		selectedProveedor = selectedTomaInventario.getProveedor();
		selectedProducto = selectedDetalleOrdenIngreso.getProducto();
		setVerButtonDetalle(true);
		setEditarOrdenIngreso(true);
		calcular();
		FacesUtil.updateComponent("formDialogProducto");
		FacesUtil.showDialog("dlgProducto");
	}

	public void borrarDetalleOrdenIngreso(Integer id){
		for(DetalleOrdenIngreso s:listaDetalleOrdenIngreso){
			if(s.getId().equals(id)){
				selectedDetalleOrdenIngreso = s;
			}
		}
		listaDetalleOrdenIngreso.remove(selectedDetalleOrdenIngreso);
		listDetalleOrdenIngresoEliminados.add(selectedDetalleOrdenIngreso);
		setVerButtonDetalle(true);
		FacesUtil.updateComponent("form001");
	}

	private boolean verificarProductoAgregado(Producto selectedProducto){
		for(DetalleOrdenIngreso detalle : listaDetalleOrdenIngreso){
			if(detalle.getProducto().equals(selectedProducto)){
				return true;
			}
		}
		return false;
	}

	public void modificarDetalleOrdenIngreso(){
		System.out.println("modificarDetalleOrdenIngreso ");
		//validaciones
		if(selectedDetalleOrdenIngreso.getPrecioCompra()<0 || selectedProducto.getId().equals(0) || selectedProveedor.getId().equals(0)){
			FacesUtil.infoMessage("OBSERVACION", "Datos no validos.");
			return ;
		}
		//verificar si el producto ya fue agregado
		if(verificarProductoAgregado(selectedProducto)){
			FacesUtil.infoMessage("OBSERVACION", "Este producto ya fue agregado.");
			return ;
		}
		for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
			if(d.equals(selectedDetalleOrdenIngreso)){
				d = selectedDetalleOrdenIngreso;
			}
		}
		for(DetalleTomaInventario dti : listDetalleTomaInventario){
			if(dti.getId().equals(selectedDetalleTomaInventario.getId())){
				dti = selectedDetalleTomaInventario;
			}
		}
		selectedProducto = new Producto();
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		FacesUtil.updateComponent("form001");
		setVerButtonDetalle(true);
		setEditarOrdenIngreso(false);
	}

	// SELECT DETALLE ORDEN INGRESO CLICK
	public void onRowSelectDetalleOrdenIngresoClick(SelectEvent event) {
		try {
			verButtonDetalle = false;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectOrdenIngresoClick: "
					+ e.getMessage());
		}
	}

	//UTIL

	/**
	 * Method buildUrl
	 * @param nameReport Nombre Reporte
	 * @param params Parametros Clave Valor
	 * @return url format HTTP
	 * @author mbr.bejarano@gmail.com
	 */
	private String buildUrl(String nameReport,Map<String, String> params) {
		try{
			HttpServletRequest request = (HttpServletRequest) facesContext
					.getExternalContext().getRequest();
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length()
					- request.getRequestURI().length())
					+ request.getContextPath() + "/";
			StringBuilder url = new StringBuilder();
			url.append(urlPath);
			url.append(nameReport);
			url.append("?");
			boolean sw = false;
			for(Map.Entry<String, String> param : params.entrySet()){
				if(sw){ url.append("&"); }
				url.append(param.getKey());
				url.append("=");
				url.append(URLEncoder.encode(param.getValue(),"ISO-8859-1"));
				sw = true;
			}
			System.out.println("url: "+url.toString());
			return url.toString();
		}catch(Exception e){
			return "";
		}
	}


	// -------- get and set -------
	public String getTituloPanel() {
		return tituloPanel;
	}

	public void setTituloPanel(String tituloPanel) {
		this.tituloPanel = tituloPanel;
	}

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

	public List<Usuario> getListUsuario() {
		return listUsuario;
	}

	public void setListUsuario(List<Usuario> listUsuario) {
		this.listUsuario = listUsuario;
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

	public List<Almacen> getListaAlmacen() {
		return listaAlmacen;
	}

	public void setListaAlmacen(List<Almacen> listaAlmacen) {
		this.listaAlmacen = listaAlmacen;
	}

	public List<Proveedor> getListaProveedor() {
		return listaProveedor;
	}

	public void setListaProveedor(List<Proveedor> listaProveedor) {
		this.listaProveedor = listaProveedor;
	}

	public boolean isVerProcesar() {
		return verProcesar;
	}

	public void setVerProcesar(boolean verProcesar) {
		this.verProcesar = verProcesar;
	}

	public boolean isVerReport() {
		return verReport;
	}

	public void setVerReport(boolean verReport) {
		this.verReport = verReport;
	}

	public TomaInventario getNewTomaInventario() {
		return newTomaInventario;
	}

	public void setNewTomaInventario(TomaInventario newTomaInventario) {
		this.newTomaInventario = newTomaInventario;
	}

	public TomaInventario getSelectedTomaInventario() {
		return selectedTomaInventario;
	}

	public void setSelectedTomaInventario(TomaInventario selectedTomaInventario) {
		this.selectedTomaInventario = selectedTomaInventario;
	}

	public List<DetalleTomaInventario> getListDetalleTomaInventario() {
		return listDetalleTomaInventario;
	}

	public void setListDetalleTomaInventario(
			List<DetalleTomaInventario> listDetalleTomaInventario) {
		this.listDetalleTomaInventario = listDetalleTomaInventario;
	}

	public List<TomaInventario> getListTomaInventario() {
		return listTomaInventario;
	}

	public void setListTomaInventario(List<TomaInventario> listTomaInventario) {
		this.listTomaInventario = listTomaInventario;
	}

	public List<AlmacenProducto> getListAlmacenProducto() {
		return listAlmacenProducto;
	}

	public void setListAlmacenProducto(List<AlmacenProducto> listAlmacenProducto) {
		this.listAlmacenProducto = listAlmacenProducto;
	}

	public String getUrlTomaInventario() {
		return urlTomaInventario;
	}

	public void setUrlTomaInventario(String urlTomaInventario) {
		this.urlTomaInventario = urlTomaInventario;
	}

	public boolean isRevisarReport() {
		return revisarReport;
	}

	public void setRevisarReport(boolean revisarReport) {
		this.revisarReport = revisarReport;
	}

	public boolean isVerGuardar() {
		return verGuardar;
	}

	public void setVerGuardar(boolean verGuardar) {
		this.verGuardar = verGuardar;
	}

	public boolean isVerButtonReport() {
		return verButtonReport;
	}

	public void setVerButtonReport(boolean verButtonReport) {
		this.verButtonReport = verButtonReport;
	}

	public boolean isVerLista() {
		return verLista;
	}

	public void setVerLista(boolean verLista) {
		this.verLista = verLista;
	}

	public String getTipoTomaInventario() {
		return tipoTomaInventario;
	}

	public void setTipoTomaInventario(String tipoTomaInventario) {
		this.tipoTomaInventario = tipoTomaInventario;
	}

	public boolean isNuevoProducto() {
		return nuevoProducto;
	}

	public void setNuevoProducto(boolean nuevoProducto) {
		this.nuevoProducto = nuevoProducto;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public DetalleOrdenIngreso getSelectedDetalleOrdenIngreso() {
		return selectedDetalleOrdenIngreso;
	}

	public void setSelectedDetalleOrdenIngreso(
			DetalleOrdenIngreso selectedDetalleOrdenIngreso) {
		this.selectedDetalleOrdenIngreso = selectedDetalleOrdenIngreso;
	}

	public List<Producto> getListProducto() {
		return listProducto;
	}

	public void setListProducto(List<Producto> listProducto) {
		this.listProducto = listProducto;
	}

	public List<DetalleOrdenIngreso> getListaDetalleOrdenIngreso() {
		return listaDetalleOrdenIngreso;
	}

	public void setListaDetalleOrdenIngreso(List<DetalleOrdenIngreso> listaDetalleOrdenIngreso) {
		this.listaDetalleOrdenIngreso = listaDetalleOrdenIngreso;
	}

	public String getTituloProducto() {
		return tituloProducto;
	}

	public void setTituloProducto(String tituloProducto) {
		this.tituloProducto = tituloProducto;
	}

	public boolean isVerButtonDetalle() {
		return verButtonDetalle;
	}

	public void setVerButtonDetalle(boolean verButtonDetalle) {
		this.verButtonDetalle = verButtonDetalle;
	}

	public boolean isEditarOrdenIngreso() {
		return editarOrdenIngreso;
	}

	public void setEditarOrdenIngreso(boolean editarOrdenIngreso) {
		this.editarOrdenIngreso = editarOrdenIngreso;
	}

	public Producto getNewProducto() {
		return newProducto;
	}

	public void setNewProducto(Producto newProducto) {
		this.newProducto = newProducto;
	}

	public List<Proveedor> getListProveedor() {
		return listProveedor;
	}

	public void setListProveedor(List<Proveedor> listProveedor) {
		this.listProveedor = listProveedor;
	}

	public Proveedor getSelectedProveedor() {
		return selectedProveedor;
	}

	public void setSelectedProveedor(Proveedor selectedProveedor) {
		this.selectedProveedor = selectedProveedor;
	}

	public OrdenIngreso getNewOrdenIngreso() {
		return newOrdenIngreso;
	}

	public void setNewOrdenIngreso(OrdenIngreso newOrdenIngreso) {
		this.newOrdenIngreso = newOrdenIngreso;
	}

	public Gestion getGestionSesion() {
		return gestionSesion;
	}

	public void setGestionSesion(Gestion gestionSesion) {
		this.gestionSesion = gestionSesion;
	}

	public List<String> getListTipo() {
		return listTipo;
	}

	public void setListTipo(List<String> listTipo) {
		this.listTipo = listTipo;
	}

	public boolean isConciliar() {
		return conciliar;
	}

	public void setConciliar(boolean conciliar) {
		this.conciliar = conciliar;
	}

	public boolean isButtonConciliar() {
		return buttonConciliar;
	}

	public void setButtonConciliar(boolean buttonConciliar) {
		this.buttonConciliar = buttonConciliar;
	}

	public List<DetalleTomaInventario> getListSelectedDetalleTomaInventario() {
		return listSelectedDetalleTomaInventario;
	}

	public void setListSelectedDetalleTomaInventario(
			List<DetalleTomaInventario> listSelectedDetalleTomaInventario) {
		this.listSelectedDetalleTomaInventario = listSelectedDetalleTomaInventario;
	}

	public boolean isCierreAlmacen() {
		return cierreAlmacen;
	}

	public void setCierreAlmacen(boolean cierreAlmacen) {
		this.cierreAlmacen = cierreAlmacen;
	}

	public boolean isButtonEditarTomaInventarioIncial() {
		return buttonEditarTomaInventarioIncial;
	}

	public void setButtonEditarTomaInventarioIncial(
			boolean buttonEditarTomaInventarioIncial) {
		this.buttonEditarTomaInventarioIncial = buttonEditarTomaInventarioIncial;
	}

	public boolean isButtonProcesarTomaInventarioIncial() {
		return buttonProcesarTomaInventarioIncial;
	}

	public void setButtonProcesarTomaInventarioIncial(
			boolean buttonProcesarTomaInventarioIncial) {
		this.buttonProcesarTomaInventarioIncial = buttonProcesarTomaInventarioIncial;
	}

	public List<DetalleTomaInventario> getListDetalleTomaInventarioEliminadas() {
		return listDetalleTomaInventarioEliminadas;
	}

	public void setListDetalleTomaInventarioEliminadas(
			List<DetalleTomaInventario> listDetalleTomaInventarioEliminadas) {
		this.listDetalleTomaInventarioEliminadas = listDetalleTomaInventarioEliminadas;
	}

	public boolean isEditarTomaInventarioIncial() {
		return editarTomaInventarioIncial;
	}

	public void setEditarTomaInventarioIncial(boolean editarTomaInventarioIncial) {
		this.editarTomaInventarioIncial = editarTomaInventarioIncial;
	}

	public boolean isHayGestionAnterior() {
		return hayGestionAnterior;
	}

	public void setHayGestionAnterior(boolean hayGestionAnterior) {
		this.hayGestionAnterior = hayGestionAnterior;
	}

	public boolean isStateProyecto() {
		return stateProyecto;
	}

	public void setStateProyecto(boolean stateProyecto) {
		this.stateProyecto = stateProyecto;
	}

	public boolean isStateFuncionario() {
		return stateFuncionario;
	}

	public void setStateFuncionario(boolean stateFuncionario) {
		this.stateFuncionario = stateFuncionario;
	}

	public boolean isStateProveedor() {
		return stateProveedor;
	}

	public void setStateProveedor(boolean stateProveedor) {
		this.stateProveedor = stateProveedor;
	}

	public boolean isStateDetalleUnidad() {
		return stateDetalleUnidad;
	}

	public void setStateDetalleUnidad(boolean stateDetalleUnidad) {
		this.stateDetalleUnidad = stateDetalleUnidad;
	}

}
