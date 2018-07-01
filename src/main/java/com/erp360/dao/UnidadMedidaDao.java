package com.erp360.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Almacen;
import com.erp360.model.UnidadMedida;
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
public class UnidadMedidaDao extends DataAccessObjectJpa<UnidadMedida,E,R,S,O, P, Q, U, V, W> {

	public UnidadMedidaDao(){
		super(UnidadMedida.class);
	}

	public UnidadMedida registrar(UnidadMedida almacen){
		try{
			beginTransaction();
			almacen = create(almacen);
			commitTransaction();
			FacesUtil.infoMessage("Registro UnidadMedida", "UnidadMedida "+almacen.getId());
			return almacen;
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

	public UnidadMedida modificar(UnidadMedida unidadMedida){
		try{
			beginTransaction();
			unidadMedida = update(unidadMedida);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "UnidadMedida "+unidadMedida.getId());
			return unidadMedida;
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
	
	public UnidadMedida eliminar(UnidadMedida unidadMedida){
		try{
			beginTransaction();
			unidadMedida.setEstado("RM");
			unidadMedida.setNombre(new Date()+"|"+unidadMedida.getNombre());
			unidadMedida = update(unidadMedida);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "UnidadMedida "+unidadMedida.getId());
			return unidadMedida;
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

	public List<UnidadMedida> findAllOrderedByID() {
		String query = "select ser from UnidadMedida ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public UnidadMedida findByNombre(String nombre) {
		try{
			String query = "select em from UnidadMedida em where em.estado='AC' and upper(em.nombre)='"+nombre+"'";
			return (UnidadMedida) executeQuerySingleResult(query);
		}catch(Exception e){
			return null;
		}
	}

	public List<UnidadMedida> findAllActivosOrderedByID() {
		try{
			String query = "select em from UnidadMedida em where em.estado='AC' order by em.nombre asc";
			return executeQueryResulList(query);
		}catch(Exception e){
			return null;
		}
	}

	public List<UnidadMedida> findAllUnidadMedidaForDescription(String criterio) {
		try {
			String query = "select ser from UnidadMedida ser where upper(ser.nombre) like '%"
					+ criterio + "%' and ser.estado='AC' order by ser.nombre asc";
			List<UnidadMedida> listaUnidadMedida = executeQueryResulList(query);
			return listaUnidadMedida;
		} catch (Exception e) {
			return null;
		}
	}

	public List<UnidadMedida> findAllByNombre(String criterio) {
		try {
			String query = "select ser from UnidadMedida ser where upper(ser.nombre) like '%"
					+ criterio + "%' and ser.estado='AC' order by ser.nombre asc";
			List<UnidadMedida> listaUnidadMedida = executeQueryResulList(query);
			return listaUnidadMedida;
		} catch (Exception e) {
			return null;
		}
	}

}
