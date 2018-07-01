package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.erp360.util.E;
import com.erp360.util.O;
import com.erp360.util.P;
import com.erp360.util.Q;
import com.erp360.util.R;
import com.erp360.util.S;
import com.erp360.util.U;
import com.erp360.util.V;
import com.erp360.util.W;

import com.erp360.model.Modulo;

@Stateless
public class ModuloDao extends DataAccessObjectJpa<Modulo,E,R,S,O, P, Q, U, V, W> {

	public ModuloDao() {
		super(Modulo.class);
	}
	
	public List<Modulo> findAllOrderByName() {
		String query = "select em from Modulo em where em.estado='AC' order by em.nombre desc ";
		return executeQueryResulList(query);
	}
	
	public List<Modulo> findAllOrderByID() {
		String query = "select em from Modulo em where em.estado='AC' order by em.id asc ";
		return executeQueryResulList(query);
	}
}