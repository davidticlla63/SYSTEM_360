package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.erp360.dao.EjecutivoComisionesDao;
import com.erp360.dao.EjecutivoDao;
import com.erp360.dao.PagoComisionDao;
import com.erp360.model.Ejecutivo;
import com.erp360.model.EjecutivoComisiones;
import com.erp360.model.PagoComision;
import com.erp360.util.NumberUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@ManagedBean(name = "pagoComisionController")
@ViewScoped
public class PagoComisionController implements Serializable {

	private static final long serialVersionUID = -1012155707040198783L;

	//DAO
	private @Inject PagoComisionDao pagoComisionDao;
	private @Inject EjecutivoComisionesDao ejecutivoComisionesDao;
	private  @Inject EjecutivoDao ejecutivoDao;

	//OBJECT
	private PagoComision pagoComision;
	private Ejecutivo ejecutivo;
	private EjecutivoComisiones ejecutivoComision;

	//LIST
	private List<PagoComision> comisionesPagadas;
	private List<EjecutivoComisiones> ejecutivoComisiones;
	private List<Ejecutivo> ejecutivos;

	//VAR
	private String currentPage;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login

	@PostConstruct
	public void init() {

		System.out.println(" ... init New Cobranzas...");
		loadDefault();
	}
	
	public void loadDefault(){
		currentPage = "/pages/caja/comision/pagos/list.xhtml";
		
		pagoComision = new PagoComision();
		comisionesPagadas = pagoComisionDao.obtenerOrdenAscPorId();
	}

	// ------- action & event ------
	
	public List<Ejecutivo> completeEjecutivo(String query) {
		ejecutivos = new ArrayList<Ejecutivo>();
		boolean sw = NumberUtil.isNumeric(query);
		if(sw){
			ejecutivos = ejecutivoDao.obtenerTodosPorCi(query);
		}else{
			ejecutivos = ejecutivoDao.obtenerTodosPorNombres(query.toUpperCase());
		}
		return ejecutivos;
	}
	
	public void onRowSelectEjecutivoClick(SelectEvent event) {
		Ejecutivo ejecutivoAux = (Ejecutivo) event.getObject();
		for(Ejecutivo c : ejecutivos){
			if(c.getId().equals(ejecutivoAux.getId())){
				ejecutivo = c;
				ejecutivoComisiones = new ArrayList<>();
				ejecutivoComisiones = ejecutivoComisionesDao.obtenerTodosByEjecutivo(ejecutivo);
				double saldoAnterior = 0;
				for(EjecutivoComisiones kc: ejecutivoComisiones ){
					if(kc.getNotaVenta()!=null){
						kc.setTipoMovimiento("VENTA");
						kc.setConcepto("VENTA "+kc.getNotaVenta().getCodigo());
						kc.setSaldo(saldoAnterior+kc.getImporte());
						saldoAnterior = kc.getSaldo();
					}else if(kc.getCobranza()!=null){
						kc.setTipoMovimiento("CUOTA");
						kc.setConcepto("CUOTA "+kc.getCobranza().getCodigo());
						kc.setSaldo(saldoAnterior+kc.getImporte());
						saldoAnterior = kc.getSaldo();
					}else {
						kc.setTipoMovimiento("PAGO");
						kc.setConcepto("PAGO ");
						kc.setSaldo(saldoAnterior-kc.getImporte());
						saldoAnterior = kc.getSaldo();
					}
				}
				return;
			}
		}
	}

	public void prepareCreate(){
		currentPage = "/pages/caja/comision/pagos/create.xhtml";
	}


	public void onRowSelect(SelectEvent event) {
		currentPage = "/pages/caja/comision/pagos/create.xhtml";
	}

	// ------- procesos transaccional ------

	
	// -------- get and set -------

	public List<PagoComision> getComisionesPagadas() {
		return comisionesPagadas;
	}

	public void setComisionesPagadas(List<PagoComision> comisionesPagadas) {
		this.comisionesPagadas = comisionesPagadas;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public PagoComision getPagoComision() {
		return pagoComision;
	}

	public void setPagoComision(PagoComision pagoComision) {
		this.pagoComision = pagoComision;
	}

	public List<EjecutivoComisiones> getEjecutivoComisiones() {
		return ejecutivoComisiones;
	}

	public void setEjecutivoComisiones(List<EjecutivoComisiones> ejecutivoComisiones) {
		this.ejecutivoComisiones = ejecutivoComisiones;
	}

	public Ejecutivo getEjecutivo() {
		return ejecutivo;
	}

	public void setEjecutivo(Ejecutivo ejecutivo) {
		this.ejecutivo = ejecutivo;
	}

	public List<Ejecutivo> getEjecutivos() {
		return ejecutivos;
	}

	public void setEjecutivos(List<Ejecutivo> ejecutivos) {
		this.ejecutivos = ejecutivos;
	}

	public EjecutivoComisiones getEjecutivoComision() {
		return ejecutivoComision;
	}

	public void setEjecutivoComision(EjecutivoComisiones ejecutivoComision) {
		this.ejecutivoComision = ejecutivoComision;
	}

}
