package com.erp360.controller;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.erp360.dao.OrdenIngresoDao;
import com.erp360.dao.ProductoDao;
import com.erp360.dao.ProveedorDao;
import com.erp360.dao.UnidadMedidaDao;
import com.erp360.dao.UsuarioDao;
import com.erp360.model.Almacen;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.DetalleOrdenIngreso;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.OrdenIngreso;
import com.erp360.model.Producto;
import com.erp360.model.Proveedor;
import com.erp360.model.UnidadMedida;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.PageUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Named(value = "ordenIngresoController")
@ConversationScoped
public class OrdenIngresoController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private @Inject FacesContext facesContext;

	//DAO
	private @Inject AlmacenDao almacenDao;
	private @Inject UsuarioDao usuarioDao;
	private @Inject OrdenIngresoDao ordenIngresaDao;
	private @Inject ProveedorDao proveedorDao;
	private @Inject ProductoDao productoDao;
	private @Inject DetalleOrdenIngresoDao detalleOrdenIngresoDao;
	private @Inject UnidadMedidaDao unidadMedidaDao;
	private @Inject AlmacenProductoDao almacenProductoDao;
	//private @Inject DetalleProductoDao detalleProductoDao;

	//STATE
	private boolean crear ;
	private boolean registrar ;
	private boolean modificar ;
	private boolean verReport;

	private boolean editarOrdenIngreso;
	private boolean verProcesar;
	private boolean nuevoProducto;
	private boolean devolucion;
	private boolean verButtonAnular;
	private boolean atencionCliente ;

	//VAR
	private String urlOrdenIngreso ;
	private double precioPromedio;
	private boolean verButtonDetalle = true;
	private boolean ocultarProveedor;

	//OBJECT
	private Proveedor selectedProveedor;
	private Producto selectedProducto;
	private Almacen selectedAlmacen;
	private Almacen selectedAlmacenOrigen;
	private OrdenIngreso selectedOrdenIngreso;
	private OrdenIngreso newOrdenIngreso;
	private DetalleOrdenIngreso selectedDetalleOrdenIngreso;
	private UnidadMedida selectedUnidadMedida;
	private Producto newProducto;

	//LIST
	private List<Usuario> listUsuario;
	private List<DetalleOrdenIngreso> listaDetalleOrdenIngreso;
	private List<OrdenIngreso> listaOrdenIngreso;
	private List<Almacen> listaAlmacen;
	private List<Proveedor> listaProveedor;
	private List<DetalleOrdenIngreso> listDetalleOrdenIngresoEliminados ;
	private List<UnidadMedida> listUnidadMedida ;
	private List<Producto> listProducto;
	private List<Proveedor> listProveedor = new ArrayList<Proveedor>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Usuario usuarioSession;
	private Gestion gestionSesion;
	private Empresa empresaLogin;

	@PostConstruct
	public void init() {
		usuarioSession = sessionMain.getUsuarioLogin();
		gestionSesion = sessionMain.getGestionLogin();
		listUsuario = usuarioDao.obtenerUsuarioOrdenAscPorId();
		empresaLogin = sessionMain.getEmpresaLogin();
		loadDefault();
	}

	public void loadDefault(){
		//inicializar fachadaOrdenIngreso
		//fachadaOrdenIngreso = new FachadaOrdenIngreso();

		urlOrdenIngreso = "";

		selectedProducto = new Producto();
		selectedAlmacen = new Almacen();
		selectedProveedor = new Proveedor();

		selectedOrdenIngreso = new OrdenIngreso();
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		precioPromedio = 0;

		crear = true;
		registrar = false;
		modificar = false;
		verReport = false;

		devolucion = false;//tipo DEVOLUCION PARA SACAR PRECIO PROMEDIO(SE PUSO TODOs EN FALSO PARA QUE NO PROMEDIE)
		verButtonAnular= false;
		editarOrdenIngreso = false;
		atencionCliente=false;
		verProcesar = true;
		nuevoProducto = false;

		listProducto = new ArrayList<>();
		listUnidadMedida = new ArrayList<UnidadMedida>();
		listaDetalleOrdenIngreso = new ArrayList<DetalleOrdenIngreso>();
		listaOrdenIngreso = ordenIngresaDao.findAllOrderedByIDGestion(gestionSesion);
		listaAlmacen = almacenDao.obtenerTodosActivosOrdenadosPorId();
		listaProveedor = proveedorDao.obtenerProveedorOrdenAscPorId();
		listDetalleOrdenIngresoEliminados = new ArrayList<>();

		int numeroCorrelativo = ordenIngresaDao.obtenerNumeroOrdenIngreso(gestionSesion);
		newOrdenIngreso = new OrdenIngreso();
		//newOrdenIngreso.setCorrelativo(cargarCorrelativo(listaOrdenIngreso.size()+1));
		newOrdenIngreso.setCorrelativo(cargarCorrelativo(numeroCorrelativo));
		newOrdenIngreso.setEstado("AC");
		newOrdenIngreso.setTotalImporte(0);
		newOrdenIngreso.setGestion(gestionSesion);
		newOrdenIngreso.setFechaDocumento(new Date());
		newOrdenIngreso.setFechaRegistro(new Date());
		newOrdenIngreso.setUsuarioRegistro(usuarioSession.getLogin());

		selectedUnidadMedida = new UnidadMedida();
		//cuando agreguen un nuevo producto
		newProducto = new Producto();
		ocultarProveedor = false;
		
		String pIdOrdenIntgreso = sessionMain.getAttributeSession("pIdOrdenIngreso");//get atribute
		if(pIdOrdenIntgreso!=null){
			selectedOrdenIngreso = ordenIngresaDao.findById(new Integer(pIdOrdenIntgreso));
			newOrdenIngreso = selectedOrdenIngreso;
			listaDetalleOrdenIngreso = detalleOrdenIngresoDao.obtenerPorOrdenIngreso(selectedOrdenIngreso);
			selectedProveedor = selectedOrdenIngreso.getProveedor();
			ocultarProveedor = true;
			selectedAlmacen = selectedOrdenIngreso.getAlmacen();
			modificar = true;
		}
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

	// ------- action & event ------
	public void actionNuevo(){
		crear = false;
		modificar = false;
		registrar = true;
	}

	public void actionLoadDialogProducto(){
		if(selectedProveedor.getId().equals(0)){
			FacesUtil.infoMessage("Validacion", "Antes debe seleccionar un Proveedor");
			return;
		}
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		FacesUtil.updateComponent("formDialogProducto");
		FacesUtil.showDialog("dlgProducto");
	}

	public void anularOrden(){
		FacesUtil.infoMessage("PROCESO ANULACION", "El proceso de anulacion puede demorar varios segundos...");
		try{
			//actualizar estado de orden de ingreso
			selectedOrdenIngreso.setEstado("AN");
			//ordenIngresoRegistration.updated(selectedOrdenIngreso);
			Gestion gestion = selectedOrdenIngreso.getGestion();
			String numeroTransaccion = "I-"+selectedOrdenIngreso.getCorrelativo();

			//obtener kardex de acuerdo a la orden de ingreso a anular
			//KardexProducto kpSelected = kardexProductoRepository.findFirstByNumeroTransaccionAndGestion(numeroTransaccion, gestion);

			//otener el ultimo kardex registrado
			//kpUltimo = kardexProductoRepository.findUltimo();
			//if(kpUltimo == null){
			//	System.out.println("kpUltimo = "+kpUltimo);
			//}else{
			//	System.out.println("kpUltimo = "+kpUltimo.getId());
			//}

			//====================================
			//eliminar los kardex deacuerdo a la transaccion


			//actualizar DetalleProducto and AlmacenProducto
			//			Date fech1 = kpSelected.getFechaRegistro();
			//			Producto prod1 = kpSelected.getProducto();
			//			Almacen alm1 = kpSelected.getAlmacen();
			//			Gestion ges1 = kpSelected.getGestion();
			//actualizarAlmacenProductoAndDetalleProducto(prod1, alm1, ges1, fech1);


			FacesUtil.infoMessage("Orden Ingreso Anulada ", "Codigo: "+selectedOrdenIngreso.getCorrelativo());
			//=====================================
		}catch(Exception e){
			System.out.println("Error al anular : "+e.getMessage());
		}
	}
	
	public void calcularPorcentajePrecio(){
//		double precioCompra = selectedDetalleOrdenIngreso.getPrecioCompra();
//		//1000*0,25
//		double porcentajeContado = selectedProducto.getLineaProducto().getGrupoProducto().getPorcentajeVentaContado() / 100;
//		double porcentajeCredito = selectedProducto.getLineaProducto().getGrupoProducto().getPorcentajeVentaCredito() / 100;
//		System.out.println(porcentajeContado);
//		selectedDetalleOrdenIngreso.setPrecioVentaContado(precioCompra + (precioCompra*porcentajeContado));
//		System.out.println(selectedDetalleOrdenIngreso.getPrecioVentaContado());
//		selectedDetalleOrdenIngreso.setPrecioVentaCredito(precioCompra + (precioCompra*porcentajeCredito));
//		double precio = selectedDetalleOrdenIngreso.getPrecioVentaContado();//.getPrecioUnitario();
//		double precioCompra = selectedDetalleOrdenIngreso.getPrecioCompra();
//		double cantidad = selectedDetalleOrdenIngreso.getCantidad();
//		System.out.println("calcular() p="+precio+",c="+cantidad);
//		selectedDetalleOrdenIngreso.setTotal(precio * cantidad);
//		selectedDetalleOrdenIngreso.setTotalCompra(precioCompra * cantidad);
		if(selectedProducto == null){
			return;
		}
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

	private void actualizarAlmacenProductoAndDetalleProducto(Producto prod,Almacen alm, Gestion ges, Date fech){
		//actualizar almacen_producto
		try{
			List<AlmacenProducto> listAlmProd =almacenProductoDao.findByAlmacenProductoAndFecha(ges, alm, prod,fech);
			for(AlmacenProducto almProd: listAlmProd){
				almProd.setEstado("RM");
				//almacenProductoRegistration.updated(almProd);
			}

			//actualizar detalle_producto
			//List<DetalleProducto> listDetProd = detalleProductoDao.findByAlmacenProductoAndFecha(ges, alm, prod, fech);
			//for(DetalleProducto detProd: listDetProd){
			//	detProd.setEstado("RM");
				//detalleProductoRegistration.updated(detProd);
			//}
		}catch(Exception e){
			System.out.println("actualizarAlmacenProductoAndDetalleProducto Error: "+e.getMessage());
		}
	}

	public void cambiarAspecto(){
		//		//Verifica la gestion , sobre el levantamiento si ya se hizo el inicial
		//		if(gestionSesion.isIniciada()){
		//			System.out.println("Gestion SI iniciada");
		//			//verificar si el usuario logeado tiene almacen registrado
		//			selectedAlmacenOrigen = almacenDao.obtenerPorUsuarior(sessionMain.getUsuarioLogin());
		//			if(selectedAlmacenOrigen.getId() == -1){
		//				FacesUtil.infoMessage("Usuario "+usuarioSession, "No tiene asignado un almacen");
		//				return;
		//			}
		//			modificar = false;
		//			registrar = true;
		//			crear = false;
		//		}else{
		//			FacesUtil.infoMessage("INFORMACION", "Antes debe proceder a realizar una Toma de Inventario Inicial ");
		//		}
	}

	public boolean estaGestionIniciada(){
		return gestionSesion.isIniciada();
	}

	public void cambiarAspectoModificar(){
		modificar = true;
		registrar = false;
		crear = false;
		newOrdenIngreso = selectedOrdenIngreso;
		selectedAlmacen = newOrdenIngreso.getAlmacen();
		//selectedProveedor = newOrdenIngreso.getProveedor();
		listaDetalleOrdenIngreso = detalleOrdenIngresoDao.findAllByOrdenIngreso(selectedOrdenIngreso);
	}

	//correlativo incremental por gestion
	private String cargarCorrelativo(int nroOrdenIngreso){
		//pather = "000001";
		//Date fecha = new Date(); 
		//String year = new SimpleDateFormat("yy").format(fecha);
		//String mes = new SimpleDateFormat("MM").format(fecha);
		return String.format("%06d", nroOrdenIngreso);
	}

	// SELECT ORDEN INGRESO CLICK
	public void onRowSelectOrdenIngresoClick(SelectEvent event) {
		try {
			if(selectedOrdenIngreso.getEstado().equals("PR")){
				verProcesar = false;
				verButtonAnular = true;
			}else{
				verProcesar = true;
				verButtonAnular = false;
			}
			if(selectedOrdenIngreso.getEstado().equals("AN")){
				verProcesar = false;
				verButtonAnular = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectOrdenIngresoClick: "
					+ e.getMessage());
		}
	}

	// SELECT DETALLE ORDEN INGRESO CLICK
	public void onRowSelectDetalleOrdenIngresoClick(SelectEvent event) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectOrdenIngresoClick: "
					+ e.getMessage());
		}
	}

	public void registrarOrdenIngreso() {
		if( selectedAlmacen.getId()==0 || newOrdenIngreso.getNumeroDocumento().isEmpty()){
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios.");
			return;
		}
		if(listaDetalleOrdenIngreso.size()==0 ){
			FacesUtil.infoMessage("VALIDACION", "Debe Agregar items..");
			return;
		}
		Date date = new Date();
		calcularTotal();
		newOrdenIngreso.setFechaRegistro(date); //(se habilito el campo de fecha)
		newOrdenIngreso.setUsuarioRegistro(usuarioSession.getLogin());
		newOrdenIngreso.setAlmacen(selectedAlmacen);
		newOrdenIngreso.setProveedor(selectedProveedor);
		for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
			d.setFechaRegistro(date);
			d.setUsuarioRegistro(usuarioSession.getLogin());
		}
		boolean sw = ordenIngresaDao.registrar(newOrdenIngreso,listaDetalleOrdenIngreso);
		if(sw){
			//loadDefault();
			String page = "pages/proceso/list-orden-ingreso.xhtml";
			PageUtil.cargarPagina(page);
		}
	}

	public void modificarOrdenIngreso() {
		Date fechaActual = new Date();
		double total = 0;
		for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
			if(d.getId()==0){//si es un nuevo registro
				d.setFechaRegistro(fechaActual);
				d.setUsuarioRegistro(usuarioSession.getLogin());
				d.setEstado("AC");
				d.setOrdenIngreso(newOrdenIngreso);
				//detalleOrdenIngresoRegistration.register(d);
			}
			total = total + d.getTotal();
			//detalleOrdenIngresoRegistration.updated(d);
		}
		//borrado logico 
		for(DetalleOrdenIngreso d: listDetalleOrdenIngresoEliminados){
			if(d.getId() != 0){
				d.setEstado("RM");
				//detalleOrdenIngresoRegistration.updated(d);
			}
		}
		newOrdenIngreso.setAlmacen(selectedAlmacen);
		//newOrdenIngreso.setProveedor(selectedProveedor);
		//newOrdenIngreso.setTotalImporte(total);
		//ordenIngresoRegistration.updated(newOrdenIngreso);
		List<AlmacenProducto> listAlmacenProducto = almacenProductoDao.obtenerTodosPorOrdenIngreso(selectedOrdenIngreso);
		boolean sw = ordenIngresaDao.modificar(selectedOrdenIngreso,listaDetalleOrdenIngreso,listDetalleOrdenIngresoEliminados ,listAlmacenProducto);
		if(sw){
			//loadDefault();
			String page = "pages/proceso/list-orden-ingreso.xhtml";
			PageUtil.cargarPagina(page);
		}
	}

	public void eliminarOrdenIngreso() {
		List<AlmacenProducto> listAlmacenProducto = almacenProductoDao.obtenerTodosPorOrdenIngreso(selectedOrdenIngreso);
		boolean sw = ordenIngresaDao.eliminar(selectedOrdenIngreso,listaDetalleOrdenIngreso ,listAlmacenProducto);
		if(sw){
			//loadDefault();
			String page = "pages/proceso/list-orden-ingreso.xhtml";
			PageUtil.cargarPagina(page);
		}
	}

	public void procesarOrdenIngreso(){
		Date fechaActual = new Date();
		// actualizar estado de orden ingreso
		selectedOrdenIngreso.setEstado("PR");
		selectedOrdenIngreso.setFechaAprobacion(fechaActual);
		//obtener listDetalleOrdenIngreso
		List<DetalleOrdenIngreso> listDetalleOrdenIngreso = detalleOrdenIngresoDao.obtenerPorOrdenIngreso(selectedOrdenIngreso);
		List<AlmacenProducto> listAlmacenProducto = new ArrayList<>();
		Proveedor proveedor = selectedOrdenIngreso.getProveedor();
		for(DetalleOrdenIngreso d: listDetalleOrdenIngreso){
			System.out.println("FechaExpiracion: "+d.getFechaExpiracion());
			System.out.println("NumeroLote: "+d.getNumeroLote());
			System.out.println("UbicacionFisica: "+d.getUbicacionFisica());
			Producto prod = d.getProducto();
			AlmacenProducto almProd = new AlmacenProducto();
			almProd = new AlmacenProducto();
			almProd.setAlmacen(selectedOrdenIngreso.getAlmacen());
			almProd.setProducto(prod);
			almProd.setProveedor(proveedor);
			almProd.setStock(d.getCantidad());
			almProd.setPrecioCompra(d.getPrecioCompra());
			almProd.setPrecioVentaContado(d.getPrecioVentaContado());
			almProd.setPrecioVentaCredito(d.getPrecioVentaCredito());
			almProd.setStockMin(prod.getStockMin());
			almProd.setStockMax(prod.getStockMax());
			almProd.setPrecioCompra(d.getPrecioCompra());
			almProd.setPrecioVentaContado(d.getPrecioVentaContado());
			almProd.setPrecioVentaCredito(d.getPrecioVentaCredito());
			almProd.setEstado("AC");
			almProd.setFechaRegistro(fechaActual);
			almProd.setUsuarioRegistro(usuarioSession.getLogin());
			almProd.setGestion(gestionSesion);
			almProd.setFechaExpiracion(d.getFechaExpiracion());
			almProd.setNumeroLote(d.getNumeroLote());
			almProd.setUbicacionFisica(d.getUbicacionFisica());
			listAlmacenProducto.add(almProd);
		}
		boolean sw = ordenIngresaDao.procesar(empresaLogin,"ORDEN INGRESO X "+selectedOrdenIngreso.getMotivoIngreso(),usuarioSession,selectedOrdenIngreso, listAlmacenProducto);
		if(sw){
			//loadDefault();
			String page = "pages/proceso/list-orden-ingreso.xhtml";
			PageUtil.cargarPagina(page);
		}
	}

	public void cargarReporte(){
		Map<String,String> map1 = new HashMap<>();
		map1.put("pRazonSocial", empresaLogin.getRazonSocial());
		map1.put("pDireccion", empresaLogin.getDireccion());
		map1.put("pTelefono", empresaLogin.getTelefono());
		map1.put("pIdEmpresa", String.valueOf(empresaLogin.getId()));
		map1.put("pUsuario", usuarioSession.getLogin());
		map1.put("pIdOrdenIngreso", String.valueOf(selectedOrdenIngreso.getId()));
		urlOrdenIngreso = buildUrl("ReporteOrdenIngreso",map1);
		verReport = true;
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
			return url.toString();
		}catch(Exception e){
			return "";
		}
	}

	// DETALLE ORDEN INGRESO ITEMS

	public void editarDetalleOrdenIngreso(Integer idDetalleOrdenIngreso){
		for(DetalleOrdenIngreso det :listaDetalleOrdenIngreso){
			if(det.getId().equals(idDetalleOrdenIngreso)){
				selectedDetalleOrdenIngreso = det;
			}
		}
		selectedProducto = selectedDetalleOrdenIngreso.getProducto();
		editarOrdenIngreso = true;
		calcular();
		FacesUtil.updateComponent("formDialogProducto");
		FacesUtil.showDialog("dlgProducto");
	}

	public void borrarDetalleOrdenIngreso(Integer idDetalleOrdenIngreso){
		for(DetalleOrdenIngreso det :listaDetalleOrdenIngreso){
			if(det.getId().equals(idDetalleOrdenIngreso)){
				selectedDetalleOrdenIngreso = det;
			}
		}
		listaDetalleOrdenIngreso.remove(selectedDetalleOrdenIngreso);
		listDetalleOrdenIngresoEliminados.add(selectedDetalleOrdenIngreso);
		FacesUtil.updateComponent("form001");
	}

	public void limpiarDatosProducto(){
		selectedProducto = new Producto();
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		editarOrdenIngreso = false;
	}

	public void agregarDetalleOrdenIngreso(){
//		System.out.println("agregarDetalleOrdenIngreso ");
//		//verificar si el producto ya fue agregado
//		if(verificarProductoAgregado(selectedProducto)){
//			FacesUtil.infoMessage("OBSERVACION", "Este producto ya fue agregado.");
//			return ;
//		}
//		selectedDetalleOrdenIngreso.setProveedor(selectedProveedor);
//		selectedDetalleOrdenIngreso.setProducto(selectedProducto);
//		listaDetalleOrdenIngreso.add(0, selectedDetalleOrdenIngreso);
//		selectedProducto = new Producto();
//		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		//====================================================================
		//====================================================================
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
		selectedOrdenIngreso.setProveedor(selectedProveedor);
		listaDetalleOrdenIngreso.add(0, selectedDetalleOrdenIngreso);
		actionCerrarDialogProducto();
	}
	
	public void actionCerrarDialogProducto(){
		selectedProducto = new Producto();
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		setVerButtonDetalle(true);
		setEditarOrdenIngreso(false);
		FacesUtil.updateComponent("form001");
		closeDialogProducto();
	}
	
	public void closeDialogProducto(){
		FacesUtil.hideDialog("dlgProducto");
	}

	private boolean verificarProductoAgregado(Producto selectedProducto){
		for(DetalleOrdenIngreso detalle : listaDetalleOrdenIngreso){
			if(detalle.getProducto().getId().intValue()==selectedProducto.getId().intValue()){
				return true;
			}
		}
		return false;
	}

	public void modificarDetalleOrdenIngreso(){
		for(DetalleOrdenIngreso d: listaDetalleOrdenIngreso){
			if(d.equals(selectedDetalleOrdenIngreso)){
				d = selectedDetalleOrdenIngreso;
			}
		}
		selectedProducto = new Producto();
		selectedDetalleOrdenIngreso = new DetalleOrdenIngreso();
		editarOrdenIngreso = false;
	}

	//calcular totales
	public void calcular(){
//		System.out.println("calcular()");
//		double precio = selectedDetalleOrdenIngreso.getPrecioVentaContado();//.getPrecioUnitario();
//		double cantidad = selectedDetalleOrdenIngreso.getCantidad();
//		selectedDetalleOrdenIngreso.setTotal(precio * cantidad);
		double precio = selectedDetalleOrdenIngreso.getPrecioVentaContado();//.getPrecioUnitario();
		double precioCompra = selectedDetalleOrdenIngreso.getPrecioCompra();
		double cantidad = selectedDetalleOrdenIngreso.getCantidad();
		selectedDetalleOrdenIngreso.setTotal(precio * cantidad);
		selectedDetalleOrdenIngreso.setTotalCompra(precioCompra * cantidad);
	}

	public void calcularTotal(){
		//double totalImporte = 0;
		//for(DetalleOrdenIngreso d : listaDetalleOrdenIngreso){
		//	totalImporte =totalImporte + d.getTotalCompra();
		//}
		//newOrdenIngreso.setTotalImporte(totalImporte);
	}

	// ONCOMPLETETEXT PROVEEDOR
	public List<Proveedor> completeProveedor(String query) {
//		String upperQuery = query.toUpperCase();
//		List<Proveedor> results = new ArrayList<Proveedor>();
//		for(Proveedor i : listaProveedor) {
//			if(i.getNombre().toUpperCase().startsWith(upperQuery)){
//				results.add(i);
//			}
//		}         
//		return results;
		//==============================================
		String upperQuery = query.toUpperCase();
		//listProveedor = proveedorRepository.findAllProveedorForQueryNombre(upperQuery,gestionSesion);
		listProveedor = proveedorDao.findAllProveedorForQueryNombre(upperQuery,gestionSesion);
		return listProveedor;
	}

	public void onRowSelectProveedorClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Proveedor i : listaProveedor){
			if(i.getNombre().equals(nombre)){
				selectedProveedor = i;
				return;
			}
		}
	}

	// ONCOMPLETETEXT ALMACEN
	public List<Almacen> completeAlmacen(String query) {
		String upperQuery = query.toUpperCase();
		List<Almacen> results = new ArrayList<Almacen>();
		for(Almacen i : listaAlmacen) {
			if(i.getNombre().toUpperCase().startsWith(upperQuery)){
				results.add(i);
			}
		}         
		return results;
	}

	public void onRowSelectAlmacenClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Almacen i : listaAlmacen){
			if(i.getNombre().equals(nombre)){
				//verificar si el almacen-gestion ya fue cerrado
				//				if(cierreGestionAlmacenRepository.finAlmacenGestionCerrado(i,gestionSesion) != null){
				//					FacesUtil.infoMessage("INFORMACION", "El lmacen "+i.getNombre()+" fué cerrado");
				//					listaAlmacen.remove(i);
				//					selectedAlmacen = new Almacen();
				//					return ;
				//				}
				selectedAlmacen = i;
				return;
			}
		}
	}

	// ONCOMPLETETEXT PRODUCTO
	public List<Producto> completeProducto(String query) {
//		String upperQuery = query.toUpperCase();
//		listProducto = productoRepository.findAllProductoForQueryNombre(upperQuery);
//		return listProducto;
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
//		String nombre =  event.getObject().toString();
//		for(Producto i : listProducto){
//			if(i.getNombre().equals(nombre)){
//				selectedProducto = i;
//				//				if(devolucion){
//				//					calcularPrecioPromedioForDevolucion(selectedProducto);
//				//				}else{
//				//					selectedDetalleOrdenIngreso.setPrecioUnitario(0);
//				//				}
//				calcular();
//				return;
//			}
//		}
		Integer id = ((Producto) event.getObject()).getId();
		for(Producto i : listProducto){
			if(i.getId().equals(id)){
				selectedProducto = i;
				calcular();
				return;
			}
		}
	}

	// SELECCIONAR AUTOCOMPLETE UNIDAD DE MEDIDA
	public List<UnidadMedida> completeUnidadMedida(String query) {
		String upperQuery = query.toUpperCase();
		listUnidadMedida = unidadMedidaDao.findAllByNombre(upperQuery);
		return listUnidadMedida;
	}

	public void onRowSelectUnidadMedidaClick(SelectEvent event) {
		String nombre = event.getObject().toString();
		for(UnidadMedida um: listUnidadMedida){
			if(um.getNombre().equals(nombre)){
				selectedUnidadMedida = um;
			}
		}
	}

	public void registrarProducto() {
		try {
			newProducto.setUnidadMedidas(selectedUnidadMedida);
			newProducto.setEstado("AC");
			newProducto.setFechaRegistro(new Date());
			newProducto.setUsuarioRegistro(usuarioSession.getLogin());
			newProducto.setFechaRegistro(new Date());
			//newProducto = productoRegistration.register(newProducto);
			selectedProducto = newProducto;
			calcular();
			FacesUtil.infoMessage("Producto Registrado!",newProducto.getNombre());
			setNuevoProducto(false);
			newProducto = new Producto();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al registrar");
		}
	}

	// -------- get and set -------
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

	public boolean isAtencionCliente() {
		return atencionCliente;
	}

	public void setAtencionCliente(boolean atencionCliente) {
		this.atencionCliente = atencionCliente;
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

	public List<DetalleOrdenIngreso> getListaDetalleOrdenIngreso() {
		return listaDetalleOrdenIngreso;
	}

	public void setListaDetalleOrdenIngreso(List<DetalleOrdenIngreso> listaDetalleOrdenIngreso) {
		this.listaDetalleOrdenIngreso = listaDetalleOrdenIngreso;
	}

	public List<OrdenIngreso> getListaOrdenIngreso() {
		return listaOrdenIngreso;
	}

	public void setListaOrdenIngreso(List<OrdenIngreso> listaOrdenIngreso) {
		this.listaOrdenIngreso = listaOrdenIngreso;
	}

	public List<Almacen> getListaAlmacen() {
		return listaAlmacen;
	}

	public void setListaAlmacen(List<Almacen> listaAlmacen) {
		this.listaAlmacen = listaAlmacen;
	}

	public OrdenIngreso getSelectedOrdenIngreso() {
		return selectedOrdenIngreso;
	}

	public void setSelectedOrdenIngreso(OrdenIngreso selectedOrdenIngreso) {
		this.selectedOrdenIngreso = selectedOrdenIngreso;
	}

	public OrdenIngreso getNewOrdenIngreso() {
		return newOrdenIngreso;
	}

	public void setNewOrdenIngreso(OrdenIngreso newOrdenIngreso) {
		this.newOrdenIngreso = newOrdenIngreso;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public Proveedor getSelectedProveedor() {
		return selectedProveedor;
	}

	public void setSelectedProveedor(Proveedor selectedProveedor) {
		this.selectedProveedor = selectedProveedor;
	}

	public List<Proveedor> getListaProveedor() {
		return listaProveedor;
	}

	public void setListaProveedor(List<Proveedor> listaProveedor) {
		this.listaProveedor = listaProveedor;
	}

	public DetalleOrdenIngreso getSelectedDetalleOrdenIngreso() {
		return selectedDetalleOrdenIngreso;
	}

	public void setSelectedDetalleOrdenIngreso(
			DetalleOrdenIngreso selectedDetalleOrdenIngreso) {
		this.selectedDetalleOrdenIngreso = selectedDetalleOrdenIngreso;
	}

	public boolean isVerProcesar() {
		return verProcesar;
	}

	public void setVerProcesar(boolean verProcesar) {
		this.verProcesar = verProcesar;
	}

	public String getUrlOrdenIngreso() {
		return urlOrdenIngreso;
	}

	public void setUrlOrdenIngreso(String urlOrdenIngreso) {
		this.urlOrdenIngreso = urlOrdenIngreso;
	}

	public boolean isEditarOrdenIngreso() {
		return editarOrdenIngreso;
	}

	public void setEditarOrdenIngreso(boolean editarOrdenIngreso) {
		this.editarOrdenIngreso = editarOrdenIngreso;
	}

	public Almacen getSelectedAlmacenOrigen() {
		return selectedAlmacenOrigen;
	}

	public void setSelectedAlmacenOrigen(Almacen selectedAlmacenOrigen) {
		this.selectedAlmacenOrigen = selectedAlmacenOrigen;
	}

	public boolean isVerReport() {
		return verReport;
	}

	public void setVerReport(boolean verReport) {
		this.verReport = verReport;
	}

	public boolean isNuevoProducto() {
		return nuevoProducto;
	}

	public void setNuevoProducto(boolean nuevoProducto) {
		this.nuevoProducto = nuevoProducto;
	}

	public Producto getNewProducto() {
		return newProducto;
	}

	public void setNewProducto(Producto newProducto) {
		this.newProducto = newProducto;
	}

	public UnidadMedida getSelectedUnidadMedida() {
		return selectedUnidadMedida;
	}

	public void setSelectedUnidadMedida(UnidadMedida selectedUnidadMedida) {
		this.selectedUnidadMedida = selectedUnidadMedida;
	}

	public double getPrecioPromedio() {
		return precioPromedio;
	}

	public void setPrecioPromedio(double precioPromedio) {
		this.precioPromedio = precioPromedio;
	}

	public boolean getDevolucion() {
		return devolucion;
	}

	public void setDevolucion(boolean devolucion) {
		this.devolucion = devolucion;
	}

	public List<Producto> getListProducto() {
		return listProducto;
	}

	public void setListProducto(List<Producto> listProducto) {
		this.listProducto = listProducto;
	}

	public boolean isVerButtonAnular() {
		return verButtonAnular;
	}

	public void setVerButtonAnular(boolean verButtonAnular) {
		this.verButtonAnular = verButtonAnular;
	}
	
	public List<Proveedor> getListProveedor() {
		return listProveedor;
	}

	public void setListProveedor(List<Proveedor> listProveedor) {
		this.listProveedor = listProveedor;
	}

	public boolean isVerButtonDetalle() {
		return verButtonDetalle;
	}

	public void setVerButtonDetalle(boolean verButtonDetalle) {
		this.verButtonDetalle = verButtonDetalle;
	}

	public boolean isOcultarProveedor() {
		return ocultarProveedor;
	}

	public void setOcultarProveedor(boolean ocultarProveedor) {
		this.ocultarProveedor = ocultarProveedor;
	}

}
