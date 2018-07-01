package com.erp360.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import com.erp360.model.Cliente;
import com.erp360.model.CuentaCobrar;
import com.erp360.model.Empresa;
import com.erp360.model.NotaVenta;
import com.erp360.model.ReservaVenta;
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
public class CuentaCobrarDao extends DataAccessObjectJpa<CuentaCobrar,E,R,S,O, P, Q, U, V, W> {

	public CuentaCobrarDao(){
		super(CuentaCobrar.class);
	}

	public CuentaCobrar registrarBasic(CuentaCobrar deudaCliente) throws Exception{
		deudaCliente = create(deudaCliente);
		return deudaCliente;
	}

	public boolean modificarBasic(CuentaCobrar deudaCliente) throws Exception{
		try{
			update(deudaCliente);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public CuentaCobrar registrar(CuentaCobrar deudaCliente){
		try{
			beginTransaction();
			deudaCliente = create(deudaCliente);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "CuentaCobrar "+deudaCliente.getCorrelativo());
			return deudaCliente;
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

	public boolean modificar(CuentaCobrar caja){
		try{
			beginTransaction();
			update(caja);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "CuentaCobrar "+caja.getCorrelativo());
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

	public boolean eliminar(CuentaCobrar caja){
		try{
			beginTransaction();
			caja.setEstado("RM");
			update(caja);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "CuentaCobrar "+caja.getCorrelativo());
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al Eliminar");
			}
			rollbackTransaction();
			return false;
		}
	}

	public CuentaCobrar obtenerPorReservaVentaPendiente(ReservaVenta reservaVenta){
		String query = "select em from cuenta_cobrar em where em.estado='PN' and em.reservaVenta.id="+reservaVenta.getId();
		return executeQuerySingleResult(query);
	}
	
	public CuentaCobrar obtenerPorNotaVenta(NotaVenta notaVenta){
		String query = "select em from CuentaCobrar em where em.notaVenta.id="+notaVenta.getId();
		return executeQuerySingleResult(query);
	}


	public int correlativoCuentaCobrar(){
		String query = "select count(em) from  cuenta_cobrar em where (em.estado='AC' or em.estado='IN' or em.estado='PG' or em.estado='PR' or em.estado='PN' or em.estado='AN')";
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}

	public List<CuentaCobrar> obtenerTodosOrdenadosPorId() {
		String query = "select ser from CuentaCobrar ser where (ser.estado='AC' or ser.estado='PR' or ser.estado='PG' or ser.estado='IN' or ser.estado='PN') order by ser.id desc";
		return executeQueryResulList(query);
	}
	
	public List<CuentaCobrar> obtenerTodosPorCliente(Cliente cliente) {
		String query = "select ser from CuentaCobrar ser where (ser.estado='AC' or ser.estado='PR' or ser.estado='PG' or ser.estado='IN' or ser.estado='PN') AND (ser.notaVenta.cliente.id="+cliente.getId()+" ) order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<CuentaCobrar> obtenerTodosActivosOrdenadosPorId() {
		String query = "select ser from CuentaCobrar ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<CuentaCobrar> obtenerPorConsulta(String query){
		return findAllActivosByQueryAndTwoParameter("estado","AC","nombre", query);
	}

	public  List<CuentaCobrar> obtenerPorEmpresa(Empresa empresa){
		return findAllActiveParameter("empresa", empresa.getId()); 
	}
	
	public List<CuentaCobrar> obtenerPorTamanio(int inicio,int maxRows,Map filters) {
		System.out.println("inicio:" + inicio + " | maxRows:" + maxRows);
		return findAllBySize(inicio, maxRows,filters);
	}
}
