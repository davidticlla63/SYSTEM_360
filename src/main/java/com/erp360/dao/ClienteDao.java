package com.erp360.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.ClienteAdicional;
import com.erp360.model.Empresa;
import com.erp360.model.Cliente;
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
public class ClienteDao extends DataAccessObjectJpa<Cliente,ClienteAdicional,R,S,O, P, Q, U, V, W> {

	public ClienteDao(){
		super(Cliente.class,ClienteAdicional.class);
	}
	
	public Cliente registrar(Cliente cliente){
		try{
			beginTransaction();
			cliente = create(cliente);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Cliente Nº "+cliente.getCodigo());
			return cliente;
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
	
	public Cliente registrar(Cliente cliente,ClienteAdicional newClienteAdicional){
		try{
			beginTransaction();
			cliente = create(cliente);
			newClienteAdicional.setCliente(cliente);
			createE(newClienteAdicional);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Cliente Nº "+cliente.getCodigo());
			return cliente;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al Registar");
			}
			rollbackTransaction();
			return null;
		}
	}
	
	public boolean modificar(Cliente cliente,ClienteAdicional newClienteAdicional){
		try{
			beginTransaction();
			cliente = update(cliente);
			newClienteAdicional.setCliente(cliente);
			updateE(newClienteAdicional);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Cliente Nº "+cliente.getCodigo());
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
	
	public boolean eliminar(Cliente cliente,ClienteAdicional newClienteAdicional){
		try{
			beginTransaction();
			cliente.setEstado("RM");
			cliente.setCi(new Date()+"|"+cliente.getCi());
			cliente = update(cliente);
			newClienteAdicional.setEstado("RM");
			newClienteAdicional.setCliente(cliente);
			updateE(newClienteAdicional);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Cliente "+cliente.getId());
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
	
	public int obtenerCorrelativo(){
		String query = "select count(em) from  cliente em where (em.estado='AC' or em.estado='IN') ";
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}
	
	public int obtenerCorrelativo2(){
		String query = "select cast(MAX(em.codigo) as int) from  cliente em where (em.estado='AC' or em.estado='IN') ";
		return ((Integer) executeNativeQuerySingleResult(query)).intValue()+1;	
	}

	public List<Cliente> obtenerTodosOrdenadosPorId() {
		String query = "select ser from Cliente ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		return executeQueryResulList(query);
	}
	
	public List<Cliente> obtenerTodosActivosOrdenadosPorId() {
		String query = "select em from Cliente em where em.estado='AC' order by em.id desc";
		return executeQueryResulList(query);
	}

	public List<Cliente> obtenerTodosPorNit(String nitCi){
		String query = "select em from Cliente em where em.estado='AC' and (em.nit like '%"+nitCi+"%') order by em.id desc";
		return executeQueryResulList(query);
	}
	
	public List<Cliente> obtenerTodosPorNitCodigo(String nitCiCodigo){
		String query = "select em from Cliente em where em.estado='AC' and ( (em.nit like '%"+nitCiCodigo+"%') or (em.codigo like '%"+nitCiCodigo+"%') ) order by em.id desc";
		return executeQueryResulList(query);
	}
	
	public List<Cliente> obtenerTodosPorRazonSocial(String nombApeRaz){
		String query = "select em from Cliente em where em.estado='AC' and  upper(translate(em.razonSocial, 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ')) like '%"+nombApeRaz+"%' ) order by em.id desc";
		return executeQueryResulList(query);
	}
	
	public List<Cliente> obtenerPorConsulta(String query){
		return findAllActivosByQueryAndTwoParameter("estado","AC","razonSocial", query);
	}
	
	public List<Cliente> obtenerPorConsultaNit(String query){
		return findAllActivosByQueryAndTwoParameter("estado","AC","nit", query);
	}
	
	public  List<Cliente> obtenerPorEmpresa(Empresa empresa){
		return findAllActiveParameter("empresa", empresa.getId()); 
	}
}
