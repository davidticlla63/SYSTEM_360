package com.erp360.controller;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.SelectEvent;
import org.richfaces.cdi.push.Push;

import com.erp360.dao.AlmacenDao;
import com.erp360.dao.GestionDao;
import com.erp360.dao.KardexProductoDao;
import com.erp360.dao.ProductoDao;
import com.erp360.model.Almacen;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.KardexProducto;
import com.erp360.model.Producto;
import com.erp360.util.DateUtility;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;
/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Named(value = "kardexProductoController")
@ConversationScoped
public class KardexProductoController implements Serializable {

	private static final long serialVersionUID = 2039368857381714460L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	private FacesContext facesContext;

	@Inject
	Conversation conversation;

	private @Inject GestionDao gestionDao;
	private @Inject ProductoDao productoDao;
	private @Inject AlmacenDao almacenDao;
	private @Inject KardexProductoDao kardexProductoDao;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	private boolean verReport = false;
	private Date fechaInicio;
	private Date fechaFin;

	//OBJECT
	private Producto selectedProducto;
	private Gestion selectedGestion;
	private Almacen selectedAlmacen;

	//LIST
	private List<KardexProducto> listaKardexProducto = new ArrayList<KardexProducto>();
	private List<Producto> listaProducto = new ArrayList<Producto>();
	private List<Gestion> listGestion = new ArrayList<Gestion>();
	private List<Almacen> listAlmacen = new ArrayList<Almacen>();

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Gestion gestionLogin;

	private String urlKardexProducto = "";
	private String usuarioSession;
	private Empresa empresaLogin;

	@PostConstruct
	public void init() {

		System.out.println("... initNewKardexProducto ...");
		gestionLogin = sessionMain.getGestionLogin();
		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		empresaLogin = sessionMain.getEmpresaLogin();

		fechaInicio = DateUtility.getPrimerDiaDelMes();
		fechaFin = DateUtility.getUltimoDiaDelMes();

		loadDefault();
	}

	public void loadDefault(){
		verReport = false;
		selectedProducto = new Producto();
		selectedProducto.setNombre("TODOS");
		selectedAlmacen = new Almacen();
		listGestion = gestionDao.findAllByEmpresa(empresaLogin);
		selectedGestion = listGestion.get(0);
		listAlmacen = almacenDao.obtenerTodosActivosOrdenadosPorId();
		listaKardexProducto = kardexProductoDao.obtenerPorFecha(fechaInicio, fechaFin);
	}

	public void initConversation() {
		if (!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient()) {
			conversation.begin();
			System.out.println(">>>>>>>>>> CONVERSACION INICIADA...");
		}
	}

	public String endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
			System.out.println(">>>>>>>>>> CONVERSACION TERMINADA...");
		}
		return "kardex_producto.xhtml?faces-redirect=true";
	}

	public void cargarOrdenIngreso(Integer codigoOrdenIngreso){
		try{
			FacesUtil.setSessionAttribute("pIdOrdenIngresoXKardex", codigoOrdenIngreso);
			FacesUtil.redirect("/pages/proceso/list-orden-ingreso.xhtml");
		}catch(Exception e){

		}
	}

	public void cargarOrdenSalida(Integer codigoOrdenSalida){
		try{
			FacesUtil.setSessionAttribute("pIdOrdenSalida", codigoOrdenSalida);
			FacesUtil.redirect("/pages/proceso/list-orden-salida.xhtml");
		}catch(Exception e){

		}
	}

	public List<Producto> completeProducto(String query) {
		String upperQuery = query.toUpperCase();
		listaProducto = productoDao.findAllProductoForQueryNombre(upperQuery);
		return listaProducto;
	}

	public void onRowSelectProductoClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Producto i : listaProducto){
			if(i.getNombre().equals(nombre)){
				this.selectedProducto = i;
				return;
			}
		}
	}

	public void procesarConsulta(){
		int valueDate = DateUtility.diferenciasDeFechas(fechaInicio, fechaFin); //verificar el orden de las fechas
		if(valueDate<0){
			FacesUtil.infoMessage("Fechas Incorrectas.","");
			return ;
		}
		listaKardexProducto = kardexProductoDao.obtenerPorFecha(fechaInicio, fechaFin);
		if(listaKardexProducto.size()==0){
			FacesUtil.infoMessage("VALIDACION", "No se encontraron Registros.");
		}
	}

	public void cargarReporte(){
		try {
			urlKardexProducto = loadURL();
			//RequestContext context = RequestContext.getCurrentInstance();
			//context.execute("PF('dlgVistaPreviaOrdenIngreso').show();");
			verReport = true;

			//initNewOrdenIngreso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public String loadURL(){
		try{
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String pIdAlmacen = String.valueOf(selectedAlmacen.getId());
			if(selectedAlmacen.getNombre().isEmpty()){
				pIdAlmacen = "0";
			}
			//URLEncoder.encode(q, "UTF-8") ; ISO-8859-1

			String urlPDFreporte = urlPath+"ReporteKardexProducto?pIdProducto="+selectedProducto.getId()+"&pIdGestion="+selectedGestion.getId()+"&pUsuario="+URLEncoder.encode(usuarioSession,"ISO-8859-1")+"&pNitEmpresa="+empresaLogin.getNit()+"&pNombreEmpresa="+URLEncoder.encode(empresaLogin.getRazonSocial(),"ISO-8859-1")+"&pIdAlmacen="+pIdAlmacen;
			System.out.println(">>>>>>>>>>   urlPDFreporte = "+urlPDFreporte);
			return urlPDFreporte;
		}catch(Exception e){
			return "error";
		}
	}

	// -------- get and set -------
	public List<KardexProducto> getListaKardexProducto() {
		return listaKardexProducto;
	}

	public void setListaKardexProducto(List<KardexProducto> listaKardexProducto) {
		this.listaKardexProducto = listaKardexProducto;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
	}

	public List<Producto> getListaProducto() {
		return listaProducto;
	}

	public void setListaProducto(List<Producto> listaProducto) {
		this.listaProducto = listaProducto;
	}

	public List<Gestion> getListGestion() {
		return listGestion;
	}

	public void setListGestion(List<Gestion> listGestion) {
		this.listGestion = listGestion;
	}

	public Gestion getSelectedGestion() {
		return selectedGestion;
	}

	public void setSelectedGestion(Gestion selectedGestion) {
		this.selectedGestion = selectedGestion;
	}

	public boolean isVerReport() {
		return verReport;
	}

	public void setVerReport(boolean verReport) {
		this.verReport = verReport;
	}

	public String getUrlKardexProducto() {
		return urlKardexProducto;
	}

	public void setUrlKardexProducto(String urlKardexProducto) {
		this.urlKardexProducto = urlKardexProducto;
	}

	public Almacen getSelectedAlmacen() {
		return selectedAlmacen;
	}

	public void setSelectedAlmacen(Almacen selectedAlmacen) {
		this.selectedAlmacen = selectedAlmacen;
	}

	public List<Almacen> getListAlmacen() {
		return listAlmacen;
	}

	public void setListAlmacen(List<Almacen> listAlmacen) {
		this.listAlmacen = listAlmacen;
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
}
