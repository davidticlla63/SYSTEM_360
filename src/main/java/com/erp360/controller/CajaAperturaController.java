/**
banco * @author WILSON
 */
package com.erp360.controller;

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

import org.primefaces.event.SelectEvent;

import com.erp360.enums.TipoMovimiento;
import com.erp360.enums.TipoPago;
import com.erp360.interfaces.ICajaDao;
import com.erp360.interfaces.ICajaMovimientoDao;
import com.erp360.interfaces.ICajaSesionDao;
import com.erp360.model.Caja;
import com.erp360.model.CajaMovimiento;
import com.erp360.model.CajaSesion;
import com.erp360.model.Empresa;
import com.erp360.model.MovimientoCaja;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.FacturacionUtil;
import com.erp360.util.SessionMain;
import com.erp360.util.Time;

/**
 * @author david
 *
 */
@ManagedBean(name = "cajaOperacdionController")
@ViewScoped
public class CajaAperturaController {
	private static final long serialVersionUID = 1L;
	private @Inject FacesContext facesContext;

	// DAO

	private @Inject ICajaSesionDao cajaSesionDao;
	private @Inject ICajaDao cajaDao;
	private @Inject SessionMain sessionDao;
	private @Inject ICajaMovimientoDao cajaMovimientoDao;
	// OBJECT
	private CajaSesion cajaSesion;

	private Empresa empresaSeleccionada;
	private String numeroOrden;
	private Usuario usuario;

	// VAR
	private String currentPage = "/pages/caja/apertura/list.xhtml";

	// LIST
	private List<CajaSesion> listaAperturaCaja;
	private List<Caja> listaCaja;
	private List<CajaMovimiento> movimientos = new ArrayList<CajaMovimiento>();
	private List<CajaMovimiento> cajaMovimientoSinComprobantes = new ArrayList<>();

	public static List<Caja> cajas;

	// STATE
	private boolean nuevo;
	private boolean registrar;
	private boolean seleccionado;
	private String mensaje;
	private boolean anulacion;
	private boolean ver;
	private String urlReport;

	private String idTablaProductos;
	private Integer idMovimiento;

	private Date fechaInicio = Time.getPrimerDiaDelMes(new Date());
	private Date fechaFin = Time.getUltimoDiaDelMes(new Date());

	private double totalNacional;
	private String glosa;
	private String razonSocial;

	@PostConstruct
	public void init() {
		loadDefault();
	}

	public void loadDefault() {
		ver = false;
		nuevo = true;
		seleccionado = false;
		registrar = false;
		totalNacional = 0;
		listaAperturaCaja = cajaSesionDao.listaPorCompaniaUSuarioYActivos(
				sessionDao.getEmpresaLogin(), sessionDao.getUsuarioLogin());
		listaCaja = new ArrayList<Caja>();
		cajaMovimientoSinComprobantes = new ArrayList<CajaMovimiento>();
	}

	public void consulta() {
		movimientos = cajaMovimientoDao.obtenerPorSessionCajaEntreFechas(
				cajaSesion, fechaInicio, fechaFin);
		cajaSesion.setListaCajaMovimientos(movimientos);
	}

	// public void registrarComprobante(){
	// try {
	// comprobante=accountingCreateVoucher.prepareNewEgresoVoucherByCajaMovimiento(cajaMovimientoSinComprobantes,
	// glosa, totalNacional,
	// totalNacional/sessionDao.getTipoCambioActual().getUnidad(),
	// accountingCreateVoucher.getSalerParameterByEmpresa(sessionDao.getEmpresaLogin()).getCuentaCaja());
	// Comprobante co=comprobanteDao.registrar(comprobante);
	// if (co != null) {
	// loadDefault();
	// } else {
	// FacesUtil.infoMessage("Informacion", "ERROR AL INSERTAR");
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// public void verMovimientosSinComprobantes(CajaSesion cajaSesion) {
	// try {
	// this.cajaSesion=cajaSesion;
	// setVer(true);
	// this.registrar=true;
	// this.glosa="";
	// this.razonSocial="";
	// this.listPlanCuenta=sessionContable.getListPlanCuentaAuxiliar();
	// this.cajaMovimientoSinComprobantes=
	// cajaMovimientoDao.obtenerMovimientosSinComprobantes(cajaSesion);
	// this.totalNacional=0;
	// for (CajaMovimiento cajaMovimiento : cajaMovimientoSinComprobantes) {
	// if (cajaMovimiento.getPlanCuenta()==null) {
	// cajaMovimiento.setPlanCuenta(new PlanCuenta());
	// }
	// totalNacional=totalNacional+cajaMovimiento.getMonto();
	// }
	// currentPage = "/pages/caja/apertura/list_sin_comprobantes.xhtml";
	// } catch (Exception e) {
	// System.out.println("Fallo en " + e.toString());
	// }
	//
	// }
	//
	// // ONCOMPLETETEXT CUENTA
	// public List<PlanCuenta> completeCuentaIngreso(String query) {
	// listPlanCuenta = new ArrayList<>();
	// System.out.println("Plan Cuenta : "+query);
	// // String planCuenta=query.split(":")[0];
	// // idMovimiento=new Integer(query.split(":")[1]);
	// for(PlanCuenta i : sessionContable.getListPlanCuentaAuxiliar()) {
	// if(i.getDescripcion().toUpperCase().startsWith(query.toUpperCase())){
	// listPlanCuenta.add(i);
	// }
	// }
	// return listPlanCuenta;
	// }

	// public void onRowSelectCuentaClick1(SelectEvent event) {
	// Integer idPlanCuenta = ((PlanCuenta)event.getObject()).getId();
	// for (PlanCuenta i : listPlanCuenta) {
	// if (i.getId().equals(idPlanCuenta)) {
	// for (CajaMovimiento cajaMovimiento : cajaMovimientoSinComprobantes) {
	// if (cajaMovimiento.getId().equals(idMovimiento)) {
	// cajaMovimiento.setPlanCuenta(i);
	// return;
	// }
	// }
	// return ;
	// }
	// }
	// }

	public void verReporteResumido() {
		try {
			setVer(true);
			registrar = true;
			System.out.println("Ingreso a verReporteResumido");
			HttpServletRequest request = (HttpServletRequest) facesContext
					.getCurrentInstance().getExternalContext().getRequest();
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length()
					- request.getRequestURI().length())
					+ request.getContextPath() + "/";

			// String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
			// + sessionDao.getEmpresaLogin().getId() + "&type=EMPRESA";

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID_SESION", cajaSesion.getId());
			map.put("USUARIO", sessionDao.getUsuarioLogin().getLogin());
			map.put("FECHA_INICIO",
					new Integer(Time.obtenerFormatoYYYYMMDD(fechaInicio)));
			map.put("FECHA_FIN",
					new Integer(Time.obtenerFormatoYYYYMMDD(fechaFin)));
			map.put("FECHA_INICIO_TEXT",
					Time.convertSimpleDateToString(fechaInicio));
			map.put("FECHA_FIN_TEXT", Time.convertSimpleDateToString(fechaFin));
			// map.put("pais",
			// notaVenta.getSucursal().getCiudad().getPais().getNombre());
			// map.put("logo", URL_SERVLET_LOGO);
			map.put("REPORT_LOCALE", new Locale("en", "US"));

			String reportPath = urlPath
					+ "resources/report/caja/movimientos/reportCajaMovimientoSession.jasper";

			request.getSession().setAttribute("parameter", map);
			request.getSession().setAttribute("path", reportPath);
			setUrlReport(urlPath + "ReportPdfServlet");
			currentPage = "/pages/caja/apertura/report.xhtml";
			// FacesUtil.updateComponent("formReporte");
			// FacesUtil.showDialog("dlgrReporte");
		} catch (Exception e) {
			System.out.println("Fallo en " + e.toString());
		}

	}

	public void verReporteDetallado() {
		try {
			setVer(true);
			registrar = true;
			System.out.println("Ingreso a verReporteDetallado");
			HttpServletRequest request = (HttpServletRequest) facesContext
					.getCurrentInstance().getExternalContext().getRequest();
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length()
					- request.getRequestURI().length())
					+ request.getContextPath() + "/";

			String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
					+ sessionDao.getEmpresaLogin().getId() + "&type=EMPRESA";

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID_SESION", cajaSesion.getId());
			map.put("USUARIO", sessionDao.getUsuarioLogin().getLogin());
			map.put("FECHA_INICIO",
					new Integer(Time.obtenerFormatoYYYYMMDD(fechaInicio)));
			map.put("FECHA_FIN",
					new Integer(Time.obtenerFormatoYYYYMMDD(fechaFin)));
			map.put("FECHA_INICIO_TEXT",
					Time.convertSimpleDateToString(fechaInicio));
			map.put("FECHA_FIN_TEXT", Time.convertSimpleDateToString(fechaFin));
			// map.put("pais",
			// notaVenta.getSucursal().getCiudad().getPais().getNombre());
			map.put("logo", URL_SERVLET_LOGO);
			map.put("REPORT_LOCALE", new Locale("en", "US"));

			String reportPath = urlPath
					+ "resources/report/caja/movimientos/reportCajaMovimientoDetalladoSession.jasper";

			request.getSession().setAttribute("parameter", map);
			request.getSession().setAttribute("path", reportPath);
			setUrlReport(urlPath + "ReportPdfServlet");
			currentPage = "/pages/caja/apertura/report.xhtml";
			// FacesUtil.updateComponent("formReporte");
			// FacesUtil.showDialog("dlgrReporte");
		} catch (Exception e) {
			System.out.println("Fallo en " + e.toString());
		}

	}

	public void visualizarReporte(CajaSesion cajaSesion) {
		this.cajaSesion = cajaSesion;
		verReporte();

	}

	public void verReporte() {
		try {
			setVer(true);
			registrar = true;
			System.out.println("Ingreso a verReporte");
			HttpServletRequest request = (HttpServletRequest) facesContext
					.getCurrentInstance().getExternalContext().getRequest();
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length()
					- request.getRequestURI().length())
					+ request.getContextPath() + "/";

			// String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
			// + sessionMain.getEmpresaLogin().getId() + "&type=EMPRESA";

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID", cajaSesion.getListaCajaMovimientos().get(0).getId());
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
			currentPage = "/pages/caja/apertura/report.xhtml";
			// FacesUtil.updateComponent("formReporte");
			// FacesUtil.showDialog("dlgrReporte");
		} catch (Exception e) {
			System.out.println("Fallo en " + e.toString());
		}

	}

	// ACTION

	public void actionCancelar() {
		nuevo = true;
		seleccionado = false;
		registrar = false;
		loadDefault();
		currentPage = "/pages/caja/apertura/list.xhtml";
	}

	public void actionAperturarCaja() {

		if (sessionDao.getCajaSesion() == null) {
			nuevo = false;
			// seleccionado = false;
			registrar = true;
			cajaSesion = new CajaSesion();
			usuario = sessionDao.getUsuarioLogin();
			
			currentPage = "/pages/caja/apertura/edit.xhtml";
		} else {
			this.mensaje = "CAJA "
					+ sessionDao.getCajaSesion().getCaja().getNombre()
					+ " ABIERTA";
			FacesUtil.updateComponent("form002");
			FacesUtil.showDialog("dlg2");
		}
	}

	/* CRUD NOTA DE VENTA */

	public void procesar() {

		if (!cajaDao.esCajero(usuario, cajaSesion.getCaja())) {
			FacesUtil.infoMessage("Informacion", usuario.getNombre()+ " no es cajero de : "+cajaSesion.getCaja().getNombre());
			return;
		}
		cajaSesion.setEstado("AC");
		cajaSesion.setFechaApertura(new Date());
		cajaSesion.setUsuarioRegistro(sessionDao.getUsuarioLogin().getNombre());
		cajaSesion.setUsuario(sessionDao.getUsuarioLogin());
		cajaSesion.setFechaRegistro(new Date());
		cajaSesion.setProcesada(false);
		CajaMovimiento movimientoCaja= new CajaMovimiento();
		
		
		movimientoCaja.setCajaSesion(cajaSesion);
		movimientoCaja.setMonto(cajaSesion.getMontoInicial());
		movimientoCaja.setMontoExtranjero(cajaSesion.getMontoInicial()/sessionDao.getTipoCambio().getUnidad());
		movimientoCaja.setDescripcion("Apertura Caja :"+cajaSesion.getCaja().getNombre());
		movimientoCaja.setTipo("I");
		movimientoCaja.setTipoMovimiento(TipoMovimiento.APE);
		movimientoCaja.setTipoPago(TipoPago.EFE);
		movimientoCaja.setTipoCambio(sessionDao.getTipoCambio().getUnidad());
		movimientoCaja.setFechaModificacion(new Date());
		movimientoCaja.setFechaRegistro(new Date());
		movimientoCaja.setSucursal(sessionDao.getSucursalLogin());
		movimientoCaja.setMontoLiteral(FacturacionUtil.obtenerMontoLiteral(movimientoCaja.getMonto()));
		movimientoCaja.setRazonSocial(sessionDao.getUsuarioLogin().getNombre());
//		movimientoCaja.setSaldoExtranjero(movimientoCaja.getMontoExtranjero());
//		movimientoCaja.setSaldoNacional(movimientoCaja.getMonto());
		cajaSesion.getListaCajaMovimientos().add(movimientoCaja);
		CajaSesion co = cajaSesionDao.registrar(cajaSesion);

		if (co != null) {
			verReporte();
			sessionDao.setCajaSesion(co);
			loadDefault();
			// currentPage = "/pages/caja/apertura/list.xhtml";
		} else {
			FacesUtil.infoMessage("Informacion", "ERROR AL INSERTAR");
		}
	}

	public void modificar() {
	}

	public void mostrarMovimietos(CajaSesion cajaSesion) {
		this.cajaSesion = cajaSesion;
		seleccionado = true;
		nuevo = false;
		currentPage = "/pages/caja/apertura/list_movimiento.xhtml";
	}

	/* DETALLE NOTA DE VENTA */
	public void cancelarOrdenLab() {

		numeroOrden = "";
	}

	/* DETALLE NOTA DE VENTA */

	/* METODOS ONCOMPLETE */

	public List<Caja> onCompleteCaja(String query) {
		// ystem.out.println("Entro en Oncomplete Caja"+ query);
		cajas = cajaDao.obtenerPorEmpresa(query.toUpperCase(),
				sessionDao.getEmpresaLogin());
		return cajas;
	}

	// ACTION

	public void onSelectCaja(SelectEvent event) {
		cajaSesion.setCaja((Caja) event.getObject());
		if (!cajaDao.esCajero(usuario, cajaSesion.getCaja())) {
			FacesUtil.infoMessage("Informacion", usuario.getNombre()+ " no es cajero de : "+cajaSesion.getCaja().getNombre());
		}

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

	public boolean isAnulacion() {
		return anulacion;
	}

	public void setAnulacion(boolean anulacion) {
		this.anulacion = anulacion;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Empresa getEmpresaSeleccionada() {
		return empresaSeleccionada;
	}

	public void setEmpresaSeleccionada(Empresa empresaSeleccionada) {
		this.empresaSeleccionada = empresaSeleccionada;
	}

	public String getIdTablaProductos() {
		return idTablaProductos;
	}

	public void setIdTablaProductos(String idTablaProductos) {
		this.idTablaProductos = idTablaProductos;
	}

	public String getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(String numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
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

	public List<CajaSesion> getListaAperturaCaja() {
		return listaAperturaCaja;
	}

	public void setListaAperturaCaja(List<CajaSesion> listaAperturaCaja) {
		this.listaAperturaCaja = listaAperturaCaja;
	}

	public List<Caja> getListaCaja() {
		return listaCaja;
	}

	public void setListaCaja(List<Caja> listaCaja) {
		this.listaCaja = listaCaja;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public List<CajaMovimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<CajaMovimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public boolean isVer() {
		return ver;
	}

	public void setVer(boolean ver) {
		this.ver = ver;
	}

	public String getUrlReport() {
		return urlReport;
	}

	public void setUrlReport(String urlReport) {
		this.urlReport = urlReport;
	}

	public List<CajaMovimiento> getCajaMovimientoSinComprobantes() {
		return cajaMovimientoSinComprobantes;
	}

	public void setCajaMovimientoSinComprobantes(
			List<CajaMovimiento> cajaMovimientoSinComprobantes) {
		this.cajaMovimientoSinComprobantes = cajaMovimientoSinComprobantes;
	}

	public Integer getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(Integer idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public double getTotalNacional() {
		return totalNacional;
	}

	public void setTotalNacional(double totalNacional) {
		this.totalNacional = totalNacional;
	}

	public String getGlosa() {
		return glosa;
	}

	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

}
