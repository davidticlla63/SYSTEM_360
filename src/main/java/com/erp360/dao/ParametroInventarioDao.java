package com.erp360.dao;


import javax.ejb.Stateless;

import com.erp360.model.ParametroInventario;
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
public class ParametroInventarioDao extends DataAccessObjectJpa<ParametroInventario,E,R,S,O, P, Q, U, V, W> {

	public ParametroInventarioDao(){
		super(ParametroInventario.class);
	}

	public boolean modificar(ParametroInventario usuario){
		try{
			beginTransaction();
			usuario = update(usuario);
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

	public ParametroInventario obtenerParametroInventario(){
		return findById(1);
	}

}
