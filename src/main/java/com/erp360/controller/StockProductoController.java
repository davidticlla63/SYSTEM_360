package com.erp360.controller;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import com.erp360.dao.AlmacenProductoDao;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.Gestion;
import com.erp360.model.Producto;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

@ManagedBean(name = "stockProductoController")
@ViewScoped
public class StockProductoController implements Serializable {

	private static final long serialVersionUID = -14432996097479924L;

	//DAO
	private @Inject AlmacenProductoDao almacenProductoDao;
	private @Inject FacesContext facesContext;

	//OBJECT
	private Producto selectedProducto;
	private Gestion selectedGestion;

	//LIST
	private List<Producto> listaProducto = new ArrayList<Producto>();
	private List<Gestion> listGestion = new ArrayList<Gestion>();
	private List<AlmacenProducto> listaAlmacenProducto = new ArrayList<AlmacenProducto>();
	
	//VAR
	private String urlStockProducto;
	private boolean verReporte;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login

	@PostConstruct
	public void initNewStockProducto() {

		System.out.println("initNewStockProducto()");
		loadDefault();
	}
	public void loadDefault(){
		verReporte = false;
		urlStockProducto = "";
		listaAlmacenProducto = almacenProductoDao.findProductoConStockOrderedByIDAndGestion(sessionMain.getGestionLogin());
	}

	public void cargarReporte(){
		verReporte = true;
		Map<String,String> map1 = new HashMap<>();
		map1.put("pUsuario", sessionMain.getUsuarioLogin().getLogin());
		map1.put("pRazonSocial",sessionMain.getEmpresaLogin().getRazonSocial());
		map1.put("pDireccion", sessionMain.getEmpresaLogin().getDireccion());
		map1.put("pTelefono", sessionMain.getEmpresaLogin().getTelefono());
		urlStockProducto = buildUrl("ReportStockProducto",map1);
	}
	
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
			System.out.println("url: "+url.toString());
			return url.toString();
		}catch(Exception e){
			return "";
		}
	}
	// -------- get and set -------

	public List<Producto> getListaProducto() {
		return listaProducto;
	}

	public void setListaProducto(List<Producto> listaProducto) {
		this.listaProducto = listaProducto;
	}

	public Producto getSelectedProducto() {
		return selectedProducto;
	}

	public void setSelectedProducto(Producto selectedProducto) {
		this.selectedProducto = selectedProducto;
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

	public List<AlmacenProducto> getListaAlmacenProducto() {
		return listaAlmacenProducto;
	}

	public void setListaAlmacenProducto(List<AlmacenProducto> listaAlmacenProducto) {
		this.listaAlmacenProducto = listaAlmacenProducto;
	}
	public String getUrlStockProducto() {
		return urlStockProducto;
	}
	public void setUrlStockProducto(String urlStockProducto) {
		this.urlStockProducto = urlStockProducto;
	}
	public boolean isVerReporte() {
		return verReporte;
	}
	public void setVerReporte(boolean verReporte) {
		this.verReporte = verReporte;
	}

}
