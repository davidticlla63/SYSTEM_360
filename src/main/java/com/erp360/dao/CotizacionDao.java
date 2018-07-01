package com.erp360.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Cliente;
import com.erp360.model.Cotizacion;
import com.erp360.model.DetalleCotizacion;
import com.erp360.model.Gestion;
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
public class CotizacionDao extends DataAccessObjectJpa<Cotizacion,DetalleCotizacion, R, S, O, P, Q, U, V, W> {


	public CotizacionDao(){
		super(Cotizacion.class);
	}

	/**
	 * Registrar Nota Venta, implica la disminucion de stock de los productos a traves de orden de salida
	 * @param notaVenta
	 * @param listDetalleNotaVenta
	 * @param listPlanCobranza
	 * @param gestionSesion
	 * @param param
	 * @param param2
	 * @return
	 */
	public Cotizacion registrar(Cotizacion cotizacion,List<DetalleCotizacion> listDetalleCotizacion){
		try{
			beginTransaction();
			cotizacion = create(cotizacion);
			for(DetalleCotizacion dnv:listDetalleCotizacion){
				dnv.setId(0);
				dnv.setEstado("AC");
				dnv.setFechaRegistro(cotizacion.getFechaRegistro());
				dnv.setCotizacion(cotizacion);
				dnv.setUsuarioRegistro(cotizacion.getUsuarioRegistro());
				createE(dnv);
			}
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Cotizacion "+cotizacion.getCodigo());
			return cotizacion;
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

	public List<Cotizacion> obtenerTodosOrdenadosPorId() {
		String query = "select ser from Cotizacion ser where ser.estado='AC' or ser.estado='IN' or ser.estado='PN' or ser.estado='PR' order by ser.id desc";
		return executeQueryResulList(query);
	}
	
	public List<Cotizacion> obtenerTodosEntreFechas(Date fechaInicial,Date fechaFinal) {
		String query = "select ser from Cotizacion ser where (ser.estado='AC' OR ser.estado='IN' OR ser.estado='PN' OR ser.estado='PR') AND ser.fechaRegistro>=:stDate AND ser.fechaRegistro<=:edDate order by ser.id desc";
		//return executeQueryResulList(query);
		return getEm().createQuery(query).setParameter("stDate", fechaInicial).setParameter("edDate", fechaFinal).getResultList();
	}

	public List<Cotizacion> obtenerTodosActivosOrdenadosPorId() {
		String query = "select ser from Cotizacion ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public int correlativoNotaVenta2(){
		return countTotalRecord("nota_venta").intValue();
	}

	public int correlativoCotizacion( Gestion gestion){
		String query = "select count(em) from  cotizacion em where (em.estado='AC' or em.estado='IN' or em.estado='PR' or em.estado='PN') and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}

	public List<Cotizacion> obtenerPorCliente(Cliente cliente){
		try{
			return findAllActiveOtherTableAndParameter("Cliente", "cliente", "id", cliente.getId());
		}catch(Exception e){
			return new ArrayList<>();
		}
	}
}
