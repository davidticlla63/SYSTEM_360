package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.DetalleTomaInventarioOrdenIngreso;
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
public class DetalleTomaInventarioOrdenIngresoDao extends DataAccessObjectJpa<DetalleTomaInventarioOrdenIngreso,E,R,S,O, P, Q, U, V, W> {

	public DetalleTomaInventarioOrdenIngresoDao(){
		super(DetalleTomaInventarioOrdenIngreso.class);
	}

	public List<DetalleTomaInventarioOrdenIngreso> findAllOrderByDesc() {
		String query = "select em from DetalleTomaInventarioOrdenIngreso em, OrdenIngreso oc where em.estado='AC' and em.estado='IN'and oc.id=em.ordenIngreso.id  order by em.id desc";
		return executeQueryResulList(query);
	}

	public List<DetalleTomaInventarioOrdenIngreso> findAllByOrdenIngreso(OrdenIngreso ordenIngreso) {
		String query = "select em from DetalleOrdenIngreso em where em.estado='AC' and em.ordenIngreso.id="+ordenIngreso.getId()+"  order by em.id desc";
		return executeQueryResulList(query);
	}
	
	public DetalleTomaInventarioOrdenIngreso findByOrdenIngreso(OrdenIngreso ordenIngreso) {
		String query = "select em from DetalleTomaInventarioOrdenIngreso em where (em.estado='AC' or em.estado='IN') and em.ordenIngreso.id="+ordenIngreso.getId();
		return (DetalleTomaInventarioOrdenIngreso) executeQuerySingleResult(query);
	}
	
	public DetalleTomaInventarioOrdenIngreso findByTomaInventario(TomaInventario tomaInventario) {
		String query = "select em from DetalleTomaInventarioOrdenIngreso em where (em.estado='AC' or em.estado='IN') and em.tomaInventario.id="+tomaInventario.getId();
		return (DetalleTomaInventarioOrdenIngreso) executeQuerySingleResult(query);
	}

}
