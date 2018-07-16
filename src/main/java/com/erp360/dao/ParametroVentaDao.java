package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Empresa;
import com.erp360.model.ParametroCuota;
import com.erp360.model.ParametroVenta;
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
public class ParametroVentaDao extends DataAccessObjectJpa<ParametroVenta,ParametroCuota,R,S,O, P, Q, U, V, W> {

	public ParametroVentaDao(){
		super(ParametroVenta.class,ParametroCuota.class);
	}

	public boolean modificar(ParametroVenta usuario,List<ParametroCuota> listParametroCuota){
		try{
			beginTransaction();
			usuario = update(usuario);
			for(ParametroCuota pc: listParametroCuota){
				updateE(pc);
			}
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "");
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return false;
		}
	}

	public ParametroVenta obtener(){
		return findById(1);
	}
	
	public ParametroVenta obtenerPorEmpresa(Empresa empresa){
		try{
			String query = "SELECT em FROM ParametroVenta em WHERE em.empresa.id="+empresa.getId();
			System.out.println("query : "+query);
			return executeQuerySingleResult(query);
		}catch(Exception e){
			return null;
		}
	}

}
