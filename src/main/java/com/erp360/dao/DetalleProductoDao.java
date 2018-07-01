package com.erp360.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Almacen;
import com.erp360.model.DetalleProducto;
import com.erp360.model.Gestion;
import com.erp360.model.Producto;
import com.erp360.util.E;
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
public class DetalleProductoDao extends DataAccessObjectJpa<DetalleProducto,E,R,S,O, P, Q, U, V, W> {

	public DetalleProductoDao(){
		super(DetalleProducto.class);
	}

	public DetalleProducto registrar(DetalleProducto detalleProducto){
		try{
			beginTransaction();
			detalleProducto = create(detalleProducto);
			commitTransaction();
			return detalleProducto;
		}catch(Exception e){
			rollbackTransaction();
			return null;
		}
	}
	
	public List<DetalleProducto> findByAlmacenProductoAndFecha(Gestion gestion,Almacen almacen,Producto producto,Date fecha) {
		System.out.println("findByAlmacenProductoAndFecha() ");
		try{
			String query = "select em from DetalleProducto em where ( em.estado='AC' or em.estado='IN' ) and em.gestion.id="+gestion.getId()+" and em.almacen.id="
					+ almacen.getId() + " and em.producto.id="+producto.getId()+" and em.fechaRegistro='"+fecha+"'";
			return executeQueryResulList(query);
		}catch(Exception e){
			return new ArrayList<DetalleProducto>();
		}
	}

	public List<DetalleProducto> findAllOrderedByID(Gestion gestion) {
		String query = "select em from DetalleProducto em where em.estado='AC' or em.estado='IN' and em.gestion.id="+gestion.getId()+" order by em.id desc";
		return executeQueryResulList(query);
	}

	public List<DetalleProducto> findAllActivoOrderedByID(Gestion gestion) {
		String query = "select em from DetalleProducto em where em.estado='AC' and em.gestion.id="+gestion.getId()+" order by em.fecha asc";
		return executeQueryResulList(query);
	}

	public List<DetalleProducto> findAllActivoAndGestionOrderedByID(Gestion gestion) {
		String query = "select em from DetalleProducto em where em.estado='AC' and em.gestion.id="+gestion.getId()+" order by em.fecha asc";
		return executeQueryResulList(query);
	}

	/**
	 * PEPS
	 * Obtener detalleProducto por fechas 
	 * @param producto
	 * @return
	 */
	public List<DetalleProducto> findAllByProductoOrderByFecha(Producto producto,Gestion gestion) {
		try{
			String query = "select em from DetalleProducto em where em.estado='AC' and em.gestion.id="+gestion.getId()+" and em.producto.id="
					+ producto.getId() +" order by em.fecha desc";
			return executeQueryResulList(query);
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * PEPS
	 * Obtener detalleProducto por fechas
	 *  @param almacen 
	 * @param producto
	 * @return
	 */
	public List<DetalleProducto> findAllByProductoAndAlmacenOrderByFecha(Almacen almacen,Producto producto,Gestion gestion) {
		try{
			String query = "select em from DetalleProducto em where em.estado='AC' and em.gestion.id="+gestion.getId()+" and em.producto.id="
					+ producto.getId() +" and em.almacen.id="+almacen.getId()+" order by em.fecha asc";
			return executeQueryResulList(query);
		}catch(Exception e){
			return null;
		}
	}
	//select em from DetalleProducto em where em.estado='AC' and em.producto.id=21 and em.almacen.id=3 order by em.fecha desc
}
