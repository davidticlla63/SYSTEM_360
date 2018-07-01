package com.erp360.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import com.erp360.dao.AlmacenProductoDao;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.Gestion;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;


/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Named(value = "catalogoProductoController")
@ConversationScoped
public class CatalogoProductoController implements Serializable {

	private static final long serialVersionUID = 749163787421586877L;

	private @Inject FacesContext facesContext;

	//DAO
	private @Inject AlmacenProductoDao almacenProductoDao;

	//LIST
	private List<AlmacenProducto> listaAlmacenProducto;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private String usuarioSession;
	private Gestion gestionLogin;

	@PostConstruct
	public void initNewOrdenIngreso() {
		usuarioSession = sessionMain.getUsuarioLogin().getLogin();
		gestionLogin = sessionMain.getGestionLogin();

		loadDefault();
	}

	public void loadDefault(){
		listaAlmacenProducto = almacenProductoDao.findProductoConOSinStockOrderedByIDAndGestion(gestionLogin);
	}

	public void cargarReporte(){
		try {
			//urlOrdenIngreso = loadURL();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgVistaPreviaOrdenIngreso').show();");

			initNewOrdenIngreso();
		} catch (Exception e) {
			FacesUtil.errorMessage("Proceso Incorrecto.");
		}
	}

	public String loadURL(){
		try{
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();  
			String urlPath = request.getRequestURL().toString();
			urlPath = urlPath.substring(0, urlPath.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
			String urlPDFreporte = urlPath+"ReporteOrdenIngreso?pIdOrdenIngreso="+1+"&pIdEmpresa=1&pUsuario="+usuarioSession;
			return urlPDFreporte;
		}catch(Exception e){
			return "error";
		}
	}

	// -------- get and set -------

	public List<AlmacenProducto> getListaAlmacenProducto() {
		return listaAlmacenProducto;
	}

	public void setListaAlmacenProducto(List<AlmacenProducto> listaAlmacenProducto) {
		this.listaAlmacenProducto = listaAlmacenProducto;
	}

}
