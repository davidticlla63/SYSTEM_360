package com.erp360.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.MovimientoCaja;
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
public class MovimientoCajaDao extends DataAccessObjectJpa<MovimientoCaja,E,R,S,O, P, Q, U, V, W> {
	
	public MovimientoCajaDao(){
		super(MovimientoCaja.class);
	}

	public MovimientoCaja registrarBasic(MovimientoCaja caja){
		try{
			caja = create(caja);
			return caja;
		}catch(Exception e){
			return null;
		}
	}

	public MovimientoCaja registrar(MovimientoCaja caja){
		try{
			beginTransaction();
			caja = create(caja);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Caja "+caja.getCorrelativo());
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

	public boolean modificar(MovimientoCaja caja){
		try{
			beginTransaction();
			update(caja);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Caja "+caja.getCorrelativo());
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

	public boolean eliminar(MovimientoCaja caja){
		try{
			beginTransaction();
			caja.setEstado("RM");
			update(caja);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "Caja "+caja.getCorrelativo());
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

	public int correlativoMovimiento( Gestion gestion){
		String query = "select count(em) from  movimiento_caja em where (em.estado='AC' or em.estado='IN' or em.estado='PR' or em.estado='AN') and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}

	public List<MovimientoCaja> obtenerTodosOrdenadosPorId() {
		String query = "select ser from MovimientoCaja ser where (ser.estado='AC' or ser.estado='PR' or ser.estado='IN' or ser.estado='PN') order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<MovimientoCaja> obtenerTodosActivosOrdenadosPorId() {
		String query = "select ser from Caja ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<MovimientoCaja> obtenerPorConsulta(String query){
		return findAllActivosByQueryAndTwoParameter("estado","AC","nombre", query);
	}

	public  List<MovimientoCaja> obtenerPorEmpresa(Empresa empresa){
		return findAllActiveParameter("empresa", empresa.getId()); 
	}

	public List<MovimientoCaja> obtenerPorTamanio(int inicio,int maxRows,Map filters) {
		System.out.println("inicio:" + inicio + " | maxRows:" + maxRows);
		return findAllBySize(inicio, maxRows,filters);
	}
}
