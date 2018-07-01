package com.erp360.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.erp360.dao.AlmacenProductoDao;
import com.erp360.dao.CuentaCobrarDao;
import com.erp360.dao.KardexClienteDao;
import com.erp360.dao.KardexProductoDao;
import com.erp360.dao.MovimientoCajaDao;
import com.erp360.dao.NotaVentaDao;
import com.erp360.dao.OrdenSalidaDao;
import com.erp360.model.MovimientoCaja;
import com.erp360.model.NotaVenta;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "notasVentasController")
@ViewScoped
public class NotasVentasController implements Serializable {

	private static final long serialVersionUID = 6986540930723020906L;

	//DAO
	private @Inject NotaVentaDao notaVentaDao;
	private @Inject MovimientoCajaDao movimientoCajaDao;
	private @Inject CuentaCobrarDao cuentaCobrarDao;
	private @Inject AlmacenProductoDao almacenProductoDao;
	private @Inject OrdenSalidaDao ordenSalidaDao;
	private @Inject KardexClienteDao kardexClienteDao;
	private @Inject KardexProductoDao kardexProductoDao;
	private  @Inject SessionMain sessionMain;

	//OBJECT
	private NotaVenta notaVenta;

	//LIST
	private LazyDataModel<NotaVenta> notaVentas;

	//VAR
	private String currentPage;
	private boolean selected;
	private int sizeList;
	private int sizePage;
	private int first;
	private double montoNotaCargoAPagar;
	private double cambioNotaCargoAPagar;

	@PostConstruct
	public void init() {
		loadDefault();
	}

	public void loadDefault(){
		cargarLazyDataModel();
		selected = false;
		notaVenta = new NotaVenta();
		currentPage = "/pages/venta/nota_venta/list.xhtml";
		montoNotaCargoAPagar = 0;
		cambioNotaCargoAPagar = 0;
		//notaVentas = notaVentaDao.obtenerTodosOrdenadosPorFecha();
		FacesUtil.setSessionAttribute("type", null);
	}

	//----
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void cargarLazyDataModel(){
		sizeList = notaVentaDao.countTotalRecord("nota_venta").intValue();
		notaVentas = new LazyDataModel() {
			private static final long serialVersionUID = 3565223586960673287L;

			@Override
			public List<NotaVenta> load(int first, int pageSize,
					String sortField, SortOrder sortOrder, Map filters) {
				setFirst(first);
				setSizePage(pageSize);
				return notaVentaDao.obtenerPorTamanio(getFirst(),getSizePage(),filters);
			}

			@Override
			public NotaVenta getRowData(String rowKey) {
				List<NotaVenta> notasVentas = (List<NotaVenta>) getWrappedData();
				Integer value = Integer.valueOf(rowKey);
				for (NotaVenta notaVenta : notasVentas) {
					if (notaVenta.getId().equals(value)) {
						return notaVenta;
					}
				}
				return null;
			}
		};
		notaVentas.setRowCount(getSizeList());
		notaVentas.setPageSize(getSizePage());
	}

	public void hideDialogNotaCargo(){
		FacesUtil.hideModal("m-r-01");
	}
	
	public void showDialogNotaCargo(){
		FacesUtil.updateComponent("formModalPago");
		FacesUtil.showModal("m-r-01");
	}
	
	public void onRowSelect(SelectEvent event){
		selected = true;
	}

	public void prepareCancellationSalesNote(){
		notaVentaDao.anularNotaVenta(notaVenta);
	}

	public void prepareReport(){

	}
	
	public void newQuotation(){
		try{
			FacesUtil.setSessionAttribute("type", "quotation");
			FacesUtil.redirect("nota_venta.xhtml");
		}catch(Exception e){
			System.out.println("Error : " +e.getMessage());
		}
	}

	public void prepareViewDetail(){
		try{
			FacesUtil.setSessionAttribute("pIdNotaVenta", notaVenta.getId());
			FacesUtil.redirect("nota_venta.xhtml");
		}catch(Exception e){
			System.out.println("Error : " +e.getMessage());
		}
	}
	
	public void registerNotaCargo(){
		//verificar si la nota de cargo no supero ahun el monto de la reserva
		Date fechaActual = new Date();
		MovimientoCaja movimientoCaja = new MovimientoCaja();
		movimientoCaja.setCorrelativo(String.format("%06d",movimientoCajaDao.correlativoMovimiento(sessionMain.getGestionLogin())));
		movimientoCaja.setEstado("PR");
		movimientoCaja.setFechaAprobacion(fechaActual);
		movimientoCaja.setFechaDocumento(fechaActual);
		movimientoCaja.setFechaRegistro(fechaActual);
		movimientoCaja.setGestion(sessionMain.getGestionLogin());
		movimientoCaja.setMoneda(notaVenta.getMoneda());
		movimientoCaja.setObservacion("Ninguna");
		movimientoCaja.setTipoCambio(sessionMain.getTipoCambio().getUnidad());
		movimientoCaja.setNumeroDocumento(notaVenta.getCodigo());
		movimientoCaja.setTipoDocumento("NOTA CARGO");
		movimientoCaja.setMotivoIngreso("NOTA CARGO, POR RESERVA VENTA DE PRODUCTO(S) AL CREDITO");
		movimientoCaja.setTotalImporteExtranjero(montoNotaCargoAPagar);
		movimientoCaja.setTotalImporteNacional(montoNotaCargoAPagar / sessionMain.getTipoCambio().getUnidad());
		//tambien actualizar la reserva en la venta
		notaVenta.setMontoReservaExtranjero(notaVenta.getMontoReservaExtranjero()+movimientoCaja.getTotalImporteExtranjero());
		movimientoCaja.setNotaVenta(notaVenta);
		movimientoCaja = movimientoCajaDao.registrarBasic(movimientoCaja);
		if(movimientoCaja!= null){
			FacesUtil.infoMessage("Correcto", "Nota de Cargo Registrada Correctamente.");
			// cerrar dialog FacesUtil.
		}
	}

	// GET & SET

	public NotaVenta getNotaVenta() {
		return notaVenta;
	}

	public void setNotaVenta(NotaVenta notaVenta) {
		this.notaVenta = notaVenta;
	}

	public LazyDataModel<NotaVenta> getNotaVentas() {
		return notaVentas;
	}

	public void setNotaVentas(LazyDataModel<NotaVenta> notaVentas) {
		this.notaVentas = notaVentas;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
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

	public double getMontoNotaCargoAPagar() {
		return montoNotaCargoAPagar;
	}

	public void setMontoNotaCargoAPagar(double montoNotaCargoAPagar) {
		this.montoNotaCargoAPagar = montoNotaCargoAPagar;
	}

	public double getCambioNotaCargoAPagar() {
		return cambioNotaCargoAPagar;
	}

	public void setCambioNotaCargoAPagar(double cambioNotaCargoAPagar) {
		this.cambioNotaCargoAPagar = cambioNotaCargoAPagar;
	}

}
