package com.erp360.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.erp360.dao.MovimientoCajaDao;
import com.erp360.model.MovimientoCaja;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "movimientoCajaController")
@ViewScoped
public class MovimientoCajaController implements Serializable {

	private static final long serialVersionUID = -7148739425514986109L;

	//DAO
	private @Inject MovimientoCajaDao cajaDao;

	//LIST
	private LazyDataModel<MovimientoCaja> movimientosCaja;
	
	//VAR
	private int sizeList;
	private int sizePage;
	private int first;

	@PostConstruct
	public void init() {
		System.out.println(" init New Caja");
		loadDefault();
	}

	private void loadDefault(){
		cargarLazyDataModel();
		//movimientosCaja = cajaDao.obtenerTodosOrdenadosPorId();
	}
	
	//PROCESS
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void cargarLazyDataModel(){
		sizeList = cajaDao.countTotalRecord("movimiento_caja").intValue();
		movimientosCaja = new LazyDataModel() {
			private static final long serialVersionUID = 3565223586960673287L;

			@Override
			public List<MovimientoCaja> load(int first, int pageSize,
					String sortField, SortOrder sortOrder, Map filters) {
				setFirst(first);
				setSizePage(pageSize);
				return cajaDao.obtenerPorTamanio(getFirst(),getSizePage(),filters);
			}

			@Override
			public MovimientoCaja getRowData(String rowKey) {
				List<MovimientoCaja> movimientosCaja = (List<MovimientoCaja>) getWrappedData();
				Integer value = Integer.valueOf(rowKey);
				for (MovimientoCaja movimientoCaja : movimientosCaja) {
					if (movimientoCaja.getId().equals(value)) {
						return movimientoCaja;
					}
				}
				return null;
			}
		};
		movimientosCaja.setRowCount(getSizeList());
		movimientosCaja.setPageSize(getSizePage());
	}

	// EVENT
	public void onRowSelect(SelectEvent event) {
	
	}

	// --------------   get and set  ---------------

	public LazyDataModel<MovimientoCaja> getMovimientosCaja() {
		return movimientosCaja;
	}

	public void setMovimientosCaja(LazyDataModel<MovimientoCaja> movimientosCaja) {
		this.movimientosCaja = movimientosCaja;
	}

	public int getSizeList() {
		return sizeList;
	}

	public void setSizeList(int sizeList) {
		this.sizeList = sizeList;
	}

	public int getSizePage() {
		return sizePage;
	}

	public void setSizePage(int sizePage) {
		this.sizePage = sizePage;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}
}

