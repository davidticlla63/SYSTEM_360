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
@ManagedBean(name = "comisionEjecutivoController")
@ViewScoped
public class ComisionEjecutivoController implements Serializable {

	private static final long serialVersionUID = 6986540930723020906L;

	//DAO
	private  @Inject EjecutivoComisionesDao ejecutivoComisionesDao;

	//OBJECT
	private EjecutivoComisiones ejecutivoComision;

	//LIST
	private List<EjecutivoComisiones> ejecutivoComisiones;

	//VAR
	private String currentPage;

	@PostConstruct
	public void init() {
		loadDefault();
	}

	public void loadDefault(){
		currentPage = "/pages/ejecutivo/comision/list.xhtml";
		ejecutivoComisiones = ejecutivoComisionesDao.obtenerTodos();
		//cargarLazyDataModel();
	}

	//----
	
	//@SuppressWarnings({ "unchecked", "rawtypes" })
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
	
	public void onRowSelect(){
		currentPage = "/pages/ejecutivo/comision/view.xhtml";
	}


	// GET & SET

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public List<EjecutivoComisiones> getEjecutivoComisiones() {
		return ejecutivoComisiones;
	}

	public void setEjecutivoComisiones(List<EjecutivoComisiones> ejecutivoComisiones) {
		this.ejecutivoComisiones = ejecutivoComisiones;
	}

	public EjecutivoComisiones getEjecutivoComision() {
		return ejecutivoComision;
	}

	public void setEjecutivoComision(EjecutivoComisiones ejecutivoComision) {
		this.ejecutivoComision = ejecutivoComision;
	}

}
