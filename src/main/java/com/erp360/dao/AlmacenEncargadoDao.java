package com.erp360.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Almacen;
import com.erp360.model.AlmacenEncargado;
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
public class AlmacenEncargadoDao extends DataAccessObjectJpa<AlmacenEncargado,E,R,S,O, P, Q, U, V, W> {

	public AlmacenEncargadoDao(){
		super(AlmacenEncargado.class);
	}

	public AlmacenEncargado registrar(AlmacenEncargado almacen){
		try{
			beginTransaction();
			almacen = create(almacen);
			commitTransaction();
			return almacen;
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

	public boolean modificar(AlmacenEncargado almacen){
		try{
			beginTransaction();
			update(almacen);
			commitTransaction();
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

	public List<AlmacenEncargado> obtenerTodosOrdenadosPorId() {
		String query = "select ser from AlmacenEncargado ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<AlmacenEncargado> obtenerTodosActivosOrdenadosPorId() {
		String query = "select ser from AlmacenEncargado ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<AlmacenEncargado> obtenerPorAlmacen(Almacen almacen) {
		String query = "select ser from AlmacenEncargado ser where (ser.estado='AC' or ser.estado='IN') and ser.almacen.id="
				+ almacen.getId() + " order by ser.id desc";
		List<AlmacenEncargado> listAlmacen = executeQueryResulList(query);
		return listAlmacen;
	}

	public List<AlmacenEncargado> obtenerPorUsuario(Usuario usuario) {
		String query = "select ser from AlmacenEncargado ser where (ser.estado='AC' or ser.estado='IN') and usuario.id="
				+ usuario.getId() + " order by ser.id desc";
		List<AlmacenEncargado> listAlmacen = executeQueryResulList(query);
		return listAlmacen;
	}

}
