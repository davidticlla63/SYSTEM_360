package com.erp360.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Gestion;
import com.erp360.model.GrupoProducto;
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
public class GrupoProductoDao extends DataAccessObjectJpa<GrupoProducto,E,R,S,O, P, Q, U, V, W> {

	public GrupoProductoDao(){
		super(GrupoProducto.class);
	}

	public GrupoProducto registrar(GrupoProducto usuario){
		try{
			beginTransaction();
			usuario = create(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Grupo de Producto "+usuario.getNombre());
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

	public boolean modificar(GrupoProducto grupoProducto){
		try{
			beginTransaction();
			update(grupoProducto);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Grupo de Producto "+grupoProducto.getNombre());
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al registrar");
			}
			rollbackTransaction();
			return false;
		}
	}

	public boolean eliminar(GrupoProducto grupoProducto){
		try{
			beginTransaction();
			grupoProducto.setEstado("RM");
			grupoProducto.setCodigo(new Date()+"|"+grupoProducto.getCodigo());
			update(grupoProducto);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "Grupo de Producto "+grupoProducto.getNombre());
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al registrar");
			}
			rollbackTransaction();
			return false;
		}
	}
	
	public int correlativoGrupoProducto(){
		String query = "select count(em) from  grupo_producto em where (em.estado='AC' or em.estado='IN')";
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}

	public List<GrupoProducto> obtenerTodosOrdenFecha() {
		String query = "select ser from GrupoProducto ser where ser.estado='AC' or ser.estado='IN' order by ser.fechaRegistro desc";
		return executeQueryResulList(query);
	}
	
	public List<GrupoProducto> obtenerTodosOrdenDescripcion() {
		String query = "select ser from GrupoProducto ser where ser.estado='AC' or ser.estado='IN' order by ser.descripcion desc";
		return executeQueryResulList(query);
	}

	public List<GrupoProducto> findAllProductoActivosByID() {
		String query = "select ser from GrupoProducto ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<GrupoProducto> obtenerTodosPorDescripcion(String criterio) {
		try {
			String query = "select ser from GrupoProducto ser where ser.nombre like '%"
					+ criterio + "%'";
			List<GrupoProducto> listaProducto = executeQueryResulList(query);
			return listaProducto;
		} catch (Exception e) {
			return null;
		}
	}

}
