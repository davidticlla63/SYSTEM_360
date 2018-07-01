package com.erp360.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Gestion;
import com.erp360.model.OrdenTraspaso;
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
public class OrdenTraspasoDao extends DataAccessObjectJpa<OrdenTraspaso,E,R,S,O, P, Q, U, V, W> {

	public OrdenTraspasoDao() {
		super(OrdenTraspaso.class);
	}
	
	public int obtenerNumeroOrdenTraspaso( Gestion gestion){
		String query = "select count(em) from  orden_traspaso em where (em.estado='AC' or em.estado='IN' or em.estado='PR') and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}

	public List<OrdenTraspaso> findAllOrderedByID() {
		String query = "select ser from OrdenTraspaso ser where ser.estado='AC' or ser.estado='IN' or ser.estado='PR' order by ser.id desc";
		System.out.println("Query OrdenIngreso: " + query);
		return executeQueryResulList(query);
	}
	
	public List<OrdenTraspaso> findAllOrderedByIDGestion(Gestion gestion) {
		String query = "select ser from OrdenTraspaso ser where ( ser.estado='AC' or ser.estado='IN' or ser.estado='PR') and ser.gestion.id="+gestion.getId()+" order by ser.id desc";
		return executeQueryResulList(query);
	}
	
	public int obtenerNumeroOrdenIngreso(Date date, Gestion gestion){
		Integer year = Integer.parseInt( new SimpleDateFormat("yyyy").format(date));
		String query = "select em from OrdenTraspaso em where (em.estado='AC' or em.estado='IN' or em.estado='PR') and em.gestion.id="+gestion.getId()+" and date_part('year', em.fechaDocumento) ="+year;
		return (( List<OrdenTraspaso>)executeQueryResulList(query)).size() + 1;
	}
	
	public int totalNuevosPorGestion( Gestion gestion){
		String query = "select count(em.id) from  orden_traspaso em where em.estado='AC' and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue();	
	}
	
	public int totalProcesadosPorGestion( Gestion gestion){
		String query = "select count(em.id) from  orden_traspaso em where em.estado='PR' and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue();	
	}
	
//	public double contarOrdenesActivas(Gestion gestion){
//		String query = "select count(em) from OrdenTraspaso em where em.estado='AC' and em.gestion.id="+gestion.getId();
//		return (Long)executeQuerySingleResult(query);
//	}
//
//	public double contarOrdenesProcesadas(Gestion gestion){
//		String query = "select count(em) from OrdenTraspaso em where em.estado='PR' and em.gestion.id="+gestion.getId();
//		System.out.println("Query contarOrdenesActivas: "+query);
//		return (Long)em.createQuery(query).getSingleResult();
//	}
//	
//	public double contarOrdenesTotales(Gestion gestion){
//		String query = "select count(em) from OrdenTraspaso em where (em.estado='AC' or em.estado='PR') and em.gestion.id="+gestion.getId();
//		System.out.println("Query contarOrdenesTotales: "+query);
//		return (Long)em.createQuery(query).getSingleResult();
//	}

}
