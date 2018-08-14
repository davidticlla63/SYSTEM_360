package com.erp360.controller;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.richfaces.cdi.push.Push;

import com.erp360.dao.DetalleOrdenIngresoDao;
import com.erp360.dao.OrdenIngresoDao;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.DetalleOrdenIngreso;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.OrdenIngreso;
import com.erp360.model.Producto;
import com.erp360.model.Proveedor;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.PageUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Named(value = "listOrdenIngresoController")
@ConversationScoped
public class ListOrdenIngresoController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private @Inject FacesContext facesContext;

	//DAO
	private @Inject OrdenIngresoDao ordenIngresaDao;
	private @Inject DetalleOrdenIngresoDao detalleOrdenIngresoDao;

	//STATE
	private boolean modificar ;
	private boolean verReport;
	private boolean cargadoDesdeOtraPagina;

	//VAR
	private String urlOrdenIngreso ;

	//OBJECT
	private OrdenIngreso selectedOrdenIngreso;

	//LIST
	private LazyDataModel<OrdenIngreso> ordenesIngreso;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Usuario usuarioSession;
	private Gestion gestionSesion;
	private Empresa empresaLogin;
	
	//VAR
	private int sizeList;
	private int sizePage;
	private int first;

	@PostConstruct
	public void init() {
		sessionMain.setAttributeSession("pIdOrdenIngreso", null);//clear atribute
		usuarioSession = sessionMain.getUsuarioLogin();
		gestionSesion = sessionMain.getGestionLogin();
		empresaLogin = sessionMain.getEmpresaLogin();
		loadDefault();
	}

	public void loadDefault(){
		cargarLazyDataModel();
		setVerReport(false);
		cargadoDesdeOtraPagina = false;
		selectedOrdenIngreso = new OrdenIngreso();
		modificar = false;
		//ordenesIngreso = ordenIngresaDao.findAllOrderedByIDGestion(gestionSesion);

		//si viene desde otra pagina
		Integer pIdOrdenIngreso = (Integer)FacesUtil.getSessionAttribute("pIdOrdenIngresoXKardex");
		if(pIdOrdenIngreso!=null){
			cargadoDesdeOtraPagina = true;
			FacesUtil.setSessionAttribute("pIdOrdenIngresoXKardex", null);
			selectedOrdenIngreso = ordenIngresaDao.findById(pIdOrdenIngreso);
			cargarReporte();
		}
	}
	
	//PROCESS
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void cargarLazyDataModel(){
			sizeList = ordenIngresaDao.countTotalRecord("orden_ingreso").intValue();
			ordenesIngreso = new LazyDataModel() {
				private static final long serialVersionUID = 3565223586960673287L;

				@Override
				public List<OrdenIngreso> load(int first, int pageSize,
						String sortField, SortOrder sortOrder, Map filters) {
					setFirst(first);
					setSizePage(pageSize);
					return ordenIngresaDao.obtenerPorTamanio(getFirst(),getSizePage(),filters);
				}

				@Override
				public OrdenIngreso getRowData(String rowKey) {
					List<OrdenIngreso> ordenesIngreso = (List<OrdenIngreso>) getWrappedData();
					Integer value = Integer.valueOf(rowKey);
					for (OrdenIngreso ordenIngreso : ordenesIngreso) {
						if (ordenIngreso.getId().equals(value)) {
							return ordenIngreso;
						}
					}
					return null;
				}
			};
			ordenesIngreso.setRowCount(getSizeList());
			ordenesIngreso.setPageSize(getSizePage());
		}

	public void initConversation() {
		if (!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient()) {
			conversation.begin();
		}
	}

	public String endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
		return "orden_ingreso.xhtml?faces-redirect=true";
	}

	// ------- action & event ------
	public void actionModificar(){
		sessionMain.setAttributeSession("pIdOrdenIngreso", String.valueOf(selectedOrdenIngreso.getId()));
		String page = "pages/proceso/orden_ingreso.xhtml";
		PageUtil.cargarPagina(page);
	}

	public void onRowSelect(SelectEvent event) {
		modificar = true;
		if(selectedOrdenIngreso.getEstado().equals("PR")){
			modificar = false;
		}
	}

	//PROCESO----------
	public void procesarOrdenIngreso(){
		Date fechaActual = new Date();
		// actualizar estado de orden ingreso
		selectedOrdenIngreso.setEstado("PR");
		selectedOrdenIngreso.setFechaAprobacion(fechaActual);
		//obtener listDetalleOrdenIngreso
		List<DetalleOrdenIngreso> listDetalleOrdenIngreso = detalleOrdenIngresoDao.obtenerPorOrdenIngreso(selectedOrdenIngreso);
		List<AlmacenProducto> listAlmacenProducto = new ArrayList<>();
		Proveedor proveedor = selectedOrdenIngreso.getProveedor();
		for(DetalleOrdenIngreso d: listDetalleOrdenIngreso){
			Producto prod = d.getProducto();
			AlmacenProducto almProd = new AlmacenProducto();
			almProd = new AlmacenProducto();
			almProd.setAlmacen(selectedOrdenIngreso.getAlmacen());
			almProd.setOrdenIngreso(selectedOrdenIngreso);
			almProd.setProducto(prod);
			almProd.setProveedor(proveedor);
			almProd.setStock(d.getCantidad());
			almProd.setStockMin(prod.getStockMin());
			almProd.setStockMax(prod.getStockMax());
			almProd.setPrecioCompra(d.getPrecioCompra());
			almProd.setPrecioVentaContado(d.getPrecioVentaContado());
			almProd.setPrecioVentaCredito(d.getPrecioVentaCredito());
			almProd.setEstado("AC");
			almProd.setFechaRegistro(fechaActual);
			almProd.setUsuarioRegistro(usuarioSession.getLogin());
			almProd.setGestion(gestionSesion);
			almProd.setFechaExpiracion(d.getFechaExpiracion());
			almProd.setNumeroLote(d.getNumeroLote());
			almProd.setUbicacionFisica(d.getUbicacionFisica());
			//precios
			almProd.setPrecioAlmacen(d.getPrecioAlmacen());
			almProd.setPrecio1(d.getPrecio1());
			almProd.setPrecio2(d.getPrecio2());
			almProd.setPrecio3(d.getPrecio3());
			almProd.setPrecio4(d.getPrecio4());
			almProd.setPrecio5(d.getPrecio5());
			almProd.setPrecio6(d.getPrecio6());
			listAlmacenProducto.add(almProd);
		}
		boolean sw = ordenIngresaDao.procesar(empresaLogin,"ORDEN INGRESO X "+selectedOrdenIngreso.getMotivoIngreso(),usuarioSession,selectedOrdenIngreso, listAlmacenProducto);
		if(sw){
			loadDefault();
		}
	}

	public void cargarReporte(){
		Map<String,String> map1 = new HashMap<>();
		map1.put("pRazonSocial", empresaLogin.getRazonSocial());
		map1.put("pDireccion", empresaLogin.getDireccion());
		map1.put("pTelefono", empresaLogin.getTelefono());
		map1.put("pIdEmpresa", String.valueOf(empresaLogin.getId()));
		map1.put("pUsuario", usuarioSession.getLogin());
		map1.put("pIdOrdenIngreso", String.valueOf(selectedOrdenIngreso.getId()));
		setUrlOrdenIngreso(buildUrl("ReporteOrdenIngreso",map1));
		setVerReport(true);
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


	// -------- get and set -------
	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public LazyDataModel<OrdenIngreso> getOrdenesIngreso() {
		return ordenesIngreso;
	}

	public void setOrdenesIngreso(LazyDataModel<OrdenIngreso> ordenesIngreso) {
		this.ordenesIngreso = ordenesIngreso;
	}

	public OrdenIngreso getSelectedOrdenIngreso() {
		return selectedOrdenIngreso;
	}

	public void setSelectedOrdenIngreso(OrdenIngreso selectedOrdenIngreso) {
		this.selectedOrdenIngreso = selectedOrdenIngreso;
	}

	public String getUrlOrdenIngreso() {
		return urlOrdenIngreso;
	}

	public void setUrlOrdenIngreso(String urlOrdenIngreso) {
		this.urlOrdenIngreso = urlOrdenIngreso;
	}

	public boolean isVerReport() {
		return verReport;
	}

	public void setVerReport(boolean verReport) {
		this.verReport = verReport;
	}

	public boolean isCargadoDesdeOtraPagina() {
		return cargadoDesdeOtraPagina;
	}

	public void setCargadoDesdeOtraPagina(boolean cargadoDesdeOtraPagina) {
		this.cargadoDesdeOtraPagina = cargadoDesdeOtraPagina;
	}

	public int getSizeList() {
		return sizeList;
	}

	public void setSizeList(int sizeList) {
		this.sizeList = sizeList;
	}

	public int getSizePage() {
		return sizePage;
	}

	public void setSizePage(int sizePage) {
		this.sizePage = sizePage;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

}
