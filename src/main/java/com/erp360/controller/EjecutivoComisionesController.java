package com.erp360.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.erp360.dao.EjecutivoComisionesDao;
import com.erp360.model.EjecutivoComisiones;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "ejecutivoComisionesController")
@ViewScoped
public class EjecutivoComisionesController implements Serializable {

	private static final long serialVersionUID = 6986540930723020906L;

	//DAO
	private @Inject EjecutivoComisionesDao ejecutivoComisionesDao;
	
	private EjecutivoComisiones ejecutivoComision;
	private List<EjecutivoComisiones> ejecutivoComisiones;

	//VAR
	private String currentPage;


	@PostConstruct
	public void init() {
		currentPage = "/pages/venta/nota_venta/list.xhtml";
		ejecutivoComisiones = ejecutivoComisionesDao.findAll();
	}

	//----
	

	// GET & SET

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public EjecutivoComisiones getEjecutivoComision() {
		return ejecutivoComision;
	}

	public void setEjecutivoComision(EjecutivoComisiones ejecutivoComision) {
		this.ejecutivoComision = ejecutivoComision;
	}

	public List<EjecutivoComisiones> getEjecutivoComisiones() {
		return ejecutivoComisiones;
	}

	public void setEjecutivoComisiones(List<EjecutivoComisiones> ejecutivoComisiones) {
		this.ejecutivoComisiones = ejecutivoComisiones;
	}

}
