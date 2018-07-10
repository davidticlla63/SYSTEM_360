/**
 * @author WILSON
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
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.SelectEvent;

import com.erp360.interfaces.ICajaMovimientoDao;
import com.erp360.interfaces.ICajaSesionDao;
import com.erp360.model.CajaSesion;
import com.erp360.model.Empresa;
import com.erp360.model.Usuario;
import com.erp360.util.CajaIngreso;
import com.erp360.util.EDFlujoCaja;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;






/**
 * @author WILSON
 *
 */
@ManagedBean(name="cajaCierreController")
@ViewScoped
public class CajaCierreController {
	private static final long serialVersionUID = 1L;
	private @Inject FacesContext facesContext;
	//DAO

	private @Inject ICajaSesionDao cajaSesionDao;
	private @Inject ICajaMovimientoDao cajaMovimientoDao;
	private @Inject SessionMain sessionDao;

	//OBJECT 
	private CajaSesion cajaSesion;
	
	private Empresa empresaSeleccionada;
	

	private String numeroOrden;
	private Usuario usuario;

	//VAR
	private String currentPage = "/pages/caja/cierre/list.xhtml";

	//LIST
	private List<CajaSesion> listaCierreCaja;
	private List<CajaIngreso> listaIngresos;
	private List<CajaIngreso> listaEgresos;
	private List<EDFlujoCaja> listaFlujoCaja;


	//STATE
	private boolean nuevo;
	private boolean registrar;
	private boolean seleccionado;
	private String mensaje;
	private boolean anulacion;
	private boolean procesar;
	private String idTablaProductos;
	public  Date fechaCierre;
	private Double totalIngresos=0.0;
	private Double totalEgresos=0.0;
	private Double totales=0.0;
	
	private String urlReport;
	private boolean ver;

	@PostConstruct
	public void init() {
		loadDefault();
	}

	public void loadDefault(){	
		nuevo = true;
		ver= false;
		seleccionado = false;
		registrar = false;
		cajaSesion=new CajaSesion();
		listaCierreCaja=cajaSesionDao.listaPorCompaniaUSuarioYActivos(sessionDao.getEmpresaLogin(), sessionDao.getUsuarioLogin());		
	
	}

	//ACTION
	public void actionCancelar(){
		nuevo = true;
		seleccionado = false;
		registrar = false;
		loadDefault();
		currentPage = "/pages/caja/cierre/list.xhtml";
	}
	public void actionCerrarCaja(){

		if (sessionDao.getCajaSesion()!=null) {
			nuevo = false;
			registrar = true;
			cajaSesion=sessionDao.getCajaSesion();
			usuario=sessionDao.getUsuarioLogin();
			cajaSesion.setMontoFinal(new Double(0));
			fechaCierre=new Date();
			listaIngresos=cajaMovimientoDao.listarPorSesionEIngresosYModoDePago(cajaSesion);
			listaEgresos=cajaMovimientoDao.listarPorSesionEgresosYModoDePago(cajaSesion);
			totalIngresos=0.0;
			for (CajaIngreso ingresos : listaIngresos) {
				totalIngresos=totalIngresos+ingresos.getSuma();
			}
			totalEgresos=0.0;
			for (CajaIngreso egresos: listaEgresos) {
				totalEgresos=totalEgresos+egresos.getSuma();
			}
//			totales=(cajaSesion.getMontoInicial()+totalIngresos)-totalEgresos;
			totales=totalIngresos-totalEgresos;
			cajaSesion.setMontoFinal(new Double(totales));
			System.out.println("EL TOTAL ES "+totales);
			currentPage = "/pages/caja/cierre/edit.xhtml";

		} else {
			this.mensaje="NO TIENE ABIERTA NINGUNA CAJA";
			FacesUtil.updateComponent("form002");
			FacesUtil.showDialog("dlg2");
		}
	}


	public void eventoDobleClick(SelectEvent event){
		nuevo = false;
		registrar = true;
		listaFlujoCaja=new ArrayList<>();
//		for (VentaNotaVenta notaVenta : cajaSesion.getListaVentas()){
//		EDFlujoCaja fc=new EDFlujoCaja();
//		
//		System.out.println("Entro en evento"+notaVenta.getId()+notaVenta.getCodigo());
//		fc.setId(notaVenta.getId());
//		fc.setConcepto("Examenes de Laboratorio");
//		fc.setNumeroDocumento(notaVenta.getCodigo());
//		fc.setMonto(notaVenta.getPrecioTotalBs());
//		fc.setTipoMovimiento("Ingreso");
//		fc.setTipoPago("Efectivo");	
//		listaFlujoCaja.add(fc);
//		
//		}
		currentPage = "/pages/caja/cierre/flujo_caja.xhtml";
	}

	
	public void eventoOneClick(SelectEvent event){
		
	
		System.out.println("ENTRO EN 1 CLICK");
	}
	/*CRUD NOTA DE VENTA*/

	public void actualizarDiferencia(AjaxBehaviorEvent event){
		
		cajaSesion.setDiferencia(cajaSesion.getMontoFinal()-totales);
	}
	
	public void cerrarCaja(){
		System.out.println("Ingreso  a cerrarCaja");
		cajaSesion.setFechaCierre(new Date());
		//cajaSesion.setMontoFinal(200.00);
		cajaSesion.setProcesada(true);
		
		
		boolean co=false;
		co= cajaSesionDao.modificar(cajaSesion);
		if (co) {
			sessionDao.setCajaSesion(null);
			reporteCierre();
		       FacesUtil.updateComponent("formReporte");
		        FacesUtil.showDialog("dlgReporte");
			loadDefault();				
//			currentPage = "/pages/caja/cierre/list.xhtml";
		} else {
			FacesUtil.infoMessage("Informacion", "El detalle no debe estar vacio");
		}

	}
	public void modificar(){

		if (true) {

			if(true){
				loadDefault();

			}
		} else {
			FacesUtil.infoMessage("Informacion", "El detalle no debe estar vacio");
		}

	}
	/*DETALLE NOTA DE VENTA*/
	
	
	public void reporteCierre(){
		//cajaSesion=cajaSesionDao.RetornarPorCaja(sessionDao.getCaja());
		
		//if (cajaSesion.getListaVentas().size()>0) {
			//seleccionado=true;
			//registrar=false;
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("listaVentas",cajaSesion.getListaCajaMovimientos());
				
				map.put("usuario", sessionDao.getUsuarioLogin().getLogin());
				map.put("pRazonSocial", sessionDao.getEmpresaLogin().getRazonSocial());
				map.put("pDireccion", sessionDao.getEmpresaLogin().getDireccion());
				map.put("pTelefono", sessionDao.getEmpresaLogin().getTelefono());	
				String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/report/caja/movimientos/cierre.jasper");
				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();       
				
				request.getSession().setAttribute("map", map);
				request.getSession().setAttribute("ruta", reportPath);
				//request.getSession().setAttribute("lista",cajaSesion.getListaVentas() );
				

			} catch (Exception e) {
				System.out.println("Fallo en "+e.toString());
			}
			//resetearValores();
			currentPage = "/pages/inventario/reporte/reporte.xhtml";
	//	}else{

		//	FacesUtil.showDialog("dlg2"); 
		//}
	}


	public void visualizarReporte(CajaSesion cajaSesion){
		this.cajaSesion=cajaSesion;
		verReporte();
		
	}

	
	public void verReporte() {
		try {
			setVer(true);
			registrar=true;
			System.out.println("Ingreso a verReporte");
			HttpServletRequest request = (HttpServletRequest) facesContext
					.getCurrentInstance().getExternalContext().getRequest();
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length()
					- request.getRequestURI().length())
					+ request.getContextPath() + "/";

//			String URL_SERVLET_LOGO = urlPath + "ServletImageLogo?id="
//					+ sessionMain.getEmpresaLogin().getId() + "&type=EMPRESA";
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID", cajaSesion.getListaCajaMovimientos().get(0).getId());
			map.put("USUARIO", sessionDao.getUsuarioLogin().getLogin());
//			map.put("pais", notaVenta.getSucursal().getCiudad().getPais().getNombre());
//			map.put("logo", URL_SERVLET_LOGO);
			map.put("REPORT_LOCALE", new Locale("en", "US"));
			
			String reportPath = urlPath +
							"resources/report/caja/movimientos/reportReciboCaja.jasper";		
			
			request.getSession().setAttribute("parameter", map);
			request.getSession().setAttribute("path", reportPath);
			setUrlReport(urlPath + "ReportPdfServlet");
			currentPage = "/pages/caja/apertura/report.xhtml";
//			FacesUtil.updateComponent("formReporte");
//			FacesUtil.showDialog("dlgrReporte");
		} catch (Exception e) {
			System.out.println("Fallo en " + e.toString());
		}

	}




	/*SETTERS AND GETTERS*/


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

	public boolean isProcesar() {
		return procesar;
	}

	public void setProcesar(boolean procesar) {
		this.procesar = procesar;
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







	public boolean isRegistrar() {
		return registrar;
	}

	public void setRegistrar(boolean registrar) {
		this.registrar = registrar;
	}

	public List<CajaSesion> getListaCierreCaja() {
		return listaCierreCaja;
	}

	public void setListaCierreCaja(List<CajaSesion> listaCierreCaja) {
		this.listaCierreCaja = listaCierreCaja;
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

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	
	public List<CajaIngreso> getListaIngresos() {
		return listaIngresos;
	}

	public void setListaIngresos(List<CajaIngreso> listaIngresos) {
		this.listaIngresos = listaIngresos;
	}

	public List<CajaIngreso> getListaEgresos() {
		return listaEgresos;
	}

	public void setListaEgresos(List<CajaIngreso> listaEgresos) {
		this.listaEgresos = listaEgresos;
	}

	public Double getTotalIngresos() {
		totalIngresos=0.0;
		for (CajaIngreso ingresos : listaIngresos) {
			totalIngresos=totalIngresos+ingresos.getSuma();
		}
		
		return totalIngresos;
	}

	public void setTotalIngresos(Double totalIngresos) {
		this.totalIngresos = totalIngresos;
	}

	public Double getTotalEgresos() {
		totalEgresos=0.0;
		for (CajaIngreso ingresos : listaEgresos) {
			totalEgresos=totalEgresos+ingresos.getSuma();
		}
		
		return totalEgresos;
	}

	public void setTotalEgresos(Double totalEgresos) {
		this.totalEgresos = totalEgresos;
	}

	public Double getTotales() {
		return totales;
	}

	public void setTotales(Double totales) {
		this.totales = totales;
	}

	public List<EDFlujoCaja> getListaFlujoCaja() {
		return listaFlujoCaja;
	}

	public void setListaFlujoCaja(List<EDFlujoCaja> listaFlujoCaja) {
		this.listaFlujoCaja = listaFlujoCaja;
	}

	public CajaSesion getCajaSesion() {
		return cajaSesion;
	}

	public void setCajaSesion(CajaSesion cajaSesion) {
		this.cajaSesion = cajaSesion;
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
