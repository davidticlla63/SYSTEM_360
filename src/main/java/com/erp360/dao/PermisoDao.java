package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.erp360.util.E;
import com.erp360.util.O;
import com.erp360.util.P;
import com.erp360.util.Q;
import com.erp360.util.R;
import com.erp360.util.S;
import com.erp360.util.U;
import com.erp360.util.V;
import com.erp360.util.W;
import com.erp360.model.Permiso;
import com.erp360.model.Roles;

@Stateless
public class PermisoDao  extends DataAccessObjectJpa<Permiso,E,R,S,O, P, Q, U, V, W> {


	public PermisoDao() {
		super(Permiso.class);
	}
	
	public List<Permiso> findAllOrderByName() {
		String query = "select em from Permiso em where em.estado='AC' order by em.nombre desc ";
		return  executeQueryResulList(query);
	}
	
	public List<Permiso> obtenerPermisoByRol(Roles rol){
		//return findAllByParameter("rol",rol);
		String query = "select em ";
		return findAllByParameterObjectTwoOrderByDesc("estado", "roles","AC", rol);
	}
}