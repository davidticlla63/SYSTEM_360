package com.erp360.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.erp360.util.DateUtility;
import com.erp360.util.FacesUtil;
import com.erp360.util.ReportUtil;
import com.erp360.util.SessionMain;


/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@ManagedBean(name = "reportGeneralesController")
@ViewScoped
public class ReportGeneralesController implements Serializable {

	private static final long serialVersionUID = -1012155707040198783L;

	//ESTATE
	private boolean verReporte;

	//OBJECT

	//LIST

	//VARIABES
	private String urlProductosAgrupadoLinea;
	private Date fechaInicio;
	private Date fechaFin;
	private String urlGeneric;
	private String tituloReporte;
	private Integer idTicket;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login

	@PostConstruct
	public void initNewObject() {

		System.out.println(" ... initNewObject ...");

		verReporte = false;
		urlProductosAgrupadoLinea = "";
		urlGeneric = "";
		tituloReporte = "";

		idTicket = 0;

		fechaInicio = new Date();
		fechaFin = new Date();

	}

	public void clearUrl(){
		urlProductosAgrupadoLinea = "";
		verReporte = false;
		//FacesUtil.updateComponent("form001");
	}

	//----- LOAD DIALOG

	public void loadDialgReportProductoAgrupadoLinea(){
		FacesUtil.updateComponent("formDlgReportTicketEspecifico");
		FacesUtil.showDialog("dlgReportTicketEspecifico");
	}

	// ---------------------------- REPORTE  --------------------------------
	
	public void loadInformeProductosPorLinea(){
		Map<String,String> map1 = new HashMap<>();
		map1.put("pRazonSocial", sessionMain.getEmpresaLogin().getRazonSocial());
		map1.put("pDireccion", sessionMain.getEmpresaLogin().getDireccion());
		map1.put("pTelefono", sessionMain.getEmpresaLogin().getTelefono());
		map1.put("pUsuario", sessionMain.getUsuarioLogin().getLogin());
		urlGeneric = ReportUtil.buildUrl("ReportProductoAgrupadoLinea",map1);
		verReporte = true;
		tituloReporte = "INFORME DE PRODUCTOS POR LINEA";
		FacesUtil.updateComponent("form001");
	}
	
	public void loadInformeCobranzaPorMes(){
		Date fechaActual = new Date();
		int anio = DateUtility.obtenerYearNumeral(fechaActual);
		int mes =  DateUtility.obtenerMesNumeral(fechaActual);
		String mesLiteral = String.valueOf( DateUtility.obtenerMesLiteral(mes));
		Map<String,String> map1 = new HashMap<>();
		map1.put("pRazonSocial", sessionMain.getEmpresaLogin().getRazonSocial());
		map1.put("pDireccion", sessionMain.getEmpresaLogin().getDireccion());
		map1.put("pTelefono", sessionMain.getEmpresaLogin().getTelefono());
		map1.put("pUsuario", sessionMain.getUsuarioLogin().getLogin());
		map1.put("pAnio",String.valueOf(anio));
		map1.put("pMes",String.valueOf(mes));
		map1.put("pMesLiteral",mesLiteral);
		urlGeneric = ReportUtil.buildUrl("ReportCobranzasPorMes",map1);
		verReporte = true;
		tituloReporte = "INFORME DE COBRANZAS DE "+mesLiteral+" DEL "+anio;
		FacesUtil.updateComponent("form001");
	}

	// ---- get and set -----

	public boolean isVerReporte() {
		return verReporte;
	}

	public void setVerReporte(boolean verReporte) {
		this.verReporte = verReporte;
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

	public String getUrlGeneric() {
		return urlGeneric;
	}

	public void setUrlGeneric(String urlGeneric) {
		this.urlGeneric = urlGeneric;
	}

	public String getTituloReporte() {
		return tituloReporte;
	}

	public void setTituloReporte(String tituloReporte) {
		this.tituloReporte = tituloReporte;
	}

	public Integer getIdTicket() {
		return idTicket;
	}

	public void setIdTicket(Integer idTicket) {
		this.idTicket = idTicket;
	}

	public String getUrlProductosAgrupadoLinea() {
		return urlProductosAgrupadoLinea;
	}

	public void setUrlProductosAgrupadoLinea(String urlProductosAgrupadoLinea) {
		this.urlProductosAgrupadoLinea = urlProductosAgrupadoLinea;
	}

}
