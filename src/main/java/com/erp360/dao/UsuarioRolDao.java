package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Usuario;
import com.erp360.model.UsuarioRol;
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
public class UsuarioRolDao extends DataAccessObjectJpa<UsuarioRol,E,R,S,O, P, Q, U, V, W> {

	public UsuarioRolDao(){
		super(UsuarioRol.class);
	}

	public UsuarioRol registrar(UsuarioRol usuario){
		try{
			beginTransaction();
			usuario = create(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Usuario "+usuario.getId());
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

	public UsuarioRol modificar(UsuarioRol usuario){
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

	public UsuarioRol obtenerPorUsuario(Usuario usuario){
		String query = "select em from UsuarioRol em where em.usuario.id="+usuario.getId();
		return (UsuarioRol) executeQuerySingleResult(query);
	}

	public List<UsuarioRol> obtenerOrdenAscPorId(){
		return findAscAllOrderedByParameter("id");
	}
	
	public List<UsuarioRol> obtenerOrdenDescPorId(){
		return findDescAllOrderedByParameter("id");
	}
}
