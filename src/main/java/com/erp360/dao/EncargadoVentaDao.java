package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Empresa;
import com.erp360.model.EncargadoVenta;
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
public class EncargadoVentaDao extends DataAccessObjectJpa<EncargadoVenta,E,R,S,O, P, Q, U, V, W> {

	public EncargadoVentaDao(){
		super(EncargadoVenta.class);
	}
	
	public EncargadoVenta registrar(EncargadoVenta encargadoVenta){
		try{
			beginTransaction();
			encargadoVenta = create(encargadoVenta);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Encargado de Venta "+encargadoVenta.getNombres());
			return encargadoVenta;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return null;
		}
	}
	
	public boolean modificar(EncargadoVenta encargadoVenta){
		try{
			beginTransaction();
			update(encargadoVenta);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Encargado de Venta "+encargadoVenta.getNombres());
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return false;
		}
	}
	
	public boolean eliminar(EncargadoVenta encargadoVenta){
		try{
			beginTransaction();
			encargadoVenta.setEstado("RM");
			update(encargadoVenta);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "Encargado de Venta "+encargadoVenta.getNombres());
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return false;
		}
	}	
	
	public List<EncargadoVenta> getAllSalesPersionOrderById() {
		String query = "select ser from EncargadoVenta ser where (ser.estado='AC' or ser.estado='IN') and ser.salesPerson=TRUE order by ser.id desc";
		return executeQueryResulList(query);
	}
	
	public List<EncargadoVenta> getAllPublisherPersionOrderById() {
		String query = "select ser from EncargadoVenta ser where (ser.estado='AC' or ser.estado='IN') and ser.publisher=TRUE order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<EncargadoVenta> obtenerTodosOrdenadosPorId() {
		String query = "select ser from EncargadoVenta ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		return executeQueryResulList(query);
	}
	
	public List<EncargadoVenta> obtenerTodosActivosOrdenadosPorId() {
		String query = "select ser from EncargadoVenta ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<EncargadoVenta> obtenerPorConsulta(String query){
		return findAllActivosByQueryAndTwoParameter("estado","AC","nombre", query);
	}
	
	public  List<EncargadoVenta> obtenerPorEmpresa(Empresa empresa){
		return findAllActiveParameter("empresa", empresa.getId()); 
	}
}
