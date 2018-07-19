package com.erp360.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Cliente;
import com.erp360.model.Ejecutivo;
import com.erp360.model.EjecutivoCliente;
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
public class EjecutivoClienteDao extends DataAccessObjectJpa<EjecutivoCliente,E,R,S,O, P, Q, U, V, W> {

	public EjecutivoClienteDao(){
		super(EjecutivoCliente.class);
	}

	public EjecutivoCliente registrar(EjecutivoCliente ejecutivoCliente){
		try{
			ejecutivoCliente = create(ejecutivoCliente);
			return ejecutivoCliente;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			return null;
		}
	}

	public boolean registrar(List<EjecutivoCliente> ejecutivoClientes){
		try{
			for(EjecutivoCliente ejecutivoCliente: ejecutivoClientes ){
				ejecutivoCliente = create(ejecutivoCliente);
			}
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			return false;
		}
	}

	public boolean modificar(EjecutivoCliente ejecutivoCliente){
		try{
			update(ejecutivoCliente);
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			return false;
		}
	}

	public boolean eliminar(EjecutivoCliente ejecutivoCliente){
		try{
			beginTransaction();
			ejecutivoCliente.setEstado("RM");
			update(ejecutivoCliente);
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			return false;
		}
	}	

	public List<EjecutivoCliente> getAllByIdEjecutivo(Ejecutivo ejecutivo) {
		try{
			String query = "select ser from EjecutivoCliente ser where (ser.estado='AC' or ser.estado='IN') and ser.ejecutivo.id="+ejecutivo.getId()+" order by ser.id desc";
			return executeQueryResulList(query);
		}catch (Exception e) {
			System.out.println("error: "+e.getMessage());
			return new ArrayList<>();
		}
	}
	
	public EjecutivoCliente getEjecutivoClienteByIdCliente(Cliente cliente) {
		String query = "select ser from EjecutivoCliente ser where (ser.estado='AC' or ser.estado='IN') and ser.cliente.id="+cliente.getId();
		return executeQuerySingleResult(query);
	}

}
