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

import com.erp360.dao.CuentaCobrarDao;
import com.erp360.model.CuentaCobrar;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "cuentaCobrarController")
@ViewScoped
public class CuentaCobrarController implements Serializable {

	private static final long serialVersionUID = -7148739425514986109L;

	//DAO
	private @Inject CuentaCobrarDao cuentasCobrarDao;

	//LIST
	private LazyDataModel<CuentaCobrar> cuentasPorCobrar;

	//VAR
	private int sizeList;
	private int sizePage;
	private int first;

	@PostConstruct
	public void init() {
		loadDefault();
	}

	private void loadDefault(){
		cargarLazyDataModel();
		//cuentasPorCobrar = cuentasCobrarDao.obtenerTodosOrdenadosPorId();
	}

	// PROCESS
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void cargarLazyDataModel(){
		sizeList = cuentasCobrarDao.countTotalRecord("cuenta_cobrar").intValue();
		cuentasPorCobrar = new LazyDataModel() {
			private static final long serialVersionUID = 3565223586960673287L;

			@Override
			public List<CuentaCobrar> load(int first, int pageSize,
					String sortField, SortOrder sortOrder, Map filters) {
				setFirst(first);
				setSizePage(pageSize);
				return cuentasCobrarDao.obtenerPorTamanio(getFirst(),getSizePage(),filters);
			}

			@Override
			public CuentaCobrar getRowData(String rowKey) {
				List<CuentaCobrar> cuentasPorCobrar = (List<CuentaCobrar>) getWrappedData();
				Integer value = Integer.valueOf(rowKey);
				for (CuentaCobrar cuentaCobrar : cuentasPorCobrar) {
					if (cuentaCobrar.getId().equals(value)) {
						return cuentaCobrar;
					}
				}
				return null;
			}
		};
		cuentasPorCobrar.setRowCount(getSizeList());
		cuentasPorCobrar.setPageSize(getSizePage());
	}
	
	// EVENT

	public void onRowSelect(SelectEvent event) {

	}

	// --------------   get and set  ---------------

	public LazyDataModel<CuentaCobrar> getCuentasPorCobrar() {
		return cuentasPorCobrar;
	}

	public void setCuentasPorCobrar(LazyDataModel<CuentaCobrar> cuentasPorCobrar) {
		this.cuentasPorCobrar = cuentasPorCobrar;
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

