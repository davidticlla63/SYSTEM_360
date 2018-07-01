package com.erp360.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Empresa;
import com.erp360.model.Sucursal;
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
public class SucursalDao extends DataAccessObjectJpa<Sucursal,E,R,S,O, P, Q, U, V, W> {

	public SucursalDao(){
		super(Sucursal.class);
	}

	public Sucursal registrar(Sucursal usuario){
		try{
			beginTransaction();
			usuario = create(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Sucursal "+usuario.getNombre());
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

	public boolean modificar(Sucursal usuario){
		try{
			beginTransaction();
			usuario = update(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Sucursal "+usuario.getNombre());
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

	public boolean eliminar(Sucursal sucursal){
		try{
			beginTransaction();
			sucursal.setNombre(sucursal.getNombre()+"|"+new Date());
			sucursal.setEstado("RM");
			update(sucursal);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "Sucursal "+sucursal.getNombre());
			return true;
		}catch(Exception e){
			FacesUtil.errorMessage("Error al eliminar");
			rollbackTransaction();
			return false;
		}
	}

	public List<Sucursal> obtenerSucursalOrdenAscPorId(){
		return findAscAllOrderedByParameter("id");
	}
	
	public List<Sucursal> obtenerUsuarioOrdenDescPorId(){
		return findDescAllOrderedByParameter("id");
	}
	
	public List<Sucursal> obtenerPorEmpresa(Empresa empresa){
		return findAllActivosByParameter("empresa", empresa);
	}
}
