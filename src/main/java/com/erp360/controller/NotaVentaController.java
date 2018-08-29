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
import com.erp360.dao.EjecutivoClienteDao;
import com.erp360.dao.NotaVentaDao;
import com.erp360.dao.ParametroCobranzaDao;
import com.erp360.dao.ParametroCuotaDao;
import com.erp360.dao.ParametroInventarioDao;
import com.erp360.dao.ParametroVentaDao;
import com.erp360.dao.PlanCobranzaDao;
import com.erp360.dao.ProductoDao;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.Cliente;
import com.erp360.model.DetalleNotaVenta;
import com.erp360.model.Ejecutivo;
import com.erp360.model.EjecutivoCliente;
import com.erp360.model.Empresa;
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
@ManagedBean(name = "notaVentaController")
@ViewScoped
public class NotaVentaController implements Serializable {

	private static final long serialVersionUID = 6986540930723020906L;

	//DAO
	private @Inject FacesContext facesContext;
	private @Inject NotaVentaDao notaVentaDao;
	private @Inject PlanCobranzaDao planCobranzaDao;
	private @Inject ClienteDao clienteDao;
	private @Inject ParametroVentaDao parametroVentaDao;
	private @Inject ParametroCobranzaDao parametroCobranzaDao;
	private @Inject ParametroCuotaDao parametroCuotaDao;
	private @Inject ParametroInventarioDao parametroInventarioDao;
	private @Inject AlmacenProductoDao almacenProductoDao;
	private @Inject ProductoDao productoDao;
	private @Inject EjecutivoClienteDao ejecutivoClienteDao;
	private @Inject DetalleNotaVentaDao detalleNotaVentaDao;

	//OBJECT
	private NotaVenta notaVenta;
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
	private List<Ejecutivo> ejecutivos;
	private List<DetalleNotaVenta> detalleNotaVentas;

	//STATE
	private boolean crear ;
	private boolean registrar ;
	private boolean modificar ;
	private boolean verReporte;
	private boolean modoVista;//cuando solo quiere ver , una venta realizada
	private boolean cuotaMesActual;
	private boolean pendingQuotation;

	//VAR
	private String type;
	private String nombreEstado="ACTIVO";
	private double importeParcialDetalleNotaVenta;
	private double porcentajeCuotaInicial;
	private double cuotaInicialNacional ;
	private double cuotaInicialExtranjera ;
	private double backTotalAmountForeign;
	private double backTotalAmountNational;
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
	private double montoInteresNacional;
	private double montoInteresExtranjero;
	
	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Empresa empresaSession;
	private Ejecutivo  ejecutivo;
	private Gestion gestionSesion;
	
	//private String text;

	@PostConstruct
	public void init() {
		empresaSession = sessionMain.getEmpresaLogin();
		gestionSesion = sessionMain.getGestionLogin();
		loadTypeOrder();
	}

	public void loadDefault(){
		
		pendingQuotation = false;
		modoVista = false;
		urlReporteNotaCargo = "";
		urlReciboAmortizacion = "";
		urlProformaAlCredito = "";
		observacion = "Pago de cuota inicial";
		totalPlanPagoNacional = 0;
		totalPlanPagoExtranjero = 0;
		pagoNacional = 0;
		pagoExtranjero = 0;
		porcentajeCuotaInicial = 28;
		cuotaInicialNacional = 0;
		cuotaInicialExtranjera = 0;
		diaPago = 15;
		importeParcialDetalleNotaVenta = 0;
		numeroPagos = 3;
		numeroDiasPlanPago = 30;
		backTotalAmountForeign = 0;
		backTotalAmountNational = 0;
		cuotaInicialNacional = 0;
		cuotaInicialExtranjera = 0;


		cuotaMesActual = false;
		crear = true;
		registrar = false;
		modificar = false;
		verReporte = false;

		newCliente = new Cliente();
		newCliente.setCodigo(String.format("%08d",clienteDao.obtenerCorrelativo2()));
		//notaVenta = new NotaVenta();
		notaVenta.setId(0);
		notaVenta.setFechaPagoInicial(new Date());
		notaVenta.setCodigo(String.format("%06d",notaVentaDao.correlativoNotaVenta(gestionSesion)));
		
		notaVenta.setFechaVenta(new Date());
		notaVenta.setFechaRegistro(new Date());
		notaVenta.setTipoCambio(sessionMain.getTipoCambio().getUnidad());
		notaVenta.setEjecutivo(ejecutivo);
		selectedDetalleNotaVenta= new DetalleNotaVenta();

		selectedCliente = new Cliente();
		selectedProducto = new Producto();
		selectedAlmacenProducto = new AlmacenProducto();
		enableButtonPlanPago = true;

		listProducto = new ArrayList<>();
		listAlmacenProducto = new ArrayList<>();
		listCliente = new ArrayList<>();
		listDetalleNotaVenta = new ArrayList<>();
		listPlanPago = new ArrayList<>();
		selectedParametroInventario = parametroInventarioDao.obtenerParametroInventario();
		selectedParametroVenta = parametroVentaDao.obtener();
		selectedParametroCobranza = parametroCobranzaDao.obtenerParametroCobanza();
		//selectedParametroEmpresa = parametroEmpresaDao.obtener();

		if(FacesUtil.getSessionAttribute("pIdNotaVentaAnulada")!=null){
			//1.- estados de los botones, para desabilitar opciones
			//2.- cambiar estado nota de venta "AN"
			//3.- verificar como se ingresara nuevamente ese producto a inventario (Ej: Orden Ingreso Por Devolucion)
			//4.-
		}
		if(FacesUtil.getSessionAttribute("pIdNotaVenta")!=null){
			//modo ver detalle de la nota de venta
			Integer idNotaVenta = (Integer) FacesUtil.getSessionAttribute("pIdNotaVenta");
			FacesUtil.setSessionAttribute("pIdNotaVenta",null);
			notaVenta = notaVentaDao.findById(idNotaVenta);
			listDetalleNotaVenta = detalleNotaVentaDao.obtenerDetalleNotaVenta(notaVenta);
			selectedCliente = notaVenta.getCliente();
			modoVista = true;
			//traer plan de pagos
			if(notaVenta.getTipoVenta().equals("CREDITO")){
				listPlanPago = planCobranzaDao.obtenerPlanCobranzaPorVenta(notaVenta);
				totalPlanPagoExtranjero = notaVenta.getMontoTotalExtranjero() - notaVenta.getCuotaInicialExtranjero();
				totalPlanPagoNacional = notaVenta.getMontoTotal() - notaVenta.getCuotaInicial();
			}
			reloadPlanPago();
			//cargarReporteContrato();
			if(notaVenta.getEstadoPago().equals("CO")){
				pendingQuotation = true;
			}
		}
		//text = selectedParametroVenta.getContrato();
	}

	private boolean enableButtonPlanPago;

	private void reloadPlanPago() {
		if (notaVenta.getTipoVenta().equals("CREDITO") ){
			//nada
		}else{
			enableButtonPlanPago = true;
		}
	}

	public void changeCuotaMesActual(){
		int diaFechaInicial = DateUtility.getDayOfMonth(notaVenta.getFechaPagoInicial());
		if(diaFechaInicial <= diaPago){
			if(cuotaMesActual){
				cuotaMesActual = true;
			}else{
				cuotaMesActual = false;
			}
			actionReloadPlanPago();
		}else{
			cuotaMesActual = false;
			FacesUtil.infoMessage("Validación", "La Fecha Inicial es Mayor al dia de Pago");
		}
	}

	public void changeSalesType(){
		montoInteresNacional = 0;
		montoInteresExtranjero = 0;
		if(selectedProducto != null){ 
			if(notaVenta.getTipoVenta().equals("CONTADO") ){
				notaVenta.setCoeficienteInteres(0);
				notaVenta.setPorcentajeCuotaInicial(0);
				notaVenta.setNumeroCuotas(0);
				notaVenta.setConcepto("Nota de cargo por Venta de producto(s)");
				double tipoCambio = notaVenta.getTipoCambio();
				selectedProducto.setDescripcion(" "+selectedProducto.getDescripcion());
				selectedDetalleNotaVenta.setPrecioContadoNacional(selectedProducto.getPrecioVentaContado()*tipoCambio);//contado nacional
				selectedDetalleNotaVenta.setPrecioContadoExtranjero(selectedProducto.getPrecioVentaContado());
				importeParcialDetalleNotaVenta = selectedDetalleNotaVenta.getPrecioContadoExtranjero() * selectedDetalleNotaVenta.getCantidad();
			}else{
				double tipoCambio = notaVenta.getTipoCambio();
				selectedProducto.setDescripcion(" "+selectedProducto.getDescripcion());
				selectedDetalleNotaVenta.setPrecio(selectedProducto.getPrecioVentaCredito()*tipoCambio);//credito nacional
				selectedDetalleNotaVenta.setPrecioExtranjero(selectedProducto.getPrecioVentaCredito());//credito extranjero
				importeParcialDetalleNotaVenta = selectedDetalleNotaVenta.getPrecioExtranjero() * selectedDetalleNotaVenta.getCantidad();
				notaVenta.setConcepto("Nota de cargo por Venta de producto(s), Cuota Inicial.");
			}
		}
		loadPayPLan();
	}

	/**
	 * 
	 */
	private void loadPayPLan(){
		if(notaVenta.getTipoVenta().equals("CONTADO") ){
			montoInteresNacional = 0;
			montoInteresExtranjero = 0;
			notaVenta.setCoeficienteInteres(0);
			notaVenta.setPorcentajeCuotaInicial(0);
			notaVenta.setNumeroCuotas(0);
			notaVenta.setSaldoFinanciarNacional(0);
			notaVenta.setSaldoFinanciarExtranjero(0);
			notaVenta.setMontoTotal(loadBackTotalAmountCashNational());
			notaVenta.setMontoTotalExtranjero(loadBackTotalAmountCashForeign());
		}else if (notaVenta.getTipoVenta().equals("CREDITO") ){
			loadParameterForPayPlan();
			action();
		}
	}

	private void action() {
		int diaFechaInicial = DateUtility.getDayOfMonth(notaVenta.getFechaPagoInicial());
		if(diaFechaInicial > diaPago){
			cuotaMesActual = false;
		}
		String formaPago = notaVenta.getFormaPago();//MENSUAL , QUINCENAL , SEMANAL
		Date today = new Date();
		today.setTime(notaVenta.getFechaPagoInicial().getTime());
		double coeficienteInteres = notaVenta.getCoeficienteInteres();
		int numeroCuotas = notaVenta.getNumeroCuotas();
		double montoTotalNacional = backTotalAmountNational;
		double montoTotalExtranjero = backTotalAmountForeign;//590
		double tipoCambio = notaVenta.getTipoCambio();
		double x1 = numeroCuotas * coeficienteInteres;// 8 * 2.5 = 20
		double x11 = montoTotalExtranjero - cuotaInicialExtranjera;//590-165.20=424.80
		double x2 = x11 * ( x1 / 100 );//424.80 * 0.20 = 84.96
		double x3 = x11 + x2;// 424.80 + 84.96 = 509.76
		double montoMensualDolar = x3 / numeroCuotas;//montoMensualDolar
		double montoMensualBolivianos = montoMensualDolar * tipoCambio; // montoMensualBolivianos
		double interesMensualNacional = ((montoMensualBolivianos*numeroCuotas)-(montoTotalNacional - cuotaInicialNacional))/numeroCuotas; //interesMensualNacional
		double interesMensualExtranjero = ((montoMensualDolar*numeroCuotas)-(montoTotalExtranjero - cuotaInicialExtranjera))/numeroCuotas; ; //interesMensualExtranjero
		montoInteresNacional = x2 *tipoCambio;
		montoInteresExtranjero = x2;

		today = validateDayOfMonth(today,formaPago);
		int cont = 1;
		listPlanPago = new ArrayList<>();
		totalPlanPagoNacional = 0;
		totalPlanPagoExtranjero = 0;
		if(cuotaMesActual){
			PlanCobranza pg = new PlanCobranza();
			pg.setNumeroPago(cont);
			pg.setEstado("AC");
			pg.setFechaRegistro(new Date());
			today = addDayToDateWayToPay(formaPago,today,diaPago,true);
			pg.setFechaPago(today);
			pg.setMontoNacional(montoMensualBolivianos);
			pg.setMontoExtranjero(montoMensualDolar);
			pg.setInteresMensual(interesMensualNacional);
			pg.setInteresMensualExtranjero(interesMensualExtranjero);
			pg.setTipoCambio(notaVenta.getTipoCambio());
			pg.setTipoMoneda(notaVenta.getMoneda());
			pg.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
			listPlanPago.add(pg);
			totalPlanPagoNacional = totalPlanPagoNacional + pg.getMontoNacional();
			totalPlanPagoExtranjero = totalPlanPagoExtranjero + pg.getMontoExtranjero();
			cont = cont + 1;
		}
		while(cont<=numeroCuotas){
			PlanCobranza pg = new PlanCobranza();
			pg.setNumeroPago(cont);
			pg.setEstado("AC");
			pg.setFechaRegistro(new Date());
			today = addDayToDateWayToPay(formaPago,today,diaPago,false);
			pg.setFechaPago(today);
			pg.setMontoNacional(montoMensualBolivianos);
			pg.setMontoExtranjero(montoMensualDolar);
			pg.setInteresMensual(interesMensualNacional);
			pg.setInteresMensualExtranjero(interesMensualExtranjero);
			pg.setTipoCambio(notaVenta.getTipoCambio());
			pg.setTipoMoneda(notaVenta.getMoneda());
			pg.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
			listPlanPago.add(pg);
			totalPlanPagoNacional = totalPlanPagoNacional + pg.getMontoNacional();
			totalPlanPagoExtranjero = totalPlanPagoExtranjero + pg.getMontoExtranjero();
			cont = cont + 1;
		}
		//---
		notaVenta.setSaldoFinanciarNacional(totalPlanPagoNacional);
		notaVenta.setSaldoFinanciarExtranjero(totalPlanPagoExtranjero);
		notaVenta.setMontoTotal(notaVenta.getCuotaInicial()+notaVenta.getSaldoFinanciarNacional());
		notaVenta.setMontoTotalExtranjero(notaVenta.getCuotaInicialExtranjero()+notaVenta.getSaldoFinanciarExtranjero());

	}

	private void loadParameterForPayPlan() {
		//coef. interes mensual
		ParametroVenta pv = parametroVentaDao.obtener();
		notaVenta.setCoeficienteInteres(pv.getCoeficienteInteresMensual());//por default MENSUAL
		//-----------------------------------------------------
		//obtener : % cuota inicial y numero Pagos
		List<ParametroCuota> listParametroCuota = parametroCuotaDao.obtenerTodosActivos();
		porcentajeCuotaInicial = 28;
		numeroPagos = 2;
		for(ParametroCuota pc: listParametroCuota){
			if(pc.getMontoRangoInicial() <= notaVenta.getMontoTotalExtranjero() && notaVenta.getMontoTotalExtranjero()<= pc.getMontoRangoFinal()){
				porcentajeCuotaInicial = pc.getPorcentajeCuotaInicial();
				numeroPagos = pc.getNumeroCuotas();
				break;
			}
		}
		//-----------------------------------------------------
		backTotalAmountForeign = loadBackTotalAmountCreditForeign();
		backTotalAmountNational = loadBackTotalAmountCreditNational();
		notaVenta.setPorcentajeCuotaInicial(porcentajeCuotaInicial);
		notaVenta.setNumeroCuotas(numeroPagos);
		cuotaInicialNacional = backTotalAmountNational * (porcentajeCuotaInicial/100) ;
		cuotaInicialExtranjera = backTotalAmountForeign * (porcentajeCuotaInicial/100);
		notaVenta.setCuotaInicial(cuotaInicialNacional);
		notaVenta.setCuotaInicialExtranjero(cuotaInicialExtranjera);
	}

	private double loadBackTotalAmountCashForeign() {
		double backTotalAmount = 0;
		for(DetalleNotaVenta dnv : listDetalleNotaVenta){
			backTotalAmount = backTotalAmount + (dnv.getCantidad() * dnv.getPrecioContadoExtranjero());
		}
		return backTotalAmount;
	}

	private double loadBackTotalAmountCashNational() {
		double backTotalAmount = 0;
		for(DetalleNotaVenta dnv : listDetalleNotaVenta){
			double subTotal = (dnv.getPrecioContadoNacional() + ((dnv.getPrecioContadoNacional()*dnv.getPorcentajeDescuento())/100));
			backTotalAmount = backTotalAmount + (dnv.getCantidad() * subTotal);
		}
		return backTotalAmount;
	}

	private double loadBackTotalAmountCreditForeign() {
		double backTotalAmount = 0;
		for(DetalleNotaVenta dnv : listDetalleNotaVenta){
			double subTotal = (dnv.getPrecioExtranjero() + ((dnv.getPrecioExtranjero()*dnv.getPorcentajeDescuento())/100));
			backTotalAmount = backTotalAmount + (dnv.getCantidad() * subTotal);
		}
		return backTotalAmount;
	}

	private double loadBackTotalAmountCreditNational() {
		double backTotalAmount = 0;
		for(DetalleNotaVenta dnv : listDetalleNotaVenta){
			backTotalAmount = backTotalAmount + (dnv.getCantidad() * dnv.getPrecio());
		}
		return backTotalAmount;
	}
	
	public void reimprimir(){
		//cargar reporte de Amortizacion
		
		//Cargar reporte de Plan dePagos
		cargarReporteAmortizacion();
		//cargar reporte de Contrato
		//cargarReporteContrato();
		//modal
		//loadModalPrint();
	}

	//ACTION EVENT
	
	public void loadModalPrint(){
		//FacesUtil.updateComponent("formModalPlanPago");
		FacesUtil.showModal("m-r-5");
	}
	
	public void closeModalPrint(){
		FacesUtil.hideModal("m-r-5");	
	}

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

	public void actualizarImporteParcialDetalleNotaVenta1(){
		if(notaVenta.getTipoVenta().equals("CREDITO")){
			importeParcialDetalleNotaVenta = selectedDetalleNotaVenta.getPrecioExtranjero() * selectedDetalleNotaVenta.getCantidad();
		}else{
			importeParcialDetalleNotaVenta = selectedDetalleNotaVenta.getPrecioContadoExtranjero() * selectedDetalleNotaVenta.getCantidad();
		}
	}

	public void calcularCambio(){
		double montoReserva = 0;
		if(notaVenta.getTipoVenta().equals("CREDITO")){
			cambioNacional = pagoNacional -  notaVenta.getCuotaInicial() + montoReserva;
			cambioExtranjero = pagoExtranjero - notaVenta.getCuotaInicialExtranjero() + montoReserva;
		}else{
			cambioNacional = pagoNacional -  notaVenta.getMontoTotal();
			cambioExtranjero = pagoExtranjero - notaVenta.getMontoTotalExtranjero();
		}
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
		if (sessionMain.getCajaSesion()==null) {
			FacesUtil.infoMessage("Verificación", "No hay Cajas abiertas con el Cajero : "+sessionMain.getUsuarioLogin().getNombre());
			return;
		}
		if(selectedCliente.getId() == 0){
			FacesUtil.infoMessage("Verificación", "Seleccione un cliente");
			return;
		}
		if(listDetalleNotaVenta.size() == 0){
			FacesUtil.infoMessage("Verificación", "Debe agregar Producto(s).");
			return;
		}
		if(notaVenta.getEstadoPago().equals("CO")){
			registrarNotaVenta();
		}else{
			//loadModalPago();
			registrarNotaVenta();
		}
	}

	public void actionModificarDetalleNotaVenta(Integer idDetalleNotaVenta){
		for(DetalleNotaVenta dnv :listDetalleNotaVenta){
			if(dnv.getId().equals(idDetalleNotaVenta)){
				selectedDetalleNotaVenta = dnv;
			}
		}
		selectedProducto = selectedDetalleNotaVenta.getProducto();
		//actualizarImporteParcialDetalleNotaVenta();
		/*******/
		loadPayPLan();
		/*******/
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
		pendingQuotation = false;
		modoVista = false;
		Map<String,String> map1 = new HashMap<>();
		map1.put("pNombreUsuario", sessionMain.getUsuarioLogin().getLogin());
		map1.put("pRazonSocial", empresaSession.getRazonSocial());
		map1.put("pDireccion", empresaSession.getDireccion());
		map1.put("pTelefono", empresaSession.getTelefono());
		map1.put("pIdNotaVenta", String.valueOf(notaVenta.getId()));
		map1.put("pIdEmpresa", String.valueOf(empresaSession.getId()));
		map1.put("pMoneda", notaVenta.getMoneda());
		map1.put("pPagoNacional", String.valueOf(pagoNacional));
		map1.put("pPagoExtranjero", String.valueOf(pagoExtranjero));
		map1.put("pTipoRecibo", "ORIGINAL");
		if(notaVenta.getTipoVenta().equals("CONTADO")){
			if(notaVenta.getEstadoPago().equals("CO")){
				//pending
			}else{
				urlReciboAmortizacion = buildUrl("ReportReciboAmortizacionNotaVentaContado",map1);
			}
		}else if(notaVenta.getTipoVenta().equals("CREDITO")){
			if(notaVenta.getEstadoPago().equals("CO")){
			//pending
			}else{
				urlReciboAmortizacion = "";// buildUrl("ReportReciboAmortizacionNotaVenta",map1);	
			}
			//	mostrar proforma con su plan de pago al credito
			Map<String,String> map2 = new HashMap<>();
			map2.put("pNombreUsuario", sessionMain.getUsuarioLogin().getLogin());
			map2.put("pRazonSocial", empresaSession.getRazonSocial());
			map2.put("pDireccion", empresaSession.getDireccion());
			map2.put("pTelefono", empresaSession.getTelefono());
			map2.put("pIdNotaVenta", String.valueOf(notaVenta.getId()));
			map2.put("pIdEmpresa", String.valueOf(empresaSession.getId()));
			map2.put("pMoneda", notaVenta.getMoneda());
			map2.put("pPagoNacional", String.valueOf(pagoNacional));
			map2.put("pPagoExtranjero", String.valueOf(pagoExtranjero));
			map2.put("pTipoRecibo", "ORIGINAL");
			urlProformaAlCredito = buildUrl("ReportProformaAlCredito",map2);
		}
		System.out.println("urlReciboAmortizacion: "+urlReciboAmortizacion);
		System.out.println("urlProformaAlCredito: "+urlProformaAlCredito);
	}

	//PROCESOS

	public void registrarNotaVenta(){
		//validaciones
		NotaVenta nv = new NotaVenta();
		if(notaVenta.getTipoVenta().equals("CONTADO")){
			if( ! notaVenta.getEstadoPago().equals("CO")){
				notaVenta.setEstadoPago("PR");//PROCESADO
			}
			notaVenta.setEstado("AC");
			notaVenta.setFormaPago("N/A");
			//limpiar
			notaVenta.setCoeficienteInteres(0);
			notaVenta.setFechaPagoInicial(null);
			notaVenta.setNumeroCuotas(0);
			notaVenta.setPorcentajeCuotaInicial(0);
			listPlanPago = new ArrayList<>();
			notaVenta.setGestion(gestionSesion);
			notaVenta.setCliente(selectedCliente);
			notaVenta.setEmpresa(empresaSession);
			notaVenta.setCuotaIncialPagada(Boolean.TRUE);
			notaVenta.setFechaRegistro(new Date());
			notaVenta.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
			notaVenta.setEjecutivo(ejecutivo);
			nv = notaVentaDao.registrarContado(observacion,sessionMain.getUsuarioLogin(),notaVenta,listDetalleNotaVenta,gestionSesion,selectedParametroInventario,selectedParametroCobranza ,selectedParametroVenta);
		
		}else if(notaVenta.getTipoVenta().equals("CREDITO")){
			if( ! notaVenta.getEstadoPago().equals("CO")){
				notaVenta.setEstadoPago("PN");//PENDIENTE
			}
			notaVenta.setEstado("AC");
		
			notaVenta.setGestion(gestionSesion);
			notaVenta.setCliente(selectedCliente);
			notaVenta.setEmpresa(empresaSession);
			notaVenta.setCuotaIncialPagada(Boolean.FALSE);
			notaVenta.setFechaRegistro(new Date());
			notaVenta.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
			notaVenta.setEjecutivo(ejecutivo);
			nv = notaVentaDao.registrarSinCuotaInicial(observacion,sessionMain.getUsuarioLogin(),notaVenta,listDetalleNotaVenta,listPlanPago,gestionSesion,selectedParametroInventario,selectedParametroCobranza ,selectedParametroVenta);
		}
		
		if(nv != null){
			//CajaMovimiento c=cajaMovimientoDao.registrar(cajaServicio.IngresoPorVenta(notaVenta));
			cargarReporteAmortizacion();
			//cargarReporteContrato();
			closeModalPlanPago();
			closeModalPago();
			FacesUtil.updateComponent("form001");
		}
	}
	
	/*
	private void cargarReporteContrato() {
		if(notaVenta.getTipoVenta().equals("CREDITO")){
			if(! notaVenta.getEstadoPago().equals("CO")){
				ClienteAdicional ca = clienteAdicionalDao.obtenerPorCLiente(notaVenta.getCliente());
				System.out.println("Cliente Adicional: "+ca);
				text= text.replace("{CODIGO_CONTRATO}", notaVenta.getCodigo());//CODIGO_CONTRATO
				text= text.replace("{NOMBRE_VENDEDOR}", selectedParametroEmpresa.getEmpresa().getRepresentanteLegal());//NOMBRE_VENDEDOR				
				text= text.replace("{CARNET_VENDEDOR}", selectedParametroEmpresa.getEmpresa().getCarnetIdentidad());//CARNET_VENDEDOR
				text= text.replace("{DOMICILIO_VENDEDOR}", selectedParametroEmpresa.getEmpresa().getDireccion());//DOMICILIO_VENDEDOR
				text= text.replace("{EMPRESA_VENDEDOR}", selectedParametroEmpresa.getEmpresa().getRazonSocial());//EMPRESA_VENDEDOR
				text= text.replace("{NIT_VENDEDOR}", selectedParametroEmpresa.getEmpresa().getNit());//NIT_VENDEDOR
				text= text.replace("{NOMBRE_COMPRADOR}", notaVenta.getCliente().getRazonSocial());//NOMBRE_COMPRADOR
				text= text.replace("{CI_COMPRADOR}", notaVenta.getCliente().getNit());//CI_COMPRADOR
				text= text.replace("{DOMICILIO_COMPRADOR}", notaVenta.getCliente().getDireccion());//DOMICILIO_COMPRADOR
				text= text.replace("{TOTAL_CREDITO}", NumberUtil.decimalFormat(notaVenta.getMontoTotalExtranjero()));//TOTAL_CREDITO
				text= text.replace("{CUOTA_INICIAL}", NumberUtil.decimalFormat(notaVenta.getCuotaInicialExtranjero()));//CUOTA_INICIA L
				text= text.replace("{FECHA_CREDITO}", String.valueOf(Time.convertSimpleDateToString(notaVenta.getFechaVenta())));//FECHA_CREDITO
				buildDetalleOrden();
				buildCuotas();
			}
		}
	}

	private void buildDetalleOrden() {
		String data = "";
		String textLineTemplate = "<p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:10pt;text-align: justify;\"><span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">{COD}</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\"><span class=\"Apple-tab-span\" style=\"white-space:pre;\">	</span>"+
            "</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">{CANT}</span>"+
            
             "   <span class=\"Apple-tab-span\" style=\"white-space:pre;\">	</span>"+
            "</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">{PROD}</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">"+
                "<span class=\"Apple-tab-span\" style=\"white-space:pre;\">	</span>"+
            "</span>"+
            "</p>";
		for(DetalleNotaVenta dnv:listDetalleNotaVenta){
			String textLine = textLineTemplate;
			textLine = textLine.replace("{COD}", dnv.getProducto().getCodigo());//CODIGO_PROD
			textLine = textLine.replace("{PROD}", dnv.getProducto().getModelo()==null? "":dnv.getProducto().getNombre()+", "+dnv.getProducto().getModelo() +", "+ dnv.getProducto().getLineaProducto().getNombre());//MODELO_PROD
			textLine = textLine.replace("{CANT}", String.valueOf(dnv.getCantidad()));//CANTIDAD_PROD
			data = data + textLine;
		}
		text = text.replace("{PRODUCTOS}", data);//CODIGO_PROD
	}
	
	private void buildCuotas() {
		String data = "";
		String textLineTemplate = "<p dir=\"ltr\" style=\"line-height:1.2;margin-top:0pt;margin-bottom:10pt;text-align: justify;\"><span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">{CUOTA}</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\"><span class=\"Apple-tab-span\" style=\"white-space:pre;\">	</span>"+
            "</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">"+
             "   <span class=\"Apple-tab-span\" style=\"white-space:pre;\">	</span>"+
            "</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">{FECHA}</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">"+
                "<span class=\"Apple-tab-span\" style=\"white-space:pre;\">	</span>"+
            "</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">"+
                "<span class=\"Apple-tab-span\" style=\"white-space:pre;\">	</span>"+
            "</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">"+
                "<span class=\"Apple-tab-span\" style=\"white-space:pre;\">	</span>"+
            "</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">"+
                "<span class=\"Apple-tab-span\" style=\"white-space:pre;\">	</span>"+
            "</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">"+
                "<span class=\"Apple-tab-span\" style=\"white-space:pre;\">								</span>"+
            "</span>"+
            "<span style=\"font-size: 14pt; font-family: Calibri; font-variant-numeric: normal; font-variant-east-asian: normal; vertical-align: baseline; white-space: pre-wrap;\">{MONTO}</span>"+
        "</p>";
		for(PlanCobranza pc:listPlanPago){
			String textLine = textLineTemplate;
			textLine = textLine.replace("{CUOTA}", String.valueOf(pc.getNumeroPago()));
			textLine = textLine.replace("{FECHA}", String.valueOf(DateUtility.obtenerFormatoDDMMYYYY(pc.getFechaPago())));
			textLine = textLine.replace("{MONTO}", String.valueOf(NumberUtil.decimalFormat(pc.getMontoExtranjero())));
			data = data + textLine;
		}
		text = text.replace("{CUOTAS}", data);//CODIGO_PROD
	}
	*/

	public void updateNotaVenta(){
		if(notaVenta.getTipoVenta().equals("CONTADO")){
			notaVenta.setEstadoPago("PR");//PROCESADO
			notaVenta.setEstado("AC");
			notaVenta.setFormaPago("N/A");
			//limpiar
			notaVenta.setCoeficienteInteres(0);
			notaVenta.setFechaPagoInicial(null);
			notaVenta.setNumeroCuotas(0);
			notaVenta.setPorcentajeCuotaInicial(0);
			listPlanPago = new ArrayList<>();
		}else if(notaVenta.getTipoVenta().equals("CREDITO")){
			notaVenta.setEstadoPago("PN");//PENDIENTE
			notaVenta.setEstado("AC");
		}
		notaVenta.setGestion(gestionSesion);
		notaVenta.setCliente(selectedCliente);
		notaVenta.setEmpresa(empresaSession);
		notaVenta.setFechaRegistro(new Date());
		//notaVenta.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		notaVenta.setEjecutivo(ejecutivo);
		NotaVenta nv = notaVentaDao.update(observacion,sessionMain.getUsuarioLogin(),notaVenta,listDetalleNotaVenta,listPlanPago,gestionSesion,selectedParametroInventario,selectedParametroCobranza ,selectedParametroVenta);
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
		loadPayPLan();
		selectedProducto = new Producto();
		selectedDetalleNotaVenta = new DetalleNotaVenta();
		importeParcialDetalleNotaVenta = 0;
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
			if(detalleNotaVentas.size()>0){
				//nada
			}else if (selectedDetalleNotaVenta.getPrecio() <= 0 ) {
				FacesUtil.infoMessage("VALIDACION", "Revisar el precio.");
				return;
			}
			if (selectedCliente == null ) {
				FacesUtil.infoMessage("VALIDACION", "Seleccione un Cliente");
				return;
			}
			// verifica si tiene stock
			if(! notaVenta.getEstadoPago().equals("CO")){
				AlmacenProducto almProd = almacenProductoDao.findByProductoConStockPromedio(sessionMain.getGestionLogin(), selectedProducto,selectedParametroVenta.getAlmacenVenta(),selectedDetalleNotaVenta.getCantidad());//selectedProducto.getAlmacenProductos().get(0);
				if(almProd.getStock()==0){
					FacesUtil.showDialog("dlgpValidationStock");
					return;
				}else if(selectedDetalleNotaVenta.getCantidad()> almProd.getStock()){
					FacesUtil.showDialog("dlgpValidationStockMinimo");
					return;
				}
			}
			DetalleNotaVenta detalle = obtenerDetalleNotaVentaSiExisteProducto();
			double porcentajeDescuento = selectedCliente.getTipoCliente().getPorcentaje();
			porcentajeDescuento = selectedCliente.getTipoCliente().getTipoDescuento().equals("DESC")?(porcentajeDescuento*(-1)):porcentajeDescuento;
			if(detalle != null){
				//no avanzar
				FacesUtil.infoMessage("VALIDACION", "El Producto ya existe");
				return;
				/*if(notaVenta.getTipoVenta().equals("CREDITO")){
					detalle.setCantidad(detalle.getCantidad()+1);
					detalle.setPrecio(detalle.getPrecio()+selectedDetalleNotaVenta.getPrecio());
					listDetalleNotaVenta.set(listDetalleNotaVenta.indexOf(detalle),detalle);
				}else if(notaVenta.getTipoVenta().equals("CONTADO")){
					detalle.setCantidad(detalle.getCantidad()+1);
					detalle.setPrecio(detalle.getPrecio()+selectedDetalleNotaVenta.getPrecio());
					listDetalleNotaVenta.set(listDetalleNotaVenta.indexOf(detalle),detalle);
				}*/
			}else{
				selectedDetalleNotaVenta.setId(listDetalleNotaVenta.size() * (-1));//asignacion de id's temp
				selectedDetalleNotaVenta.setProducto(selectedProducto);
				selectedDetalleNotaVenta.setPorcentajeDescuento(porcentajeDescuento);
				if(detalleNotaVentas.size()>0){
					listDetalleNotaVenta.addAll(detalleNotaVentas);
				}else{
					listDetalleNotaVenta.add(selectedDetalleNotaVenta);
				}
			}
			loadPayPLan();
			selectedDetalleNotaVenta = new DetalleNotaVenta();
			selectedProducto = new Producto();
			importeParcialDetalleNotaVenta = 0;
		} catch (Exception e) {
			System.out.println("Error en agregarConcepto : " + e.getStackTrace());
		}
	}

	private DetalleNotaVenta obtenerDetalleNotaVentaSiExisteProducto(){
		for(DetalleNotaVenta detalle: listDetalleNotaVenta){
			if(detalle.getProducto().equals(selectedProducto)){
				return detalle;
			}
		}
		return null;
	}

	//PLAN DE PAGO

	public void changeWeyToPay(){
		String weyToPay = notaVenta.getFormaPago();//MENSUAL , QUINCENAL , SEMANAL
		ParametroVenta salesOfParameter = parametroVentaDao.obtener();
		switch (weyToPay) {
		case "SEMANAL":
			notaVenta.setCoeficienteInteres(salesOfParameter.getCoeficienteInteresSemanal());
			break;
		case "QUINCENAL":
			notaVenta.setCoeficienteInteres(salesOfParameter.getCoeficienteInteresQuincenal());
			break;
		case "MENSUAL":
			notaVenta.setCoeficienteInteres(salesOfParameter.getCoeficienteInteresMensual());
			break;
		default:
			break;
		}
		actionReloadPlanPago();//call method general , load plan 
	}

	public void actionReloadPlanPago(){
		if(porcentajeCuotaInicial != notaVenta.getPorcentajeCuotaInicial()){
			porcentajeCuotaInicial = notaVenta.getPorcentajeCuotaInicial() ;
			cuotaInicialNacional = backTotalAmountNational * (porcentajeCuotaInicial /100);
			cuotaInicialExtranjera = backTotalAmountForeign * (porcentajeCuotaInicial /100);
		}else if(cuotaInicialExtranjera != notaVenta.getCuotaInicialExtranjero() ){
			cuotaInicialNacional = notaVenta.getCuotaInicial();
			cuotaInicialExtranjera = notaVenta.getCuotaInicialExtranjero();
			porcentajeCuotaInicial = backTotalAmountForeign / notaVenta.getCuotaInicialExtranjero();
		}
		notaVenta.setPorcentajeCuotaInicial(porcentajeCuotaInicial);
		notaVenta.setCuotaInicialExtranjero(cuotaInicialExtranjera);
		notaVenta.setCuotaInicial(cuotaInicialNacional);
		action();
	}

	private Date addDayToDateWayToPay(String formaPago,Date today,int day,boolean sw) {
		if(formaPago.equals("SEMANAL")){
			if(!sw){
				today = DateUtility.addWeek(today, 1);//fecha siguente +1 semana
			}
		}else if(formaPago.equals("QUINCENAL")){
			if(!sw){
				today = DateUtility.addBiweekly(today, 1);//fecha siguente +1 quincena
			}
		}else if(formaPago.equals("MENSUAL")){
			if(!sw){
				today = DateUtility.sumarFechaMes(today, 1);//fecha siguente +1 mes	
			}			
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
			listCliente = clienteDao.obtenerTodosPorNitCodigo(query);
		}else{
			listCliente = clienteDao.obtenerTodosPorRazonSocial(query.toUpperCase());
		}
		return listCliente;
	}

	public void onRowSelectClienteClick(SelectEvent event) {
		Cliente customer = (Cliente) event.getObject();
		int index = listCliente.indexOf(customer);
		selectedCliente = listCliente.get(index);
		selectedCliente.setNombres(selectedCliente.getNombres()+" ");
		EjecutivoCliente ejecutivoCliente = ejecutivoClienteDao.getEjecutivoClienteByIdCliente(selectedCliente);
		if(ejecutivoCliente==null){
			selectedCliente = null;
			FacesUtil.infoMessage("VALIDACION", "El cliente no está asociado a ningun Ejecutivo.");
		}else{
			ejecutivo = ejecutivoCliente.getEjecutivo();
		}
	}
	
	public String getNombreEjecutivo(){
		return ejecutivo == null? "":ejecutivo.getNombres();
	}

	// ONCOMPLETETEXT Cliente
	public List<Producto> completeProducto(String query) {
		if(notaVenta.getEstadoPago().equals("CO")){
			listProducto = productoDao.obtenerTodosPorNombreCodigo(query.toUpperCase());
		}else{
			listProducto = almacenProductoDao.obtenerTodosPorNombreCodigo(query.toUpperCase());	
		}
		return listProducto;
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		System.out.println("selectedCliente: "+selectedCliente);
		if(selectedCliente.getId()==0){
			FacesUtil.infoMessage("VALIDACION", "El cliente no está asociado a ningun Ejecutivo.");
			return;
		}
		Producto pr = (Producto)event.getObject();
		selectedProducto = listProducto.get(listProducto.indexOf(pr));
		
		loadSelectProduct();
		updateImporte();
	}
	
	public void changeCantidad(){
		loadSelectProduct();
		updateImporte();
	}
	
	private void loadSelectProduct(){
		detalleNotaVentas = new ArrayList<>();
		if(notaVenta.getEstadoPago().equals("CO")){
			double tipoCambio = notaVenta.getTipoCambio();
			selectedProducto.setDescripcion(" "+selectedProducto.getDescripcion());
			selectedDetalleNotaVenta.setPrecio(selectedProducto.getPrecioVentaCredito()*tipoCambio);//credito nacional
			selectedDetalleNotaVenta.setPrecioExtranjero(selectedProducto.getPrecioVentaCredito());//credito extranjero
			selectedDetalleNotaVenta.setPrecioContadoNacional(selectedProducto.getPrecioVentaContado()*tipoCambio);//contado nacional
			selectedDetalleNotaVenta.setPrecioContadoExtranjero(selectedProducto.getPrecioVentaContado());
		}else{
			double cantidad = selectedDetalleNotaVenta.getCantidad();
			List<AlmacenProducto> list = almacenProductoDao.findAllByProductoAndAlmacenOrderByFecha(sessionMain.getGestionLogin(),selectedParametroVenta.getAlmacenVenta(),selectedProducto);//selectedProducto.getAlmacenProductos().get(0);
			AlmacenProducto i = new AlmacenProducto();
			if(obtenerStocktTotalAlmacenProducto(list)<cantidad){
				FacesUtil.showDialog("dlgpValidationStock");
				return;
			}else if(list.size()>1){
				//hay varias lineas de stock
				for(AlmacenProducto ap : list){
					//==7,4
					//3
					//5
					if(cantidad > 0){
						DetalleNotaVenta d = new DetalleNotaVenta();
						d.setProducto(selectedProducto);
						d.setAlmacenProductoId(ap.getId());
						double disminucion = ap.getStock() < cantidad ? (ap.getStock()) : (cantidad);
						d.setCantidad(disminucion);
						double precio = 0;
						if(selectedCliente.getTipoCliente().getPrecio().equals("A")){
							precio = ap.getPrecio1();
						}else if(selectedCliente.getTipoCliente().getPrecio().equals("B")){
							precio = ap.getPrecio2();
						}else if(selectedCliente.getTipoCliente().getPrecio().equals("C")){
							precio = ap.getPrecio3();
						}else if(selectedCliente.getTipoCliente().getPrecio().equals("D")){
							precio = ap.getPrecio4();
						}else if(selectedCliente.getTipoCliente().getPrecio().equals("E")){
							precio = ap.getPrecio5();
						}else{
							precio = ap.getPrecio6();
						}
						double tipoCambio = notaVenta.getTipoCambio();
						selectedProducto.setDescripcion(" "+selectedProducto.getDescripcion());
						//credito
						d.setPrecio(precio*tipoCambio);//credito nacional
						d.setPrecioExtranjero(precio);//credito extranjero
						//contado
						d.setPrecioContadoNacional(precio*tipoCambio);//contado nacional
						d.setPrecioContadoExtranjero(precio);//contado extanjero
						detalleNotaVentas.add(d);
						cantidad = cantidad - ap.getStock();
					}
				}
			}else{
				//solo hay una linea de stock
				i = list.get(0);
				double precio = 0;
				if(selectedCliente.getTipoCliente().getPrecio().equals("A")){
					precio = i.getPrecio1();
				}else if(selectedCliente.getTipoCliente().getPrecio().equals("B")){
					precio = i.getPrecio2();
				}else if(selectedCliente.getTipoCliente().getPrecio().equals("C")){
					precio = i.getPrecio3();
				}else if(selectedCliente.getTipoCliente().getPrecio().equals("D")){
					precio = i.getPrecio4();
				}else if(selectedCliente.getTipoCliente().getPrecio().equals("E")){
					precio = i.getPrecio5();
				}else{
					precio = i.getPrecio6();
				}
				double tipoCambio = notaVenta.getTipoCambio();
				selectedDetalleNotaVenta.setAlmacenProductoId(i.getId());
				selectedProducto.setDescripcion(" "+selectedProducto.getDescripcion());
				//credito
				selectedDetalleNotaVenta.setPrecio(precio*tipoCambio);//credito nacional
				selectedDetalleNotaVenta.setPrecioExtranjero(precio);//credito extranjero
				//contado
				selectedDetalleNotaVenta.setPrecioContadoNacional(precio*tipoCambio);//contado nacional
				selectedDetalleNotaVenta.setPrecioContadoExtranjero(precio);//contado extanjero
			}
		}
	}
	
	private double obtenerStocktTotalAlmacenProducto(List<AlmacenProducto> list) {
		double cantidad = 0;
		for(AlmacenProducto ap: list){
			cantidad = cantidad + ap.getStock();
		}
		return cantidad;
	}

	public void updateImporte(){
		if(notaVenta.getTipoVenta().equals("CONTADO")){
			if(notaVenta.getMoneda().equals("DOLAR")){
				//double descuento = selectedDetalleNotaVenta.getPrecioContadoNacional() * (selectedDetalleNotaVenta.getPorcentajeDescuento()/100);
				importeParcialDetalleNotaVenta = selectedDetalleNotaVenta.getPrecioContadoNacional() * selectedDetalleNotaVenta.getCantidad();				
				//importeParcialDetalleNotaVenta = importeParcialDetalleNotaVenta - descuento;
			}else{
				//double descuento = selectedDetalleNotaVenta.getPrecioContadoExtranjero() * (selectedDetalleNotaVenta.getPorcentajeDescuento()/100);
				importeParcialDetalleNotaVenta = selectedDetalleNotaVenta.getPrecioContadoExtranjero() * selectedDetalleNotaVenta.getCantidad();
				//importeParcialDetalleNotaVenta = importeParcialDetalleNotaVenta - descuento;
			}
		}else{//credito
			if(notaVenta.getMoneda().equals("DOLAR")){
				//double descuento = selectedDetalleNotaVenta.getPrecioExtranjero() * (selectedDetalleNotaVenta.getPorcentajeDescuento()/100);
				importeParcialDetalleNotaVenta = selectedDetalleNotaVenta.getPrecioExtranjero() * selectedDetalleNotaVenta.getCantidad();
				//importeParcialDetalleNotaVenta = importeParcialDetalleNotaVenta - descuento;
			}else{
				//double descuento = selectedDetalleNotaVenta.getPrecio() * (selectedDetalleNotaVenta.getPorcentajeDescuento()/100);
				importeParcialDetalleNotaVenta = selectedDetalleNotaVenta.getPrecio() * selectedDetalleNotaVenta.getCantidad();
				//importeParcialDetalleNotaVenta = importeParcialDetalleNotaVenta - descuento;
			}
		}
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
			return url.toString();
		}catch(Exception e){
			return "";
		}
	}

	public void loadTypeOrder(){
		notaVenta = new NotaVenta();
		notaVenta.setEstadoPago("PN");
		type = (String) FacesUtil.getSessionAttribute("type");
		System.out.println("type: "+type);
		if(type != null){
			if(type.equals("quotation")){
				notaVenta.setEstadoPago("CO");//COTIZACION
			}
		}
		loadDefault();
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
		return importeParcialDetalleNotaVenta;
	}

	public void setImporteParcialDetalleNotaVenta(
			double importeParcialDetalleNotaVenta) {
		this.importeParcialDetalleNotaVenta = importeParcialDetalleNotaVenta;
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

	public NotaVenta getNotaVenta() {
		return notaVenta;
	}

	public void setNotaVenta(NotaVenta notaVenta) {
		this.notaVenta = notaVenta;
	}

	public List<DetalleNotaVenta> getListDetalleNotaVenta() {
		return listDetalleNotaVenta;
	}

	public void setListDetalleNotaVenta(List<DetalleNotaVenta> listDetalleNotaVenta) {
		this.listDetalleNotaVenta = listDetalleNotaVenta;
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

	public boolean isModoVista() {
		return modoVista;
	}

	public void setModoVista(boolean modoVista) {
		this.modoVista = modoVista;
	}

	public String getUrlProformaAlCredito() {
		return urlProformaAlCredito;
	}

	public void setUrlProformaAlCredito(String urlProformaAlCredito) {
		this.urlProformaAlCredito = urlProformaAlCredito;
	}

	public boolean isCuotaMesActual() {
		return cuotaMesActual;
	}

	public void setCuotaMesActual(boolean cuotaMesActual) {
		this.cuotaMesActual = cuotaMesActual;
	}

	public boolean isEnableButtonPlanPago() {
		return enableButtonPlanPago;
	}

	public void setEnableButtonPlanPago(boolean enableButtonPlanPago) {
		this.enableButtonPlanPago = enableButtonPlanPago;
	}

	public double getBackTotalAmountForeign() {
		return backTotalAmountForeign;
	}

	public void setBackTotalAmountForeign(double backTotalAmountForeign) {
		this.backTotalAmountForeign = backTotalAmountForeign;
	}

	public double getBackTotalAmountNational() {
		return backTotalAmountNational;
	}

	public void setBackTotalAmountNational(double backTotalAmountNational) {
		this.backTotalAmountNational = backTotalAmountNational;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isPendingQuotation() {
		return pendingQuotation;
	}

	public void setPendingQuotation(boolean pendingQuotation) {
		this.pendingQuotation = pendingQuotation;
	}

//	public String getText() {
//		return text;
//	}
//
//	public void setText(String text) {
//		this.text = text;
//	}

	public double getMontoInteresNacional() {
		return montoInteresNacional;
	}

	public void setMontoInteresNacional(double montoInteresNacional) {
		this.montoInteresNacional = montoInteresNacional;
	}

	public double getMontoInteresExtranjero() {
		return montoInteresExtranjero;
	}

	public void setMontoInteresExtranjero(double montoInteresExtranjero) {
		this.montoInteresExtranjero = montoInteresExtranjero;
	}

	public Ejecutivo getEjecutivo() {
		return ejecutivo;
	}

	public void setEjecutivo(Ejecutivo ejecutivo) {
		this.ejecutivo = ejecutivo;
	}

	public List<Ejecutivo> getEjecutivos() {
		return ejecutivos;
	}

	public void setEjecutivos(List<Ejecutivo> ejecutivos) {
		this.ejecutivos = ejecutivos;
	}

}
