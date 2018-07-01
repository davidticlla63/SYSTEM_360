package com.erp360.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Roles;
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
public class RolDao extends DataAccessObjectJpa<Roles,E,R,S,O, P, Q, U, V, W> {

	public RolDao(){
		super(Roles.class);
	}
	
	public boolean registar(Roles roles){
		try{
			beginTransaction();
			create(roles);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Rol "+roles.getNombre());
			return true;
		}catch(Exception e){
			String cause = e.getMessage();
			System.out.println(e.getMessage());
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al registrar");
			}
			rollbackTransaction();
			return false;
		}
	}
	
	public boolean modificar(Roles roles){
		try{
			beginTransaction();
			update(roles);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Rol "+roles.getNombre());
			return true;
		}catch(Exception e){
			String cause = e.getMessage();
			System.out.println(e.getMessage());
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al registrar");
			}
			rollbackTransaction();
			return false;
		}
	}
	
	public boolean eliminar(Roles roles){
		try{
			beginTransaction();
			roles.setEstado("RM");
			roles.setNombre(new Date()+"|"+roles.getNombre());
			update(roles);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Rol "+roles.getNombre());
			return true;
		}catch(Exception e){
			String cause = e.getMessage();
			System.out.println(e.getMessage());
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al registrar");
			}
			rollbackTransaction();
			return false;
		}
	}

	public Roles obtenerPorUsuario(Usuario usuario){
		String query = "select em from Roles em where em.usuario.id="+usuario.getId();
		return (Roles) executeQuerySingleResult(query);
	}

	public List<Roles> obtenerOrdenAscPorId(){
		return findAscAllOrderedByParameter("id");
	}
	
	public List<Roles> obtenerOrdenDescPorId(){
		return findDescAllOrderedByParameter("id");
	}
	
	public Roles obtenerPorNombre(String nombre){
		String query = "select em from Roles em  where em.nombre='"+nombre+"'";
		return executeQuerySingleResult(query);
	}
}
