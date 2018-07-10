package com.erp360.controller;

import java.io.Serializable;
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

import org.primefaces.event.SelectEvent;

import com.erp360.interfaces.ICajaDao;
import com.erp360.interfaces.ICajaMovimientoDao;
import com.erp360.model.Caja;
import com.erp360.model.CajaMovimiento;
import com.erp360.util.SessionMain;
import com.erp360.util.Time;

/**
 * @author david
 *
 */
@ManagedBean(name = "cajaMovimientoConsultasController")
@ViewScoped
public class CajaMovimientoConsultasController implements Serializable {
	private static final long serialVersionUID = 1L;
	private @Inject FacesContext facesContext;

	// DAO
	private @Inject ICajaMovimientoDao cajaMovimientoDao;
	private @Inject SessionMain sessionMain;
	private @Inject ICajaDao cajaDao;

	// OBJECT
	private Caja caja;

	// VAR
	private String currentPage = "/pages/caja/movimiento_consultas/list.xhtml";

	// LIST
	private List<CajaMovimiento> cajaMovimientos;
	public static List<Caja> cajas;

	// STATE
	private boolean nuevo;
	private boolean registrar;
	private boolean seleccionado;
	private boolean ver;

	private Date fechaInicio = Time.getPrimerDiaDelMes(new Date());
	private Date fechaFin = Time.getUltimoDiaDelMes(new Date());
	
	private String urlReport;

	@PostConstruct
	public void init() {
		loadDefault();
	}

	public void loadDefault() {
		nuevo = true;
		seleccionado = false;
		registrar = false;
		ver=false;
		caja = new Caja();
		cajas = cajaDao.listaPorEmpresaYActivos(sessionMain.getEmpresaLogin());
	}

	public void consulta() {
		cajaMovimientos = cajaMovimientoDao.obtenerPorSucursalEntreFechas(
				sessionMain.getSucursalLogin(), caja, fechaInicio, fechaFin);
	}
	
	
	public void verReporteResumido() {
		try {
			ver=true;
			registrar=true;
			System.out.println("Ingreso a verReporteResumido");
			HttpServletRequest request = (HttpServletRequest) facesContext
					.getCurrentInstance().getExternalContext().getRequest();
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length()
					- request.getRequestURI().length())
					+ request.getContextPath() + "/";

//			String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
//					+ sessionMain.getEmpresaLogin().getId() + "&type=EMPRESA";
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID_CAJA", caja.getId());
			map.put("USUARIO", sessionMain.getUsuarioLogin().getLogin());
			map.put("FECHA_INICIO",new Integer( Time.obtenerFormatoYYYYMMDD(fechaInicio)));
			map.put("FECHA_FIN", new Integer(Time.obtenerFormatoYYYYMMDD(fechaFin)));
			map.put("FECHA_INICIO_TEXT", Time.convertSimpleDateToString(fechaInicio));
			map.put("FECHA_FIN_TEXT", Time.convertSimpleDateToString(fechaFin));
//			map.put("pais", notaVenta.getSucursal().getCiudad().getPais().getNombre());
//			map.put("logo", URL_SERVLET_LOGO);
			map.put("REPORT_LOCALE", new Locale("en", "US"));
			
			String reportPath = urlPath +
							"resources/report/caja/movimientos/reportCajaMovimiento.jasper";		
			
			request.getSession().setAttribute("parameter", map);
			request.getSession().setAttribute("path", reportPath);
			urlReport=urlPath + "ReportPdfServlet";
			currentPage = "/pages/caja/movimiento_consultas/report.xhtml";
//			FacesUtil.updateComponent("formReporte");
//			FacesUtil.showDialog("dlgrReporte");
		} catch (Exception e) {
			System.out.println("Fallo en " + e.toString());
		}

	}
	
	public void verReporteDetallado() {
		try {
			ver=true;
			registrar=true;
			System.out.println("Ingreso a verReporteDetallado");
			HttpServletRequest request = (HttpServletRequest) facesContext
					.getCurrentInstance().getExternalContext().getRequest();
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length()
					- request.getRequestURI().length())
					+ request.getContextPath() + "/";

			String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
					+ sessionMain.getEmpresaLogin().getId() + "&type=EMPRESA";
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID_CAJA", caja.getId());
			map.put("USUARIO", sessionMain.getUsuarioLogin().getLogin());
			map.put("FECHA_INICIO",new Integer( Time.obtenerFormatoYYYYMMDD(fechaInicio)));
			map.put("FECHA_FIN",new Integer( Time.obtenerFormatoYYYYMMDD(fechaFin)));
			map.put("FECHA_INICIO_TEXT", Time.convertSimpleDateToString(fechaInicio));
			map.put("FECHA_FIN_TEXT", Time.convertSimpleDateToString(fechaFin));
//			map.put("pais", notaVenta.getSucursal().getCiudad().getPais().getNombre());
			map.put("logo", URL_SERVLET_LOGO);
			map.put("REPORT_LOCALE", new Locale("en", "US"));
			
			String reportPath = urlPath +
							"resources/report/caja/movimientos/reportCajaMovimientoDetallado.jasper";		
			
			request.getSession().setAttribute("parameter", map);
			request.getSession().setAttribute("path", reportPath);
			urlReport=urlPath + "ReportPdfServlet";
			currentPage = "/pages/caja/movimiento_consultas/report.xhtml";
//			FacesUtil.updateComponent("formReporte");
//			FacesUtil.showDialog("dlgrReporte");
		} catch (Exception e) {
			System.out.println("Fallo en " + e.toString());
		}

	}

	
	public void verReporteRendicion() {
		try {
			ver=true;
			registrar=true;
			System.out.println("Ingreso a verReporteRendicion");
			HttpServletRequest request = (HttpServletRequest) facesContext
					.getCurrentInstance().getExternalContext().getRequest();
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length()
					- request.getRequestURI().length())
					+ request.getContextPath() + "/";

			String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
					+ sessionMain.getEmpresaLogin().getId() + "&type=EMPRESA";
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID_CAJA", caja.getId());
			map.put("USUARIO", sessionMain.getUsuarioLogin().getLogin());
			map.put("FECHA_INICIO",new Integer( Time.obtenerFormatoYYYYMMDD(fechaInicio)));
			map.put("FECHA_FIN",new Integer( Time.obtenerFormatoYYYYMMDD(fechaFin)));
			map.put("FECHA_INICIO_TEXT", Time.convertSimpleDateToString(fechaInicio));
			map.put("FECHA_FIN_TEXT", Time.convertSimpleDateToString(fechaFin));
//			map.put("pais", notaVenta.getSucursal().getCiudad().getPais().getNombre());
			map.put("logo", URL_SERVLET_LOGO);
			map.put("REPORT_LOCALE", new Locale("en", "US"));
			
			String reportPath = urlPath +
							"resources/report/caja/movimientos/reportCajaMovimientoDetalladoRendicion.jasper";		
			
			request.getSession().setAttribute("parameter", map);
			request.getSession().setAttribute("path", reportPath);
			urlReport=urlPath + "ReportPdfServlet";
			currentPage = "/pages/caja/movimiento_consultas/report.xhtml";
//			FacesUtil.updateComponent("formReporte");
//			FacesUtil.showDialog("dlgrReporte");
		} catch (Exception e) {
			System.out.println("Fallo en " + e.toString());
		}

	}

	/* METODOS ONCOMPLETE */

	public List<Caja> onCompleteCaja(String query) {
		// ystem.out.println("Entro en Oncomplete Caja"+ query);
		cajas = cajaDao.obtenerPorEmpresa(query.toUpperCase(),
				sessionMain.getEmpresaLogin());
		return cajas;
	}
	
	// ACTION

		public void onSelectCaja(SelectEvent event){	
			caja=(Caja) event.getObject();
			
		}

	// ACTION

	public void actionCancelar() {
		nuevo = true;
		seleccionado = false;
		registrar = false;
		loadDefault();
		currentPage = "/pages/caja/movimiento_consultas/list.xhtml";
	}

	/* SETTERS AND GETTERS */

	public boolean isNuevo() {
		return nuevo;
	}

	public void setNuevo(boolean nuevo) {
		this.nuevo = nuevo;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public boolean isRegistrar() {
		return registrar;
	}

	public void setRegistrar(boolean registrar) {
		this.registrar = registrar;
	}

	public List<CajaMovimiento> getCajaMovimientos() {
		return cajaMovimientos;
	}

	public void setCajaMovimientos(List<CajaMovimiento> cajaMovimientos) {
		this.cajaMovimientos = cajaMovimientos;
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

	public String getUrlReport() {
		return urlReport;
	}

	public void setUrlReport(String urlReport) {
		this.urlReport = urlReport;
	}

	public boolean isVer() {
		return ver;
	}

	public void setVer(boolean ver) {
		this.ver = ver;
	}

}
