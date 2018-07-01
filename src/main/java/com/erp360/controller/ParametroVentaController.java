package com.erp360.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.erp360.dao.ParametroCuotaDao;
import com.erp360.dao.ParametroVentaDao;
import com.erp360.model.ParametroCuota;
import com.erp360.model.ParametroVenta;
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

	//STATE
	private boolean modificar = false;

	//OBJECT
	private ParametroVenta selectedParametroVenta;

	//LIST
	private List<ParametroCuota> listParametroCuota;

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
	}

	// ------- action & event ------

	public void actionModificar(){
		modificar = true;
	}

	// ------- procesos transaccional ------

	public void modificarParametroVenta(){
		Date date = new Date();
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


}
