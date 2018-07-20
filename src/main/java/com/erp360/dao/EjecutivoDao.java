package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Empresa;
import com.erp360.model.Cliente;
import com.erp360.model.Ejecutivo;
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
public class EjecutivoDao extends DataAccessObjectJpa<Ejecutivo,E,R,S,O, P, Q, U, V, W> {

	public EjecutivoDao(){
		super(Ejecutivo.class);
	}
	
	public Ejecutivo registrar(Ejecutivo encargadoVenta){
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
	
	public boolean modificar(Ejecutivo encargadoVenta){
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
	
	public boolean eliminar(Ejecutivo encargadoVenta){
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
	
	public List<Ejecutivo> getAllSalesPersionOrderById() {
		String query = "select ser from Ejecutivo ser where (ser.estado='AC' or ser.estado='IN') and ser.salesPerson=TRUE order by ser.id desc";
		return executeQueryResulList(query);
	}
	
	public List<Ejecutivo> getAllPublisherPersionOrderById() {
		String query = "select ser from Ejecutivo ser where (ser.estado='AC' or ser.estado='IN') and ser.publisher=TRUE order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<Ejecutivo> obtenerTodosOrdenadosPorId() {
		String query = "select ser from Ejecutivo ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		return executeQueryResulList(query);
	}
	
	public List<Ejecutivo> obtenerTodosActivosOrdenadosPorId() {
		String query = "select ser from Ejecutivo ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<Ejecutivo> obtenerPorConsulta(String query){
		return findAllActivosByQueryAndTwoParameter("estado","AC","nombre", query);
	}
	
	public  List<Ejecutivo> obtenerPorEmpresa(Empresa empresa){
		return findAllActiveParameter("empresa", empresa.getId()); 
	}
	
	public List<Ejecutivo> obtenerTodosPorCi(String ci){
		String query = "select em from Ejecutivo em where em.estado='AC' and (em.ci like '%"+ci+"%') order by em.id desc";
		return executeQueryResulList(query);
	}
	
	public List<Ejecutivo> obtenerTodosPorNombres(String nombApeRaz){
		String query = "select em from Ejecutivo em where em.estado='AC' and  upper(translate(em.nombres, 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ')) like '%"+nombApeRaz+"%' ) order by em.id desc";
		return executeQueryResulList(query);
	}
}
