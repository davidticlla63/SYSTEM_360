package com.erp360.dao;


import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.Sucursal;
import com.erp360.model.Usuario;
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
public class EmpresaDao extends DataAccessObjectJpa<Empresa,E,R,S,O, P, Q, U, V, W> {

	public EmpresaDao(){
		/*
		 * Empresa T
		 * Gestion E
		 * Sucursal R
		 * Nivel S
		 * UsuarioEmpresa O
		 * TipoCambio P
		 * TipoUfv Q
		 * List<TipoCuenta> U
		 * List<MonedaEmpresa> V
		 * PlanCuenta W
		 */
		super(Empresa.class);
	}

	public Empresa modificar(Empresa empresa){
		try{
			beginTransaction();
			empresa = update(empresa);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Empresa "+empresa.getId());
			return empresa;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return null;
		}
	}
	
	public List<Empresa> findAllOrderedByID() {
		String query = "select em from Empresa em where em.estado='AC' or ser.estado='IN' order by em.id desc";
		return executeQueryResulList(query);// em.createQuery(query).getResultList();
	}

	public Empresa findByRazonSocial(String razonSocial) {
		String query = "select em from Empresa em where em.razonSocial='"+razonSocial+"'";
		return (Empresa) executeQuerySingleResult(query);// em.createQuery(query).getSingleResult();
	}

	public Empresa findByNIT(String nit) {
		String query = "select em from Empresa em where em.nit='"+nit+"'";
		return (Empresa) executeQuerySingleResult(query);//em.createQuery(query).getSingleResult();
	}

	//    public List<Empresa> findAll(){
	//    	CriteriaBuilder cb = em.getCriteriaBuilder();
	//		CriteriaQuery<Empresa> criteria = cb.createQuery(Empresa.class);
	//		Root<Empresa> company = criteria.from(Empresa.class);
	//		criteria.select(company);
	//		return em.createQuery(criteria).getResultList();
	//    }

	public List<Empresa> findAllByUsuario(Usuario u) {
		String query = "select em from Empresa em ,UsuarioEmpresa ue where (em.estado='AC' or em.estado='IN') and ue.usuario.id="+u.getId()
				+ " and em.id=ue.empresa.id  order by em.id desc";
		return executeQueryResulList(query);//em.createQuery(query).getResultList();
	}

	public List<Empresa> findAllActivasByUsuario(Usuario u) {
		String query = "select em from Empresa em ,UsuarioEmpresa ue where em.estado='AC' and ue.usuario.id="+u.getId()
				+ " and em.id=ue.empresa.id  order by em.id desc";
		return executeQueryResulList(query);//em.createQuery(query).getResultList();
	}

	public Empresa findByUsuarioEmpresa(Usuario u, String nombreEmpresa) {
		String query = "select em from Empresa em ,UsuarioEmpresa ue where ue.usuario.id="+u.getId()
				+ " and em.id=ue.empresa.id and em.razonSocial='"+nombreEmpresa+"' order by em.id desc";
		return (Empresa) executeQuerySingleResult(query);//em.createQuery(query).getSingleResult();
	}


}
