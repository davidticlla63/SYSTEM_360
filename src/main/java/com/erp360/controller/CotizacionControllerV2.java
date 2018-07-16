package com.erp360.controller;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.CaptureEvent;
import org.primefaces.event.SelectEvent;

import com.erp360.dao.AlmacenProductoDao;
import com.erp360.dao.ClienteDao;
import com.erp360.dao.DetalleNotaVentaDao;
import com.erp360.dao.EncargadoVentaDao;
import com.erp360.dao.NotaVentaDao;
import com.erp360.dao.ParametroCobranzaDao;
import com.erp360.dao.ParametroCuotaDao;
import com.erp360.dao.ParametroInventarioDao;
import com.erp360.dao.ParametroVentaDao;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.Cliente;
import com.erp360.model.DetalleNotaVenta;
import com.erp360.model.Empresa;
import com.erp360.model.EncargadoVenta;
import com.erp360.model.Gestion;
import com.erp360.model.NotaVenta;
import com.erp360.model.ParametroCobranza;
import com.erp360.model.ParametroCuota;
import com.erp360.model.ParametroInventario;
import com.erp360.model.ParametroVenta;
import com.erp360.model.PlanCobranza;
import com.erp360.model.Producto;
import com.erp360.util.DateUtility;
import com.erp360.util.FacesUtil;
import com.erp360.util.NumberUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "cotizacionControllerV2")
@ViewScoped
public class CotizacionControllerV2 implements Serializable {

	private static final long serialVersionUID = 6986540930723020906L;

	//DAO
	private @Inject FacesContext facesContext;
	private @Inject NotaVentaDao notaVentaDao;
	private @Inject ClienteDao clienteDao;
	private @Inject ParametroVentaDao parametroVentaDao;
	private @Inject ParametroCobranzaDao parametroCobranzaDao;
	private @Inject ParametroCuotaDao parametroCuotaDao;
	private @Inject ParametroInventarioDao parametroInventarioDao;
	private @Inject AlmacenProductoDao almacenProductoDao;
	private @Inject EncargadoVentaDao encargadoVentaDao;
	private @Inject DetalleNotaVentaDao detalleNotaVentaDao;

	//OBJECT
	private NotaVenta cotizacion;
	private Cliente selectedCliente;
	private Cliente newCliente;
	private Producto selectedProducto;
	private AlmacenProducto selectedAlmacenProducto;
	private DetalleNotaVenta selectedDetalleNotaVenta;
	private ParametroInventario selectedParametroInventario;
	private ParametroCobranza selectedParametroCobranza;
	private ParametroVenta selectedParametroVenta;
	private byte[] data;

	//LIST
	private List<Cliente> listCliente;
	private List<Producto> listProducto;
	private List<AlmacenProducto> listAlmacenProducto;
	private List<DetalleNotaVenta> listDetalleNotaVenta;
	private List<PlanCobranza> listPlanPago;
	private List<EncargadoVenta> listVendedores;
	private List<EncargadoVenta> publishers;

	//STATE
	private boolean crear ;
	private boolean registrar ;
	private boolean modificar ;
	private boolean verReporte;
	private boolean modoVista;//cuando solo quiere ver , una venta realizada
	//private boolean encargadoVenta;

	//VAR
	private String nombreEstado="ACTIVO";
	private double importeParcialDetalleCotizacion;
	private double importeParcialExtanjeroDetalleCotizacion;
	private double porcentajeCuotaInicial;
	private double cuotaInicialNacional ;
	private double cuotaInicialExtranjera ;
	private int numeroPagos;
	private int numeroDiasPlanPago;
	private String urlReciboAmortizacion;
	private String urlProformaAlCredito;
	private String urlReporteNotaCargo;
	private String urlReportePlanPago;
	private int diaPago;
	private double pagoNacional;
	private double pagoExtranjero;
	private double cambioNacional;
	private double cambioExtranjero;
	private String observacion;
	private double totalPlanPagoNacional;
	private double totalPlanPagoExtranjero;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Empresa empresaSession;
	private EncargadoVenta  selectedVendedor;
	private Gestion gestionSesion;

	@PostConstruct
	public void init() {
		empresaSession = sessionMain.getEmpresaLogin();
		gestionSesion = sessionMain.getGestionLogin();
		loadDefault();
	}

	public void loadDefault(){
		modoVista = false;
		totalPlanPagoNacional = 0;
		totalPlanPagoExtranjero = 0;
		observacion = "Pago de cuota inicial";
		pagoNacional = 0;
		pagoExtranjero = 0;
		porcentajeCuotaInicial = 28;
		cuotaInicialNacional = 0;
		cuotaInicialExtranjera = 0;
		diaPago = 15;
		urlReporteNotaCargo = "";
		urlReciboAmortizacion = "";
		urlProformaAlCredito = "";
		importeParcialDetalleCotizacion = 0;
		importeParcialExtanjeroDetalleCotizacion = 0;
		numeroPagos = 3;
		numeroDiasPlanPago = 30;

		selectedVendedor = new EncargadoVenta();

		crear = true;
		registrar = false;
		modificar = false;
		verReporte = false;
		listVendedores = encargadoVentaDao.getAllSalesPersionOrderById();
		publishers = encargadoVentaDao.getAllPublisherPersionOrderById();

		newCliente = new Cliente();
		newCliente.setCodigo(String.format("%08d",clienteDao.obtenerCorrelativo2()));
		cotizacion = new NotaVenta();
		cotizacion.setFechaPagoInicial(new Date());
		cotizacion.setCodigo(String.format("%06d",notaVentaDao.correlativoNotaVenta(gestionSesion)));
		cotizacion.setFechaVenta(new Date());
		cotizacion.setFechaRegistro(new Date());
		cotizacion.setTipoCambio(sessionMain.getTipoCambio().getUnidad());
		cotizacion.setPublisher(publishers.get(0));
		selectedDetalleNotaVenta= new DetalleNotaVenta();

		selectedCliente = new Cliente();
		selectedProducto = new Producto();
		selectedAlmacenProducto = new AlmacenProducto();

		listProducto = new ArrayList<>();
		listAlmacenProducto = new ArrayList<>();
		listCliente = new ArrayList<>();
		listDetalleNotaVenta = new ArrayList<>();
		listPlanPago = new ArrayList<>();
		selectedParametroInventario = parametroInventarioDao.obtenerParametroInventario();
		selectedParametroVenta = parametroVentaDao.obtener();
		selectedParametroCobranza = parametroCobranzaDao.obtenerParametroCobanza();

		selectedVendedor = listVendedores.size()>0? listVendedores.get(0):new EncargadoVenta();
		//si es para anular
		if(FacesUtil.getSessionAttribute("pIdNotaVenta")!=null){
			//modo ver detalle de la nota de venta
			Integer idNotaVenta = (Integer) FacesUtil.getSessionAttribute("pIdNotaVenta");
			FacesUtil.setSessionAttribute("pIdNotaVenta",null);
			cotizacion = notaVentaDao.findById(idNotaVenta);
			listDetalleNotaVenta = detalleNotaVentaDao.obtenerDetalleNotaVenta(cotizacion);
			selectedCliente = cotizacion.getCliente();
			modoVista = true;
		}
	}

	public void changeSalesType(){
		System.out.println("sales type: "+cotizacion.getTipoVenta());
		actualizarTotalNotaVenta();
		if(cotizacion.getTipoVenta().equals("CREDITO")){
			//actualizar la cuota inicial
			cotizacion.setCuotaInicial(cotizacion.getMontoTotal() * (porcentajeCuotaInicial/100));
			cotizacion.setCuotaInicialExtranjero(cotizacion.getMontoTotalExtranjero() * (porcentajeCuotaInicial/100));
		}else{
			//actualizar la cuota inicial
			cotizacion.setCuotaInicial(cotizacion.getMontoTotal());
			cotizacion.setCuotaInicialExtranjero(cotizacion.getMontoTotalExtranjero());
			//reload plan de pago
			cargarPlanPago();
		}
	}

	public void cargarPlanPago(){
		if(cotizacion.getTipoVenta().equals("CONTADO") ){
			cotizacion.setCoeficienteInteres(0);
			cotizacion.setPorcentajeCuotaInicial(0);
			cotizacion.setNumeroCuotas(0);
		}else if (cotizacion.getTipoVenta().equals("CREDITO") ){
			//coef. interes mensual
			ParametroVenta pv = parametroVentaDao.obtener();
			cotizacion.setCoeficienteInteres(pv.getCoeficienteInteresMensual());//por default MENSUAL
			//-----------------------------------------------------
			//obtener : % cuota inicial y numero Pagos
			List<ParametroCuota> listParametroCuota = parametroCuotaDao.obtenerTodosActivos();
			porcentajeCuotaInicial = 28;
			numeroPagos = 2;
			for(ParametroCuota pc: listParametroCuota){
				if(pc.getMontoRangoInicial() <= cotizacion.getMontoTotalExtranjero() && cotizacion.getMontoTotalExtranjero()<= pc.getMontoRangoFinal()){
					porcentajeCuotaInicial = pc.getPorcentajeCuotaInicial();
					numeroPagos = pc.getNumeroCuotas();
					break;
				}
			}
			//-----------------------------------------------------
			cotizacion.setPorcentajeCuotaInicial(porcentajeCuotaInicial);
			cotizacion.setNumeroCuotas(numeroPagos);
			cotizacion.setCuotaInicial(cotizacion.getMontoTotal() * (porcentajeCuotaInicial/100));
			cotizacion.setCuotaInicialExtranjero(cotizacion.getMontoTotalExtranjero() * (porcentajeCuotaInicial/100));
			cuotaInicialNacional = cotizacion.getCuotaInicial();
			cuotaInicialExtranjera = cotizacion.getCuotaInicialExtranjero();
		}
		actionReloadPlanPago();
	}
	//ACTION EVENT

	public void loadModalPlanPago(){
		FacesUtil.updateComponent("formModalPlanPago");
		FacesUtil.showModal("m-r-1");
	}

	public void closeModalPlanPago(){
		FacesUtil.hideModal("m-r-1");	
	}

	public void loadModalCliente(){
		FacesUtil.updateComponent("formModalCliente");
		FacesUtil.showModal("m-r-2");
	}

	public void closeModalCliente(){
		FacesUtil.hideModal("m-r-2");	
	}

	public void loadModalModificarProducto(){
		FacesUtil.updateComponent("formModalModificarProducto");
		FacesUtil.showModal("m-r-3");
	}

	public void closeModalModificarProducto(){
		FacesUtil.hideModal("m-r-3");	
	}

	public void loadModalPago(){
		FacesUtil.updateComponent("formModalPago");
		FacesUtil.showModal("m-r-4");
	}

	public void closeModalPago(){
		FacesUtil.hideModal("m-r-4");	
	}

	public void loadDialogImage() {
		FacesUtil.showDialog("dlgImagenCliente");
		FacesUtil.updateComponent("dlgImagenCliente");
	}

	//CAMERA
	public void oncapture(CaptureEvent captureEvent) {
		data = captureEvent.getData();
		FacesUtil.setSessionAttribute("imagenCliente", data);
		FacesUtil.hideDialog("dlgphotoCam");
	}

	public void actualizarImporteParcialDetalleNotaVenta(){
		importeParcialDetalleCotizacion = selectedDetalleNotaVenta.getPrecio() * selectedDetalleNotaVenta.getCantidad();
		importeParcialExtanjeroDetalleCotizacion = selectedDetalleNotaVenta.getPrecioExtranjero() * selectedDetalleNotaVenta.getCantidad();
	}

	public void actionNuevoCliente(){
		selectedCliente = new Cliente();
		loadModalCliente();
	}

	public void verPlanPago(){
		if(listDetalleNotaVenta.size() == 0){
			FacesUtil.infoMessage("Verificación", "Agregue productos al detalle.");
			return;
		}
		loadModalPlanPago();
	}

	public void loadPagoCliente(){
		if(selectedCliente.getId() == 0){
			FacesUtil.infoMessage("Verificación", "Seleccione un cliente");
			return;
		}
		if(listDetalleNotaVenta.size() == 0){
			FacesUtil.infoMessage("Verificación", "Debe agregar Producto(s).");
			return;
		}
		if( cotizacion.getTipoVenta().equals("CREDITO")){//CREDITO
			actionReloadPlanPago();
		}else {//CONTADO
			cotizacion.setCuotaInicial(cotizacion.getMontoTotal());
			cotizacion.setCuotaInicialExtranjero(cotizacion.getCuotaInicialExtranjero());
		}
		loadModalPago();
	}

	public void actionModificarDetalleNotaVenta(Integer idDetalleNotaVenta){
		for(DetalleNotaVenta dnv :listDetalleNotaVenta){
			if(dnv.getId().equals(idDetalleNotaVenta)){
				selectedDetalleNotaVenta = dnv;
			}
		}
		selectedProducto = selectedDetalleNotaVenta.getProducto();
		actualizarImporteParcialDetalleNotaVenta();
		loadModalModificarProducto();
	}

	public void actionCancelarModificarProducto(){
		closeModalModificarProducto();
		selectedProducto = new Producto();
		selectedDetalleNotaVenta = new DetalleNotaVenta();
		FacesUtil.updateComponent("form001");
	}

	private void cargarReporteAmortizacion(){
		verReporte = true;
		Map<String,String> map1 = new HashMap<>();
		map1.put("pUsuario", sessionMain.getUsuarioLogin().getLogin());
		map1.put("pRazonSocial", empresaSession.getRazonSocial());
		map1.put("pDireccion", empresaSession.getDireccion());
		map1.put("pTelefono", empresaSession.getTelefono());
		map1.put("pIdNotaVenta", String.valueOf(cotizacion.getId()));
		map1.put("pIdEmpresa", String.valueOf(empresaSession.getId()));
		map1.put("pMoneda", cotizacion.getMoneda());
		map1.put("pPagoNacional", String.valueOf(pagoNacional));
		map1.put("pPagoExtranjero", String.valueOf(pagoExtranjero));
		map1.put("pTipoRecibo", "ORIGINAL");
		urlReciboAmortizacion = buildUrl("ReportReciboAmortizacionNotaVenta",map1);
		if(cotizacion.getTipoVenta().equals("CREDITO")){
			//	mostrar proforma con su plan de pago al credito
			Map<String,String> map2 = new HashMap<>();
			map2.put("pUsuario", sessionMain.getUsuarioLogin().getLogin());
			map2.put("pRazonSocial", empresaSession.getRazonSocial());
			map2.put("pDireccion", empresaSession.getDireccion());
			map2.put("pTelefono", empresaSession.getTelefono());
			map2.put("pIdNotaVenta", String.valueOf(cotizacion.getId()));
			map2.put("pIdEmpresa", String.valueOf(empresaSession.getId()));
			map2.put("pMoneda", cotizacion.getMoneda());
			map2.put("pPagoNacional", String.valueOf(pagoNacional));
			map2.put("pPagoExtranjero", String.valueOf(pagoExtranjero));
			map2.put("pTipoRecibo", "ORIGINAL");
			urlProformaAlCredito = buildUrl("ReportProformaAlCredito",map2);
		} else if(cotizacion.getTipoVenta().equals("CONTADO")){
			//mostrar recibo del pago total???????
		}
	}
	
	public void actionChangeTipoCambio(){
		//verificar la moneda
		String moneda = cotizacion.getMoneda();
		//tipo cambio
		double tipoCambio = cotizacion.getTipoCambio();
		if(moneda.equals("BOLIVIANOS")){
			//monto reserva nacional
			//double montoReservaNacional = notaVenta.getMontoReserva();
			//notaVenta.setMontoReservaExtranjero(montoReservaNacional/tipoCambio);
		}else{
			//monto reserva extranjera
			//double montoReservaExtranjero = notaVenta.getMontoReservaExtranjero();
			//notaVenta.setMontoReserva(montoReservaExtranjero*tipoCambio);
		}
	}

	//PROCESOS

	public void registrarNotaVenta(){
		//validaciones
		if(selectedVendedor.getId().equals(0)){
			FacesUtil.infoMessage("Verificación", "Debe agregar un Vendedor");
			return;
		}
		if(cotizacion.getTipoVenta().equals("CONTADO")){
			cotizacion.setEstado("AC");
			cotizacion.setEstadoPago("PR");//PROCESADO
			//limpiar
			cotizacion.setCoeficienteInteres(0);
			cotizacion.setFechaPagoInicial(null);
			cotizacion.setNumeroCuotas(0);
			cotizacion.setPorcentajeCuotaInicial(0);
			listPlanPago = new ArrayList<>();
		}else if(cotizacion.getTipoVenta().equals("CREDITO")){
			cotizacion.setSaldoFinanciarNacional(totalPlanPagoNacional);
			cotizacion.setSaldoFinanciarExtranjero(totalPlanPagoExtranjero);
			cotizacion.setEstado("AC");
			cotizacion.setEstadoPago("PN");//PENDIENTE
		}
		cotizacion.setGestion(gestionSesion);
		cotizacion.setCliente(selectedCliente);
		cotizacion.setEmpresa(empresaSession);
		cotizacion.setFechaRegistro(new Date());
		cotizacion.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		cotizacion.setEncargadoVenta(selectedVendedor);
		NotaVenta nv = null;//notaVentaDao.registrar(totalPlanPagoNacional,totalPlanPagoExtranjero,observacion,sessionMain.getUsuarioLogin(),cotizacion,listDetalleNotaVenta,listPlanPago,gestionSesion,selectedParametroInventario,selectedParametroCobranza ,selectedParametroVenta);
		if(nv != null){
			cargarReporteAmortizacion();
			closeModalPlanPago();
			closeModalPago();
			FacesUtil.updateComponent("form001");
		}
	}

	public void modificarDetalleNotaVenta(){
		if(selectedProducto.getId() == 0){
			FacesUtil.infoMessage("", "Seleccione un producto");
			return;
		}
		for(DetalleNotaVenta dnv : listDetalleNotaVenta){
			if(dnv.getId().equals(selectedDetalleNotaVenta.getId())){
				selectedDetalleNotaVenta.setProducto(selectedProducto);
				dnv = selectedDetalleNotaVenta;
			}
		}
		closeModalModificarProducto();
		actualizarTotalNotaVenta();
		selectedProducto = new Producto();
		selectedDetalleNotaVenta = new DetalleNotaVenta();
		importeParcialDetalleCotizacion = 0;
		importeParcialExtanjeroDetalleCotizacion = 0;
		FacesUtil.updateComponent("form001");
	}

	public void eliminarDetalleNotaVenta(Integer idDetalleNotaVenta){
		DetalleNotaVenta temp = new DetalleNotaVenta();
		for(DetalleNotaVenta dnv :listDetalleNotaVenta){
			if(dnv.getId().equals(idDetalleNotaVenta)){
				temp = dnv;
			}
		}
		listDetalleNotaVenta.remove(temp);
	//	actualizarTotalNotaVenta();
		changeSalesType();
		FacesUtil.updateComponent("form001");
	}

	public void agregarDetalleNotaVenta() {
		try {
			if(selectedProducto.getId()== 0){
				FacesUtil.infoMessage("VALIDACION", "Agregue un producto.");
				return;
			}
			if (selectedDetalleNotaVenta.getCantidad() <= 0 ) {
				FacesUtil.infoMessage("VALIDACION", "La Cantidad debe ser mayor a 0.");
				return;
			}
			if (selectedDetalleNotaVenta.getPrecio() <= 0 ) {
				FacesUtil.infoMessage("VALIDACION", "Revisar el precio.");
				return;
			}
			// verifica si tiene stock
			AlmacenProducto almProd = productoTieneStock(selectedProducto);
			boolean sw = almProd==null?false:almProd.getStock()>=selectedDetalleNotaVenta.getCantidad();
			if(!sw){
				FacesUtil.infoMessage("VALIDACION", "El producto NO tiene existencias.");
				return;
			}
			DetalleNotaVenta detalle = obtenerDetalleNotaVentaSiExisteProducto();
			if(detalle != null){
				detalle.setCantidad(detalle.getCantidad()+1);
				//double precio = detalle.getCantidad()*detalle.getPrecio();
				detalle.setPrecio(detalle.getPrecio()+selectedDetalleNotaVenta.getPrecio());
				listDetalleNotaVenta.set(listDetalleNotaVenta.indexOf(detalle),detalle);
			}else{
				selectedDetalleNotaVenta.setId(listDetalleNotaVenta.size() * (-1));//asignacion de id's temp
				selectedDetalleNotaVenta.setProducto(selectedProducto);
				listDetalleNotaVenta.add(selectedDetalleNotaVenta);
			}
			//actualizar monto total
			actualizarTotalNotaVenta();
			//limpiar campos
			selectedDetalleNotaVenta = new DetalleNotaVenta();
			selectedProducto = new Producto();
			importeParcialDetalleCotizacion = 0;
			importeParcialExtanjeroDetalleCotizacion = 0;
			cargarPlanPago();
			changeSalesType();
		} catch (Exception e) {
			System.out.println("Error en agregarConcepto : " + e.getStackTrace());
		}
	}

	private AlmacenProducto productoTieneStock(Producto selectedProducto){
		//Verificar que tipo de Metodo {PEPS | UEPS | PPP}
		String valuacionInventario = selectedParametroInventario.getTipoValuacion();
		//Verificar si tiene stock
		AlmacenProducto almacenProducto = new AlmacenProducto();
		switch (valuacionInventario) {
		case  "PEPS":
			//almacenProducto = almacenProductoDao.obtenerAlmacenProductoPorPEPS(selectedProducto);
			break;
		case  "UEPS":
			//almacenProducto = almacenProductoDao.obtenerAlmacenProductoPorUEPS(selectedProducto);
			break;
		case  "PPP":
			//almacenProducto = almacenProductoDao.obtenerAlmacenProductoPorPPP(selectedProducto);
			break;
		default:
			break;
		}
		return almacenProducto;//==null?false:almacenProducto.getStock()>=selectedDetalleNotaVenta.getCantidad();
	}

	private void actualizarTotalNotaVenta(){
		double total = 0;
		double totalExtranjero = 0;
		double totalContadoNacional = 0;
		double totalContadoExtranjero = 0;
		for(DetalleNotaVenta dnv : listDetalleNotaVenta){
			if(cotizacion.getTipoVenta().equals("CREDITO")){
				total = total + (dnv.getPrecio() * dnv.getCantidad());
				totalExtranjero = totalExtranjero + (dnv.getPrecioExtranjero() * dnv.getCantidad());
			}
			if(cotizacion.getTipoVenta().equals("CONTADO")){
				totalContadoNacional = totalContadoNacional + (dnv.getPrecioContadoNacional() * dnv.getCantidad());
				totalContadoExtranjero = totalContadoExtranjero + (dnv.getPrecioContadoExtranjero() * dnv.getCantidad());
			}
		}
		cotizacion.setMontoTotal(cotizacion.getTipoVenta().equals("CREDITO")?total:totalContadoNacional);
		cotizacion.setMontoTotalExtranjero(cotizacion.getTipoVenta().equals("CREDITO")?totalExtranjero:totalContadoExtranjero);
//		changeSalesType();
//		if(listDetalleNotaVenta.size()==0){
//			notaVenta.setCuotaInicial(0);
//			notaVenta.setCuotaInicialExtranjero(0);
//		}
	}

	private DetalleNotaVenta obtenerDetalleNotaVentaSiExisteProducto(){
		for(DetalleNotaVenta detalle: listDetalleNotaVenta){
			if(detalle.getProducto().equals(selectedProducto)){
				return detalle;//.setCantidad(detalle.getCantidad()+1);
			}
		}
		return null;
	}

	//PLAN DE PAGO

	public void changeWeyToPay(){
		String weyToPay = cotizacion.getFormaPago();//MENSUAL , QUINCENAL , SEMANAL
		ParametroVenta salesOfParameter = parametroVentaDao.obtener();
		switch (weyToPay) {
		case "SEMANAL":
			cotizacion.setCoeficienteInteres(salesOfParameter.getCoeficienteInteresSemanal());
			break;
		case "QUINCENAL":
			cotizacion.setCoeficienteInteres(salesOfParameter.getCoeficienteInteresQuincenal());
			break;
		case "MENSUAL":
			cotizacion.setCoeficienteInteres(salesOfParameter.getCoeficienteInteresMensual());
			break;

		default:
			break;
		}
		actionReloadPlanPago();//call method general , load plan 
	}

	public void actionReloadPlanPago(){
		String formaPago = cotizacion.getFormaPago();//MENSUAL , QUINCENAL , SEMANAL
		Date today = new Date();//notaVenta.getFechaPagoInicial();
		today.setTime(cotizacion.getFechaPagoInicial().getTime());
		double coeficienteInteres = cotizacion.getCoeficienteInteres();
		int numeroCuotas = cotizacion.getNumeroCuotas();
		if(porcentajeCuotaInicial != cotizacion.getPorcentajeCuotaInicial()){
			//Si se cambio el porcentajeCuotaInicial
			porcentajeCuotaInicial = cotizacion.getPorcentajeCuotaInicial();
			cuotaInicialNacional = cotizacion.getMontoTotal()*(cotizacion.getPorcentajeCuotaInicial()/100);
			cotizacion.setCuotaInicial(cuotaInicialNacional);
			cuotaInicialExtranjera = cotizacion.getMontoTotalExtranjero()*(cotizacion.getPorcentajeCuotaInicial()/100);
			cotizacion.setCuotaInicialExtranjero(cuotaInicialExtranjera);
		}else if(cuotaInicialExtranjera!=cotizacion.getCuotaInicialExtranjero() || cuotaInicialNacional!=cotizacion.getCuotaInicial()){
			//Si se cambio cuotaInicialExtranjera o cuotaInicialNacional
			if(cotizacion.getMoneda().equals("BOLIVIANOS")){
				porcentajeCuotaInicial = (cotizacion.getCuotaInicial()/cotizacion.getMontoTotal())*100;
			}else if(cotizacion.getMoneda().equals("DOLAR")){
				porcentajeCuotaInicial = (cotizacion.getCuotaInicialExtranjero()/cotizacion.getMontoTotalExtranjero())*100; 
			}
			cotizacion.setPorcentajeCuotaInicial(porcentajeCuotaInicial);
			cuotaInicialNacional = cotizacion.getCuotaInicial();
			cuotaInicialExtranjera = cotizacion.getCuotaInicialExtranjero();
		}else{
			cuotaInicialNacional = cotizacion.getCuotaInicial();
			cuotaInicialExtranjera = cotizacion.getCuotaInicialExtranjero();
		}
		double montoTotalNacional = cotizacion.getMontoTotal();
		double montoTotalNacionalExtranjero = cotizacion.getMontoTotalExtranjero();//590
		double tipoCambio = cotizacion.getTipoCambio();
		double x1 = numeroCuotas * coeficienteInteres;// 8 * 2.5 = 20
		double x11 = montoTotalNacionalExtranjero - cuotaInicialExtranjera;//590-165.20=424.80
		double x2 = x11 * ( x1 / 100 );//424.80 * 0.20 = 84.96
		double x3 = x11 + x2;// 424.80 + 84.96 = 509.76
		double montoMensualDolar = x3 / numeroCuotas;//montoMensualDolar
		double montoMensualBolivianos = montoMensualDolar * tipoCambio; // montoMensualBolivianos
		double interesMensualNacional = ((montoMensualBolivianos*numeroCuotas)-(montoTotalNacional - cuotaInicialNacional))/numeroCuotas; //interesMensualNacional
		double interesMensualExtranjero = ((montoMensualDolar*numeroCuotas)-(montoTotalNacionalExtranjero - cuotaInicialExtranjera))/numeroCuotas; ; //interesMensualExtranjero

		today = validateDayOfMonth(today,formaPago);
		int cont = 1;
		listPlanPago = new ArrayList<>();
		totalPlanPagoNacional = 0;
		totalPlanPagoExtranjero = 0;
		while(cont<=numeroCuotas){
			PlanCobranza pg = new PlanCobranza();
			pg.setNumeroPago(cont);
			pg.setEstado("AC");
			pg.setFechaRegistro(new Date());
			today = addDayToDateWayToPay(formaPago,today,diaPago);
			pg.setFechaPago(today);
			pg.setMontoNacional(montoMensualBolivianos);
			pg.setMontoExtranjero(montoMensualDolar);
			pg.setInteresMensual(interesMensualNacional);
			pg.setInteresMensualExtranjero(interesMensualExtranjero);
			pg.setTipoCambio(cotizacion.getTipoCambio());
			pg.setTipoMoneda(cotizacion.getMoneda());
			pg.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
			listPlanPago.add(pg);
			totalPlanPagoNacional = totalPlanPagoNacional + pg.getMontoNacional();
			totalPlanPagoExtranjero = totalPlanPagoExtranjero + pg.getMontoExtranjero();
			cont = cont + 1;
		}
	}

	private Date addDayToDateWayToPay(String formaPago,Date today,int day) {
		if(formaPago.equals("SEMANAL")){
			today = DateUtility.addWeek(today, 1);//fecha siguente +1 semana
		}else if(formaPago.equals("QUINCENAL")){
			today = DateUtility.addBiweekly(today, 1);//fecha siguente +1 quincena
		}else if(formaPago.equals("MENSUAL")){
			today = DateUtility.sumarFechaMes(today, 1);//fecha siguente +1 mes
		}
		return today;
	}

	private Date validateDayOfMonth(Date today,String formaPago){
		if(formaPago.equals("MENSUAL")){
			int countDayOfMonth = DateUtility.obtenerNumeroDiasMes(today);
			if(countDayOfMonth<diaPago){
				today.setDate(countDayOfMonth);
			}else{
				today.setDate(diaPago);
			}
		}
		return today;
	}

	// ONCOMPLETETEXT Cliente
	public List<Cliente> completeCliente(String query) {
		listCliente = new ArrayList<Cliente>();
		boolean sw = NumberUtil.isNumeric(query);
		if(sw){
			listCliente = clienteDao.obtenerTodosPorNit(query);
		}else{
			listCliente = clienteDao.obtenerTodosPorRazonSocial(query.toUpperCase());
		}
		return listCliente;
	}

	public void onRowSelectCliente2Click(SelectEvent event) {
		Cliente customer = (Cliente) event.getObject();
		int index = listCliente.indexOf(customer);
		selectedCliente = listCliente.get(index);
		selectedCliente.setNombres(selectedCliente.getNombres()+" ");
	}

	// ONCOMPLETETEXT Cliente
	public List<Producto> completeProducto(String query) {
		listProducto = almacenProductoDao.obtenerTodosPorNombreCodigo(query.toUpperCase());
		return listProducto;
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		Producto pr = (Producto)event.getObject();
		selectedProducto = listProducto.get(listProducto.indexOf(pr));
		AlmacenProducto i = selectedProducto.getAlmacenProductos().get(0);
		double tipoCambio = cotizacion.getTipoCambio();
		selectedProducto.setDescripcion(" "+selectedProducto.getDescripcion());
		selectedDetalleNotaVenta.setPrecio(i.getPrecioVentaCredito()*tipoCambio);
		selectedDetalleNotaVenta.setPrecioExtranjero(i.getPrecioVentaCredito());
		selectedDetalleNotaVenta.setPrecioContadoNacional(i.getPrecioVentaContado()*tipoCambio);
		selectedDetalleNotaVenta.setPrecioContadoExtranjero(i.getPrecioVentaContado());
		actualizarImporteParcialDetalleNotaVenta();
	}

	public void registrarCliente(){
		//previa validacion
		byte[] imageCliente = (byte[]) FacesUtil.getSessionAttribute("imagenCliente");
		if(imageCliente!=null){
			newCliente.setFoto(imageCliente);
			newCliente.setPesoFoto(imageCliente.length);
		}
		newCliente.setEmpresa(empresaSession);
		newCliente.setEstado("AC");
		newCliente.setFechaRegistro(new Date());
		newCliente.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		Cliente data = clienteDao.registrar(newCliente);
		if(data != null){
			closeModalCliente();
			newCliente = new Cliente();
			selectedCliente = data;
			FacesUtil.updateComponent("form001");
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

	//  ---- get and set -----

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

	public List<Cliente> getListCliente() {
		return listCliente;
	}

	public void setListCliente(List<Cliente> listCliente) {
		this.listCliente = listCliente;
	}

	public Cliente getSelectedCliente() {
		return selectedCliente;
	}

	public void setSelectedCliente(Cliente selectedCliente) {
		this.selectedCliente = selectedCliente;
	}

	public List<Producto> getListProducto() {
		return listProducto;
	}

	public void setListProducto(List<Producto> listProducto) {
		this.listProducto = listProducto;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public DetalleNotaVenta getSelectedDetalleNotaVenta() {
		return selectedDetalleNotaVenta;
	}

	public void setSelectedDetalleNotaVenta(DetalleNotaVenta selectedDetalleNotaVenta) {
		this.selectedDetalleNotaVenta = selectedDetalleNotaVenta;
	}

	public double getImporteParcialDetalleNotaVenta() {
		return importeParcialDetalleCotizacion;
	}

	public void setImporteParcialDetalleNotaVenta(
			double importeParcialDetalleCotizacion) {
		this.importeParcialDetalleCotizacion = importeParcialDetalleCotizacion;
	}

	public int getNumeroPagos() {
		return numeroPagos;
	}

	public void setNumeroPagos(int numeroPagos) {
		this.numeroPagos = numeroPagos;
	}

	public int getNumeroDiasPlanPago() {
		return numeroDiasPlanPago;
	}

	public void setNumeroDiasPlanPago(int numeroDiasPlanPago) {
		this.numeroDiasPlanPago = numeroDiasPlanPago;
	}

	public List<PlanCobranza> getListPlanPago() {
		return listPlanPago;
	}

	public void setListPlanPago(List<PlanCobranza> listPlanPago) {
		this.listPlanPago = listPlanPago;
	}

	public boolean isVerReporte() {
		return verReporte;
	}

	public void setVerReporte(boolean verReporte) {
		this.verReporte = verReporte;
	}

	public String getUrlReportePlanPago() {
		return urlReportePlanPago;
	}

	public void setUrlReportePlanPago(String urlReportePlanPago) {
		this.urlReportePlanPago = urlReportePlanPago;
	}

	public Cliente getNewCliente() {
		return newCliente;
	}

	public void setNewCliente(Cliente newCliente) {
		this.newCliente = newCliente;
	}

	public double getPorcentajeCuotaInicial() {
		return porcentajeCuotaInicial;
	}

	public void setPorcentajeCuotaInicial(double porcentajeCuotaInicial) {
		this.porcentajeCuotaInicial = porcentajeCuotaInicial;
	}

	public List<AlmacenProducto> getListAlmacenProducto() {
		return listAlmacenProducto;
	}

	public void setListAlmacenProducto(List<AlmacenProducto> listAlmacenProducto) {
		this.listAlmacenProducto = listAlmacenProducto;
	}

	public AlmacenProducto getSelectedAlmacenProducto() {
		return selectedAlmacenProducto;
	}

	public void setSelectedAlmacenProducto(AlmacenProducto selectedAlmacenProducto) {
		this.selectedAlmacenProducto = selectedAlmacenProducto;
	}

	public EncargadoVenta getSelectedVendedor() {
		return selectedVendedor;
	}

	public void setSelectedVendedor(EncargadoVenta selectedVendedor) {
		this.selectedVendedor = selectedVendedor;
	}

	public NotaVenta getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(NotaVenta cotizacion) {
		this.cotizacion = cotizacion;
	}

	public List<DetalleNotaVenta> getListDetalleNotaVenta() {
		return listDetalleNotaVenta;
	}

	public void setListDetalleNotaVenta(List<DetalleNotaVenta> listDetalleNotaVenta) {
		this.listDetalleNotaVenta = listDetalleNotaVenta;
	}

	public List<EncargadoVenta> getListVendedores() {
		return listVendedores;
	}

	public void setListVendedores(List<EncargadoVenta> listVendedores) {
		this.listVendedores = listVendedores;
	}

	public double getImporteParcialExtanjeroDetalleNotaVenta() {
		return importeParcialExtanjeroDetalleCotizacion;
	}

	public void setImporteParcialExtanjeroDetalleNotaVenta(
			double importeParcialExtanjeroDetalleCotizacion) {
		this.importeParcialExtanjeroDetalleCotizacion = importeParcialExtanjeroDetalleCotizacion;
	}

	public ParametroVenta getSelectedParametroVenta() {
		return selectedParametroVenta;
	}

	public void setSelectedParametroVenta(ParametroVenta selectedParametroVenta) {
		this.selectedParametroVenta = selectedParametroVenta;
	}

	public int getDiaPago() {
		return diaPago;
	}

	public void setDiaPago(int diaPago) {
		this.diaPago = diaPago;
	}

	public String getUrlReporteNotaCargo() {
		return urlReporteNotaCargo;
	}

	public void setUrlReporteNotaCargo(String urlReporteNotaCargo) {
		this.urlReporteNotaCargo = urlReporteNotaCargo;
	}

	public String getUrlReciboAmortizacion() {
		return urlReciboAmortizacion;
	}

	public void setUrlReciboAmortizacion(String urlReciboAmortizacion) {
		this.urlReciboAmortizacion = urlReciboAmortizacion;
	}

	public double getPagoNacional() {
		return pagoNacional;
	}

	public void setPagoNacional(double pagoNacional) {
		this.pagoNacional = pagoNacional;
	}

	public double getPagoExtranjero() {
		return pagoExtranjero;
	}

	public void setPagoExtranjero(double pagoExtranjero) {
		this.pagoExtranjero = pagoExtranjero;
	}

	public double getCambioNacional() {
		return cambioNacional;
	}

	public void setCambioNacional(double cambioNacional) {
		this.cambioNacional = cambioNacional;
	}

	public double getCambioExtranjero() {
		return cambioExtranjero;
	}

	public void setCambioExtranjero(double cambioExtranjero) {
		this.cambioExtranjero = cambioExtranjero;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public double getTotalPlanPagoNacional() {
		return totalPlanPagoNacional;
	}

	public void setTotalPlanPagoNacional(double totalPlanPagoNacional) {
		this.totalPlanPagoNacional = totalPlanPagoNacional;
	}

	public double getTotalPlanPagoExtranjero() {
		return totalPlanPagoExtranjero;
	}

	public void setTotalPlanPagoExtranjero(double totalPlanPagoExtranjero) {
		this.totalPlanPagoExtranjero = totalPlanPagoExtranjero;
	}

	public double getImporteParcialDetalleCotizacion() {
		return importeParcialDetalleCotizacion;
	}

	public void setImporteParcialDetalleCotizacion(double importeParcialDetalleCotizacion) {
		this.importeParcialDetalleCotizacion = importeParcialDetalleCotizacion;
	}

	public double getImporteParcialExtanjeroDetalleCotizacion() {
		return importeParcialExtanjeroDetalleCotizacion;
	}

	public void setImporteParcialExtanjeroDetalleCotizacion(double importeParcialExtanjeroDetalleCotizacion) {
		this.importeParcialExtanjeroDetalleCotizacion = importeParcialExtanjeroDetalleCotizacion;
	}

	public boolean isModoVista() {
		return modoVista;
	}

	public void setModoVista(boolean modoVista) {
		this.modoVista = modoVista;
	}

	public List<EncargadoVenta> getPublishers() {
		return publishers;
	}

	public void setPublishers(List<EncargadoVenta> publishers) {
		this.publishers = publishers;
	}

	public String getUrlProformaAlCredito() {
		return urlProformaAlCredito;
	}

	public void setUrlProformaAlCredito(String urlProformaAlCredito) {
		this.urlProformaAlCredito = urlProformaAlCredito;
	}

}
