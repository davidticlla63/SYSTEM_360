package com.erp360.controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.SelectEvent;

import com.erp360.dao.NotaVentaDao;
import com.erp360.model.NotaVenta;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@ManagedBean(name = "listNotaVentaController")
@ViewScoped
public class ListNotaVentaController implements Serializable {

	private static final long serialVersionUID = -1012155707040198783L;

	//DAO
	private @Inject FacesContext facesContext;
	private @Inject NotaVentaDao notaVentaDao;

	//STATE
	private boolean modificar = false;
	private boolean crear = true;
	private boolean verReporte;

	//OBJECT
	private NotaVenta selectedNotaVenta;

	//LIST
	private List<NotaVenta> listNotaVenta;

	//VAR
	private String urlReciboAmortizacion;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login

	@PostConstruct
	public void init() {

		System.out.println(" ... init New ...");
		//este parametro se utiliza para poder enviar un id a otro controller ,cuando es el caso
		//de poder anular una nota de venta, pero antes se debe limpiar los datos
		FacesUtil.setSessionAttribute("pIdNotaVentaAnulada",null);
		loadDefault();
	}

	public void loadDefault(){
		modificar = false;
		crear = true;
		verReporte = false;

		selectedNotaVenta = new NotaVenta();
		listNotaVenta = notaVentaDao.obtenerTodosOrdenadosPorId();
	}

	// ------- action & event ------

	public void onRowSelect(SelectEvent event) {
		if(selectedNotaVenta.getEstado().equals("AC")){
			crear = false;
			modificar = true;
		}
	}

	// ------- procesos transaccional ------

	public void anularNotaVenta(){
		try{
			//enviar parametro de anulacion
			FacesUtil.setSessionAttribute("pIdNotaVentaAnulada", selectedNotaVenta.getId());
			FacesUtil.redirect("nota-venta.xhtml");
		}catch(Exception e){

		}
	}

	public void cargarReporte(){
		setVerReporte(true);
		Map<String,String> map1 = new HashMap<>();
		map1.put("pUsuario", sessionMain.getUsuarioLogin().getLogin());
		map1.put("pRazonSocial", sessionMain.getEmpresaLogin().getRazonSocial());
		map1.put("pDireccion", sessionMain.getEmpresaLogin().getDireccion());
		map1.put("pTelefono", sessionMain.getEmpresaLogin().getTelefono());
		map1.put("pIdNotaVenta", String.valueOf(selectedNotaVenta.getId()));
		map1.put("pIdEmpresa", String.valueOf(sessionMain.getEmpresaLogin().getId()));
		map1.put("pMoneda", selectedNotaVenta.getMoneda());
		map1.put("pPagoNacional", "0");
		map1.put("pPagoExtranjero", "0");
		map1.put("pTipoRecibo", "COPIA ORIGINAL");
		setUrlReciboAmortizacion(buildUrl("ReportReciboAmortizacionNotaVenta",map1));
	}


	public void verDetalle(){
		try {
			FacesUtil.setSessionAttribute("pIdNotaVenta", selectedNotaVenta.getId());		
			FacesUtil.redirect("nota_venta.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public NotaVenta getSelectedNotaVenta() {
		return selectedNotaVenta;
	}

	public void setSelectedNotaVenta(NotaVenta selectedNotaVenta) {
		this.selectedNotaVenta = selectedNotaVenta;
	}

	public List<NotaVenta> getListNotaVenta() {
		return listNotaVenta;
	}

	public void setListNotaVenta(List<NotaVenta> listNotaVenta) {
		this.listNotaVenta = listNotaVenta;
	}

	public boolean isVerReporte() {
		return verReporte;
	}

	public void setVerReporte(boolean verReporte) {
		this.verReporte = verReporte;
	}

	public String getUrlReciboAmortizacion() {
		return urlReciboAmortizacion;
	}

	public void setUrlReciboAmortizacion(String urlReciboAmortizacion) {
		this.urlReciboAmortizacion = urlReciboAmortizacion;
	}


}
