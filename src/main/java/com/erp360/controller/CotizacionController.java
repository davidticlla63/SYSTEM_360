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
import com.erp360.dao.EncargadoVentaDao;
import com.erp360.dao.NotaVentaDao;
import com.erp360.dao.ParametroCuotaDao;
import com.erp360.dao.ParametroVentaDao;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.Cliente;
import com.erp360.model.DetalleCotizacion;
import com.erp360.model.Empresa;
import com.erp360.model.EncargadoVenta;
import com.erp360.model.Gestion;
import com.erp360.model.NotaVenta;
import com.erp360.model.ParametroCuota;
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
@ManagedBean(name = "cotizacionController")
@ViewScoped
public class CotizacionController implements Serializable {

	private static final long serialVersionUID = 6986540930723020906L;

	//DAO
	private @Inject FacesContext facesContext;
	private @Inject NotaVentaDao notaVentaDao;
	private @Inject ClienteDao clienteDao;
	private @Inject ParametroVentaDao parametroVentaDao;
	private @Inject ParametroCuotaDao parametroCuotaDao;
	private @Inject AlmacenProductoDao almacenProductoDao;
	private @Inject EncargadoVentaDao encargadoVentaDao;

	//OBJECT
	private NotaVenta cotizacion;
	private Cliente selectedCliente;
	private Cliente newCliente;
	private Producto selectedProducto;
	private AlmacenProducto selectedAlmacenProducto;
	private DetalleCotizacion detalleCotizacion;
	private ParametroVenta selectedParametroVenta;
	private byte[] data;

	//LIST
	private List<Cliente> listCliente;
	private List<Producto> listProducto;
	private List<AlmacenProducto> listAlmacenProducto;
	private List<DetalleCotizacion> listDetalleCotizacion;
	private List<PlanCobranza> listPlanPago;
	private List<EncargadoVenta> listVendedores;
	private List<ParametroCuota> listParametroCuota;

	//STATE
	private boolean crear ;
	private boolean registrar ;
	private boolean modificar ;
	private boolean verReporte;

	//VAR
	private String nombreEstado="ACTIVO";
	private String urlCotizacion;
	private double importeParcialDetalleCotizacion;
	private double importeParcialExtranjeroDetalleCotizacion;
	//plan de pago
	private double coeficienteInteres;
	private int numeroCuotas;
	private double cuotaInicial;
	private double cuotaInicialExtranjero;
	private double porcentajeCuotaInicial;
	private double totalPlanPagoNacional;
	private double totalPlanPagoExtranjero;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Empresa empresaSession;
	private EncargadoVenta  selectedVendedor;
	private Gestion gestionSesion;

	@PostConstruct
	public void init() {
		System.out.println(" init new NotaVenta");	
		empresaSession = sessionMain.getEmpresaLogin();
		gestionSesion = sessionMain.getGestionLogin();
		loadDefault();
	}

	public void loadDefault(){
		urlCotizacion = "";

		selectedVendedor = new EncargadoVenta();

		crear = true;
		registrar = false;
		modificar = false;
		verReporte = false;
		//encargadoVenta = sessionMain.getUsuarioLogin().isEncargadoVenta();

		newCliente = new Cliente();
		//newCliente.setCodigo("CL"+String.format("%06d",clienteDao.obtenerCorrelativo()));
		newCliente.setCodigo(String.format("%08d",clienteDao.obtenerCorrelativo2()));
		cotizacion = new NotaVenta();
		cotizacion.setFechaCaducidad(DateUtility.sumarFechaDia(new Date(), 15));
		cotizacion.setCodigo(String.format("%06d",notaVentaDao.correlativoNotaVenta(gestionSesion)));
		cotizacion.setFechaRegistro(new Date());
		cotizacion.setTipoCambio(sessionMain.getTipoCambio().getUnidad());
		detalleCotizacion= new DetalleCotizacion();
		listParametroCuota = parametroCuotaDao.obtenerTodosActivos();

		selectedCliente = new Cliente();
		selectedProducto = new Producto();
		selectedAlmacenProducto = new AlmacenProducto();

		listProducto = new ArrayList<>();
		listAlmacenProducto = new ArrayList<>();
		listCliente = new ArrayList<>();
		listDetalleCotizacion = new ArrayList<>();
		listPlanPago = new ArrayList<>();
		selectedParametroVenta = parametroVentaDao.obtener();

		listVendedores = encargadoVentaDao.obtenerTodosOrdenadosPorId();
		selectedVendedor = listVendedores.size()>0? listVendedores.get(0):new EncargadoVenta();
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

	public void actualizarImporteParcialDetalleCotizacion(){
		importeParcialDetalleCotizacion = detalleCotizacion.getPrecioContadoNacional()* detalleCotizacion.getCantidad();
		importeParcialExtranjeroDetalleCotizacion = detalleCotizacion.getPrecioContadoExtranjero() * detalleCotizacion.getCantidad();
	}

	public void actionNuevoCliente(){
		selectedCliente = new Cliente();
		loadModalCliente();
	}

	public void editDetalleCotizacion(Integer idDetalleCotizacion){
		System.out.println("selectedCliente: "+selectedCliente);
		int index = listDetalleCotizacion.indexOf(new DetalleCotizacion(idDetalleCotizacion));
		if( index >= 0){
			detalleCotizacion = listDetalleCotizacion.get(index);
			loadModalPlanPago();
		}
	}

	public void deleteDetalleCotizacion(Integer idDetalleCotizacion){
		int index = listDetalleCotizacion.indexOf(new DetalleCotizacion(idDetalleCotizacion));
		if( index >= 0) {
			listDetalleCotizacion.remove(index);
			//actualizar monto total
			actualizarTotalCotizacion();
		}
	}

	private void cargarReporte(){
		verReporte = true;
		Map<String,String> map1 = new HashMap<>();
		map1.put("pUsuario", sessionMain.getUsuarioLogin().getLogin());
		map1.put("pRazonSocial", empresaSession.getRazonSocial());
		map1.put("pDireccion", empresaSession.getDireccion());
		map1.put("pTelefono", empresaSession.getTelefono());
		map1.put("pIdCotizacion", String.valueOf(cotizacion.getId()));
		map1.put("pIdEmpresa", String.valueOf(empresaSession.getId()));
		map1.put("pMoneda", cotizacion.getMoneda());
		urlCotizacion = buildUrl("ReportCotizacion",map1);
	}

	public void actionChangeTipoCambio(){
		//verificar la moneda
		String moneda = cotizacion.getMoneda();
		System.out.println("moneda: "+moneda);
		//tipo cambio
		double tipoCambio = cotizacion.getTipoCambio();
		System.out.println("tipoCambio: "+tipoCambio);
		if(moneda.equals("BOLIVIANOS")){
			//monto reserva nacional
			//double montoReservaNacional = notaVenta.getMontoReserva();
			//System.out.println("montoReservaNacional: "+montoReservaNacional);
			//notaVenta.setMontoReservaExtranjero(montoReservaNacional/tipoCambio);

		}else{
			//monto reserva extranjera
			//double montoReservaExtranjero = notaVenta.getMontoReservaExtranjero();
			//System.out.println("montoReservaExtranjero: "+montoReservaExtranjero);
			//notaVenta.setMontoReserva(montoReservaExtranjero*tipoCambio);
		}
	}

	public void actionChangeNumeroCUotas(){
		System.out.println("actionChangeNumeroCUotas");
	}

	//PROCESOS

	public void registrarCotizacion(){
		//validaciones
		if(selectedVendedor.getId().equals(0)){
			FacesUtil.infoMessage("Verificación", "Debe agregar un Vendedor");
			return;
		}
		//si permite registro sin cliente ?
		//if(selectedCliente.getId().equals(0)){
		//	FacesUtil.infoMessage("Verificación", "Debe agregar un Cliente");
		//	return;
		//}
		cotizacion.setGestion(gestionSesion);
		cotizacion.setCliente(selectedCliente.getId().equals(0)?null:selectedCliente);
		cotizacion.setEmpresa(empresaSession);
		cotizacion.setFechaRegistro(new Date());
		cotizacion.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		cotizacion.setEncargadoVenta(selectedVendedor);
		NotaVenta cot = null;//notaVentaDao.registrar(cotizacion,listDetalleCotizacion);
		if(cot != null){
			cargarReporte();
		}
	}

	public void agregarDetalleCotizacion() {
		if(selectedProducto.getId()== 0){
			FacesUtil.infoMessage("VALIDACION", "Agregue un producto.");
			return;
		}
		double porcentajeCuotaInicial = 28;
		detalleCotizacion.setId(listDetalleCotizacion.size() * (-1));//asignacion de id's temp
		detalleCotizacion.setProducto(selectedProducto);
		detalleCotizacion.setPrecioCuotaInicialExtranjera(detalleCotizacion.getPrecioCreditoExtranjero() * (porcentajeCuotaInicial/100));
		detalleCotizacion.setPrecioCuotaInicialNacional(detalleCotizacion.getPrecioContadoNacional() * (porcentajeCuotaInicial/100));
		porcentajeCuotaInicial = 28;
		double numeroPagos = 2;
		for(ParametroCuota pc: listParametroCuota){
			if(pc.getMontoRangoInicial() <= detalleCotizacion.getPrecioCreditoExtranjero() && detalleCotizacion.getPrecioCreditoExtranjero()<= pc.getMontoRangoFinal()){
				System.out.println("TRUE");
				porcentajeCuotaInicial = pc.getPorcentajeCuotaInicial();
				numeroPagos = pc.getNumeroCuotas();
				break;
			}
		}
		double coeficienteInteres = 2.5;
		double x1 = numeroPagos * coeficienteInteres;// 8 * 2.5 = 20
		double x11 = detalleCotizacion.getPrecioCreditoExtranjero() - detalleCotizacion.getPrecioCuotaInicialExtranjera();//590-165.20=424.80
		double x2 = x11 * ( x1 / 100 );//424.80 * 0.20 = 84.96
		double x3 = x11 + x2;// 424.80 + 84.96 = 509.76
		double x4 = x3 / numeroPagos;//monto mensual dolar
		double x5 = x4 * cotizacion.getTipoCambio(); // monto mensual bolivianos
		double x7 = ((x5*numeroPagos)-(detalleCotizacion.getPrecioCreditoNacional() - detalleCotizacion.getPrecioCuotaInicialNacional()))/numeroPagos; //interes mensual nacional
		double x8 = ((x4*numeroPagos)-(detalleCotizacion.getPrecioCreditoExtranjero() - detalleCotizacion.getPrecioCuotaInicialExtranjera()))/numeroPagos; ; //interes mensual extranjero
		//System.out.println("x1: "+x1+" | x2: "+x2+" | x3 : "+x3+" | x4: "+x4+" | x5: "+x5+" | x7: "+x7+" | x8: "+x8);

		detalleCotizacion.setCoeficienteInteres(coeficienteInteres);
		detalleCotizacion.setPorcentajeCuotaInicial(porcentajeCuotaInicial);
		detalleCotizacion.setCantidad(1);
		detalleCotizacion.setNumeroCuotas(numeroPagos);
		detalleCotizacion.setPrecioCuotaMensualExtranjera(x4);
		detalleCotizacion.setPrecioCuotaMensualNacional(x5);
		detalleCotizacion.setInteresMensualNacional(x7);
		detalleCotizacion.setInteresMensualExtranjero(x8);

		//cambiar el precio del monto total al credito
		//detalleCotizacion.setPrecioCreditoNacional(detalleCotizacion.getPrecioCuotaInicialNacional() + ( detalleCotizacion.getPrecioCuotaMensualNacional() * numeroPagos));
		//detalleCotizacion.setPrecioCreditoExtranjero( detalleCotizacion.getPrecioCuotaInicialExtranjera() + ( detalleCotizacion.getPrecioCuotaMensualExtranjera() * numeroPagos));

		listDetalleCotizacion.add(detalleCotizacion);
		cotizacion.setMontoTotal(cotizacion.getMontoTotal() + (detalleCotizacion.getPrecioContadoNacional() * detalleCotizacion.getCantidad()));
		//limpiar campos
		detalleCotizacion = new DetalleCotizacion();
		selectedProducto = new Producto();
		importeParcialDetalleCotizacion = 0;
		importeParcialExtranjeroDetalleCotizacion = 0;
		//actualizar monto total
		actualizarTotalCotizacion();
	}

	private void actualizarTotalCotizacion(){
		double total = 0;
		double totalExtranjero = 0;
		for(DetalleCotizacion dnv : listDetalleCotizacion){
			total = total + (dnv.getPrecioContadoNacional() * dnv.getCantidad());
			totalExtranjero = totalExtranjero + (dnv.getPrecioContadoExtranjero() * dnv.getCantidad());
		}
		cotizacion.setMontoTotal(total);
		cotizacion.setMontoTotalExtranjero(totalExtranjero);
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

	/*
	public void onRowSelectClienteClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Cliente i : listCliente){
			if(i.getNombres().equals(nombre)){
				selectedCliente = i;
				return;
			}
		}
	}*/

	public void onRowSelectCliente2Click(SelectEvent event) {
		Cliente cliente = (Cliente) event.getObject();
		////////////
		selectedCliente = listCliente.get(listCliente.indexOf(cliente));
		selectedCliente.setNombres(selectedCliente.getNombres()+" ");
		////////////
		/*
		for(Cliente c : listCliente){
			if(c.getId().equals(cliente.getId())){
				selectedCliente = c;
				selectedCliente.setNombres(selectedCliente.getNombres()+" ");
				return;
			}
		}*/
	}

	// ONCOMPLETETEXT Cliente
	public List<Producto> completeProducto(String query) {
		//listProducto = new ArrayList<Producto>();//reset
		listProducto = almacenProductoDao.obtenerTodosPorNombreCodigo(query.toUpperCase());
		//for(AlmacenProducto i : listAlmacenProducto) {
		//	System.out.println("AlmacenProducto : "+i.toString());
		//	listProducto.add(i.getProducto());
		//}
		return listProducto;
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		Producto pr = (Producto)event.getObject();
		double tipoCambio = cotizacion.getTipoCambio();
		selectedProducto = listProducto.get(listProducto.indexOf(pr));//i.getProducto();
		AlmacenProducto i = selectedProducto.getAlmacenProductos().get(0);//order by fechaRegistro asc
		selectedProducto.setDescripcion(" "+selectedProducto.getDescripcion());
		detalleCotizacion.setPrecioContadoNacional(i.getPrecioVentaContado()*tipoCambio);
		detalleCotizacion.setPrecioContadoExtranjero (i.getPrecioVentaContado());
		detalleCotizacion.setPrecioCreditoExtranjero(i.getPrecioVentaCredito());
		detalleCotizacion.setPrecioCreditoNacional(i.getPrecioVentaCredito()*tipoCambio);
		actualizarImporteParcialDetalleCotizacion();
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


	// plan de pago

	public void actionReloadChangeNumberCout(){
		//coef interes
		double coeficienteInteres = detalleCotizacion.getCoeficienteInteres();
		//numero de cuotas
		double numeroCuotas = detalleCotizacion.getNumeroCuotas();
		System.out.println("numeroCuotas: "+numeroCuotas);

		double montoCreditoMenosIncial = detalleCotizacion.getPrecioCreditoExtranjero() - detalleCotizacion.getPrecioCuotaInicialExtranjera();//590-165.20=424.80
		//double x2 = montoCreditoMenosIncial * ( (numeroCuotas * coeficienteInteres) / 100 );//424.80 * 0.20 = 84.96
		double montoTotalCuotas = montoCreditoMenosIncial + (montoCreditoMenosIncial * ( (numeroCuotas * coeficienteInteres) / 100 ));// 424.80 + 84.96 = 509.76
		double montoMensualExtranjero = montoTotalCuotas / numeroCuotas;//monto mensual dolar
		double montoMensualNacional = montoMensualExtranjero * cotizacion.getTipoCambio(); // monto mensual bolivianos
		detalleCotizacion.setPrecioCuotaMensualExtranjera(montoMensualExtranjero);
		detalleCotizacion.setPrecioCuotaMensualNacional(montoMensualNacional);
	}

	public void actionReloadPlanPago(){
		//coef interes
		double coeficienteInteres = detalleCotizacion.getCoeficienteInteres();
		//numero de cuotas
		double numeroCuotas = detalleCotizacion.getNumeroCuotas();

		double montoCreditoMenosIncial = detalleCotizacion.getPrecioCreditoExtranjero() - detalleCotizacion.getPrecioCuotaInicialExtranjera();//590-165.20=424.80
		//double x2 = montoCreditoMenosIncial * ( (numeroCuotas * coeficienteInteres) / 100 );//424.80 * 0.20 = 84.96
		double montoTotalCuotas = montoCreditoMenosIncial + (montoCreditoMenosIncial * ( (numeroCuotas * coeficienteInteres) / 100 ));// 424.80 + 84.96 = 509.76
		double montoMensualExtranjero = montoTotalCuotas / numeroCuotas;//monto mensual dolar

		double montoMensualNacional = montoMensualExtranjero * cotizacion.getTipoCambio(); // monto mensual bolivianos
		double x7 = ((montoMensualNacional*numeroCuotas)-(detalleCotizacion.getPrecioCreditoNacional() - detalleCotizacion.getPrecioCuotaInicialNacional()))/numeroCuotas; //interes mensual nacional
		double x8 = ((montoMensualExtranjero*numeroCuotas)-(detalleCotizacion.getPrecioCreditoExtranjero() - detalleCotizacion.getPrecioCuotaInicialExtranjera()))/numeroCuotas; ; //interes mensual extranjero

		detalleCotizacion.setPrecioCuotaMensualExtranjera(montoMensualExtranjero);
		detalleCotizacion.setPrecioCuotaMensualNacional(montoMensualNacional);
		detalleCotizacion.setInteresMensualNacional(x7);
		detalleCotizacion.setInteresMensualExtranjero(x8);

		//cambiar el precio del monto total al credito
		//detalleCotizacion.setPrecioCreditoNacional(detalleCotizacion.getPrecioCuotaMensualNacional() * numeroCuotas);
		//detalleCotizacion.setPrecioCreditoExtranjero( detalleCotizacion.getPrecioCuotaMensualExtranjera() * numeroCuotas);
		//actualizar monto total
		actualizarTotalCotizacion();
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

	public double getImporteParcialDetalleCotizacion() {
		return importeParcialDetalleCotizacion;
	}

	public void setImporteParcialDetalleCotizacion(
			double importeParcialDetalleCotizacion) {
		this.importeParcialDetalleCotizacion = importeParcialDetalleCotizacion;
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

	public Cliente getNewCliente() {
		return newCliente;
	}

	public void setNewCliente(Cliente newCliente) {
		this.newCliente = newCliente;
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

	public List<EncargadoVenta> getListVendedores() {
		return listVendedores;
	}

	public void setListVendedores(List<EncargadoVenta> listVendedores) {
		this.listVendedores = listVendedores;
	}

	public double getImporteParcialExtranjeroDetalleCotizacion() {
		return importeParcialExtranjeroDetalleCotizacion;
	}

	public void setImporteParcialExtranjeroDetalleCotizacion(
			double importeParcialExtranjeroDetalleCotizacion) {
		this.importeParcialExtranjeroDetalleCotizacion = importeParcialExtranjeroDetalleCotizacion;
	}

	public ParametroVenta getSelectedParametroVenta() {
		return selectedParametroVenta;
	}

	public void setSelectedParametroVenta(ParametroVenta selectedParametroVenta) {
		this.selectedParametroVenta = selectedParametroVenta;
	}

	public NotaVenta getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(NotaVenta cotizacion) {
		this.cotizacion = cotizacion;
	}

	public List<DetalleCotizacion> getListDetalleCotizacion(){
		return listDetalleCotizacion;
	}

	public void getListDetalleCotizacion(List<DetalleCotizacion> listDetalleCotizacion){
		this.listDetalleCotizacion = listDetalleCotizacion;
	}

	public DetalleCotizacion getDetalleCotizacion() {
		return detalleCotizacion;
	}

	public void setDetalleCotizacion(DetalleCotizacion detalleCotizacion) {
		this.detalleCotizacion = detalleCotizacion;
	}

	public String getUrlCotizacion() {
		return urlCotizacion;
	}

	public void setUrlCotizacion(String urlCotizacion) {
		this.urlCotizacion = urlCotizacion;
	}

	public double getCoeficienteInteres() {
		return coeficienteInteres;
	}

	public void setCoeficienteInteres(double coeficienteInteres) {
		this.coeficienteInteres = coeficienteInteres;
	}

	public int getNumeroCuotas() {
		return numeroCuotas;
	}

	public void setNumeroCuotas(int numeroCuotas) {
		this.numeroCuotas = numeroCuotas;
	}

	public double getCuotaInicial() {
		return cuotaInicial;
	}

	public void setCuotaInicial(double cuotaInicial) {
		this.cuotaInicial = cuotaInicial;
	}

	public double getCuotaInicialExtranjero() {
		return cuotaInicialExtranjero;
	}

	public void setCuotaInicialExtranjero(double cuotaInicialExtranjero) {
		this.cuotaInicialExtranjero = cuotaInicialExtranjero;
	}

	public double getPorcentajeCuotaInicial() {
		return porcentajeCuotaInicial;
	}

	public void setPorcentajeCuotaInicial(double porcentajeCuotaInicial) {
		this.porcentajeCuotaInicial = porcentajeCuotaInicial;
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
}
