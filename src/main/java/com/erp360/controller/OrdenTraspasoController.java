package com.erp360.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URLEncoder;
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
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.richfaces.cdi.push.Push;

import com.erp360.dao.AlmacenDao;
import com.erp360.dao.AlmacenProductoDao;
import com.erp360.dao.DetalleOrdenTraspasoDao;
import com.erp360.dao.DetalleProductoDao;
import com.erp360.dao.OrdenTraspasoDao;
import com.erp360.dao.ProductoDao;
import com.erp360.model.Almacen;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.DetalleOrdenTraspaso;
import com.erp360.model.DetalleProducto;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.OrdenTraspaso;
import com.erp360.model.Producto;
import com.erp360.model.Proveedor;
import com.erp360.util.Cifrado;
import com.erp360.util.FacesUtil;
import com.erp360.util.NumberUtil;
import com.erp360.util.SessionMain;

@Named(value = "ordenTraspasoController")
@ConversationScoped
public class OrdenTraspasoController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	//Repository
	private @Inject AlmacenDao almacenRepository;
	private @Inject OrdenTraspasoDao ordenTraspasoRepository;
	private @Inject ProductoDao productoRepository;
	private @Inject DetalleOrdenTraspasoDao detalleOrdenTraspasoRepository;
	private @Inject AlmacenProductoDao almacenProductoRepository;
	private @Inject DetalleProductoDao detalleProductoRepository;
	//private @Inject CierreGestionAlmacenD cierreGestionAlmacenRepository;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	@Inject
	private FacesContext facesContext;

	//ESTADOS
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;
	private boolean verButtonDetalle = true;
	private boolean editarOrdenTraspaso = false;
	private boolean verProcesar = true;
	private boolean verReport = false;
	private boolean verExport = false;

	private String tituloProducto = "Agregar Producto";
	private String tituloPanel = "Registrar Almacen";
	private String urlOrdenTraspaso = "";
	private double total = 0;
	private double totalCantidaEntregada = 0;
	private double verificacionCantidadEntregada = 0;
	private String textDialogExistencias = "";

	//OBJECT
	private Producto selectedProducto;
	private Almacen selectedAlmacen;
	private Almacen selectedAlmacenOrigen;
	private OrdenTraspaso selectedOrdenTraspaso;
	private OrdenTraspaso newOrdenTraspaso;
	private DetalleOrdenTraspaso selectedDetalleOrdenTraspaso;

	//LIST
	private List<DetalleOrdenTraspaso> listaDetalleOrdenTraspaso = new ArrayList<DetalleOrdenTraspaso>(); // ITEMS
	private List<OrdenTraspaso> listaOrdenTraspaso = new ArrayList<OrdenTraspaso>();
	private List<Almacen> listaAlmacen = new ArrayList<Almacen>();
	private List<DetalleOrdenTraspaso> listDetalleOrdenTraspasoEliminados = new ArrayList<DetalleOrdenTraspaso>();
	private List<DetalleOrdenTraspaso> listDetalleOrdenTraspasoSinStock = new ArrayList<DetalleOrdenTraspaso>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private Gestion gestionSesion;
	private Empresa empresaLogin;

	private boolean atencionCliente=false;

	//archivo de exportacion
	private StreamedContent dFile;

	@PostConstruct
	public void initNewOrdenTraspaso() {

		beginConversation();

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		gestionSesion = sessionMain.getGestionLogin();
		empresaLogin = sessionMain.getEmpresaLogin();

		selectedProducto = new Producto();
		selectedAlmacen = new Almacen();

		selectedOrdenTraspaso = null;
		selectedDetalleOrdenTraspaso = new DetalleOrdenTraspaso();

		// tituloPanel
		tituloPanel = "Registrar Orden Traspaso";

		modificar = false;
		registrar = true;
		crear = false;
		atencionCliente=false;
		verProcesar = true;
		verReport = false;
		verExport = false;

		listaDetalleOrdenTraspaso = new ArrayList<DetalleOrdenTraspaso>();
		listaOrdenTraspaso = ordenTraspasoRepository.findAllOrderedByID();

		//-::::::::: OJO :::::::
		//la lista de almacen se obtendra al hacer click en nuevo orden traspaso
		// y luego de verificar que almacen tiene el usuario, no mostrara dicho almacen en la lista
		//de almacenes
		int numeroCorrelativo = ordenTraspasoRepository.obtenerNumeroOrdenTraspaso(gestionSesion);
		newOrdenTraspaso = new OrdenTraspaso();
		newOrdenTraspaso.setCorrelativo(cargarCorrelativo(numeroCorrelativo));
		newOrdenTraspaso.setEstado("AC");
		newOrdenTraspaso.setGestion(gestionSesion);
		newOrdenTraspaso.setFechaDocumento(new Date());
		newOrdenTraspaso.setFechaRegistro(new Date());
		newOrdenTraspaso.setUsuarioRegistro(usuarioSession);
	}

	private void cargarAlmacen(){
//		listaAlmacen = almacenRepository.findAllActivosOrderedByID();
//		if(listaAlmacen.size()>0){
//			// listaAlmacen.remove(selectedAlmacenOrigen); //establecer si se va a restringir el traspaso entre el mismo almacen
//		}
	}

	public void cambiarAspecto(){
		//verificar si el usuario logeado tiene almacen registrado
//		selectedAlmacenOrigen = almacenRepository.findAlmacenForUser(sessionMain.getUsuarioLogin());
//		if(selectedAlmacenOrigen.getId() == -1){
//			FacesUtil.infoMessage("Usuario "+usuarioSession, "No tiene asignado un almacén");
//			return;
//		}
		//verificar si el almacen-gestion ya fue cerrado
//		if(cierreGestionAlmacenRepository.finAlmacenGestionCerrado(selectedAlmacenOrigen,gestionSesion) != null){
//			FacesUtil.infoMessage("INFORMACION", "Encargado "+sessionMain.getUsuarioLogin().getNombre()+" -  El Almacén "+selectedAlmacenOrigen.getNombre()+" fué cerrado");
//			return ;
//		}
		//cargara la lista de almacen pero no mostrara el almacen del usuario logeado
		cargarAlmacen();
		modificar = false;
		registrar = true;
		crear = false;
	}

	public void cambiarAspectoModificar(){
		modificar = true;
		registrar = false;
		crear = false;
		newOrdenTraspaso = selectedOrdenTraspaso;
		selectedAlmacen = newOrdenTraspaso.getAlmacenDestino();
		listaDetalleOrdenTraspaso = detalleOrdenTraspasoRepository.findAllByOrdenTraspaso(selectedOrdenTraspaso);
	}

	public void beginConversation() {
		if (conversation.isTransient()) {
			System.out.println("beginning conversation : " + this.conversation);
			conversation.begin();
			System.out.println("---> Init Conversation");
		}
	}

	public void endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
	}

	//correlativo incremental por gestion
	private String cargarCorrelativo(int nroOrdenTraspaso){
		// pather = "000001";
		//Date fecha = new Date(); 
		//String year = new SimpleDateFormat("yy").format(fecha);
		//String mes = new SimpleDateFormat("MM").format(fecha);
		return String.format("%06d", nroOrdenTraspaso);
	}

	// SELECT ORDEN Traspaso CLICK
	public void onRowSelectOrdenTraspasoClick(SelectEvent event) {
		try {
			if(selectedOrdenTraspaso.getEstado().equals("PR")){
				verProcesar = false;
				//solo se mmostrara el boton export si la orden de  traspaso esta procesada, porq al procesar una orden de traspaso,
				//se actualizan los precios unitarios de los productos
				//verifica la conexion del almacen
				
			}else{
				verProcesar = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectOrdenTraspasoClick: "
					+ e.getMessage());
		}
	}

	// SELECT DETALLE ORDEN Traspaso CLICK
	public void onRowSelectDetalleOrdenTraspasoClick(SelectEvent event) {
		try {
			verButtonDetalle = false;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectOrdenTraspasoClick: "
					+ e.getMessage());
		}
	}

	public void redireccionarPgina(){
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			//http://localhost:8080/webapp/pages/dashboard.xhtml
			context.getExternalContext().redirect("http://localhost:8080/webapp/pages/dashboard.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void registrarOrdenTraspaso() {
		if( selectedAlmacen.getId()==0  ){
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios.");
			return;
		}
		if(listaDetalleOrdenTraspaso.size()==0 ){
			FacesUtil.infoMessage("VALIDACION", "Debe Agregar items..");
			return;
		}
		try {
			//if(validarStock()){//valida el stock de los productos
			//	FacesUtil.showDialog("dlgValidacionStock");
			//	return ;
			//}
			Date date = new Date();
			calcularTotal();
			System.out.println("paso a registrarOrdenTraspaso: ");
			//newOrdenTraspaso.setFechaRegistro(date); (Se habilito la fecha de registro en el form)
			newOrdenTraspaso.setAlmacenOrigen(selectedAlmacenOrigen);//selectedAlmacen; -> almacen destino
			newOrdenTraspaso.setAlmacenDestino(selectedAlmacen);
			//newOrdenTraspaso = ordenTraspasoRegistration.register(newOrdenTraspaso);
			for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
				d.setCantidadEntregada(0);
				d.setFechaRegistro(date);
				d.setUsuarioRegistro(usuarioSession);
				d.setOrdenTraspaso(newOrdenTraspaso);
				//d = detalleOrdenTraspasoRegistration.register(d);
			}
			FacesUtil.infoMessage("Orden de Traspaso Registrada!", ""+newOrdenTraspaso.getCorrelativo());
			// Verificar si el almacen destino es offline
			
			initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Registrar.");
		}
	}
	
	public void modificarOrdenTraspaso() {
		try {
			System.out.println("Traspaso a modificarOrdenTraspaso: ");
			Date fechaActual = new Date();
			double total = 0;
			for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
				d.setCantidadEntregada(0);
				if(d.getId()==0){//si es un nuevo registro
					d.setFechaRegistro(fechaActual);
					d.setUsuarioRegistro(usuarioSession);
					d.setEstado("AC");
					d.setOrdenTraspaso(newOrdenTraspaso);
					//detalleOrdenTraspasoRegistration.register(d);
				}
				total = total + d.getTotal();
				//detalleOrdenTraspasoRegistration.updated(d);
			}
			//borrado logico 
			for(DetalleOrdenTraspaso d: listDetalleOrdenTraspasoEliminados){
				if(d.getId() != 0){
					d.setEstado("RM");
					//detalleOrdenTraspasoRegistration.updated(d);
				}
			}
			newOrdenTraspaso.setAlmacenDestino(selectedAlmacen);
			newOrdenTraspaso.setTotalImporte(total);
			//ordenTraspasoRegistration.updated(newOrdenTraspaso);
			FacesUtil.infoMessage("Orden de Traspaso Modificada!", ""+newOrdenTraspaso.getCorrelativo());
			initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Modificar.");
		}
	}

	public void eliminarOrdenTraspaso() {
		try {
			System.out.println("Traspaso a eliminarOrdenTraspaso: ");
			//ordenTraspasoRegistration.remover(selectedOrdenTraspaso);
			//			for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
			//				detalleOrdenTraspasoRegistration.remover(d);
			//			}
			FacesUtil.infoMessage("Orden de Traspaso Eliminada!", ""+newOrdenTraspaso.getCorrelativo());
			initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al Eliminar.");
		}
	}

	public void procesarOrdenTraspaso(){
		try {
			System.out.println("procesarOrdenTraspaso()");
			total = 0;
			Date fechaActual = new Date();
			//actualizar estado de orden Traspaso
			selectedOrdenTraspaso.setEstado("PR");
			selectedOrdenTraspaso.setFechaAprobacion(fechaActual);

			Proveedor proveedor = null;

			//	newOrdenTraspaso.setAlmacenOrigen(selectedAlmacenOrigen);//selectedAlmacen; -> almacen destino

			//actualizar stock de AlmacenProducto
			listaDetalleOrdenTraspaso = detalleOrdenTraspasoRepository.findAllByOrdenTraspaso(selectedOrdenTraspaso);

			Almacen almOrig = selectedOrdenTraspaso.getAlmacenOrigen();
			Almacen almDest = selectedOrdenTraspaso.getAlmacenDestino();
			for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
				Producto prod = d.getProducto();
				//double cantidadSolicitada = d.getCantidadSolicitada();
				//1.- Actualizar detalle producto (PEPS) y tambiaen actualizar precio en detalleOrdenIngreso
				if(  actualizarDetalleProducto(almOrig,d)){
					//mostrar mensaje
					//FacesUtil.showDialog("dlgAlmacenSinExistencias");
					//initNewOrdenTraspaso();
					//return;//no se econtro stock disponible

					//2.- 
					System.out.println("actualizarStockAlmacenOrigen("+almOrig.getNombre()+","+prod.getNombre()+","+totalCantidaEntregada);
					actualizarStockAlmacenOrigen(almOrig, prod,totalCantidaEntregada);

					//4.-
					System.out.println("actualizarKardexProducto("+almOrig.getNombre()+","+almDest.getNombre()+","+prod.getNombre()+","+fechaActual+","+totalCantidaEntregada+","+d.getPrecioUnitario());
					//total = total + (totalCantidaEntregada * d.getPrecioUnitario());
				}
				//agregar detalleProductos al almacen destino
				if(verificacionCantidadEntregada>0){
					//3.-
					System.out.println("actualizarStockAlmacenDestino("+proveedor+","+prod.getNombre()+","+almDest.getNombre()+","+totalCantidaEntregada+","+fechaActual+","+d.getPrecioUnitario());
					actualizarStockAlmacenDestino(proveedor,prod,almDest, totalCantidaEntregada,fechaActual,d.getPrecioUnitario());
					//
					System.out.println("cargarDetalleProducto("+fechaActual+","+almDest.getNombre()+","+prod.getNombre()+","+totalCantidaEntregada+","+d.getPrecioUnitario()+","+d.getFechaRegistro()+","+selectedOrdenTraspaso.getCorrelativo()+","+usuarioSession);
					cargarDetalleProducto(fechaActual, almDest, prod, totalCantidaEntregada, d.getPrecioUnitario(), d.getFechaRegistro(), selectedOrdenTraspaso.getCorrelativo(), usuarioSession);
				}


			}
			//actualizar ordenTraspaso
			selectedOrdenTraspaso.setTotalImporte(total);
			//ordenTraspasoRegistration.updated(selectedOrdenTraspaso);
			FacesUtil.infoMessage("Orden de Traspaso Procesada!", "");
			initNewOrdenTraspaso();
		} catch (Exception e) {
			System.out.println("procesarOrdenTraspaso() Error: "+e.getMessage());
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public void cargarDetalleProducto(Date fechaActual,Almacen almacen,Producto producto,double cantidad, double precio, Date fecha, String correlativoTransaccion,String usuarioSession ) {
		try{
			DetalleProducto detalleProducto = new DetalleProducto();
			detalleProducto.setCodigo("OT"+correlativoTransaccion+fecha.toString());
			detalleProducto.setAlmacen(almacen);
			detalleProducto.setEstado("AC");
			detalleProducto.setPrecioVentaContado(precio);
			detalleProducto.setStockActual(cantidad);
			detalleProducto.setStockInicial(cantidad);
			detalleProducto.setCorrelativoTransaccion(correlativoTransaccion);
			detalleProducto.setFecha(fecha);
			detalleProducto.setFechaRegistro(fechaActual);
			detalleProducto.setProducto(producto);
			detalleProducto.setUsuarioRegistro(usuarioSession);
			detalleProducto.setGestion(gestionSesion);
			//detalleProductoRegistration.register(detalleProducto);
		}catch(Exception e){
			System.out.println("ERROR cargarDetalleProducto() "+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Actualiza el stock, verifica existencias de acuerdo al metodo PEPS
	 * @param almacen De que almacen se sacara los productos
	 * @param detalle
	 * @return true si hay stock, false si no hay existncias
	 */
	private boolean actualizarDetalleProducto(Almacen almacen,DetalleOrdenTraspaso detalle){
		try{
			totalCantidaEntregada = 0;
			verificacionCantidadEntregada = 0;
			Producto producto = detalle.getProducto();
			double cantidadAux = detalle.getCantidadSolicitada();
			double cantidadSolicitada = detalle.getCantidadSolicitada();// 15
			int cantidad = 1;
			//obtener todos los detalles del producto, para poder descontar stock de acuerdo a la cantidad solicitada
			List<DetalleProducto> listDetalleProducto = detalleProductoRepository.findAllByProductoAndAlmacenOrderByFecha(almacen,producto,gestionSesion);
			System.out.println("listDetalleProducto.size()"+listDetalleProducto.size());
			//50 | 10
			//52 | 10
			if(listDetalleProducto.size()>0){
				for(DetalleProducto d : listDetalleProducto){
					double stockActual = d.getStockActual();//10 |10
					double precio = d.getPrecioVentaContado(); // 50 | 52
					if(cantidadSolicitada > 0){// 15 | 5
						double stockFinal = stockActual- cantidadSolicitada; // 10-15=-5 | 10-5=5 |
						double cantidadRestada = stockFinal < 0 ? cantidadSolicitada -(cantidadSolicitada - stockActual) : cantidadSolicitada; //15-(15-10)=10 | 5 |
						d.setStockActual( stockFinal <= 0 ? 0 : stockFinal); // 0 | 5
						d.setEstado(stockFinal<=0?"IN":"AC"); // IN | AC
						//detalleProductoRegistration.updated(d);
						cantidadSolicitada = cantidadSolicitada - cantidadRestada  ;//actualizar cantidad solicitada // 15-10=5| 5-5=0|
						if(cantidad == 1){
							detalle.setCantidadEntregada(cantidadRestada);
							detalle.setCantidadSolicitada(cantidadAux);
							detalle.setPrecioUnitario(precio);
							detalle.setTotal(precio*cantidadRestada);
							//detalleOrdenTraspasoRegistration.updated(detalle);
							total = total + detalle.getTotal();
						}else{//nuevo DetalleOrdenSalida con otro precio
							detalle.setId(0);
							detalle.setCantidadEntregada(cantidadRestada );
							detalle.setCantidadSolicitada(cantidadAux);
							detalle.setPrecioUnitario(precio);
							detalle.setTotal(precio*cantidadRestada);
							//detalleOrdenTraspasoRegistration.register(detalle);
							total = total + detalle.getTotal();
						}
						verificacionCantidadEntregada = verificacionCantidadEntregada + cantidadRestada;
						cantidad = cantidad + 1;
					}
				}
				totalCantidaEntregada = cantidadAux - cantidadSolicitada;
				return true;
			}
			return false;
		}catch(Exception e){
			System.out.println("actualizarDetalleProductoByOrdenSalida() ERROR: "+e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	//aumentar stock de almacen destino
	private void actualizarStockAlmacenDestino(Proveedor proveedor,Producto prod ,Almacen almDest, double newStock,Date date,double precioUnitario) throws Exception {
		try{
			System.out.println("actualizarStockAlmacenDestino()");
			//0 . verificar si existe el producto en el almacen

			AlmacenProducto almProd =  new AlmacenProducto();
			/*
			almProd =  almacenProductoRepository.findByAlmacenProducto(almDest,prod);
			System.out.println("almProd = "+almProd);
			if(almProd != null){
				// 1 .  si existe el producto
				double oldStock = almProd.getStock();
				double oldPrecioUnitario = almProd.getPrecioUnitario();
				almProd.setStock(oldStock + newStock);
				almProd.setPrecioUnitario((oldPrecioUnitario+precioUnitario)/2);//precioPonderado
				almacenProductoRegistration.updated(almProd);
				return ;
			}
			 */
			// 2 . no existe el producto
			almProd = new AlmacenProducto();
			almProd.setAlmacen(almDest);
			almProd.setPrecioVentaContado(precioUnitario);
			almProd.setProducto(prod);
			almProd.setProveedor(proveedor);//proveedor = null (Ingreso or Orden Traspaso)
			almProd.setStock(newStock);
			almProd.setEstado("AC");
			almProd.setGestion(gestionSesion);
			almProd.setFechaRegistro(date);
			almProd.setUsuarioRegistro(usuarioSession);
			//almacenProductoRegistration.register(almProd);
		}catch(Exception e){
			System.out.println("actualizarStockAlmacenDestino() Error: "+e.getMessage());
		}
	}

	//disminuir stock de almacen origen
	private void actualizarStockAlmacenOrigen(Almacen almOrig,Producto producto,double cantidadSolicitada) throws Exception {
		try{
			System.out.println("actualizarStockAlmacenOrigen()");
			/*
			//0 . verificar si existe el producto en el almacen

			AlmacenProducto almProd =  almacenProductoRepository.findByAlmacenProducto(almOrig,prod);
			if(almProd != null){
				// 1 .  si existe el producto
				double oldStock = almProd.getStock();
				double oldPrecioUnitario = almProd.getPrecioUnitario();
				almProd.setStock(oldStock - newStock);
				almProd.setPrecioUnitario((oldPrecioUnitario+precioUnitario)/2);//precioPonderado
				almacenProductoRegistration.updated(almProd);
				return ;
			}
			 */
			//Producto producto = detalle.getProducto();
			//double cantidadSolicitada = detalle.getCantidadSolicitada();// 15
			//obtener listAlmacenProducto ordenado por fecha segun metodo PEPS
			List<AlmacenProducto> listAlmacenProducto =  almacenProductoRepository.findAllByProductoAndAlmacenOrderByFecha(gestionSesion,almOrig,producto);
			System.out.println("listAlmacenProducto.size()"+listAlmacenProducto.size());

			if(listAlmacenProducto.size()>0){
				for(AlmacenProducto d : listAlmacenProducto){
					double stockActual = d.getStock();//10 
					if(cantidadSolicitada > 0){// 15 
						double stockFinal = stockActual- cantidadSolicitada; // 10-15=-5 | 10-5=5 |
						double cantidadRestada = stockFinal < 0 ? cantidadSolicitada -(cantidadSolicitada - stockActual) : cantidadSolicitada; //15-(15-10)=10 
						d.setStock( stockFinal <= 0 ? 0 : stockFinal); // 0 | 5
						d.setEstado(stockFinal<=0?"IN":"AC"); // IN | AC
						//almacenProductoRegistration.updated(d);
						cantidadSolicitada = cantidadSolicitada - cantidadRestada  ;//actualizar cantidad solicitada // 15-10=5
					}
				}
			}
		}catch(Exception e){
			System.out.println("actualizarStockAlmacenOrigen() Error: "+e.getMessage());
		}
	}

	public void cargarReporte(){
		try {
			urlOrdenTraspaso = loadURL();
			//RequestContext context = RequestContext.getCurrentInstance();
			//context.execute("PF('dlgVistaPreviaOrdenTraspaso').show();");

			verReport = true;

			//initNewOrdenTraspaso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public String loadURL(){
		try{
			//ReporteOrdenTraspaso?pIdOrdenTraspaso=7&pUsuario=admin&pTypeExport=pdf
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String urlPDFreporte = urlPath+"ReporteOrdenTraspaso?pIdOrdenTraspaso="+selectedOrdenTraspaso.getId()+"&pUsuario="+URLEncoder.encode(usuarioSession,"ISO-8859-1")+"&pTypeExport=pdf"+"&pNitEmpresa="+empresaLogin.getNit()+"&pNombreEmpresa="+URLEncoder.encode(empresaLogin.getRazonSocial(),"ISO-8859-1");
			return urlPDFreporte;
		}catch(Exception e){
			return "error";
		}
	}

	// DETALLE ORDEN Traspaso ITEMS

	public void editarDetalleOrdenTraspaso(){
		tituloProducto = "Modificar Producto";
		selectedProducto = selectedDetalleOrdenTraspaso.getProducto();
		verButtonDetalle = true;
		editarOrdenTraspaso = true;
		calcular();
	}

	public void borrarDetalleOrdenTraspaso(){
		listaDetalleOrdenTraspaso.remove(selectedDetalleOrdenTraspaso);
		listDetalleOrdenTraspasoEliminados.add(selectedDetalleOrdenTraspaso);
		FacesUtil.resetDataTable("formTableOrdenTraspaso:itemsTable1");
		verButtonDetalle = true;
	}

	public void limpiarDatosProducto(){
		selectedProducto = new Producto();
		selectedDetalleOrdenTraspaso = new DetalleOrdenTraspaso();
		FacesUtil.resetDataTable("formTableOrdenTraspaso:itemsTable1");
		verButtonDetalle = true;
		editarOrdenTraspaso = false;
	}

	private double cantidadExistenciasByProductoAlmacen(Almacen almacen,Producto producto){
		double cantidad = 0;
		List<DetalleProducto> listDetalleProducto = detalleProductoRepository.findAllByProductoAndAlmacenOrderByFecha(almacen,producto,gestionSesion);
		for(DetalleProducto detalle:listDetalleProducto){
			cantidad = cantidad + detalle.getStockActual();
		}
		return cantidad;
	}

	public void agregarDetalleOrdenTraspaso(){
		//verificar que seleccione el almacen
		if(selectedProducto.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Seleccione un producto");
			//FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			return;
		}
		//verificar si hay stock del producto
		double cantidad =  cantidadExistenciasByProductoAlmacen(selectedAlmacenOrigen,selectedProducto);
		if( cantidad==0 ){ 
			setTextDialogExistencias("El almacen "+selectedAlmacenOrigen.getNombre()+" no tiene existencias del producto "+selectedProducto.getNombre());
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}else if(cantidad < selectedDetalleOrdenTraspaso.getCantidadSolicitada()){
			setTextDialogExistencias("El almacen "+selectedAlmacenOrigen.getNombre()+" solo tiene "+cantidad+" existencias del producto "+selectedProducto.getNombre());
			//selectedDetalleOrdenSalida.setCantidadSolicitada(cantidad);
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}
		System.out.println("agregarDetalleOrdenTraspaso ");
		selectedDetalleOrdenTraspaso.setProducto(selectedProducto);
		listaDetalleOrdenTraspaso.add(0, selectedDetalleOrdenTraspaso);
		selectedProducto = new Producto();
		selectedDetalleOrdenTraspaso = new DetalleOrdenTraspaso();
		FacesUtil.resetDataTable("formTableOrdenTraspaso:itemsTable1");
		verButtonDetalle = true;
		FacesUtil.hideDialog("dlgProducto");//ocultar dialog
	}

	public void modificarDetalleOrdenTraspaso(){
		//verificar que seleccione el almacen
		if(selectedProducto.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Seleccione un producto");
			//FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			return;
		}
		//verificar si hay stock del producto
		double cantidad =  cantidadExistenciasByProductoAlmacen(selectedAlmacenOrigen,selectedProducto);
		if( cantidad==0 ){ 
			setTextDialogExistencias("El almacen "+selectedAlmacenOrigen.getNombre()+" no tiene existencias del producto "+selectedProducto.getNombre());
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}else if(cantidad < selectedDetalleOrdenTraspaso.getCantidadSolicitada()){
			setTextDialogExistencias("El almacen "+selectedAlmacenOrigen.getNombre()+" solo tiene "+cantidad+" existencias del producto "+selectedProducto.getNombre());
			//selectedDetalleOrdenSalida.setCantidadSolicitada(cantidad);
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}
		System.out.println("modificarDetalleOrdenTraspaso ");
		for(DetalleOrdenTraspaso d: listaDetalleOrdenTraspaso){
			if(d.equals(selectedDetalleOrdenTraspaso)){
				d = selectedDetalleOrdenTraspaso;
			}
		}
		selectedProducto = new Producto();
		selectedDetalleOrdenTraspaso = new DetalleOrdenTraspaso();
		FacesUtil.resetDataTable("formTableOrdenTraspaso:itemsTable1");
		verButtonDetalle = true;
		editarOrdenTraspaso = false;
		FacesUtil.hideDialog("dlgProducto");//ocultar dialog
	}

	//calcular totales
	public void calcular(){
		System.out.println("calcular()");
		//double precio = selectedProducto.getPrecioUnitario();
		//double cantidad = selectedDetalleOrdenTraspaso.getCantidadSolicitada();
		//selectedDetalleOrdenTraspaso.setTotal(precio * cantidad);
	}

	public void calcularTotal(){
		double totalImporte = 0;
		for(DetalleOrdenTraspaso d : listaDetalleOrdenTraspaso){
			totalImporte =totalImporte + d.getTotal();
		}
		newOrdenTraspaso.setTotalImporte(totalImporte);
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
				selectedAlmacen = i;
				return;
			}
		}
	}

	// ONCOMPLETETEXT PRODUCTO
	public List<Producto> completeProducto(String query) {
		String upperQuery = query.toUpperCase();
		return productoRepository.findAllProductoForQueryNombre(upperQuery);
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		List<Producto> listProducto = productoRepository.findAllProductoActivosByID();
		for(Producto i : listProducto){
			if(i.getNombre().equals(nombre)){
				selectedProducto = i;
				calcular();
				return;
			}
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
		return selectedAlmacen;//almacen destino
	}

	public void setSelectedAlmacen(Almacen selectedAlmacen) {
		this.selectedAlmacen = selectedAlmacen;
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

	public String getTituloProducto() {
		return tituloProducto;
	}

	public void setTituloProducto(String tituloProducto) {
		this.tituloProducto = tituloProducto;
	}

	public List<DetalleOrdenTraspaso> getListaDetalleOrdenTraspaso() {
		return listaDetalleOrdenTraspaso;
	}

	public void setListaDetalleOrdenTraspaso(List<DetalleOrdenTraspaso> listaDetalleOrdenTraspaso) {
		this.listaDetalleOrdenTraspaso = listaDetalleOrdenTraspaso;
	}

	public List<OrdenTraspaso> getListaOrdenTraspaso() {
		return listaOrdenTraspaso;
	}

	public void setListaOrdenTraspaso(List<OrdenTraspaso> listaOrdenTraspaso) {
		this.listaOrdenTraspaso = listaOrdenTraspaso;
	}

	public List<Almacen> getListaAlmacen() {
		return listaAlmacen;
	}

	public void setListaAlmacen(List<Almacen> listaAlmacen) {
		this.listaAlmacen = listaAlmacen;
	}

	public OrdenTraspaso getSelectedOrdenTraspaso() {
		return selectedOrdenTraspaso;
	}

	public void setSelectedOrdenTraspaso(OrdenTraspaso selectedOrdenTraspaso) {
		this.selectedOrdenTraspaso = selectedOrdenTraspaso;
	}

	public OrdenTraspaso getNewOrdenTraspaso() {
		return newOrdenTraspaso;
	}

	public void setNewOrdenTraspaso(OrdenTraspaso newOrdenTraspaso) {
		this.newOrdenTraspaso = newOrdenTraspaso;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public DetalleOrdenTraspaso getSelectedDetalleOrdenTraspaso() {
		return selectedDetalleOrdenTraspaso;
	}

	public void setSelectedDetalleOrdenTraspaso(
			DetalleOrdenTraspaso selectedDetalleOrdenTraspaso) {
		this.selectedDetalleOrdenTraspaso = selectedDetalleOrdenTraspaso;
	}

	public boolean isVerButtonDetalle() {
		return verButtonDetalle;
	}

	public void setVerButtonDetalle(boolean verButtonDetalle) {
		this.verButtonDetalle = verButtonDetalle;
	}

	public boolean isVerProcesar() {
		return verProcesar;
	}

	public void setVerProcesar(boolean verProcesar) {
		this.verProcesar = verProcesar;
	}

	public String getUrlOrdenTraspaso() {
		return urlOrdenTraspaso;
	}

	public void setUrlOrdenTraspaso(String urlOrdenTraspaso) {
		this.urlOrdenTraspaso = urlOrdenTraspaso;
	}

	public boolean isEditarOrdenTraspaso() {
		return editarOrdenTraspaso;
	}

	public void setEditarOrdenTraspaso(boolean editarOrdenTraspaso) {
		this.editarOrdenTraspaso = editarOrdenTraspaso;
	}

	public Almacen getSelectedAlmacenOrigen() {
		return selectedAlmacenOrigen;
	}

	public void setSelectedAlmacenOrigen(Almacen selectedAlmacenOrigen) {
		this.selectedAlmacenOrigen = selectedAlmacenOrigen;
	}

	public List<DetalleOrdenTraspaso> getListDetalleOrdenTraspasoSinStock() {
		return listDetalleOrdenTraspasoSinStock;
	}

	public void setListDetalleOrdenTraspasoSinStock(
			List<DetalleOrdenTraspaso> listDetalleOrdenTraspasoSinStock) {
		this.listDetalleOrdenTraspasoSinStock = listDetalleOrdenTraspasoSinStock;
	}

	public boolean isVerReport() {
		return verReport;
	}

	public void setVerReport(boolean verReport) {
		this.verReport = verReport;
	}

	public StreamedContent getdFile() {
		System.out.println("getdFile "+dFile);
		return dFile;
	}

	public void setdFile(StreamedContent dFile) {
		System.out.println("setdFile "+dFile);
		this.dFile = dFile;
	}

	public boolean isVerExport() {
		return verExport;
	}

	public void setVerExport(boolean verExport) {
		this.verExport = verExport;
	}

	public static void main(String[] args){
		System.out.println("ingreso");
		String[] arg = {"1","2","3","4","5","6"};
		for(String s : arg){
			System.out.println("s: "+s);
			if(s.equals("4")){
				return;
			}
		}
		System.out.println("salida ");
	}

	public String getTextDialogExistencias() {
		return textDialogExistencias;
	}

	public void setTextDialogExistencias(String textDialogExistencias) {
		this.textDialogExistencias = textDialogExistencias;
	}

}
