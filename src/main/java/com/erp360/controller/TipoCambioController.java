package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.erp360.dao.TipoCambioDao;
import com.erp360.model.TipoCambio;
import com.erp360.util.EDTipoCambio;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "tipoCambioController")
@ViewScoped
public class TipoCambioController implements Serializable {

	private static final long serialVersionUID = -4058327498353920610L;

	//DAO
	private @Inject SessionMain sessionMain;
	private @Inject TipoCambioDao tipoCambioDao;

	//VAR
	private String currentPage;

	//LIST
	private List<TipoCambio> listTipoCambio;
	private List<EDTipoCambio> listEdTipoCambio;
	
	//OBJECT
	//tipo cambio
	private TipoCambio tipoCambio;
	private ScheduleModel eventModelTipoCambio;
	private ScheduleEvent eventTipoCambio ;
	//ufv
	private ScheduleEvent eventTipoCambioUfv ;
	
	@PostConstruct
	public void initNewComprobante() {
		loadDefault();
	}

	public void loadDefault(){
		currentPage = "/pages/parametrizacion/tipo_cambio/list.xhtml";
		
		eventTipoCambio = new DefaultScheduleEvent();
		listTipoCambio = tipoCambioDao.obtenerPorEmpresa(sessionMain.getEmpresaLogin());
		cargarFechasTipoCambio();
		//ufv
		eventTipoCambioUfv =  new DefaultScheduleEvent();
		
		//cargar Tipo Cambio
		listEdTipoCambio = new ArrayList<EDTipoCambio>();
	}

	// ACTION EVENT

	public void onCellEdit(CellEditEvent event) {
		//Object oldValue = event.getOldValue();
		//Object newValue = event.getNewValue();
	}

	// PROCESS
	
	private void cargarFechasTipoCambio(){
		eventModelTipoCambio = new DefaultScheduleModel();
		for(TipoCambio tc: listTipoCambio){
			DefaultScheduleEvent data = new DefaultScheduleEvent(String.valueOf(tc.getUnidad()), tc.getFecha(), tc.getFecha(), tc);
			//if(tc.getId()%2 == 0){
			//	data.setStyleClass("style-1");
			//}else{
			//	data.setStyleClass("style-2");
			//}
			eventModelTipoCambio.addEvent(data);
		}
	}
	
	public void registrarDialog(){
		System.out.println("registrarDialog");
	}

	// GET AND SET

	public String getPage() {
		return currentPage;
	}

	public void setPage(String page) {
		this.currentPage = page;
	}

	public TipoCambio getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(TipoCambio tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public ScheduleModel getEventModelTipoCambio() {
		return eventModelTipoCambio;
	}

	public void setEventModelTipoCambio(ScheduleModel eventModelTipoCambio) {
		this.eventModelTipoCambio = eventModelTipoCambio;
	}

	public ScheduleEvent getEventTipoCambio() {
		return eventTipoCambio;
	}

	public void setEventTipoCambio(ScheduleEvent eventTipoCambio) {
		this.eventTipoCambio = eventTipoCambio;
	}

	public List<TipoCambio> getListTipoCambio() {
		return listTipoCambio;
	}

	public void setListTipoCambio(List<TipoCambio> listTipoCambio) {
		this.listTipoCambio = listTipoCambio;
	}

	public ScheduleEvent getEventTipoCambioUfv() {
		return eventTipoCambioUfv;
	}

	public void setEventTipoCambioUfv(ScheduleEvent eventTipoCambioUfv) {
		this.eventTipoCambioUfv = eventTipoCambioUfv;
	}

	public List<EDTipoCambio> getListEdTipoCambio() {
		return listEdTipoCambio;
	}

	public void setListEdTipoCambio(List<EDTipoCambio> listEdTipoCambio) {
		this.listEdTipoCambio = listEdTipoCambio;
	}

}
