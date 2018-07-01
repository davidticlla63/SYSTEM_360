package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.DetalleOrdenIngreso;
import com.erp360.model.DetalleTomaInventario;
import com.erp360.model.TomaInventario;
import com.erp360.util.E;
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
public class DetalleTomaInventarioDao extends DataAccessObjectJpa<DetalleTomaInventario,E,R,S,O, P, Q, U, V, W> {

	public DetalleTomaInventarioDao(){
		super(DetalleTomaInventario.class);
	}
	
	public DetalleTomaInventario registrarBasic(DetalleTomaInventario object){
		try{
			beginTransaction();
			object = create(object);
			commitTransaction();
			return object;
		}catch(Exception e){
			String cause = e.getMessage();
			System.out.println(e.getMessage());
			rollbackTransaction();
			return null;
		}
	}

	public boolean modificar(DetalleTomaInventario object){
		try{
			beginTransaction();
			update(object);
			commitTransaction();
			return true;
		}catch(Exception e){
			String cause = e.getMessage();
			System.out.println(e.getMessage());
			rollbackTransaction();
			return false;
		}
	}
	
	public List<DetalleTomaInventario> findAllOrderedByID() {
		String query = "select ser from DetalleTomaInventario ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		return executeQueryResulList(query);
	}
	
	public List<DetalleTomaInventario> findAllByTomaInventario(TomaInventario tomaInventario) {
		String query = "select ser from DetalleTomaInventario ser where ( ser.estado='AC' or ser.estado='IN') and ser.tomaInventario.id="+tomaInventario.getId()+" order by ser.id desc";
		return  executeQueryResulList(query);
	}
	
	public List<DetalleTomaInventario> findAllActivosByTomaInventario(TomaInventario tomaInventario) {
		String query = "select ser from DetalleTomaInventario ser where ser.estado='AC' and ser.tomaInventario.id="+tomaInventario.getId()+" order by ser.id desc";
		return  executeQueryResulList(query);
	}
	
	public DetalleTomaInventario findByTomaInventario(TomaInventario tomaInventario) {
		String query = "select ser from DetalleTomaInventario ser where ( ser.estado='AC' or ser.estado='IN') and ser.tomaInventario.id="+tomaInventario.getId();
		return (DetalleTomaInventario) executeQuerySingleResult(query);
	}



}
