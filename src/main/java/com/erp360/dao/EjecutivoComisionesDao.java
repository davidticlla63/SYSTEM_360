package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Cliente;
import com.erp360.model.Ejecutivo;
import com.erp360.model.EjecutivoComisiones;
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
public class EjecutivoComisionesDao extends DataAccessObjectJpa<EjecutivoComisiones,E,R,S,O, P, Q, U, V, W> {

	public EjecutivoComisionesDao(){
		super(EjecutivoComisiones.class);
	}

	public EjecutivoComisiones registrar(EjecutivoComisiones ejecutivoComisiones){
		try{
			beginTransaction();
			ejecutivoComisiones = create(ejecutivoComisiones);
			commitTransaction();
			return ejecutivoComisiones;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al registrar");
			}
			rollbackTransaction();
			return null;
		}
	}
	
	public List<EjecutivoComisiones> obtenerTodos(){
		String query = "SELECT em FROM EjecutivoComisiones em WHERE em.estado='AC' AND em.importe <> 0 ORDER BY em.fechaRegistro DESC";
		return executeQueryResulList(query);
	}
	
	
	public List<EjecutivoComisiones> obtenerTodosByEjecutivo(Ejecutivo ejecutivo){
		String query = "select em from EjecutivoComisiones em where em.estado='AC' AND em.ejecutivo.id="+ejecutivo.getId()+" order by em.fechaRegistro asc";
		return executeQueryResulList(query);
	}
	

	public List<EjecutivoComisiones> obtenerTodosNoPagadorByEjecutivo(Ejecutivo ejecutivo){
		String query = "select em from EjecutivoComisiones em where em.estado='AC' AND em.pagado=FALSE AND em.importe<>0 AND em.ejecutivo.id="+ejecutivo.getId()+" order by em.fechaRegistro asc";
		return executeQueryResulList(query);
	}
}
