package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;
import com.erp360.model.DetalleOrdenTraspaso;
import com.erp360.model.OrdenTraspaso;
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
public class DetalleOrdenTraspasoDao extends DataAccessObjectJpa<DetalleOrdenTraspaso,E,R,S,O, P, Q, U, V, W> {

	public DetalleOrdenTraspasoDao() {
		super(DetalleOrdenTraspaso.class);
	}

	public List<DetalleOrdenTraspaso> findAllOrderByDesc() {
		String query = "select em from DetalleOrdenTraspaso em, OrdenTraspaso oc where em.estado='AC' and em.estado='IN'and oc.id=em.ordenTraspaso.id  order by em.id desc";
		return executeQueryResulList(query);
	}

	public List<DetalleOrdenTraspaso> findAllByOrdenTraspaso(OrdenTraspaso ordenTraspaso) {
		String query = "select em from DetalleOrdenTraspaso em where em.estado='AC' and em.ordenTraspaso.id="+ordenTraspaso.getId()+"  order by em.id desc";
		return executeQueryResulList(query);
	}
}
