package com.erp360.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.NotaVenta;
import com.erp360.model.PlanCobranza;
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
public class PlanCobranzaDao extends DataAccessObjectJpa<PlanCobranza,E,R,S,O, P, Q, U, V, W> {

	public PlanCobranzaDao(){
		super(PlanCobranza.class);
	}
	
	public List<PlanCobranza> obtenerOrdenAscPorId(){
		return findAscAllOrderedByParameter("id");
	}

	public List<PlanCobranza> obtenerOrdenDescPorId(){
		return findDescAllOrderedByParameter("id");
	}
	
	public List<PlanCobranza> obtenerPlanCobranzaPorVenta(NotaVenta notaVenta) {
		String query = "select ser from PlanCobranza ser where ser.notaVenta.id="+notaVenta.getId();
		return executeQueryResulList(query);
	}
	
	public List<PlanCobranza> obtenerPorNotaVenta(NotaVenta notaVenta){
		try{
			return findAllActiveOtherTableAndParameter("NotaVenta", "notaVenta", "id", notaVenta.getId());
		}catch(Exception e){
			return new ArrayList<>();
		}
	}
	
	public List<PlanCobranza> obtenerPorReservaVenta(ReservaVenta reservaVenta){
		try{
			return findAllActiveOtherTableAndParameter("ReservaVenta", "reservaVenta", "id", reservaVenta.getId());
		}catch(Exception e){
			return new ArrayList<>();
		}
	}
}
