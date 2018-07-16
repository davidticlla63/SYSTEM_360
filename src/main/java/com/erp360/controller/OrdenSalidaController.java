package com.erp360.controller;

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
import org.richfaces.cdi.push.Push;

import com.erp360.dao.AlmacenDao;
import com.erp360.dao.AlmacenProductoDao;
import com.erp360.dao.DetalleOrdenSalidaDao;
import com.erp360.dao.DetalleProductoDao;
import com.erp360.dao.OrdenSalidaDao;
import com.erp360.dao.ProductoDao;
import com.erp360.dao.UsuarioDao;
import com.erp360.model.Almacen;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.DetalleOrdenSalida;
import com.erp360.model.DetalleProducto;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.OrdenSalida;
import com.erp360.model.Producto;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

@Named(value = "ordenSalidaController")
@ConversationScoped
public class OrdenSalidaController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	@Inject
	private FacesContext facesContext;

	//Repository
	private @Inject AlmacenDao almacenRepository;
	private @Inject UsuarioDao usuarioRepository;
	private @Inject OrdenSalidaDao ordenSalidaRepository;
	private @Inject DetalleOrdenSalidaDao detalleOrdenSalidaRepository;
	private @Inject DetalleProductoDao detalleProductoRepository;
	private @Inject AlmacenProductoDao almacenProductoRepository;
	private @Inject ProductoDao productoDao; 

	//STATE
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean verButtonDetalle = true;
	private boolean editarOrdenSalida = false;
	private boolean verProcesar = true;
	private boolean verReport = false;
	private boolean verDetalle= false;
	private boolean verButtonAnular;
	private boolean atencionCliente=false;

	//VAR
	private String tituloProducto = "Agregar Producto";
	private String tituloPanel = "Registrar Orden Salida";
	private String urlOrdenSalida = "";
	//contabiliza la cantidad verdadera que se entrego, aplicado en el metodo actualizarDetalleProductoByOrdenSalida(...);
	private double cantidadEntregada = 0;
	private String textDialogExistencias = "";

	//OBJECT
	private Producto selectedProducto;
	private Almacen selectedAlmacen;
	private Almacen selectedAlmacenOrigen;
	private OrdenSalida selectedOrdenSalida;
	private OrdenSalida newOrdenSalida;
	private DetalleOrdenSalida selectedDetalleOrdenSalida;

	//LIST
	private List<Usuario> listUsuario = new ArrayList<Usuario>();
	private List<DetalleOrdenSalida> listaDetalleOrdenSalida = new ArrayList<DetalleOrdenSalida>(); // ITEMS
	//private List<OrdenSalida> listaOrdenSalida = new ArrayList<OrdenSalida>();
	private List<Almacen> listaAlmacen = new ArrayList<Almacen>();
	private List<DetalleOrdenSalida> listDetalleOrdenSalidaEliminados = new ArrayList<DetalleOrdenSalida>();
	private List<Producto> listaProducto= new ArrayList<Producto>();
	private List<AlmacenProducto> listAlmacenProducto;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private Gestion gestionSesion;
	private Empresa empresaLogin;

	@PostConstruct
	public void initNewOrdenSalida() {

		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		gestionSesion = sessionMain.getGestionLogin();
		listUsuario = usuarioRepository.obtenerUsuarioOrdenAscPorId();
		empresaLogin = sessionMain.getEmpresaLogin();

		//inicializar FachadaOrdenSalida
		//fachadaOrdenSalida =new FachadaOrdenSalida();

		selectedProducto = new Producto();
		selectedAlmacen = new Almacen();

		selectedOrdenSalida = null;
		selectedDetalleOrdenSalida = new DetalleOrdenSalida();

		// tituloPanel
		tituloPanel = "Registrar Orden Salida";

		verButtonAnular= false;
		modificar = false;
		registrar = false;
		atencionCliente=false;
		verProcesar = true;
		verReport = false;
		verDetalle = false;

		if(FacesUtil.getSessionAttribute("pIdOrdenSalida")!=null){
			//modo ver detalle de la nota de venta
			Integer pIdOrdenSalida = (Integer) FacesUtil.getSessionAttribute("pIdOrdenSalida");
			FacesUtil.setSessionAttribute("pIdOrdenSalida",null);
			newOrdenSalida = ordenSalidaRepository.findById(pIdOrdenSalida);
			listaDetalleOrdenSalida = newOrdenSalida.getDetalleOrdenSalida();
			System.out.println("listaDetalleOrdenSalida: "+listaDetalleOrdenSalida);
			selectedAlmacen = newOrdenSalida.getAlmacen();
			verButtonAnular= false;
			modificar = false;
			registrar = false;
			atencionCliente=false;
			verProcesar = false;
			verReport = false;
			verDetalle = true;
		}else{
			listaDetalleOrdenSalida = new ArrayList<DetalleOrdenSalida>();
			int numeroCorrelativo = ordenSalidaRepository.obtenerNumeroOrdenSalida(gestionSesion);
			newOrdenSalida = new OrdenSalida();
			newOrdenSalida.setCorrelativo(cargarCorrelativo(numeroCorrelativo));
			newOrdenSalida.setEstado("AC");
			newOrdenSalida.setGestion(gestionSesion);
			newOrdenSalida.setFechaPedido(new Date());
			newOrdenSalida.setFechaRegistro(new Date());
			newOrdenSalida.setUsuarioRegistro(usuarioSession);
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
		return "page.xhtml?faces-redirect=true";
	}

	//----ACTION ENVENT
	public void actionLoadDialogProducto(){
		//verificar si ya selecciono un almacen
		System.out.println("id: "+selectedAlmacen.getId());
		if(selectedAlmacen.getId()==0){
			FacesUtil.infoMessage("Verificación","Antes debe seleccionar un Almacen");
			return;
		}
		FacesUtil.updateComponent("formDialogProducto");
		FacesUtil.showDialog("dlgProducto");
	}



	public void cambiarAspecto(){
		//verificar si el usuario logeado tiene almacen registrado
		selectedAlmacenOrigen = almacenRepository.obtenerPorUsuarior(sessionMain.getUsuarioLogin());
		if(selectedAlmacenOrigen.getId() == -1){
			FacesUtil.infoMessage("Usuario "+usuarioSession, "No tiene asignado un almacen");
			return;
		}
		//
		modificar = false;
		registrar = true;
	}

	public void cambiarAspectoModificar(){
		modificar = true;
		registrar = false;
		newOrdenSalida = selectedOrdenSalida;
		selectedAlmacen = selectedOrdenSalida.getAlmacen();
		//selectedDetalleUnidad = selectedOrdenSalida.getUnidadSolicitante();
		//selectedFuncionario = selectedOrdenSalida.getFuncionario();
		//selectedProyecto = selectedOrdenSalida.getProyecto();
		listaDetalleOrdenSalida = detalleOrdenSalidaRepository.findAllByOrdenSalida(selectedOrdenSalida);
	}

	public boolean estaGestionIniciada(){
		return gestionSesion.isIniciada();
	}

	public void anularOrden(){
		System.out.println("anularOrden()");
		FacesUtil.infoMessage("PROCESO ANULACION", "El proceso de anulacion puede demorar varios segundos...");
		try{
			//actualizar estado de orden de ingreso
			selectedOrdenSalida.setEstado("AN");
			//ordenSalidaRegistration.updated(selectedOrdenSalida);
			//KardexProducto kpAnt = null;
			//KardexProducto kpUltimo;
			Gestion gestion = selectedOrdenSalida.getGestion();
			String numeroTransaccion = "I-"+selectedOrdenSalida.getCorrelativo();

			//obtener kardex de acuerdo a la orden de ingreso a anular
			//KardexProducto kpSelected = kardexProductoRepository.findFirstByNumeroTransaccionAndGestion(numeroTransaccion, gestion);

			//otener el ultimo kardex registrado
			//			kpUltimo = kardexProductoRepository.findUltimo();
			//			if(kpUltimo == null){
			//				System.out.println("kpUltimo = "+kpUltimo);
			//			}else{
			//				System.out.println("kpUltimo = "+kpUltimo.getId());
			//			}
			//
			//			//====================================
			//			//eliminar los kardex deacuerdo a la transaccion
			//			String correlativo =  "I-"+selectedOrdenSalida.getCorrelativo();
			//			List<KardexProducto> listKardexProductoEliminar = kardexProductoRepository.findAllByNumeroTransaccionAndGestion(correlativo,gestion);
			//			System.out.println("listKardexProductoEliminar.size() = "+listKardexProductoEliminar.size());
			//			System.out.println("otro kpAnt = "+listKardexProductoEliminar.get(0).getId());
			//			if(listKardexProductoEliminar.size()>0){
			//				kpAnt = listKardexProductoEliminar.get(0);
			//			}
			//			for(KardexProducto object: listKardexProductoEliminar){
			//				object.setEstado("RM");
			//				kardexProductoRegistration.updated(object);
			//			}
			//			listKardexProductoEliminar = null;//liberar
			//
			//			//actualizar DetalleProducto and AlmacenProducto
			//			Date fech1 = kpSelected.getFechaRegistro();
			//			Producto prod1 = kpSelected.getProducto();
			//			Almacen alm1 = kpSelected.getAlmacen();
			//			Gestion ges1 = kpSelected.getGestion();
			//			actualizarAlmacenProductoAndDetalleProducto(prod1, alm1, ges1, fech1);
			//
			//			//recalcular kardex por producto
			//			if( kpUltimo!=null){
			//				if(kpAnt==null){
			//					kpAnt = new KardexProducto();
			//				}
			//				List<KardexProducto> listKardexProductoRecalcular = kardexProductoRepository.findAllDesdeHastaOrderedByID(kpAnt, kpUltimo);
			//				System.out.println("listKardexProductoRecalcular.size() = "+listKardexProductoRecalcular.size());
			//				System.out.println("INICIO= "+listKardexProductoRecalcular.get(0).getId());
			//				System.out.println("FIN = "+listKardexProductoRecalcular.get(listKardexProductoRecalcular.size()-1).getId());
			//				for(KardexProducto kpR: listKardexProductoRecalcular){
			//					if(kpR.getEstado().equals("AC")){
			//						Producto prod = kpR.getProducto();
			//						Almacen alm = kpR.getAlmacen();
			//						Gestion ges = kpR.getGestion();
			//						KardexProducto kardexAnterior = obtenerAnteriorKardexProducto(alm,prod,ges);
			//						if(kardexAnterior!=null){
			//							//cantidad anterior
			//							double aentradaCantidad =  kardexAnterior.getStock();
			//							double asalidaCantidad = kardexAnterior.getStockActual();
			//							double asaldoCantidad = kardexAnterior.getStockAnterior();
			//							//valor anterior
			//							double atotalEntrada = kardexAnterior.getTotalEntrada();
			//							double atotalSalida = kardexAnterior.getTotalSalida();
			//							double atotalSaldo = kardexAnterior.getTotalSaldo();
			//							//cantidad nuevo
			//							double nsaldoCantidad = kardexAnterior.getStockAnterior();
			//							//valor nuevo
			//							double ntotalSaldo = kardexAnterior.getTotalSaldo();
			//							if(kpR.getTipoMovimiento().equals("ORDEN INGRESO")){
			//								nsaldoCantidad = asaldoCantidad + aentradaCantidad;
			//								ntotalSaldo = atotalSaldo + atotalEntrada;
			//							}else if(kpR.getTipoMovimiento().equals("ORDEN SALIDA")){
			//								nsaldoCantidad = asaldoCantidad - asalidaCantidad;
			//								ntotalSaldo = atotalSaldo - atotalSalida;
			//							}
			//							kpR.setStockAnterior(nsaldoCantidad);
			//							kpR.setTotalSaldo(ntotalSaldo);
			//							kardexProductoRegistration.updated(kpR);
			//						}
			//					}
			//				}
			//			}
			FacesUtil.infoMessage("Orden Salida Anulada ", "Codigo: "+selectedOrdenSalida.getCorrelativo());
			//=====================================
		}catch(Exception e){
			System.out.println("Error al anular : "+e.getMessage());
		}
	}

	private void actualizarAlmacenProductoAndDetalleProducto(Producto prod,Almacen alm, Gestion ges, Date fech){
		//actualizar almacen_producto
		try{
			List<AlmacenProducto> listAlmProd =almacenProductoRepository.findByAlmacenProductoAndFecha(ges, alm, prod,fech);
			for(AlmacenProducto almProd: listAlmProd){
				almProd.setEstado("RM");
				//almacenProductoRegistration.updated(almProd);
			}

			//actualizar detalle_producto
			List<DetalleProducto> listDetProd = detalleProductoRepository.findByAlmacenProductoAndFecha(ges, alm, prod, fech);
			for(DetalleProducto detProd: listDetProd){
				detProd.setEstado("RM");
				//detalleProductoRegistration.updated(detProd);
			}
		}catch(Exception e){
			System.out.println("actualizarAlmacenProductoAndDetalleProducto Error: "+e.getMessage());
		}
	}

	//correlativo incremental por gestion
	private String cargarCorrelativo(int nroOrdenIngreso){
		//pather = "000001";
		//Date fecha = new Date(); 
		//String year = new SimpleDateFormat("yy").format(fecha);
		//String mes = new SimpleDateFormat("MM").format(fecha);
		return String.format("%06d", nroOrdenIngreso);
	}

	// SELECT ORDEN SALIDA CLICK
	public void onRowSelectOrdenSalidaClick(SelectEvent event) {
		try {
			if(selectedOrdenSalida.getEstado().equals("PR")){
				verProcesar = false;
				verButtonAnular = true;
			}else{
				verProcesar = true;
				verButtonAnular = false;
			}
			if(selectedOrdenSalida.getEstado().equals("AN")){
				verProcesar = false;
				verButtonAnular = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in onRowSelectOrdenSalidaClick: "
					+ e.getMessage());
		}
	}

	// SELECT DETALLE ORDEN SALIDA CLICK
	public void onRowSelectDetalleOrdenSalidaClick(SelectEvent event) {
		try {
			verButtonDetalle = false;
		} catch (Exception e) {
			System.out.println("Error in onRowSelectDetalleOrdenSalidaClick: "
					+ e.getMessage());
		}
	}

	public void registrarOrdenSalida() {
		if( selectedAlmacen.getId()==0 || selectedAlmacen.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "No puede haber campos vacios.");
			return;
		}
		if(listaDetalleOrdenSalida.size()==0 ){
			FacesUtil.infoMessage("VALIDACION", "Debe Agregar items.");
			return;
		}
		try {
			Date fechaActual = new Date();
			calcularTotal();
			System.out.println("Ingreso a registrarOrdenSalida: ");
			//			newOrdenSalida.setUnidadSolicitante(selectedDetalleUnidad);
			//			newOrdenSalida.setFuncionario(selectedFuncionario);
			//			newOrdenSalida.setProyecto(selectedProyecto);
			newOrdenSalida.setAlmacen(selectedAlmacen);
			newOrdenSalida.setGestion(gestionSesion);
			newOrdenSalida.setEstado("AC");
			//newOrdenSalida.setFechaRegistro(fechaActual);   (se habilito la fecha en el formulario)
			newOrdenSalida.setUsuarioRegistro(usuarioSession);
			//newOrdenSalida = ordenSalidaRegistration.register(newOrdenSalida);
			for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
				d.setCantidadEntregada(0);
				d.setFechaRegistro(fechaActual);
				d.setEstado("AC");
				d.setUsuarioRegistro(usuarioSession);
				d.setOrdenSalida(newOrdenSalida);
				//detalleOrdenSalidaRegistration.register(d);
			}
			FacesUtil.infoMessage("Orden de Salida Registrada!", ""+newOrdenSalida.getCorrelativo());
			initNewOrdenSalida();
		} catch (Exception e) {
			System.out.println("registrarOrdenSalida() ERROR: "+ e.getMessage());
		}
	}

	public void modificarOrdenSalida() {
		try {
			System.out.println("Ingreso a modificarOrdenSalida: ");
			Date fechaActual = new Date();
			double total = 0;
			for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
				d.setCantidadEntregada(0);
				if(d.getId()==0){//si es un nuevo registro
					d.setFechaRegistro(fechaActual);
					d.setUsuarioRegistro(usuarioSession);
					d.setEstado("AC");
					d.setOrdenSalida(newOrdenSalida);
					//detalleOrdenSalidaRegistration.register(d);
				}
				total = total + d.getTotal();
				//detalleOrdenSalidaRegistration.updated(d);
			}
			//borrado logico 
			for(DetalleOrdenSalida d: listDetalleOrdenSalidaEliminados){
				if(d.getId() != 0){
					d.setEstado("RM");
					//detalleOrdenSalidaRegistration.updated(d);
				}
			}
			newOrdenSalida.setAlmacen(selectedAlmacen);
			//newOrdenSalida.setFuncionario(selectedFuncionario);
			newOrdenSalida.setTotalImporte(total);
			//ordenSalidaRegistration.updated(newOrdenSalida);
			FacesUtil.infoMessage("Orden de Salida Modificada!", ""+newOrdenSalida.getCorrelativo());
			initNewOrdenSalida();

		} catch (Exception e) {
			System.out.println("modificarOrdenSalida() ERROR: "+ e.getMessage());
		}
	}

	public void eliminarOrdenSalida() {
		try {
			System.out.println("Ingreso a eliminarOrdenSalida: ");
			//ordenSalidaRegistration.remover(selectedOrdenSalida);
			//			for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
			//				detalleOrdenSalidaRegistration.remover(d);
			//			}
			FacesUtil.infoMessage("Orden de Salida Eliminada!", ""+newOrdenSalida.getId());
			initNewOrdenSalida();

		} catch (Exception e) {
			System.out.println("eliminarOrdenSalida() ERROR: "+ e.getMessage());
		}
	}

	public void procesarOrdenSalida(){
		try {
			System.out.println("procesarOrdenSalida()");
			Date fechaActual = new Date();
			//actualizar estado de orden ingreso
			selectedOrdenSalida.setEstado("PR");
			selectedOrdenSalida.setFechaAprobacion(fechaActual);
			//DetalleUnidad detalleUnidad = selectedOrdenSalida.getUnidadSolicitante();
			Almacen almacenOrigen = selectedOrdenSalida.getAlmacen();
			double total = 0;
			//actuaizar stock de AlmacenProducto
			listaDetalleOrdenSalida = detalleOrdenSalidaRepository.findAllByOrdenSalida(selectedOrdenSalida);
			for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
				Producto prod = d.getProducto();
				//1.- Actualizar detalle producto (PEPS) y tambien actualizar precio en detalleOrdenIngreso
				if( actualizarDetalleProductoByOrdenSalida(almacenOrigen,d)){
					//mostrar mensaje
					//FacesUtil.showDialog("dlgAlmacenSinExistencias");
					//initNewOrdenSalida();
					//return ; //no se econtro stock disponible
					//2
					//fachadaOrdenSalida.actualizarStock(gestionSesion,almacenOrigen,prod,cantidadEntregada);
					//3
					//fachadaOrdenSalida.actualizarKardexProducto( detalleUnidad.getNombre(),gestionSesion, selectedOrdenSalida,prod,fechaActual, cantidadEntregada,d.getPrecioUnitario(),usuarioSession);
					total = total + (cantidadEntregada * d.getPrecioUnitario());
				}
			}
			//cactualizar OrdenSalida
			selectedOrdenSalida.setTotalImporte(total);
			//ordenSalidaRegistration.updated(selectedOrdenSalida);
			FacesUtil.infoMessage("Orden de Ingreso Procesada!", "");
			initNewOrdenSalida();

		} catch (Exception e) {
			System.out.println("Error : "+e.getMessage());
			FacesUtil.errorMessage("Error al Procesar!");
		}
	}

	/**
	 * Actualiza el stock, verifica existencias de acuerdo al metodo PEPS
	 * @param almacen De que almacen se sacara los productos
	 * @param detalle
	 * @return true si hay stock, false si no hay existencias
	 */
	public boolean actualizarDetalleProductoByOrdenSalida(Almacen almacen,DetalleOrdenSalida detalle){
		try{
			cantidadEntregada = 0;
			Producto producto = detalle.getProducto();
			double cantidadAux = detalle.getCantidadSolicitada();
			double cantidadSolicitada = detalle.getCantidadSolicitada();//6
			int cantidad = 1;
			//obtener todos los detalles del producto, para poder descontar stock de acuerdo a la cantidad solicitada
			List<DetalleProducto> listDetalleProducto = detalleProductoRepository.findAllByProductoAndAlmacenOrderByFecha(almacen,producto,gestionSesion);
			//5 | 10
			if(listDetalleProducto.size()>0){
				for(DetalleProducto d : listDetalleProducto){
					double stockActual = d.getStockActual();//5 
					double precio = d.getPrecioVentaContado(); // 50 
					if(cantidadSolicitada > 0){// 6
						double stockFinal = stockActual- cantidadSolicitada; // 5-6=-1 | 10-5=5 |
						double cantidadRestada = stockFinal < 0 ? cantidadSolicitada -(cantidadSolicitada - stockActual) : cantidadSolicitada; //6-(6-5)=5 
						d.setStockActual( stockFinal <= 0 ? 0 : stockFinal); // 0 | 5
						d.setEstado(stockFinal<=0?"IN":"AC"); // IN | AC
						//detalleProductoRegistration.updated(d);
						cantidadSolicitada = cantidadSolicitada - cantidadRestada  ;//actualizar cantidad solicitada // 6-5=1
						if(cantidad == 1){
							detalle.setCantidadEntregada(cantidadRestada);
							detalle.setCantidadSolicitada(cantidadAux);
							detalle.setPrecioUnitario(precio);
							detalle.setTotal(precio*cantidadRestada);
							//detalleOrdenSalidaRegistration.updated(detalle);
						}else{//nuevo DetalleOrdenSalida con otro precio
							detalle.setId(0);
							detalle.setCantidadEntregada(cantidadRestada );
							detalle.setCantidadSolicitada(cantidadAux);
							detalle.setPrecioUnitario(precio);
							detalle.setTotal(precio*cantidadRestada);
							//detalleOrdenSalidaRegistration.register(detalle);
						}
						cantidad = cantidad + 1;
					}
				}
				cantidadEntregada = cantidadAux - cantidadSolicitada;
				return true;
			}
			return false;
		}catch(Exception e){
			System.out.println("actualizarDetalleProductoByOrdenSalida() ERROR: "+e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public void cargarReporte(){
		try {
			urlOrdenSalida = loadURL();
			//RequestContext context = RequestContext.getCurrentInstance();
			//context.execute("PF('dlgVistaPreviaOrdenSalida').show();");

			//initNewOrdenSalida();
			verReport = true;

		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public String loadURL(){
		try{
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String urlPDFreporte = urlPath+"ReporteOrdenSalida?pIdOrdenSalida="+selectedOrdenSalida.getId()+"&pUsuario="+URLEncoder.encode(usuarioSession,"ISO-8859-1")+"&pTypeExport=pdf"+"&pNitEmpresa="+empresaLogin.getNit()+"&pNombreEmpresa="+URLEncoder.encode(empresaLogin.getRazonSocial(),"ISO-8859-1");
			return urlPDFreporte;
		}catch(Exception e){
			return "error";
		}
	}

	// DETALLE ORDEN  ITEMS
	public void editarDetalleOrdenSalida(Integer idDetalleOrdenSalida){
		for(DetalleOrdenSalida det :listaDetalleOrdenSalida){
			if(det.getId().equals(idDetalleOrdenSalida)){
				selectedDetalleOrdenSalida = det;
			}
		}
		tituloProducto = "Modificar Producto";
		selectedProducto = selectedDetalleOrdenSalida.getProducto();
		verButtonDetalle = true;
		editarOrdenSalida = true;
		calcular();
	}

	public void borrarDetalleOrdenSalida(Integer idDetalleOrdenSalida){
		for(DetalleOrdenSalida det :listaDetalleOrdenSalida){
			if(det.getId().equals(idDetalleOrdenSalida)){
				selectedDetalleOrdenSalida = det;
			}
		}
		listaDetalleOrdenSalida.remove(selectedDetalleOrdenSalida);
		listDetalleOrdenSalidaEliminados.add(selectedDetalleOrdenSalida);
		verButtonDetalle = true;
	}

	public void limpiarDatosProducto(){
		selectedProducto = new Producto();
		selectedDetalleOrdenSalida = new DetalleOrdenSalida();
		verButtonDetalle = true;
		editarOrdenSalida = false;
	}

	private double cantidadExistenciasByProductoAlmacen(Almacen almacen,Producto producto){
		double cantidad = 0;
		List<DetalleProducto> listDetalleProducto = detalleProductoRepository.findAllByProductoAndAlmacenOrderByFecha(almacen,producto,gestionSesion);
		for(DetalleProducto detalle:listDetalleProducto){
			cantidad = cantidad + detalle.getStockActual();
		}
		return cantidad;
	}

	public void agregarDetalleOrdenSalida(){
		
		if(selectedProducto.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Seleccione un producto");
			//FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			return;
		}
		//verificar si hay stock del producto
		//double cantidad =  cantidadExistenciasByProductoAlmacen(selectedAlmacen,selectedProducto);
		double cantidad = stockDisponible;
		if( cantidad==0 ){ 
			textDialogExistencias = "El almacen "+selectedAlmacen.getNombre()+" no tiene existencias del producto "+selectedProducto.getNombre();
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}else if(cantidad < selectedDetalleOrdenSalida.getCantidadSolicitada()){
			textDialogExistencias = "El almacen "+selectedAlmacen.getNombre()+" solo tiene "+cantidad+" existencias del producto "+selectedProducto.getNombre();
			//selectedDetalleOrdenSalida.setCantidadSolicitada(cantidad);
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}

		System.out.println("agregarDetalleOrdenIngreso ");
		selectedDetalleOrdenSalida.setProducto(selectedProducto);
		listaDetalleOrdenSalida.add(0, selectedDetalleOrdenSalida);
		selectedProducto = new Producto();
		selectedDetalleOrdenSalida = new DetalleOrdenSalida();
		//FacesUtil.resetDataTable("formTableOrdenSalida:itemsTable1");
		verButtonDetalle = true;
		FacesUtil.hideDialog("dlgProducto");//ocultar dialog
	}

	public void modificarDetalleOrdenSalida(){
		//verificar que seleccione el almacen
		if(selectedAlmacen.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Antes debe seleccionar el almacen");
			return;
		}
		if(selectedProducto.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "Seleccione un producto");
			//FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			return;
		}
		//verificar si hay stock del producto
		double cantidad =  cantidadExistenciasByProductoAlmacen(selectedAlmacen,selectedProducto);
		if( cantidad==0 ){ 
			textDialogExistencias = "El almacen "+selectedAlmacen.getNombre()+" no tiene existencias del producto "+selectedProducto.getNombre();
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}else if(cantidad < selectedDetalleOrdenSalida.getCantidadSolicitada()){
			textDialogExistencias = "El almacen "+selectedAlmacen.getNombre()+" solo tiene "+cantidad+" existencias del producto "+selectedProducto.getNombre();
			//selectedDetalleOrdenSalida.setCantidadSolicitada(cantidad);
			//ocultar dialgo
			FacesUtil.hideDialog("dlgProducto");//ocultar dialog
			//abrir dialog
			FacesUtil.showDialog("dlgValidacionExistenciasAlmacen");
			return;
		}

		System.out.println("modificarDetalleOrdenSalida ");
		for(DetalleOrdenSalida d: listaDetalleOrdenSalida){
			if(d.equals(selectedDetalleOrdenSalida)){
				d = selectedDetalleOrdenSalida;
			}
		}
		selectedProducto = new Producto();
		selectedDetalleOrdenSalida = new DetalleOrdenSalida();
		FacesUtil.resetDataTable("formTableOrdenSalida:itemsTable1");
		verButtonDetalle = true;
		editarOrdenSalida = false;
		FacesUtil.hideDialog("dlgProducto");//ocultar dialog
	}

	//calcular totales
	public void calcular(){
		System.out.println("calcular()");
		//double precio = selectedProducto.getPrecioUnitario();
		//double cantidad = selectedDetalleOrdenSalida.getCantidadSolicitada();
		//selectedDetalleOrdenSalida.setTotal(precio * cantidad);
	}

	public void calcularTotal(){
		double totalImporte = 0;
		for(DetalleOrdenSalida d : listaDetalleOrdenSalida){
			totalImporte =totalImporte + d.getTotal();
		}
		newOrdenSalida.setTotalImporte(totalImporte);
	}


	// ONCOMPLETETEXT ALMACEN
	public List<Almacen> completeAlmacen(String query) {
		String upperQuery = query.toUpperCase();
		listaAlmacen = almacenRepository.findAllAlmacenForQueryNombre(upperQuery);
		return listaAlmacen;
	}

	public void onRowSelectAlmacenClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Almacen i : listaAlmacen){
			if(i.getNombre().equals(nombre)){
				//verificar si el almacen-gestion ya fue cerrado
				//				if(cierreGestionAlmacenRepository.finAlmacenGestionCerrado(i,gestionSesion) != null){
				//					FacesUtil.infoMessage("INFORMACION", "El lmacen "+i.getNombre()+" fué cerrado");
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
		String upperQuery = query.toUpperCase();
		//listAlmacenProducto = almacenProductoRepository.findAllProductoForQueryNombreAndAlmacen(upperQuery,selectedAlmacen);
		listaProducto =  productoDao.obtenerTodosPorNombreCodigo(upperQuery);
		//for(AlmacenProducto ap: listAlmacenProducto){
		//	listaProducto.add(ap.getProducto());
		//}
		return listaProducto;
	}

	public List<Producto> completeProducto2(String query) {
		String upperQuery = query.toUpperCase();
		listAlmacenProducto = almacenProductoRepository.findAllProductoForQueryNombreAndAlmacen(upperQuery,selectedAlmacen);
		listaProducto =  new ArrayList<>();
		for(AlmacenProducto ap: listAlmacenProducto){
			listaProducto.add(ap.getProducto());
		}
		return listaProducto;
	}
	
	public void onRowSelectProductoClick(SelectEvent event) {
		//Integer id =  ((Producto) event.getObject()).getId();
		//Producto p = new Producto();
		Producto p = (Producto)event.getObject();
		//p.setId(id);
		selectedProducto = listaProducto.get(listaProducto.indexOf(p));
		System.out.println("getDescripcion: "+selectedProducto.getDescripcion());
		System.out.println("getNombre: "+selectedProducto.getNombre());
		System.out.println("id: "+selectedProducto.getId());
		selectedProducto.setDescripcion(" "+selectedProducto.getDescripcion());
		AlmacenProducto ap = almacenProductoRepository.findByProductoConStockPromedio(sessionMain.getGestionLogin(),selectedProducto,selectedAlmacen);
		//for(AlmacenProducto ap : listAlmacenProducto){
			//if(ap.getProducto().getId().equals(id)){
				selectedProducto = ap.getProducto();
				selectedDetalleOrdenSalida.setCantidadSolicitada(ap.getStock());
				stockDisponible = ap.getStock();
				if(ap.getStock()==0){
					stockDisponible = 0;
					FacesUtil.infoMessage("Validación", "Este producto no tiene stock");
				}
			//	return;
			//}
		//}
	}
	
	private double stockDisponible = 0;
	
	public void onRowSelectProductoClick2(SelectEvent event) {
		Integer id =  ((Producto) event.getObject()).getId();
		
		System.out.println("id onrow: "+id);
		for(AlmacenProducto ap : listAlmacenProducto){
			if(ap.getProducto().getId().equals(id)){
				selectedProducto = ap.getProducto();
				selectedDetalleOrdenSalida.setCantidadSolicitada(ap.getStock());
				stockDisponible = ap.getStock();
				if(ap.getStock()==0){
					stockDisponible = 0;
					FacesUtil.infoMessage("Validación", "Este producto no tiene stock");
				}
				return;
			}
		}
	}

	private  boolean verificarExistencias(Producto producto, double cantidad){
		List<DetalleProducto> listDetalleProducto = detalleProductoRepository.findAllByProductoOrderByFecha(selectedProducto,gestionSesion);
		if(listDetalleProducto.size()>0){
		}
		return true;
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

	public boolean isAtencionCliente() {
		return atencionCliente;
	}

	public void setAtencionCliente(boolean atencionCliente) {
		this.atencionCliente = atencionCliente;
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

	public List<Almacen> getListaAlmacen() {
		return listaAlmacen;
	}

	public void setListaAlmacen(List<Almacen> listaAlmacen) {
		this.listaAlmacen = listaAlmacen;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
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

	public String getUrlOrdenSalida() {
		return urlOrdenSalida;
	}

	public void setUrlOrdenSalida(String urlOrdenSalida) {
		this.urlOrdenSalida = urlOrdenSalida;
	}

	public boolean isEditarOrdenSalida() {
		return editarOrdenSalida;
	}

	public void setEditarOrdenSalida(boolean editarOrdenSalida) {
		this.editarOrdenSalida = editarOrdenSalida;
	}

	public Almacen getSelectedAlmacenOrigen() {
		return selectedAlmacenOrigen;
	}

	public void setSelectedAlmacenOrigen(Almacen selectedAlmacenOrigen) {
		this.selectedAlmacenOrigen = selectedAlmacenOrigen;
	}

	public OrdenSalida getSelectedOrdenSalida() {
		return selectedOrdenSalida;
	}

	public void setSelectedOrdenSalida(OrdenSalida selectedOrdenSalida) {
		this.selectedOrdenSalida = selectedOrdenSalida;
	}

	public OrdenSalida getNewOrdenSalida() {
		return newOrdenSalida;
	}

	public void setNewOrdenSalida(OrdenSalida newOrdenSalida) {
		this.newOrdenSalida = newOrdenSalida;
	}

	public DetalleOrdenSalida getSelectedDetalleOrdenSalida() {
		return selectedDetalleOrdenSalida;
	}

	public void setSelectedDetalleOrdenSalida(DetalleOrdenSalida selectedDetalleOrdenSalida) {
		this.selectedDetalleOrdenSalida = selectedDetalleOrdenSalida;
	}

	public List<DetalleOrdenSalida> getListaDetalleOrdenSalida() {
		return listaDetalleOrdenSalida;
	}

	public void setListaDetalleOrdenSalida(List<DetalleOrdenSalida> listaDetalleOrdenSalida) {
		this.listaDetalleOrdenSalida = listaDetalleOrdenSalida;
	}

	public List<DetalleOrdenSalida> getListDetalleOrdenSalidaEliminados() {
		return listDetalleOrdenSalidaEliminados;
	}

	public void setListDetalleOrdenSalidaEliminados(
			List<DetalleOrdenSalida> listDetalleOrdenSalidaEliminados) {
		this.listDetalleOrdenSalidaEliminados = listDetalleOrdenSalidaEliminados;
	}

	public List<Producto> getListaProducto() {
		return listaProducto;
	}

	public void setListaProducto(List<Producto> listaProducto) {
		this.listaProducto = listaProducto;
	}

	public boolean isVerReport() {
		return verReport;
	}

	public void setVerReport(boolean verReport) {
		this.verReport = verReport;
	}

	public String getTextDialogExistencias() {
		return textDialogExistencias;
	}

	public void setTextDialogExistencias(String textDialogExistencias) {
		this.textDialogExistencias = textDialogExistencias;
	}

	public boolean isVerButtonAnular() {
		return verButtonAnular;
	}

	public void setVerButtonAnular(boolean verButtonAnular) {
		this.verButtonAnular = verButtonAnular;
	}

	public boolean isVerDetalle() {
		return verDetalle;
	}

	public void setVerDetalle(boolean verDetalle) {
		this.verDetalle = verDetalle;
	}

	public double getStockDisponible() {
		return stockDisponible;
	}

	public void setStockDisponible(double stockDisponible) {
		this.stockDisponible = stockDisponible;
	}

}
