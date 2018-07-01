package com.erp360.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Gestion;
import com.erp360.model.Proveedor;
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
public class ProveedorDao extends DataAccessObjectJpa<Proveedor,E,R,S,O, P, Q, U, V, W> {

	public ProveedorDao(){
		super(Proveedor.class);
	}

	public Proveedor registrar(Proveedor usuario){
		try{
			beginTransaction();
			usuario = create(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Proveedor "+usuario.getNombre());
			return usuario;
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

	public Proveedor modificar(Proveedor usuario){
		try{
			beginTransaction();
			usuario = update(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Proveedor "+usuario.getNombre());
			return usuario;
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

	public boolean eliminar(Proveedor proveedor){
		try{
			beginTransaction();
			proveedor.setEstado("RM");
			proveedor.setNombre(new Date()+"|"+proveedor.getNombre());
			update(proveedor);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "Proveedor "+proveedor.getNombre());
			return true;
		}catch(Exception e){
			FacesUtil.errorMessage("Error al eliminar");
			rollbackTransaction();
			return false;
		}
	}
	
	public int correlativoProveedor(){
		String query = "select count(em) from Proveedor em where (em.estado='AC' or em.estado='IN')";
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}
	
	public List<Proveedor> obtenerACyINOrdenAscPorId(){
		String query = "select em from Proveedor em where (em.estado='AC' or em.estado='IN') order by em.id asc";
		return executeQueryResulList(query);
	}

	public List<Proveedor> obtenerProveedorOrdenAscPorId(){
		return findAscAllOrderedByParameter("id");
	}

	public List<Proveedor> obtenerProveedorOrdenDescPorId(){
		return findDescAllOrderedByParameter("id");
	}

	public Proveedor obtenerPrimerProveedor(){
		try{
			return findAllActivosByMaxResultOrderByAsc(1).get(0);
		}catch(Exception e){
			return new Proveedor();
		}
	}

	public List<Proveedor> obtenerPorConsulta(String query){
		return findAllActivosByQueryAndTwoParameter("estado","AC","nombre", query);
	}
	
	public List<Proveedor> obtenerPorConsultaNit(String query){
		return findAllActivosByQueryAndTwoParameter("estado","AC","nit", query);
	}
	
	public List<Proveedor> findAllOrderedByID(Gestion gestion) {
		String query = "select em from Proveedor em where em.gestion.id="+gestion.getId();
		return executeQueryResulList(query);
	}
	
	public List<Proveedor> findAllActivoOrderedByID(Gestion gestion) {
		String query = "select em from Proveedor em where em.estado='AC' and em.gestion.id="+gestion.getId()+" order by em.id desc";
		return executeQueryResulList(query);
	}
	
	public List<Proveedor> findAllProveedorForQueryNombre(String criterio, Gestion gestion) {
		try {
			String query = "select ser from Proveedor ser where ser.nombre like '%"
					+ criterio + "%' and ser.estado='AC' and ser.gestion.id="+gestion.getId()+" order by ser.nombre asc";
			List<Proveedor> listaProveedor=  executeQueryResulList(query);
			return listaProveedor;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Proveedor> findAllByEmpresa(Gestion gestion) {
		String query = "select em from Proveedor em where (em.estado='AC' or em.estado='IN') and em.gestion.id="+gestion.getId()+" order by em.nombre asc";
		return executeQueryResulList(query);
	}
}
