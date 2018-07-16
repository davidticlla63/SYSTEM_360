package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.erp360.dao.AlmacenDao;
import com.erp360.dao.ParametroCuotaDao;
import com.erp360.dao.ParametroVentaDao;
import com.erp360.model.Almacen;
import com.erp360.model.ParametroCuota;
import com.erp360.model.ParametroVenta;
import com.erp360.model.Producto;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@ManagedBean(name = "parametroVentaController")
@ViewScoped
public class ParametroVentaController implements Serializable {

	private static final long serialVersionUID = -50560680381546802L;

	//DAO
	private @Inject ParametroVentaDao parametroVentaDao;
	private @Inject ParametroCuotaDao parametroCuotaDao;
	private @Inject AlmacenDao almacenDao;

	//STATE
	private boolean modificar = false;

	//OBJECT
	private ParametroVenta selectedParametroVenta;
	private Almacen selectedAlmacen;

	//LIST
	private List<ParametroCuota> listParametroCuota;
	private List<Almacen> listaAlmacen;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Usuario usuarioLogin;

	@PostConstruct
	public void init() {

		System.out.println(" ... init New ...");
		usuarioLogin = sessionMain.getUsuarioLogin();
		loadDefault();
	}

	public void loadDefault(){
		modificar = false;
		selectedParametroVenta = parametroVentaDao.obtener();
		if(selectedParametroVenta.getContrato() == null){
			selectedParametroVenta.setContrato("");
		}
		listParametroCuota = parametroCuotaDao.obtenerTodosActivos();
		selectedAlmacen = selectedParametroVenta.getAlmacenVenta();
		listaAlmacen = almacenDao.obtenerTodosActivosOrdenadosPorId();
	}

	// ------- action & event ------

	public void actionModificar(){
		modificar = true;
	}

	// ------- procesos transaccional ------

	public void modificarParametroVenta(){
		Date date = new Date();
		selectedParametroVenta.setAlmacenVenta(selectedAlmacen);
		selectedParametroVenta.setFechaModificacion(date);
		selectedParametroVenta.setUsuarioRegistro(usuarioLogin.getLogin());
		for(ParametroCuota pc: listParametroCuota){
			if(pc.getNumeroCuotas()<=0){
				FacesUtil.infoMessage("Error", "Revice el campo Nº Cuotas de la tabla Rango de cuotas.");
				return;
			}
			if(pc.getPorcentajeCuotaInicial()<=0){
				FacesUtil.infoMessage("Error", "Revice el campo % Cuota Inicial de la tabla Rango de cuotas.");
				return;
			}
			if(pc.getMontoRangoInicial()>=pc.getMontoRangoFinal()){
				FacesUtil.infoMessage("Error", "Revice los rangos de la tabla Rango de cuotas.");
				return;
			}
			pc.setFechaModificacion(date);
		}
		System.out.println("selectedParametroVenta: "+selectedParametroVenta.getContrato());
		boolean sw = parametroVentaDao.modificar(selectedParametroVenta,listParametroCuota);
		FacesUtil.infoMessage("Correcto", "Modificación Correcta.");
		if(sw){
			loadDefault();
		}
	}
	
	public void onRowSelectAlmacenClick(SelectEvent event) {
		String nombre =  event.getObject().toString();
		for(Almacen i : listaAlmacen){
			if(i.getNombre().equals(nombre)){
				setSelectedAlmacen(i);
				return;
			}
		}
	}
	
	public List<Almacen> completeAlmacen(String query) {
		String upperQuery = query.toUpperCase();
		List<Almacen> results = new ArrayList<Almacen>();
		for(Almacen i : listaAlmacen) {
			if(i.getNombre().toUpperCase().startsWith(upperQuery)){
				results.add(i);
			}
		}         
		return results;
	}

	// -------- get and set -------

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public ParametroVenta getSelectedParametroVenta() {
		return selectedParametroVenta;
	}

	public void setSelectedParametroVenta(ParametroVenta selectedParametroVenta) {
		this.selectedParametroVenta = selectedParametroVenta;
	}

	public List<ParametroCuota> getListParametroCuota() {
		return listParametroCuota;
	}

	public void setListParametroCuota(List<ParametroCuota> listParametroCuota) {
		this.listParametroCuota = listParametroCuota;
	}

	public List<Almacen> getListaAlmacen() {
		return listaAlmacen;
	}

	public void setListaAlmacen(List<Almacen> listaAlmacen) {
		this.listaAlmacen = listaAlmacen;
	}

	public Almacen getSelectedAlmacen() {
		return selectedAlmacen;
	}

	public void setSelectedAlmacen(Almacen selectedAlmacen) {
		this.selectedAlmacen = selectedAlmacen;
	}


}
