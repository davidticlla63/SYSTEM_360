package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.DetalleDeudaCliente;
import com.erp360.model.Empresa;
import com.erp360.util.E;
import com.erp360.util.FacesUtil;
import com.erp360.util.O;
import com.erp360.util.P;
import com.erp360.util.Q;
import com.erp360.util.R;
import com.erp360.util.S;
import com.erp360.util.U;
import com.erp360.util.V;
import com.erp360.util.W;
/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@Stateless
public class DetalleDeudaClienteDao extends DataAccessObjectJpa<DetalleDeudaCliente,E,R,S,O, P, Q, U, V, W> {

	public DetalleDeudaClienteDao(){
		super(DetalleDeudaCliente.class);
	}

	public DetalleDeudaCliente registrarBasic(DetalleDeudaCliente caja) throws Exception{
		caja = create(caja);
		return caja;
	}

	public DetalleDeudaCliente registrar(DetalleDeudaCliente caja){
		try{
			beginTransaction();
			caja = create(caja);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "DeudaCliente "+caja.getId());
			return caja;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al Regstrar");
			}
			rollbackTransaction();
			return null;
		}
	}

	public boolean modificar(DetalleDeudaCliente caja){
		try{
			beginTransaction();
			update(caja);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "DeudaCliente "+caja.getId());
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al Modificar");
			}
			rollbackTransaction();
			return false;
		}
	}

	
	public List<DetalleDeudaCliente> obtenerTodosOrdenadosPorId() {
		String query = "select ser from DetalleDeudaCliente ser where (ser.estado='AC' or ser.estado='PR' or ser.estado='IN' or ser.estado='PN') order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<DetalleDeudaCliente> obtenerTodosActivosOrdenadosPorId() {
		String query = "select ser from DetalleDeudaCliente ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<DetalleDeudaCliente> obtenerPorConsulta(String query){
		return findAllActivosByQueryAndTwoParameter("estado","AC","nombre", query);
	}

	public  List<DetalleDeudaCliente> obtenerPorEmpresa(Empresa empresa){
		return findAllActiveParameter("empresa", empresa.getId()); 
	}
}
