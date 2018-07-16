package com.erp360.dao;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Cliente;
import com.erp360.model.GrupoProducto;
import com.erp360.model.LineaProducto;
import com.erp360.model.Producto;
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
public class ProductoDao extends DataAccessObjectJpa<Producto,E,R,S,O, P, Q, U, V, W> {

	public ProductoDao(){
		super(Producto.class);
	}
	
	public Producto registrar(Producto usuario){
		try{
			beginTransaction();
			usuario = create(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Producto "+usuario.getNombre());
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

	public boolean modificar(Producto usuario){
		try{
			beginTransaction();
			 update(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Producto "+usuario.getNombre());
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

	public List<Producto> findAllOrderedByDescripcion() {
		//String query = "select ser from Producto ser where ser.estado='AC' or ser.estado='IN' order by ser.descripcion desc";
		String query = "SELECT p FROM Producto p WHERE p.id IN (SELECT i.producto.id FROM AlmacenProducto i WHERE i.estado = 'AC' AND i.stock > 0) ORDER BY p.id ASC";
		return executeQueryResulList(query);
	}

	public List<Producto> findAllProductoActivosByID() {
		String query = "select ser from Producto ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<Producto> findAllProductoForQueryNombre(String criterio) {
		try {
			String query = "select ser from Producto ser where upper(ser.nombre) like '%" + criterio + "%' and ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta: " + query);
			List<Producto> listaProducto = executeQueryResulList(query);
			return listaProducto;
		} catch (Exception e) {
			return null;
		}
	}

	public int correlativoProducto(){
		String query = "select count(em) from producto em where (em.estado='AC' or em.estado='IN')";
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}
	
	public List<Producto> findAllProductoByID() {
		String query = "select ser from Producto ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<Producto> findAllProductoForPartidaID(int partidaID) {
		try {
			String query = "select pro from Producto pro where pro.estado='AC' and pro.partida.id="+partidaID+" order by pro.id desc";
			return executeQueryResulList(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Producto findByCodigo(String codigo) {
		try{
			String query = "select ser from Producto ser where (ser.estado='AC' or ser.estado='IN') and upper(ser.codigo)='"+ codigo + "'";
			return (Producto) executeQuerySingleResult(query);
		}catch(Exception e){
			System.out.println("Error: " + e.getMessage());
			return null;
		}
	}

	public List<Producto> findAllProductoByFechaRegistro() {
		String query = "select ser from Producto ser where ser.estado='AC' or ser.estado='IN' order by ser.fechaRegistro desc";
		return executeQueryResulList(query);
	}

	public List<Producto> findAllProductoForDescription(String criterio) {
		try {
			String query = "select ser from Producto ser where ser.nombre like '%"
					+ criterio + "%'";
			List<Producto> listaProducto = executeQueryResulList(query);
			return listaProducto;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Producto> traerProductoActivas() {
		try {
			String query = "select ser from Producto ser where ser.estado='AC' order by ser.nombre asc";
			List<Producto> listaProducto = executeQueryResulList(query);
			return listaProducto;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Producto> obtenerTodosOrdenFecha() {
		String query = "select ser from Producto ser where ser.estado='AC' or ser.estado='IN' order by ser.fechaRegistro desc";
		return executeQueryResulList(query);
	}
	
	public List<Producto> obtenerPorConsulta(String query){
		return findAllActivosByQueryAndTwoParameter("estado","AC","nombre", query);
	}
	
	public List<Producto> obtenerTodosPorNombreCodigo(String nombreCodigo){
		String query = "select  pr from Producto pr where pr.estado='AC' and ( upper(translate(pr.nombre, 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ')) like '%"+nombreCodigo+"%' or upper(translate(pr.codigo, 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ')) like '%"+nombreCodigo+"%' )";
		//new com.erp360.model.Producto(pr.id,pr.codigo,pr.nombre,pr.descripcion) return executeQueryResulList(query);
		return super.getEm().createQuery(query, Producto.class).getResultList();
	}
	
	public List<Producto> obtenerTodosPorLineaProducto(LineaProducto lineaProducto) {
		String query = "select ser from Producto ser where ser.lineaProducto.id="+lineaProducto.getId();
		return executeQueryResulList(query);
	}

}
