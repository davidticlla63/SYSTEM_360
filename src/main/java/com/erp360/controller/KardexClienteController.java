package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import com.erp360.dao.ClienteDao;
import com.erp360.dao.KardexClienteDao;
import com.erp360.model.Cliente;
import com.erp360.model.KardexCliente;
import com.erp360.util.FacesUtil;
import com.erp360.util.NumberUtil;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "kardexClienteController")
@ViewScoped
public class KardexClienteController implements Serializable {

	private static final long serialVersionUID = -7148739425514986109L;

	//DAO
	private @Inject KardexClienteDao kardexClienteDao;
	private @Inject ClienteDao clienteDao;

	//estados
	private boolean crear ;
	private boolean registrar ;
	private boolean modificar ;

	//VARIABLES
	private String nombreEstado="ACTIVO";

	//OBJECT
	private Cliente selectedCliente;

	//LIST
	private List<KardexCliente> listKardexCliente;
	private List<Cliente> listCliente;

	//MAP

	@PostConstruct
	public void init() {
		System.out.println(" init new initNewCliente");
		loadDefault();
	}

	private void loadDefault(){
		crear = true;
		registrar = false;
		modificar = false;

		listKardexCliente = new ArrayList<>();
	}

	// -- event action

	public void loadDialogImage() {
		FacesUtil.showDialog("dlgImagenCliente");
		FacesUtil.updateComponent("dlgImagenCliente");
	}

	public void onRowSelect(SelectEvent event) {
		crear = false;
		registrar = false;
		modificar = true;
	}

	// ONCOMPLETETEXT Cliente
	public List<Cliente> completeCliente(String query) {
		listCliente = new ArrayList<Cliente>();
		boolean sw = NumberUtil.isNumeric(query);
		if(sw){
			listCliente = clienteDao.obtenerTodosPorNit(query);
		}else{
			listCliente = clienteDao.obtenerTodosPorRazonSocial(query.toUpperCase());
		}
		return listCliente;
	}

	public void onRowSelectCliente2Click(SelectEvent event) {
		Cliente cliente = (Cliente) event.getObject();
		for(Cliente c : listCliente){
			if(c.getId().equals(cliente.getId())){
				selectedCliente = c;
				selectedCliente.setNombres(selectedCliente.getNombres()+" ");
				listKardexCliente = kardexClienteDao.obtenerKardexPorCliente(selectedCliente);
				KardexCliente kcSaldo =new KardexCliente();
				kcSaldo.setId(0);
				kcSaldo.setTipo("-");
				kcSaldo.setMotivo("Saldo");
				kcSaldo.setTotalImporteExtranjero(0);
				kcSaldo.setSaldo(0);
				listKardexCliente.add(0, kcSaldo);
				double saldoAnterior = 0;
				for(KardexCliente kc: listKardexCliente ){
					kc.setSaldo(saldoAnterior+kc.getTotalImporteExtranjero());
					saldoAnterior = kc.getSaldo();
				}
				return;
			}
		}
	}

	// --------------   get and set  ---------------

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public String getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	public boolean isCrear() {
		return crear;
	}

	public void setCrear(boolean crear) {
		this.crear = crear;
	}

	public boolean isRegistrar() {
		return registrar;
	}

	public void setRegistrar(boolean registrar) {
		this.registrar = registrar;
	}

	public List<KardexCliente> getListKardexCliente() {
		return listKardexCliente;
	}

	public void setListKardexCliente(List<KardexCliente> listKardexCliente) {
		this.listKardexCliente = listKardexCliente;
	}

	public Cliente getSelectedCliente() {
		return selectedCliente;
	}

	public void setSelectedCliente(Cliente selectedCliente) {
		this.selectedCliente = selectedCliente;
	}
}

