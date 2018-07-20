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
import com.erp360.model.Cliente;
import com.erp360.model.Ejecutivo;
import com.erp360.model.EjecutivoComisiones;
import com.erp360.model.KardexCliente;
import com.erp360.util.NumberUtil;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "kardexEjecutivoController")
@ViewScoped
public class KardexEjecutivoController implements Serializable {

	private static final long serialVersionUID = 6986540930723020906L;

	//DAO
	private  @Inject EjecutivoComisionesDao ejecutivoComisionesDao;
	private  @Inject EjecutivoDao ejecutivoDao;

	//OBJECT
	private EjecutivoComisiones ejecutivoComision;
	private Ejecutivo ejecutivo;

	//LIST
	private List<EjecutivoComisiones> ejecutivoComisiones;
	private List<Ejecutivo> ejecutivos;

	//VAR
	private String currentPage;

	@PostConstruct
	public void init() {
		loadDefault();
	}

	public void loadDefault(){
		currentPage = "/pages/ejecutivo/kardex/list.xhtml";
		ejecutivoComisiones = new ArrayList<>();// ejecutivoComisionesDao.obtenerTodos();
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
	
	// ONCOMPLETETEXT Cliente
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
					EjecutivoComisiones kcSaldo =new EjecutivoComisiones();
					kcSaldo.setId(0);
					kcSaldo.setNotaVenta(null);
					kcSaldo.setTipoMovimiento("SALDO");
					kcSaldo.setConcepto("Saldo");
					kcSaldo.setImporte(0d);
					kcSaldo.setSaldo(0d);
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
					ejecutivoComisiones.add(0,kcSaldo);
					return;
				}
			}
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

}
