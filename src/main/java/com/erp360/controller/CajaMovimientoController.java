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
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.junit.experimental.categories.Categories.IncludeCategory;
import org.primefaces.event.SelectEvent;

import com.erp360.dao.ParametroVentaDao;
import com.erp360.enums.TipoMovimiento;
import com.erp360.interfaces.ICajaMovimientoDao;
import com.erp360.model.CajaMovimiento;
import com.erp360.model.CajaSesion;
import com.erp360.model.ParametroVenta;
import com.erp360.util.FacesUtil;
import com.erp360.util.FacturacionUtil;
import com.erp360.util.SessionMain;

/**
 * @author david
 *
 */
@ManagedBean(name = "cajaMovimientoController")
@ViewScoped
public class CajaMovimientoController implements Serializable {
	private static final long serialVersionUID = 1L;
	private @Inject FacesContext facesContext;

	// DAO
	private @Inject SessionMain sessionDao;
	private @Inject ICajaMovimientoDao cajaMovimientoDao;
	private @Inject ParametroVentaDao parametroVentaDao;

	// OBJECT
	private CajaSesion cajaSesion;
	private CajaMovimiento cajaMovimiento;
	private ParametroVenta parametroVenta;

	// VAR
	private String currentPage = "/pages/caja/movimiento/list.xhtml";

	// LIST
	private List<CajaMovimiento> cajaMovimientos;

	// STATE
	private boolean nuevo;
	private boolean registrar;
	private boolean seleccionado;

	private boolean activado;
	private String cuenta = "bs";
	private String urlReport;
	private boolean ver;

	@PostConstruct
	public void init() {
		loadDefault();
	}

	public void loadDefault() {
		 parametroVenta=parametroVentaDao.obtenerPorEmpresa(sessionDao.getEmpresaLogin());
		System.out.println(parametroVenta);
		if (parametroVenta != null) {
			nuevo = true;
			seleccionado = false;
			registrar = false;
			setVer(false);
			cajaSesion = sessionDao.getCajaSesion();
			// selectedConceptoCaja= new ConceptoCaja();
			// setConceptoCajas(new ArrayList<ConceptoCaja>());
			if (cajaSesion != null) {
				System.out
						.println("caja : " + cajaSesion.getCaja().getNombre());
				cajaMovimientos = cajaMovimientoDao
						.listarMovimientosPorSesion(cajaSesion);
			}
		}
	}

	// ACTION

	public void actionCancelar() {
		nuevo = true;
		seleccionado = false;
		registrar = false;
		loadDefault();
		currentPage = "/pages/caja/movimiento/list.xhtml";
	}

	public void actionCajaMovimiento() {
		System.out.println("Ingreso a actionCajaMovimiento..");
		if (sessionDao.getCajaSesion() != null) {
			nuevo = false;
			seleccionado = false;
			registrar = true;
			cajaMovimiento = new CajaMovimiento();
			cajaMovimiento.setCajaSesion(cajaSesion);
			cajaMovimiento.setSucursal(sessionDao.getSucursalLogin());
			getTipoMovimientos();
			currentPage = "/pages/caja/movimiento/edit.xhtml";
		} else {
			FacesUtil.updateComponent("form002");
			FacesUtil.showDialog("dlg2");
		}
	}

	/* CRUD NOTA DE VENTA */

	public void procesar() {
		if (cajaMovimiento.getMonto() > 0
				&& cajaMovimiento.getMontoExtranjero() > 0) {
			cajaMovimiento.setEstado("AC");
			cajaMovimiento.setUsuarioRegistro(sessionDao.getUsuarioLogin()
					.getNombre());
			cajaMovimiento.setFechaRegistro(new Date());
			cajaMovimiento.setProcesada(false);
			cajaMovimiento.setMontoLiteral(FacturacionUtil.obtenerMontoLiteral(cajaMovimiento.getMonto()));
			CajaMovimiento co = null;
			co = cajaMovimientoDao.registrar(cajaMovimiento);

			if (co != null) {
				verReporte();
				//loadDefault();
				// currentPage = "/pages/caja/movimiento/list.xhtml";
			} else {
				FacesUtil.infoMessage("Informacion", "ERROR AL INSERTAR");
			}
		} else {
			FacesUtil.errorMessage("Ingrese el monto de Movimiento");
		}
	}

	public void modificar() {

		if (true) {
			if (true) {
				loadDefault();
			}
		} else {
			FacesUtil.infoMessage("Informacion",
					"El detalle no debe estar vacio");
		}

	}

	public void actualizarExtranjero(AjaxBehaviorEvent event) {
		cajaMovimiento.setMontoExtranjero(cajaMovimiento.getMonto() / sessionDao.getTipoCambio().getUnidad());
		cajaMovimiento.setTipoCambio(sessionDao.getTipoCambio().getUnidad());
		// cajaMovimiento.setMontoExtranjero(cajaMovimiento.getMonto()
		// / sessionDao.getTipoCambioActual().getUnidad());
	}

	public void actualizarNacional(AjaxBehaviorEvent event) {
		cajaMovimiento.setMonto(cajaMovimiento.getMontoExtranjero() * sessionDao.getTipoCambio().getUnidad());
		cajaMovimiento.setTipoCambio(sessionDao.getTipoCambio().getUnidad());
		// cajaMovimiento.setMonto(cajaMovimiento.getMontoExtranjero()
		// * sessionDao.getTipoCambioActual().getUnidad());
	}

	public void actualizarCambio(AjaxBehaviorEvent event) {
		cajaMovimiento.setCambio(cajaMovimiento.getMontoRecibido()
				- cajaMovimiento.getMonto());
	}

	private SelectItem[] items = new SelectItem[3];

	public void getTipoMovimientos() {
		System.out.println("Tipo : " + cajaMovimiento.getTipo());
		if (cajaMovimiento.getTipo().trim().equals("I")) {
			items = getTipoMovimientosIngreso();
		} else if (cajaMovimiento.getTipo().trim().equals("E")) {
			items = getTipoMovimientosEgreso();
		}
	}

	public SelectItem[] getTipoMovimientosIngreso() {
		System.out.println("Ingreso a getTipoMovimientosIngreso");
		SelectItem[] items = new SelectItem[3];
		int i = 0;
		for (TipoMovimiento t : TipoMovimiento.values()) {
			if (t == TipoMovimiento.VEN || t == TipoMovimiento.COB
					|| t == TipoMovimiento.DEP) {
				items[i++] = new SelectItem(t, t.getLabel());
			}
		}
		return items;
	}

	public SelectItem[] getTipoMovimientosEgreso() {
		System.out.println("Ingreso a getTipoMovimientosEgreso");
		SelectItem[] items = new SelectItem[3];
		int i = 0;
		for (TipoMovimiento t : TipoMovimiento.values()) {
			if (t == TipoMovimiento.COM || t == TipoMovimiento.RET
					|| t == TipoMovimiento.PAG) {
				items[i++] = new SelectItem(t, t.getLabel());
			}
		}
		return items;
	}

	public void visualizarReporte(CajaMovimiento cajaMovimiento) {
		this.cajaMovimiento = cajaMovimiento;
		verReporte();

	}

	public void verReporte() {
		try {
			setVer(true);
			registrar = true;
			nuevo=false;
			System.out.println("Ingreso a verReporteResumido");
			HttpServletRequest request = (HttpServletRequest) facesContext
					.getCurrentInstance().getExternalContext().getRequest();
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length()
					- request.getRequestURI().length())
					+ request.getContextPath() + "/";

			// String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
			// + sessionMain.getEmpresaLogin().getId() + "&type=EMPRESA";

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID", cajaMovimiento.getId());
			map.put("USUARIO", sessionDao.getUsuarioLogin().getLogin());
			// map.put("pais",
			// notaVenta.getSucursal().getCiudad().getPais().getNombre());
			// map.put("logo", URL_SERVLET_LOGO);
			map.put("REPORT_LOCALE", new Locale("en", "US"));

			String reportPath = urlPath
					+ "resources/report/caja/movimientos/reportReciboCaja.jasper";

			request.getSession().setAttribute("parameter", map);
			request.getSession().setAttribute("path", reportPath);
			setUrlReport(urlPath + "ReportPdfServlet");
			currentPage = "/pages/caja/movimiento/report.xhtml";
			// FacesUtil.updateComponent("formReporte");
			// FacesUtil.showDialog("dlgrReporte");
		} catch (Exception e) {
			System.out.println("Fallo en " + e.toString());
		}

	}

	// ONCOMPLETETEXT CUENTA
	// public List<ConceptoCaja> completeConcepto(String query) {
	// conceptoCajas=
	// conceptoCajaDao.obtenerPorEmpresaAutoComplete(query.toUpperCase(),
	// sessionDao.getEmpresaLogin());
	//
	// return conceptoCajas;
	// }
	//
	// public void onRowConceptoClick1(SelectEvent event) {
	// selectedConceptoCaja = ((ConceptoCaja)event.getObject());
	// cajaMovimiento.setDescripcion(selectedConceptoCaja.getNombre());
	// cajaMovimiento.setPlanCuenta(selectedConceptoCaja.getPlanCuenta());
	// return;
	// }
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

	public CajaSesion getCajaSesion() {
		return cajaSesion;
	}

	public void setCajaSesion(CajaSesion cajaSesion) {
		this.cajaSesion = cajaSesion;
	}

	public List<CajaMovimiento> getCajaMovimientos() {
		return cajaMovimientos;
	}

	public void setCajaMovimientos(List<CajaMovimiento> cajaMovimientos) {
		this.cajaMovimientos = cajaMovimientos;
	}

	public CajaMovimiento getCajaMovimiento() {
		return cajaMovimiento;
	}

	public void setCajaMovimiento(CajaMovimiento cajaMovimiento) {
		this.cajaMovimiento = cajaMovimiento;
	}

	public SelectItem[] getItems() {
		return items;
	}

	public void setItems(SelectItem[] items) {
		this.items = items;
	}

	public boolean isActivado() {
		return activado;
	}

	public void setActivado(boolean activado) {
		this.activado = activado;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
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

	// public List<ConceptoCaja> getConceptoCajas() {
	// return conceptoCajas;
	// }
	//
	// public void setConceptoCajas(List<ConceptoCaja> conceptoCajas) {
	// this.conceptoCajas = conceptoCajas;
	// }
	//
	// public ConceptoCaja getSelectedConceptoCaja() {
	// return selectedConceptoCaja;
	// }
	//
	// public void setSelectedConceptoCaja(ConceptoCaja selectedConceptoCaja) {
	// this.selectedConceptoCaja = selectedConceptoCaja;
	// }

}
