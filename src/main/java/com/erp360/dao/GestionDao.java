package com.erp360.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
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
public class GestionDao extends DataAccessObjectJpa<Gestion,E,R,S,O, P, Q, U, V, W> {

	public GestionDao(){
		super(Gestion.class);
	}

	public List<Gestion> findAllByEmpresa(Empresa empresa){
		String query = "select em from Gestion em  where em.empresa.id="+empresa.getId()+" order by em.gestion desc";
		return executeQueryResulList(query);
	}
	public Gestion findByGestionCierreActivo() {
		String query = "select em from Gestion em where em.estadoCierre = 'AC'";
		return (Gestion) executeQuerySingleResult(query);
	}

	public List<Gestion> obtenerOrdenAscPorId(){
		return findAscAllOrderedByParameter("id");
	}

	public List<Gestion> obtenerOrdenDescPorId(){
		return findDescAllOrderedByParameter("id");
	}

	public List<Gestion> obtenerActivosOrdenDescPorId(){
		return findAllByEstadoOrderByDesc("AC");
	}

	public List<Gestion> obtenerActivosOrdenAscPorId(){
		try{
			return findAllByEstadoOrderByAsc("AC");
		}catch(Exception e){
			return new ArrayList<Gestion>();
		}
	}
}
