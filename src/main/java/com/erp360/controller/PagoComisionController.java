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

import com.erp360.dao.EjecutivoComisionesDao;
import com.erp360.dao.EjecutivoDao;
import com.erp360.dao.PagoComisionDao;
import com.erp360.model.DetallePagoComision;
import com.erp360.model.Ejecutivo;
import com.erp360.model.EjecutivoComisiones;
import com.erp360.model.PagoComision;
import com.erp360.util.FacesUtil;
import com.erp360.util.NumberUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@ManagedBean(name = "pagoComisionController")
@ViewScoped
public class PagoComisionController implements Serializable {

	private static final long serialVersionUID = -1012155707040198783L;
	
	private @Inject FacesContext facesContext;

	//DAO
	private @Inject PagoComisionDao pagoComisionDao;
	private @Inject EjecutivoComisionesDao ejecutivoComisionesDao;
	private  @Inject EjecutivoDao ejecutivoDao;

	//OBJECT
	private PagoComision pagoComision;
	private Ejecutivo ejecutivo;
	private EjecutivoComisiones ejecutivoComision;

	//LIST
	private List<PagoComision> comisionesPagadas;
	private List<EjecutivoComisiones> ejecutivoComisiones;
	private List<Ejecutivo> ejecutivos;
	private List<DetallePagoComision> detallePagoComisiones;

	//VAR
	private String currentPage;
	private boolean verReport;
	private String urlReportPago;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login

	@PostConstruct
	public void init() {

		System.out.println(" ... init New Cobranzas...");
		loadDefault();
	}
	
	public void loadDefault(){
		verReport = false;
		urlReportPago = "";
		currentPage = "/pages/caja/comision/pagos/list.xhtml";
		ejecutivo = null;
		ejecutivoComisiones = new ArrayList<>();
		pagoComision = new PagoComision();
		pagoComision.setEstado("PR");
		pagoComision.setFechaRegistro(new Date());
		pagoComision.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		detallePagoComisiones = new ArrayList<>();
		comisionesPagadas = pagoComisionDao.obtenerOrdenAscPorId();
	}

	// ------- action & event ------
	
	public List<Ejecutivo> completeEjecutivo(String query) {
		ejecutivos = new ArrayList<Ejecutivo>();
		boolean sw = NumberUtil.isNumeric(query);
		if(sw){
			ejecutivos = ejecutivoDao.obtenerTodosPorCi(query);
		}else{
			ejecutivos = ejecutivoDao.obtenerTodosPorNombres(query.toUpperCase());
		}
		return ejecutivos;
	}
	
	public void onRowSelectEjecutivoClick(SelectEvent event) {
		Ejecutivo ejecutivoAux = (Ejecutivo) event.getObject();
		for(Ejecutivo c : ejecutivos){
			if(c.getId().equals(ejecutivoAux.getId())){
				ejecutivo = c;
				ejecutivoComisiones = new ArrayList<>();
				ejecutivoComisiones = ejecutivoComisionesDao.obtenerTodosNoPagadorByEjecutivo(ejecutivo);
				double saldoAnterior = 0;
				for(EjecutivoComisiones kc: ejecutivoComisiones ){
					if(kc.getNotaVenta()!=null){
						kc.setTipoMovimiento("VENTA");
						kc.setConcepto("VENTA "+kc.getNotaVenta().getCodigo());
						kc.setSaldo(saldoAnterior+kc.getImporte());
						saldoAnterior = kc.getSaldo();
					}else if(kc.getCobranza()!=null){
						kc.setTipoMovimiento("CUOTA");
						kc.setConcepto("CUOTA "+kc.getCobranza().getCodigo());
						kc.setSaldo(saldoAnterior+kc.getImporte());
						saldoAnterior = kc.getSaldo();
					}
				}
				detallePagoComisiones = new ArrayList<>();
				return;
			}
		}
	}
	
	public void aplicarPago(EjecutivoComisiones ejecutivoComision){
		DetallePagoComision dpc = new DetallePagoComision();
		dpc.setEjecutivoComisiones(ejecutivoComision);
		dpc.setEstado("AC");
		dpc.setFechaRegistro(new Date());
		dpc.setMontoExtranjero(ejecutivoComision.getImporte());
		dpc.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		detallePagoComisiones.add(dpc);
		//
		double montoExtranjero = ejecutivoComision.getImporte();
		montoExtranjero = montoExtranjero + pagoComision.getMontoExtranjero();
		pagoComision.setMontoExtranjero(montoExtranjero);
		//
		ejecutivoComision.setPagado(Boolean.TRUE);
	}
	
	public void quitarPago(EjecutivoComisiones ejecutivoComision){
		DetallePagoComision temp = new DetallePagoComision();
		for(DetallePagoComision dpc : detallePagoComisiones){
			if(dpc.getEjecutivoComisiones().getId().equals(ejecutivoComision.getId())){
				temp = dpc;
			}
		}
		
		//
		double montoExtranjero = ejecutivoComision.getImporte();
		montoExtranjero =  pagoComision.getMontoExtranjero() - montoExtranjero;
		pagoComision.setMontoExtranjero(montoExtranjero);
		//
		detallePagoComisiones.remove(temp);
		for(EjecutivoComisiones ec: ejecutivoComisiones){
			if(ec.getId().equals(ejecutivoComision.getId())){
				ec.setPagado(Boolean.FALSE);
			}
		}
	}
	
	public void registrarPago(){
		if (sessionMain.getCajaSesion()==null) {
			FacesUtil.infoMessage("Verificación", "No hay Cajas abiertas con el Cajero : "+sessionMain.getUsuarioLogin().getNombre());
			return;
		}
		if(ejecutivo == null){
			FacesUtil.infoMessage("Validación", "Debe seleccionar un ejecutivo");
			return;
		}
		if(detallePagoComisiones.size()==0){
			FacesUtil.infoMessage("Validación", "Debe Aplicar uno o mas Pagos");
			return;
		}
		pagoComision.setCodigo(getCodigoPagoComision());
		pagoComision.setGlosa("Ninguno");
		pagoComision.setMontoNacional(0);
		pagoComision.setTipoCambio(0);
		pagoComision.setTipoMoneda("Ninguno");
		pagoComision.setGestion(sessionMain.getGestionLogin());
		pagoComision.setEjecutivo(ejecutivo);
		
		//registrar  PagoComision
		//registrar DetallePagoComision
		//registrar en caja
		//kardex ejecutivo
		//actualizar ejecutivoComisiones (pagado)
		
		pagoComision = pagoComisionDao.registrar(pagoComision, detallePagoComisiones, ejecutivoComisiones);
		//loadDefault();
		cargarReporte(pagoComision);
	}
	
	private void cargarReporte(PagoComision pagoComision){
		verReport = true;
		Map<String,String> map1 = new HashMap<>();
		map1.put("pRazonSocial", sessionMain.getEmpresaLogin().getRazonSocial());
		map1.put("pDireccion", sessionMain.getEmpresaLogin().getDireccion());
		map1.put("pTelefono", sessionMain.getEmpresaLogin().getTelefono());
		map1.put("pIdPagoComision", String.valueOf(pagoComision.getId()));
		map1.put("pIdEmpresa", String.valueOf(sessionMain.getEmpresaLogin().getId()));
		//---URL
		urlReportPago = buildUrl("ReportPagoComision",map1);
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

	private String getCodigoPagoComision() {
		String value = String.format("%08d",pagoComisionDao.obtenerCorrelativo());
		System.out.println("getCodigoPagoComision: "+value);
		return value;
	}

	public void prepareCreate(){
		currentPage = "/pages/caja/comision/pagos/create.xhtml";
	}


	public void onRowSelect(SelectEvent event) {
		currentPage = "/pages/caja/comision/pagos/create.xhtml";
	}

	// ------- procesos transaccional ------

	
	// -------- get and set -------

	public List<PagoComision> getComisionesPagadas() {
		return comisionesPagadas;
	}

	public void setComisionesPagadas(List<PagoComision> comisionesPagadas) {
		this.comisionesPagadas = comisionesPagadas;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public PagoComision getPagoComision() {
		return pagoComision;
	}

	public void setPagoComision(PagoComision pagoComision) {
		this.pagoComision = pagoComision;
	}

	public List<EjecutivoComisiones> getEjecutivoComisiones() {
		return ejecutivoComisiones;
	}

	public void setEjecutivoComisiones(List<EjecutivoComisiones> ejecutivoComisiones) {
		this.ejecutivoComisiones = ejecutivoComisiones;
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

	public EjecutivoComisiones getEjecutivoComision() {
		return ejecutivoComision;
	}

	public void setEjecutivoComision(EjecutivoComisiones ejecutivoComision) {
		this.ejecutivoComision = ejecutivoComision;
	}

	public List<DetallePagoComision> getDetallePagoComisiones() {
		return detallePagoComisiones;
	}

	public void setDetallePagoComisiones(List<DetallePagoComision> detallePagoComisiones) {
		this.detallePagoComisiones = detallePagoComisiones;
	}

	public boolean isVerReport() {
		return verReport;
	}

	public void setVerReport(boolean verReport) {
		this.verReport = verReport;
	}

	public String getUrlReportPago() {
		return urlReportPago;
	}

	public void setUrlReportPago(String urlReportPago) {
		this.urlReportPago = urlReportPago;
	}

}
