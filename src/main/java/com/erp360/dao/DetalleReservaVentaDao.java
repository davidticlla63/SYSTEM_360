package com.erp360.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.DetalleReservaVenta;
import com.erp360.model.ReservaVenta;
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
public class DetalleReservaVentaDao extends DataAccessObjectJpa<DetalleReservaVenta, E, R, S, O, P, Q, U, V, W> {

	
	public DetalleReservaVentaDao(){
		super(DetalleReservaVenta.class);
	}

	
	public List<DetalleReservaVenta> obtenerPorReservaVenta(ReservaVenta reservaVenta){
		try{
			String query = "select em from DetalleReservaVenta em where em.reservaVenta.id="+reservaVenta.getId();
			return executeQueryResulList(query);
		}catch(Exception e){
			return new ArrayList<>();
		}
	}

}
