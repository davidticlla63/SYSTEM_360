package com.erp360.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.DetalleNotaVenta;
import com.erp360.model.NotaVenta;
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
public class DetalleNotaVentaDao extends DataAccessObjectJpa<DetalleNotaVenta,E, R, S, O, P, Q, U, V, W> {

	
	public List<DetalleNotaVenta> obtenerDetalleNotaVenta(NotaVenta notaVenta){
		try{
			String query = "select em from DetalleNotaVenta em where em.notaVenta.id="+notaVenta.getId();
			return executeQueryResulList(query);
		}catch(Exception e){
			return new ArrayList<>();
		}
	}
}
