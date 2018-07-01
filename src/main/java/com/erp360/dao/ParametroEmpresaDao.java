package com.erp360.dao;


import javax.ejb.Stateless;

import com.erp360.model.Empresa;
import com.erp360.model.ParametroEmpresa;
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
public class ParametroEmpresaDao extends DataAccessObjectJpa<ParametroEmpresa,E,R,S,O, P, Q, U, V, W> {

	public ParametroEmpresaDao(){
		super(ParametroEmpresa.class);
	}

	public ParametroEmpresa registrarParametroEmpresa(ParametroEmpresa usuario){
		try{
			beginTransaction();
			usuario = create(usuario);
			commitTransaction();
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

	public ParametroEmpresa modificarParametroEmpresa(ParametroEmpresa usuario){
		try{
			beginTransaction();
			usuario = update(usuario);
			commitTransaction();
			return usuario;
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

	public ParametroEmpresa obtenerParametroEmpresaPorEmpresa(Empresa empresa){
		return findByParameter("empresa", empresa);
	}
	
	public ParametroEmpresa obtener(){
		return findById(1);
	}

}
