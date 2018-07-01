package com.erp360.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.erp360.dao.ReservaVentaDao;
import com.erp360.dao.ReservaVentaDao;
import com.erp360.model.ReservaVenta;
import com.erp360.model.ReservaVenta;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "listReservaVentaController")
@ViewScoped
public class ListReservaVentaController implements Serializable {

	private static final long serialVersionUID = -1012155707040198783L;

	//DAO
	private @Inject SessionMain sessionMain;
	private @Inject ReservaVentaDao reservaVentaDao;

	//STATE
	private boolean modificar = false;
	private boolean crear = true;

	//OBJECT
	private ReservaVenta selectedReservaVenta;

	//LIST
	private List<ReservaVenta> listReservaVenta;

	//VAR

	@PostConstruct
	public void init() {

		System.out.println(" ... init New ...");
		//este parametro se utiliza para poder enviar un id a otro controller ,cuando es el caso
		//de poder anular una nota de venta, pero antes se debe limpiar los datos
		FacesUtil.getSessionAttribute("pIdReservaVentaAnulada");
		loadDefault();
	}

	public void loadDefault(){
		modificar = false;
		crear = true;

		selectedReservaVenta = new ReservaVenta();
		listReservaVenta = reservaVentaDao.obtenerTodosOrdenadosPorId();
	}

	// ------- action & event ------

	public void onRowSelect(SelectEvent event) {
		if(selectedReservaVenta.getEstado().equals("PN")){
			crear = false;
			modificar = true;
		}
	}

	// ------- procesos transaccional ------

	public void anularReservaVenta(){
		try{
			//enviar parametro de anulacion
			FacesUtil.setSessionAttribute("pIdReservaVentaAnulada", selectedReservaVenta.getId());
			FacesUtil.redirect("reserva_venta.xhtml");
		}catch(Exception e){

		}
	}

	public void actionIrANotaVentaPorReserva(){
		try{
			FacesUtil.setSessionAttribute("pIdReservaVenta", selectedReservaVenta.getId());
			FacesUtil.redirect("nota_venta.xhtml");
		}catch(Exception e){
			e.printStackTrace();
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

	public List<ReservaVenta> getListReservaVenta() {
		return listReservaVenta;
	}

	public void setListReservaVenta(List<ReservaVenta> listReservaVenta) {
		this.listReservaVenta = listReservaVenta;
	}

	public ReservaVenta getSelectedReservaVenta() {
		return selectedReservaVenta;
	}

	public void setSelectedReservaVenta(ReservaVenta selectedReservaVenta) {
		this.selectedReservaVenta = selectedReservaVenta;
	}


}
