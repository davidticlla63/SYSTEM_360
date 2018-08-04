package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.erp360.interfaces.ICajaDao;
import com.erp360.model.Caja;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;
import com.erp360.util.Time;


/**
 * 
 * @author david
 *
 */

@ManagedBean(name = "reportGeneralesCajaController")
@ViewScoped
public class ReportGeneralesCajaController implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -239167704780908003L;
	private @Inject FacesContext facesContext;
	//ESTATE
	private boolean verReporte;

	//OBJECT
	private @Inject ICajaDao cajaDao;
	
	private Caja caja;

	//LIST
	private List<Caja> cajas= new ArrayList<Caja>();

	//VARIABES
	private Date fechaInicio=Time.getPrimerDiaDelMes(new Date());
	private Date fechaFin=Time.getUltimoDiaDelMes(new Date());
	private String urlGeneric;
	private String tituloReporte;
	
	

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login

	@PostConstruct
	public void initNewObject() {

		System.out.println(" ... initNewObject ...");
		caja= new Caja();
		verReporte = false;
		urlGeneric = "";
		tituloReporte = "";

		cajas= cajaDao.listaPorEmpresaYActivos(sessionMain.getEmpresaLogin());
		if (cajas.size()>0) {
			caja=cajas.get(0);
		}

	}

	public void clearUrl(){
		verReporte = false;
		urlGeneric="";
		//FacesUtil.updateComponent("form001");
	}

	//----- LOAD DIALOG

	public void loadDialgReportProductoAgrupadoLinea(){
		FacesUtil.updateComponent("formDlgReportTicketEspecifico");
		FacesUtil.showDialog("dlgReportTicketEspecifico");
	}

	// ---------------------------- REPORTE  --------------------------------
	
public void loadInformePorTipo(){
		
		HttpServletRequest request = (HttpServletRequest) facesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String urlPath = request.getRequestURL().toString();
		urlPath = urlPath.substring(0, urlPath.length()
				- request.getRequestURI().length())
				+ request.getContextPath() + "/";

		// String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
		// + sessionMain.getEmpresaLogin().getId() + "&type=EMPRESA";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID_CAJA", caja.getId());
		map.put("USUARIO", sessionMain.getUsuarioLogin().getLogin());
		map.put("FECHA_INICIO",Time.convertDateToString(fechaInicio));
		map.put("FECHA_FIN", Time.convertDateToString(fechaFin));
		map.put("FECHA_INICIO_NUM",Integer.parseInt(  Time.obtenerFormatoYYYYMMDD(fechaInicio)));
		map.put("FECHA_FIN_NUM",Integer.parseInt(  Time.obtenerFormatoYYYYMMDD(fechaFin)));
		// map.put("pais",
		// notaVenta.getSucursal().getCiudad().getPais().getNombre());
		// map.put("logo", URL_SERVLET_LOGO);
		map.put("REPORT_LOCALE", new Locale("en", "US"));

		System.out.println(map);
		String reportPath = urlPath
				+ "resources/report/caja/movimientos/reportCajaMovimientoPorTipo.jasper";
		System.out.println("reportPath : "+reportPath);

		request.getSession().setAttribute("parameter", map);
		request.getSession().setAttribute("path", reportPath);
		urlGeneric=urlPath + "ReportPdfServlet";
		//currentPage = "/pages/caja/apertura/report.xhtml";
		
		verReporte = true;
		tituloReporte = "INFORME DE INGRESO";
		FacesUtil.updateComponent("form001");
		
	
	}

public void loadInformeTodos(){
	
	HttpServletRequest request = (HttpServletRequest) facesContext
			.getCurrentInstance().getExternalContext().getRequest();
	String urlPath = request.getRequestURL().toString();
	urlPath = urlPath.substring(0, urlPath.length()
			- request.getRequestURI().length())
			+ request.getContextPath() + "/";

	// String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
	// + sessionMain.getEmpresaLogin().getId() + "&type=EMPRESA";

	Map<String, Object> map = new HashMap<String, Object>();
	map.put("ID_CAJA", caja.getId());
	map.put("USUARIO", sessionMain.getUsuarioLogin().getLogin());
	map.put("FECHA_INICIO",Time.convertDateToString(fechaInicio));
	map.put("FECHA_FIN", Time.convertDateToString(fechaFin));
	map.put("FECHA_INICIO_NUM",Integer.parseInt(  Time.obtenerFormatoYYYYMMDD(fechaInicio)));
	map.put("FECHA_FIN_NUM",Integer.parseInt(  Time.obtenerFormatoYYYYMMDD(fechaFin)));
	// map.put("pais",
	// notaVenta.getSucursal().getCiudad().getPais().getNombre());
	// map.put("logo", URL_SERVLET_LOGO);
	map.put("REPORT_LOCALE", new Locale("en", "US"));

	System.out.println(map);
	String reportPath = urlPath
			+ "resources/report/caja/movimientos/reportCajaMovimientoPorTipoTodos.jasper";
	System.out.println("reportPath : "+reportPath);

	request.getSession().setAttribute("parameter", map);
	request.getSession().setAttribute("path", reportPath);
	urlGeneric=urlPath + "ReportPdfServlet";
	//currentPage = "/pages/caja/apertura/report.xhtml";
	
	verReporte = true;
	tituloReporte = "INFORME DE INGRESO";
	FacesUtil.updateComponent("form001");
	

}

public void loadInformeComisiones(){
	
	HttpServletRequest request = (HttpServletRequest) facesContext
			.getCurrentInstance().getExternalContext().getRequest();
	String urlPath = request.getRequestURL().toString();
	urlPath = urlPath.substring(0, urlPath.length()
			- request.getRequestURI().length())
			+ request.getContextPath() + "/";

	// String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
	// + sessionMain.getEmpresaLogin().getId() + "&type=EMPRESA";

	Map<String, Object> map = new HashMap<String, Object>();
	map.put("ID_CAJA", caja.getId());
	map.put("USUARIO", sessionMain.getUsuarioLogin().getLogin());
	map.put("FECHA_INICIO",Time.convertDateToString(fechaInicio));
	map.put("FECHA_FIN", Time.convertDateToString(fechaFin));
	map.put("FECHA_INICIO_NUM",Integer.parseInt(  Time.obtenerFormatoYYYYMMDD(fechaInicio)));
	map.put("FECHA_FIN_NUM",Integer.parseInt(  Time.obtenerFormatoYYYYMMDD(fechaFin)));
	// map.put("pais",
	// notaVenta.getSucursal().getCiudad().getPais().getNombre());
	// map.put("logo", URL_SERVLET_LOGO);
	map.put("REPORT_LOCALE", new Locale("en", "US"));

	System.out.println(map);
	String reportPath = urlPath
			+ "resources/report/caja/movimientos/reportCajaMovimientoComisiones.jasper";
	System.out.println("reportPath : "+reportPath);

	request.getSession().setAttribute("parameter", map);
	request.getSession().setAttribute("path", reportPath);
	urlGeneric=urlPath + "ReportPdfServlet";
	//currentPage = "/pages/caja/apertura/report.xhtml";
	
	verReporte = true;
	tituloReporte = "INFORME DE INGRESO";
	FacesUtil.updateComponent("form001");
	

}
	public void loadInformeIgreso(){
		
		HttpServletRequest request = (HttpServletRequest) facesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String urlPath = request.getRequestURL().toString();
		urlPath = urlPath.substring(0, urlPath.length()
				- request.getRequestURI().length())
				+ request.getContextPath() + "/";

		// String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
		// + sessionMain.getEmpresaLogin().getId() + "&type=EMPRESA";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID_CAJA", caja.getId());
		map.put("USUARIO", sessionMain.getUsuarioLogin().getLogin());
		map.put("FECHA_INICIO",Time.convertDateToString(fechaInicio));
		map.put("FECHA_FIN", Time.convertDateToString(fechaFin));
		map.put("FECHA_INICIO_NUM",Integer.parseInt(  Time.obtenerFormatoYYYYMMDD(fechaInicio)));
		map.put("FECHA_FIN_NUM",Integer.parseInt(  Time.obtenerFormatoYYYYMMDD(fechaFin)));
		// map.put("pais",
		// notaVenta.getSucursal().getCiudad().getPais().getNombre());
		// map.put("logo", URL_SERVLET_LOGO);
		map.put("REPORT_LOCALE", new Locale("en", "US"));

		System.out.println(map);
		String reportPath = urlPath
				+ "resources/report/caja/movimientos/reportCajaMovimientoIngreso.jasper";
		System.out.println("reportPath : "+reportPath);

		request.getSession().setAttribute("parameter", map);
		request.getSession().setAttribute("path", reportPath);
		urlGeneric=urlPath + "ReportPdfServlet";
		//currentPage = "/pages/caja/apertura/report.xhtml";
		
		verReporte = true;
		tituloReporte = "INFORME DE INGRESO";
		FacesUtil.updateComponent("form001");
		
	
	}

public void loadInformeEgreso(){
		
		HttpServletRequest request = (HttpServletRequest) facesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String urlPath = request.getRequestURL().toString();
		urlPath = urlPath.substring(0, urlPath.length()
				- request.getRequestURI().length())
				+ request.getContextPath() + "/";

		// String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
		// + sessionMain.getEmpresaLogin().getId() + "&type=EMPRESA";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID_CAJA", caja.getId());
		map.put("USUARIO", sessionMain.getUsuarioLogin().getLogin());
		map.put("FECHA_INICIO", Time.convertDateToString(fechaInicio));
		map.put("FECHA_FIN", Time.convertDateToString(fechaFin));
		map.put("FECHA_INICIO_NUM",Integer.parseInt(  Time.obtenerFormatoYYYYMMDD(fechaInicio)));
		map.put("FECHA_FIN_NUM",Integer.parseInt(  Time.obtenerFormatoYYYYMMDD(fechaFin)));
		// map.put("pais",
		// notaVenta.getSucursal().getCiudad().getPais().getNombre());
		// map.put("logo", URL_SERVLET_LOGO);
		map.put("REPORT_LOCALE", new Locale("en", "US"));

		System.out.println(map);
		String reportPath = urlPath
				+ "resources/report/caja/movimientos/reportCajaMovimientoEngreso.jasper";
		System.out.println("reportPath : "+reportPath);

		request.getSession().setAttribute("parameter", map);
		request.getSession().setAttribute("path", reportPath);
		urlGeneric=urlPath + "ReportPdfServlet";
		//currentPage = "/pages/caja/apertura/report.xhtml";
		
		verReporte = true;
		tituloReporte = "INFORME DE INGRESO";
		FacesUtil.updateComponent("form001");
		
	
	}
	// ---- get and set -----

	public boolean isVerReporte() {
		return verReporte;
	}

	public void setVerReporte(boolean verReporte) {
		this.verReporte = verReporte;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getUrlGeneric() {
		return urlGeneric;
	}

	public void setUrlGeneric(String urlGeneric) {
		this.urlGeneric = urlGeneric;
	}

	public String getTituloReporte() {
		return tituloReporte;
	}

	public void setTituloReporte(String tituloReporte) {
		this.tituloReporte = tituloReporte;
	}

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	public List<Caja> getCajas() {
		return cajas;
	}

	public void setCajas(List<Caja> cajas) {
		this.cajas = cajas;
	}

}
