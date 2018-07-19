package com.erp360.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "pagosEjecutivoController")
@ViewScoped
public class PagosEjecutivoController implements Serializable {

	private static final long serialVersionUID = 6986540930723020906L;

	//DAO
	private  @Inject SessionMain sessionMain;

	//OBJECT

	//LIST

	//VAR
	private String currentPage;

	@PostConstruct
	public void init() {
		loadDefault();
	}

	public void loadDefault(){
		cargarLazyDataModel();
		currentPage = "/pages/venta/nota_venta/list.xhtml";
	}

	//----
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void cargarLazyDataModel(){
//		sizeList = notaVentaDao.countTotalRecord("nota_venta").intValue();
//		notaVentas = new LazyDataModel() {
//			private static final long serialVersionUID = 3565223586960673287L;
//
//			@Override
//			public List<NotaVenta> load(int first, int pageSize,
//					String sortField, SortOrder sortOrder, Map filters) {
//				setFirst(first);
//				setSizePage(pageSize);
//				return notaVentaDao.obtenerPorTamanio(getFirst(),getSizePage(),filters);
//			}
//
//			@Override
//			public NotaVenta getRowData(String rowKey) {
//				List<NotaVenta> notasVentas = (List<NotaVenta>) getWrappedData();
//				Integer value = Integer.valueOf(rowKey);
//				for (NotaVenta notaVenta : notasVentas) {
//					if (notaVenta.getId().equals(value)) {
//						return notaVenta;
//					}
//				}
//				return null;
//			}
//		};
	}


	// GET & SET

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

}
