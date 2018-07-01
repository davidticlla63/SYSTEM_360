package com.erp360.dao;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Gestion;
import com.erp360.model.NotaVenta;
import com.erp360.model.OrdenSalida;
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
public class OrdenSalidaDao extends DataAccessObjectJpa<OrdenSalida,E,R,S,O, P, Q, U, V, W> {

	public OrdenSalidaDao(){
		super(OrdenSalida.class);
	}

	public OrdenSalida obtenerPorNotaVenta( NotaVenta notaVenta){
		try{
			String query = "select em from  OrdenSalida em where  em.notaVenta.id="+notaVenta.getId();
			return executeQuerySingleResult(query);
		}catch(Exception e){
			return new OrdenSalida();
		}
	}

	public int obtenerNumeroOrdenSalida( Gestion gestion){
		String query = "select count(em) from  orden_salida em where (em.estado='AC' or em.estado='IN' or em.estado='PR') and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}

	public List<OrdenSalida> findAllOrderedByID() {
		String query = "select ser from OrdenSalida ser where ser.estado='AC' or ser.estado='PR' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<OrdenSalida> findAllOrderedByIDGestion(Gestion gestion) {
		String query = "select ser from OrdenSalida ser where ( ser.estado='AC' or ser.estado='PR' or ser.estado='AN') and ser.gestion.id="+gestion.getId()+" order by ser.id desc";
		return executeQueryResulList(query);
	}

	public int totalNuevosPorGestion( Gestion gestion){
		String query = "select count(em.id) from  orden_salida em where em.estado='AC' and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue();	
	}

	public int totalProcesadosPorGestion( Gestion gestion){
		String query = "select count(em.id) from  orden_salida em where em.estado='PR' and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue();	
	}

	//	public List<OrdenSalida> findByFechas(Date fechaInicial,Date fechaFinal) {
	//		String query = "select os from DetalleOrdenSalida em,OrdenSalida os where em.ordenSalida.id=os.id and os.estado='PR' and os.fechaAprobacion>=:stDate and os.fechaAprobacion<=:edDate order by os.id desc";
	//		return em.createQuery(query).setParameter("stDate", fechaInicial).setParameter("edDate", fechaFinal).getResultList();
	//	}

	//	public double contarOrdenesActivas(Gestion gestion){
	//		String query = "select count(em) from OrdenSalida em where em.estado='AC' and em.gestion.id="+gestion.getId();
	//		return (Long)em.createQuery(query).getSingleResult();
	//	}
	//
	//	public double contarOrdenesProcesadas(Gestion gestion){
	//		String query = "select count(em) from OrdenSalida em where em.estado='PR' and em.gestion.id="+gestion.getId();
	//		return (Long)em.createQuery(query).getSingleResult();
	//	}
	//	
	//	public double contarOrdenesTotales(Gestion gestion){
	//		String query = "select count(em) from OrdenSalida em where (em.estado='AC' or em.estado='PR') and em.gestion.id="+gestion.getId();
	//		return (Long)em.createQuery(query).getSingleResult();
	//	}

}
