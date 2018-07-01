package com.erp360.controller;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import org.richfaces.cdi.push.Push;

import com.erp360.dao.ClienteDao;
import com.erp360.dao.NotaVentaDao;
import com.erp360.model.Cliente;
import com.erp360.model.Empresa;
import com.erp360.model.NotaVenta;
import com.erp360.model.PlanCobranza;
import com.erp360.model.Usuario;
import com.erp360.util.EDEstadoCuentaCliente;
import com.erp360.util.FacesUtil;
import com.erp360.util.NumberUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@Named(value = "estadoCuentaClienteController")
@ConversationScoped
public class EstadoCuentaClienteController implements Serializable {

	private static final long serialVersionUID = -1012155707040198783L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	
	@Inject Conversation conversation;
	private @Inject FacesContext facesContext;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject NotaVentaDao notaVentaDao;
	private @Inject ClienteDao clienteDao;

	//STATE
	private boolean modificar = false;
	private boolean registrar = false;
	private boolean crear = true;
	private boolean verReporte;
	
	//OBJECT
	private EDEstadoCuentaCliente selectedEstadoCuenta;
	private Cliente selectedCliente;

	//LIST
	private List<EDEstadoCuentaCliente> listEstadoCuentaCliente;
	private List<Cliente> listCliente ;

	//VAR
	private String urlReporte;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Usuario usuarioLogin;
	private Empresa empresaLogin;

	@PostConstruct
	public void init() {

		System.out.println(" ... init New ...");

		usuarioLogin = sessionMain.getUsuarioLogin();
		empresaLogin = sessionMain.getEmpresaLogin();
		loadDefault();
	}

	public void loadDefault(){
		modificar = false;
		registrar = false;
		crear = true;
		verReporte = false;
		
		urlReporte = "";

		selectedCliente = new Cliente();
		//load default cliente todo
		selectedCliente.setNombres("TODOS");
		listCliente = new ArrayList<>();
		selectedEstadoCuenta = new EDEstadoCuentaCliente();

		cargarEstadoCuenta();
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
		return "page.xhtml?faces-redirect=true";
	}

	// ------- action & event ------

	private void cargarEstadoCuenta(){
		List<NotaVenta> listNotaVenta = new ArrayList<>();
		if(selectedCliente.getId().intValue() == 0){//TODOS
			listNotaVenta = notaVentaDao.obtenerTodosOrdenadosPorId();
		}else{
			listNotaVenta = notaVentaDao.obtenerPorCliente(selectedCliente);
		}
		listEstadoCuentaCliente = new ArrayList<>();
		for(NotaVenta nv: listNotaVenta){
			EDEstadoCuentaCliente ecc = new EDEstadoCuentaCliente();
			ecc.setCuotasPendientes(0);
			ecc.setId(listEstadoCuentaCliente.size());
			List<PlanCobranza> listPlanCobranza = new ArrayList<>(); 
			ecc.setListPlanCobranza(listPlanCobranza);
			ecc.setNotaVenta(nv);
			listEstadoCuentaCliente.add(ecc);
		}

	}
	
	public void cargarReporte(){
		verReporte = true;
		Map<String,String> map1 = new HashMap<>();
		map1.put("pRazonSocial", empresaLogin.getRazonSocial());
		map1.put("pDireccion", empresaLogin.getDireccion());
		map1.put("pTelefono", empresaLogin.getTelefono());
		map1.put("pIdEmpresa", String.valueOf(empresaLogin.getId()));
		//---URL NOTA VENTA
		urlReporte = buildUrl("ReportEstadoCuentaClientes",map1);
		FacesUtil.updateComponent("form001");
	}


	// ------- procesos transaccional ------

	// ONCOMPLETETEXT Cliente
	public List<Cliente> completeCliente(String query) {
		listCliente = new ArrayList<Cliente>();//reset
		List<Cliente> results = new ArrayList<Cliente>();
		Cliente c = new Cliente();
		c.setNombres("TODOS");
		results.add(c);
		boolean sw = NumberUtil.isNumeric(query);
		if(sw){
			listCliente = clienteDao.obtenerPorConsultaNit(query);
			for(Cliente i : listCliente) {
				if(i.getNit().toUpperCase().startsWith(query)){
					results.add(i);
				}
			}  
		}else{
			listCliente = clienteDao.obtenerPorConsulta(query);
			for(Cliente i : listCliente) {
				if(i.getNombres().toUpperCase().startsWith(query.toUpperCase())){
					results.add(i);
				}
			}  
		}
		return results;
	}

	public void onRowSelectClienteClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Cliente i : listCliente){
			if(i.getNombres().equals(nombre)){
				selectedCliente = i;
				cargarEstadoCuenta();
				return;
			}
		}
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



	// -------- get and set -------

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
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

	public List<EDEstadoCuentaCliente> getListEstadoCuentaCliente() {
		return listEstadoCuentaCliente;
	}

	public void setListEstadoCuentaCliente(List<EDEstadoCuentaCliente> listEstadoCuentaCliente) {
		this.listEstadoCuentaCliente = listEstadoCuentaCliente;
	}

	public EDEstadoCuentaCliente getSelectedEstadoCuenta() {
		return selectedEstadoCuenta;
	}

	public void setSelectedEstadoCuenta(EDEstadoCuentaCliente selectedEstadoCuenta) {
		this.selectedEstadoCuenta = selectedEstadoCuenta;
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

	public boolean isVerReporte() {
		return verReporte;
	}

	public void setVerReporte(boolean verReporte) {
		this.verReporte = verReporte;
	}

	public String getUrlReporte() {
		return urlReporte;
	}

	public void setUrlReporte(String urlReporte) {
		this.urlReporte = urlReporte;
	}


}
