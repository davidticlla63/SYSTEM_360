package com.erp360.dao;

import java.util.Date;
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
public class UsuarioDao extends DataAccessObjectJpa<Usuario,UsuarioRol,R,S,O, P, Q, U, V, W> {

	public UsuarioDao(){
		super(Usuario.class,UsuarioRol.class);
	}

	public boolean registrar(Usuario usuario,UsuarioRol usuarioRol){
		try{
			beginTransaction();
			usuario = create(usuario);
			usuarioRol.setUsuario(usuario);
			createE(usuarioRol);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Usuario "+usuario.getNombre());
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
	
	public boolean modificar(Usuario usuario,UsuarioRol usuarioRol){
		try{
			beginTransaction();
			update(usuario);
			updateE(usuarioRol);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Usuario "+usuario.getNombre());
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

	public Usuario modificar(Usuario usuario){
		try{
			beginTransaction();
			usuario = update(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Usuario "+usuario.getNombre());
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

	public boolean eliminar(Usuario usuario){
		try{
			beginTransaction();
			usuario.setLogin(new Date()+"|"+usuario.getLogin());
			Usuario usr = modificar(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "Usuario "+usuario.getNombre());
			return usr!=null?true:false;
		}catch(Exception e){
			FacesUtil.errorMessage("Error al eliminar");
			rollbackTransaction();
			return false;
		}
	}

	public boolean eliminar(Usuario usuario,UsuarioRol usuarioRol){
		try{
			beginTransaction();
			usuario.setState("RM");
			update(usuario);
			usuarioRol.setEstado("RM");
			updateE(usuarioRol);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Usuario "+usuario.getNombre());
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
	
	public Usuario obtenerPorLogin(String login, String password){
		try{
			return findByParameterObjectTwo("login", "password", login, password);
		}catch(Exception e){
			return null;
		}
	}
	
	public Usuario obtenerPorLogin(String login){
		try{
			return findByParameter("login", login);
		}catch(Exception e){
			return null;
		}
	}

	public List<Usuario> obtenerUsuarioOrdenAscPorId(){
		return findAscAllOrderedByParameter("id");
	}

	public List<Usuario> obtenerUsuarioOrdenDescPorId(){
		return findDescAllOrderedByParameter("id");
	}
}
