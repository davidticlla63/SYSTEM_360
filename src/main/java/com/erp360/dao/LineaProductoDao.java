package com.erp360.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.GrupoProducto;
import com.erp360.model.LineaProducto;
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
public class LineaProductoDao extends DataAccessObjectJpa<LineaProducto,E,R,S,O, P, Q, U, V, W> {

	public LineaProductoDao(){
		super(LineaProducto.class);
	}

	public LineaProducto registrar(LineaProducto lineaProducto){
		try{
			beginTransaction();
			lineaProducto = create(lineaProducto);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Linea de Producto "+lineaProducto.getNombre());
			return lineaProducto;
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

	public boolean modificar(LineaProducto lineaProducto){
		try{
			beginTransaction();
			update(lineaProducto);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Linea de Producto "+lineaProducto.getNombre());
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

	public boolean eliminar(LineaProducto lineaProducto){
		try{
			beginTransaction();
			lineaProducto.setEstado("RM");
			lineaProducto.setCodigo(new Date()+"|"+lineaProducto.getCodigo());
			update(lineaProducto);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Linea de Producto "+lineaProducto.getNombre());
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
	
	public int correlativoLineaProducto(){
		String query = "select count(em) from  linea_producto em where (em.estado='AC' or em.estado='IN')";
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}

	public List<LineaProducto> obtenerTodosOrdenFecha() {
		String query = "select ser from LineaProducto ser where ser.estado='AC' or ser.estado='IN' order by ser.fechaRegistro desc";
		return executeQueryResulList(query);
	}
	
	public List<LineaProducto> obtenerTodosPorGrupoProducto(GrupoProducto grupoProducto) {
		String query = "select ser from LineaProducto ser where ser.grupoProducto.id="+grupoProducto.getId();
		return executeQueryResulList(query);
	}
	
	public List<LineaProducto> obtenerTodosOrdenDescripcion() {
		String query = "select ser from LineaProducto ser where ser.estado='AC' or ser.estado='IN' order by ser.descripcion desc";
		return executeQueryResulList(query);
	}
	
	public List<LineaProducto> obtenerTodosByNombreOrdrByDesc() {
		String query = "select ser from LineaProducto ser where ser.estado='AC' or ser.estado='IN' order by ser.nombre desc";
		return executeQueryResulList(query);
	}

	public List<LineaProducto> findAllProductoActivosByID() {
		String query = "select ser from LineaProducto ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<LineaProducto> obtenerTodosPorDescripcion(String criterio) {
		try {
			String query = "select ser from LineaProducto ser where ser.nombre like '%"
					+ criterio + "%'";
			List<LineaProducto> listaProducto = executeQueryResulList(query);
			return listaProducto;
		} catch (Exception e) {
			return null;
		}
	}

}
