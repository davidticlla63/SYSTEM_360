package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.DetalleOrdenIngreso;
import com.erp360.model.DetalleTomaInventario;
import com.erp360.model.OrdenIngreso;
import com.erp360.model.TomaInventario;
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
public class DetalleOrdenIngresoDao extends DataAccessObjectJpa<DetalleOrdenIngreso,E,R,S,O, P, Q, U, V, W> {

	public DetalleOrdenIngresoDao(){
		super(DetalleOrdenIngreso.class);
	}
	
	public DetalleOrdenIngreso registrarBasic(DetalleOrdenIngreso object){
		try{
			beginTransaction();
			object = create(object);
			commitTransaction();
			return object;
		}catch(Exception e){
			String cause = e.getMessage();
			System.out.println(e.getMessage());
			rollbackTransaction();
			return null;
		}
	}
	
	public boolean modificar(DetalleOrdenIngreso object){
		try{
			beginTransaction();
			update(object);
			commitTransaction();
			return true;
		}catch(Exception e){
			String cause = e.getMessage();
			System.out.println(e.getMessage());
			rollbackTransaction();
			return false;
		}
	}
	
	public List<DetalleOrdenIngreso> obtenerPorOrdenIngreso(OrdenIngreso ordenIngreso) {
		String query = "select em from DetalleOrdenIngreso em where em.ordenIngreso.id="+ordenIngreso.getId()+" and (em.estado='AC' or em.estado='IN') order by em.id desc";
		return executeQueryResulList(query);
	}
	
	public List<DetalleOrdenIngreso> findAllOrderByDesc() {
		String query = "select em from DetalleOrdenIngreso em, OrdenIngreso oc where em.estado='AC' or em.estado='IN'and oc.id=em.ordenIngreso.id  order by em.id desc";
		return executeQueryResulList(query);
	}

	public List<DetalleOrdenIngreso> findAllByOrdenIngreso(OrdenIngreso ordenIngreso) {
		String query = "select em from DetalleOrdenIngreso em where em.estado='AC' and em.ordenIngreso.id="+ordenIngreso.getId()+"  order by em.id desc";
		return executeQueryResulList(query);
	}
}
