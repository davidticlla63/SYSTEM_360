package com.erp360.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Privilegio;
import com.erp360.model.Roles;
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
public class PrivilegioDao extends DataAccessObjectJpa<Privilegio,E,R,S,O, P, Q, U, V, W> {

	public PrivilegioDao(){
		super(Privilegio.class);
	}

	public boolean registrar(List<Privilegio> listPrivilegioAnteriores,List<Privilegio> listPrivilegioNuevos){
		try{
			Date fechaActual = new Date();
			beginTransaction();
			for(Privilegio p: listPrivilegioAnteriores){
				p.setFechaModificacion(fechaActual);
				p.setEstado("RM");
				update(p);
			}
			for(Privilegio pe: listPrivilegioNuevos){
				pe.setFechaRegistro(fechaActual);
				create(pe);
			}
			commitTransaction();
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al registrar");
			}
			rollbackTransaction();
			return false;
		}
	}

	public Privilegio registrar(Privilegio usuario){
		try{
			beginTransaction();
			usuario = create(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Privilegio "+usuario.getId());
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

	public Privilegio modificar(Privilegio usuario){
		try{
			beginTransaction();
			usuario = update(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Usuario "+usuario.getId());
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


	public List<Privilegio> obtenerTodosPorRoles(Roles roles){
		String query = "select em from Privilegio em where em.estado='AC' and em.roles.id="+roles.getId();
		return executeQueryResulList(query);
	}

	public List<Privilegio> findAllByRol(Roles roles) {
		String query = "select em from Privilegio em where em.estado='AC' and em.roles.id="+roles.getId();
		return executeQueryResulList(query);
	}
}
