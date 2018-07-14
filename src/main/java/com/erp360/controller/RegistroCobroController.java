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

import org.primefaces.event.SelectEvent;

import com.erp360.model.PlanCobranza;
import com.erp360.util.EDDeudaPendiente;
import com.erp360.util.FacesUtil;
import com.erp360.util.NumberUtil;
import com.erp360.dao.ClienteDao;
import com.erp360.dao.CobranzaDao;
import com.erp360.dao.CuentaCobrarDao;
import com.erp360.dao.PlanCobranzaDao;
import com.erp360.model.Cliente;
import com.erp360.model.Cobranza;
import com.erp360.model.CuentaCobrar;
import com.erp360.model.DetalleCobranza;
import com.erp360.model.Empresa;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "registroCobroController")
@ViewScoped
public class RegistroCobroController implements Serializable {

	private static final long serialVersionUID = 6986540930723020906L;

	private @Inject FacesContext facesContext;

	//DAO
	private @Inject ClienteDao clienteDao;
	private @Inject CobranzaDao cobranzaDao;
	private @Inject PlanCobranzaDao planCobranzaDao;
	private @Inject CuentaCobrarDao cuentaCobrarDao;

	//OBJECT
	private Cobranza newCobranza;
	private Cliente selectedCliente;
	private EDDeudaPendiente selectedEDDeudaPendiente;

	//LIST
	private List<Cliente> listCliente;
	private List<PlanCobranza> listPlanPago;
	private List<EDDeudaPendiente> listDeudaPendiente;
	private List<PlanCobranza> selectedListPlanCobranza;

	//STATE
	private boolean crear ;
	private boolean registrar ;
	private boolean modificar ;
	private boolean verReporte;

	//VAR
	private String nombreEstado="ACTIVO";
	private String urlReporteCobro;
	private String urlReporteAmortizacion;
	private double totalCobrarNacional;
	private double totalCobrarExtranjero;
	private double cobroNacional;
	private double cobroExtranjero;
	private double cambioNacional;
	private double cambioExtranjero;
	private int numeroCuotasACobrar;
	private String glosa;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Empresa empresaSession;

	@PostConstruct
	public void init() {
		empresaSession = sessionMain.getEmpresaLogin();
		loadDefault();
	}

	public void loadDefault(){
		urlReporteCobro = "";
		urlReporteAmortizacion = "";

		crear = true;
		registrar = false;
		modificar = false;
		verReporte = false;

		cobroNacional = 0;
		cobroExtranjero = 0;
		numeroCuotasACobrar = 0;
		totalCobrarNacional=0;
		totalCobrarExtranjero =0;
		cambioNacional= 0;
		cambioExtranjero=0;

		glosa = "Pago de Cuota";

		newCobranza = new Cobranza();
		newCobranza.setFechaCobranza(new Date());
		newCobranza.setFechaRegistro(newCobranza.getFechaCobranza());
		selectedCliente = new Cliente();
		selectedEDDeudaPendiente = new EDDeudaPendiente();
		listCliente = new ArrayList<>();
		selectedListPlanCobranza = new ArrayList<>();
		listPlanPago = planCobranzaDao.obtenerOrdenAscPorId();
		cargarDeudasPendientes();
	}

	private void cargarDeudasPendientes(){
		listDeudaPendiente = new ArrayList<>();
		if( ! selectedCliente.getId().equals(0)){
			List<CuentaCobrar> listCuentaCobrar = cuentaCobrarDao.obtenerTodosPorCliente(selectedCliente);
			for(CuentaCobrar cc :listCuentaCobrar){
				EDDeudaPendiente edDeudaPendiente = new EDDeudaPendiente();
				edDeudaPendiente.setId(listDeudaPendiente.size() + 1);
				edDeudaPendiente.setCuentaCobrar(cc);
				edDeudaPendiente.setCuotasPendientes(obtenerCoutasPendientes(cc));
				edDeudaPendiente.setMontoProximaCuota(obtenerMontoProximaCouta(cc)*cc.getNotaVenta().getTipoCambio());
				edDeudaPendiente.setMontoProximaCuotaExtranjero(obtenerMontoProximaCouta(cc));
				edDeudaPendiente.setPagarSiguienteCuota(false);
				edDeudaPendiente.setPagarTodo(false);
				edDeudaPendiente.setTotalCuotas(0);
				edDeudaPendiente.setCollectionPlan(cc.getNotaVenta().getListPlanCobranza());
				if(cc.getNotaVenta().getTipoVenta().equals("CREDITO") && !cc.getNotaVenta().getEstadoPago().equals("AC")){
					listDeudaPendiente.add(edDeudaPendiente);
				}
			}
		}
		FacesUtil.updateComponent("form001");
	}

	private int obtenerCoutasPendientes(CuentaCobrar cc){
		int nuotasPendientes = 0;
		for(PlanCobranza pc : cc.getNotaVenta().getListPlanCobranza()){
			if(pc.getEstadoCobro().equals("NC")){
				nuotasPendientes = nuotasPendientes + 1;
			}
		}
		return nuotasPendientes;
	}

	private double obtenerMontoProximaCouta(CuentaCobrar cc){
		for(PlanCobranza pc : cc.getNotaVenta().getListPlanCobranza()){
			if(pc.getEstadoCobro().equals("NC")){
				return pc.getMontoExtranjero();
			}
		}
		return 0;
	}
	
	//ACTION EVENT
	
	public void loadModalCliente(){
		FacesUtil.updateComponent("formModalCliente");
		FacesUtil.showModal("m-r-0");
	}
	
	public void closeModalCliente(){
		FacesUtil.hideModal("m-r-0");	
	}

	public void loadModalPlanPago(){
		FacesUtil.updateComponent("formModalPlanPago");
		FacesUtil.showModal("m-r-1");
	}

	public void closeModalPlanPago(){
		FacesUtil.hideModal("m-r-2");	
	}

	public void loadModalModificarProducto(){
		FacesUtil.updateComponent("formModalModificarProducto");
		FacesUtil.showModal("m-r-3");
	}

	public void closeModalModificarProducto(){
		FacesUtil.hideModal("m-r-3");	
	}

	public void loadDialogCollectMoney(){
		if (sessionMain.getCajaSesion()==null) {
			FacesUtil.infoMessage("Verificación", "No hay Cajas abiertas con el Cajero : "+sessionMain.getUsuarioLogin().getNombre());
			return;
		}
		FacesUtil.updateComponent("formModalCobro");
		FacesUtil.showModal("m-r-2");
	}

	int numeroCuotasPendientePorCobrar = 0;
	
	public void checkCashCollectionForCreditFee(Integer  idEDDeudaPendiente,Integer idPlanCobranza){
		EDDeudaPendiente deudaPendiente = new EDDeudaPendiente();
		numeroCuotasPendientePorCobrar = 0;
		for(EDDeudaPendiente dp: listDeudaPendiente){
			if(dp.getId().equals(idEDDeudaPendiente)){
				deudaPendiente = dp;
				break;
			}
		}
		//verificar que no debe seleccionar si ya se selecciona en otra cuenta_cobrar
		if(checkPaymentOnlyToASale(deudaPendiente)){
			FacesUtil.infoMessage("Valdiación", "No puede seleccion pagos de diferentes ventas");
			return;
		}
		String estadoActualCobro = "none";
		PlanCobranza planCobranza = new PlanCobranza();
		for(PlanCobranza pc: deudaPendiente.getCollectionPlan()){
			if(pc.getId().equals(idPlanCobranza)){
				estadoActualCobro = pc.getCobro();
				pc.setCobro( pc.getCobro().equals("UNSELECTED")?"SELECTED":"UNSELECTED");
				planCobranza = pc;
				//break;
			}
			if(pc.getEstadoCobro().equals("NC") &&  pc.getCobro().equals("UNSELECTED")){
				numeroCuotasPendientePorCobrar = numeroCuotasPendientePorCobrar + 1;
			}
		}
		System.out.println("estadoActualCobro: "+estadoActualCobro);
		if(estadoActualCobro.equals("UNSELECTED")){
			numeroCuotasACobrar = numeroCuotasACobrar + 1;
			totalCobrarNacional = totalCobrarNacional + planCobranza.getMontoNacional() + planCobranza.getMontoMulta();
			totalCobrarExtranjero = totalCobrarExtranjero + planCobranza.getMontoExtranjero() + planCobranza.getMontoMultaExtranjera();
		}else{
			numeroCuotasACobrar = numeroCuotasACobrar > 0 ? (numeroCuotasACobrar - 1) : 0;
			totalCobrarNacional = totalCobrarNacional - planCobranza.getMontoNacional() - planCobranza.getMontoMulta();
			totalCobrarExtranjero = totalCobrarExtranjero - planCobranza.getMontoExtranjero() - planCobranza.getMontoMultaExtranjera();

		}
		System.out.println("numeroCuotasACobrar: "+numeroCuotasACobrar);
		System.out.println("numeroCuotasPendientePorCobrar: "+numeroCuotasPendientePorCobrar);
		System.out.println("totalCobrarNacional: "+totalCobrarNacional);
		System.out.println("totalCobrarExtranjero: "+totalCobrarExtranjero);
		//analizar si el check que hizo esta desordenadao(Es decir no corresponde a la cuota que le toca pagar)
		// Actualizar los montos totales
		//1.2 No es el primer
		//analizar si el check que hizo esta desordenadao(Es decir no corresponde a la cuota que le toca pagar)
		// Actualizar los montos totales
		//FacesUtil.updateComponent("form001");
	}

	private boolean checkPaymentOnlyToASale(EDDeudaPendiente deudaPendiente) {
		for(EDDeudaPendiente dp: listDeudaPendiente){
			if( ! dp.getId().equals(deudaPendiente.getId())){
				for(PlanCobranza pc: dp.getCollectionPlan()){
					if(pc.getCobro().equals("SELECTED")){
						return true;
					}
				}
			}
		}
		return false;
	}

	public void calcularCambio(){
		cambioNacional = cobroNacional - totalCobrarNacional ;
		cambioExtranjero = cobroExtranjero - totalCobrarExtranjero;
	}

	public void loadCheck(Integer idCheck){
		for(EDDeudaPendiente dp: listDeudaPendiente){
			if(dp.getId().equals(idCheck)){
				dp.setPagarSiguienteCuota(true);
			}else{
				dp.setPagarSiguienteCuota(false);
			}
		}
		calcularTotalesDeudas();
	}

	public void cargarPlanPago(Integer idEDDeudaPendiente){
		for(EDDeudaPendiente ed : listDeudaPendiente){
			if(ed.getId().equals(idEDDeudaPendiente)){
				selectedEDDeudaPendiente = ed;
				selectedListPlanCobranza = planCobranzaDao.obtenerPorNotaVenta(selectedEDDeudaPendiente.getCuentaCobrar().getNotaVenta());
			}
		}
		loadModalPlanPago();
	}

	private void calcularTotalesDeudas(){
		numeroCuotasACobrar = 0;
		totalCobrarNacional = 0;
		totalCobrarExtranjero = 0;
		for(EDDeudaPendiente dp: listDeudaPendiente){
			if(dp.isPagarSiguienteCuota()){
				numeroCuotasACobrar = numeroCuotasACobrar + 1;
				totalCobrarNacional = totalCobrarNacional + dp.getMontoProximaCuota();
				totalCobrarExtranjero = totalCobrarExtranjero + dp.getMontoProximaCuotaExtranjero();
			}
		}
	}

	//PROCESOS
	public void registrarCobro(){
		List<DetalleCobranza> detalleCobranzas = new ArrayList<>();
		List<PlanCobranza> planCobranzas = new ArrayList<>();
		Date fecha = new Date();
		newCobranza.setEstado("AC");
		newCobranza.setFechaRegistro(fecha);
		newCobranza.setCuentaCobrar(listDeudaPendiente.get(0).getCuentaCobrar());
		newCobranza.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		newCobranza.setTipoMoneda("DOLAR");
		newCobranza.setTipoCambio(sessionMain.getTipoCambio().getUnidad());
		newCobranza.setCodigo(String.format("%08d",cobranzaDao.obtenerCorrelativo2(sessionMain.getGestionLogin())));
		newCobranza.setGlosa(glosa);
		newCobranza.setGestion(sessionMain.getGestionLogin());
		newCobranza.setMontoNacional(totalCobrarNacional);
		newCobranza.setMontoExtranjero(totalCobrarExtranjero);
		for(EDDeudaPendiente dp: listDeudaPendiente){
			for(PlanCobranza pc: dp.getCollectionPlan()){
				if(pc.getCobro().equals("SELECTED")){
					int numeroPqgo = pc.getNumeroPago();
					DetalleCobranza detalle = new DetalleCobranza();
					detalle.setCobranza(newCobranza);
					detalle.setEstado("AC");
					detalle.setFechaRegistro(fecha);
					detalle.setNumeroPago(numeroPqgo);
					detalle.setSubTotalNacional(pc.getMontoNacional()+pc.getMontoMulta());
					detalle.setSubTotalExranjero(pc.getMontoExtranjero() + pc.getMontoMultaExtranjera());
					detalle.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
					detalleCobranzas.add(detalle);
					planCobranzas.add(pc);
				}
			}
			newCobranza.setDetalleCobranzas(detalleCobranzas);
		}
		System.out.println("c - numeroCuotasPendientePorCobrar: "+numeroCuotasPendientePorCobrar);
		Cobranza c = cobranzaDao.registrar(sessionMain.getUsuarioLogin(),newCobranza,planCobranzas,selectedCliente,numeroCuotasPendientePorCobrar);
		if(c != null){
			closeModalPlanPago();
			cargarReporte(c);
			//cargarReporteAmortizacion(c);
			FacesUtil.updateComponent("form001");
		}
	}

	private void cargarReporteAmortizacion(Cobranza c) {
		verReporte = true;
		Map<String,String> map1 = new HashMap<>();
		map1.put("pNombreUsuario", sessionMain.getUsuarioLogin().getLogin());
		map1.put("pRazonSocial", empresaSession.getRazonSocial());
		map1.put("pDireccion", empresaSession.getDireccion());
		map1.put("pTelefono", empresaSession.getTelefono());
		map1.put("pIdCobranza", String.valueOf(c.getId()));
		map1.put("pIdEmpresa", String.valueOf(empresaSession.getId()));
		map1.put("pMoneda", "DOLAR");
		map1.put("pPagoNacional", String.valueOf(0));
		map1.put("pPagoExtranjero", String.valueOf(cobroExtranjero));
		map1.put("pTipoRecibo", "ORIGINAL");
		//---URL NOTA VENTA
		urlReporteAmortizacion = buildUrl("ReportReciboAmortizacionCobranza",map1);
	}
	
	private void cargarReporte(Cobranza c){
		verReporte = true;
		Map<String,String> map1 = new HashMap<>();
		map1.put("pRazonSocial", sessionMain.getEmpresaLogin().getRazonSocial());
		map1.put("pDireccion", sessionMain.getEmpresaLogin().getDireccion());
		map1.put("pTelefono", sessionMain.getEmpresaLogin().getTelefono());
		map1.put("pIdCobranza", String.valueOf(c.getId()));
		map1.put("pIdEmpresa", String.valueOf(sessionMain.getEmpresaLogin().getId()));
		//---URL NOTA VENTA
		urlReporteCobro = buildUrl("ReportCobranza",map1);
	}

	public void agregarCuotaAPagar(){
		for(EDDeudaPendiente ed: listDeudaPendiente){
			if(ed.getId().equals(selectedEDDeudaPendiente.getId())){
				System.out.println("Cuota CHECK");
				ed.setPagarSiguienteCuota(true);
				FacesUtil.updateComponent("form001");
				closeModalPlanPago();
				return;
			}
		}
	}

	//PLAN DE PAGO

	// ONCOMPLETETEXT Cliente
	public List<Cliente> completeCliente(String query) {
		listCliente = new ArrayList<Cliente>();//reset
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
		cargarDeudasPendientes();	
	}

	//UTIL
	/**
	 * Method buildUrl
	 * @param nameReport Nombre Reporte
	 * @param params Parametros Clave Valor
	 * @return url 
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

	public String getUrlReporteCobro() {
		return urlReporteCobro;
	}

	public void setUrlReporteCobro(String urlReporteCobro) {
		this.urlReporteCobro = urlReporteCobro;
	}

	public Cobranza getNewCobranza() {
		return newCobranza;
	}

	public void setNewCobranza(Cobranza newCobranza) {
		this.newCobranza = newCobranza;
	}

	public List<EDDeudaPendiente> getListDeudaPendiente() {
		return listDeudaPendiente;
	}

	public void setListDeudaPendiente(List<EDDeudaPendiente> listDeudaPendiente) {
		this.listDeudaPendiente = listDeudaPendiente;
	}

	public EDDeudaPendiente getSelectedEDDeudaPendiente() {
		return selectedEDDeudaPendiente;
	}

	public void setSelectedEDDeudaPendiente(EDDeudaPendiente selectedEDDeudaPendiente) {
		this.selectedEDDeudaPendiente = selectedEDDeudaPendiente;
	}

	public List<PlanCobranza> getSelectedListPlanCobranza() {
		return selectedListPlanCobranza;
	}

	public void setSelectedListPlanCobranza(List<PlanCobranza> selectedListPlanCobranza) {
		this.selectedListPlanCobranza = selectedListPlanCobranza;
	}

	public double getTotalCobrarNacional() {
		return totalCobrarNacional;
	}

	public void setTotalCobrarNacional(double totalCobrarNacional) {
		this.totalCobrarNacional = totalCobrarNacional;
	}

	public double getTotalCobrarExtranjero() {
		return totalCobrarExtranjero;
	}

	public void setTotalCobrarExtranjero(double totalCobrarExtranjero) {
		this.totalCobrarExtranjero = totalCobrarExtranjero;
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

	public int getNumeroCuotasACobrar() {
		return numeroCuotasACobrar;
	}

	public void setNumeroCuotasACobrar(int numeroCuotasACobrar) {
		this.numeroCuotasACobrar = numeroCuotasACobrar;
	}

	public double getCobroNacional() {
		return cobroNacional;
	}

	public void setCobroNacional(double cobroNacional) {
		this.cobroNacional = cobroNacional;
	}

	public double getCobroExtranjero() {
		return cobroExtranjero;
	}

	public void setCobroExtranjero(double cobroExtranjero) {
		this.cobroExtranjero = cobroExtranjero;
	}

	public String getGlosa() {
		return glosa;
	}

	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}

	public String getUrlReporteAmortizacion() {
		return urlReporteAmortizacion;
	}

	public void setUrlReporteAmortizacion(String urlReporteAmortizacion) {
		this.urlReporteAmortizacion = urlReporteAmortizacion;
	}
}
