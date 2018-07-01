package com.erp360.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.erp360.model.AlmacenProducto;
import com.erp360.model.DetalleOrdenIngreso;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.KardexProducto;
import com.erp360.model.MovimientoCaja;
import com.erp360.model.OrdenIngreso;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.O;
import com.erp360.util.P;
import com.erp360.util.Q;
import com.erp360.util.U;
import com.erp360.util.V;
import com.erp360.util.W;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Stateless
public class OrdenIngresoDao extends DataAccessObjectJpa<OrdenIngreso,DetalleOrdenIngreso,AlmacenProducto,KardexProducto,O, P, Q, U, V, W> {

	private @Inject MovimientoCajaDao movimientoCajaDao;
	private @Inject TipoCambioDao  tipoCambioDao;

	public OrdenIngresoDao() {
		super(OrdenIngreso.class,DetalleOrdenIngreso.class,AlmacenProducto.class,KardexProducto.class);
	}

	public boolean registrar(OrdenIngreso ordenIngreso,List<DetalleOrdenIngreso> listDetalleOrdenIngreso){
		try{
			beginTransaction();
			ordenIngreso = create(ordenIngreso);
			for(DetalleOrdenIngreso det: listDetalleOrdenIngreso){
				det.setOrdenIngreso(ordenIngreso);
				createE(det);
			}
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Orden Ingreso Nº "+ordenIngreso.getCorrelativo());
			return true;
		}catch(Exception e){
			rollbackTransaction();
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			return false;
		}
	}

	public boolean procesar(Empresa empresa,String tipoTransaccion,Usuario usuarioAprobacion,OrdenIngreso ordenIngreso,List<AlmacenProducto> listAlmacenProducto){
		try{
			beginTransaction();
			update(ordenIngreso);
			for(AlmacenProducto ap:listAlmacenProducto){
				ap.setOrdenIngreso(ordenIngreso);
				createR(ap);
				//kardex
				KardexProducto kardex = new KardexProducto();
				kardex.setEstado("AC");
				kardex.setFechaRegistro(ap.getFechaRegistro());
				kardex.setOrdenIngreso(ordenIngreso);
				kardex.setStockEntrante(ap.getStock());
				kardex.setTipoMovimiento("OI");//orden de ingreso
				kardex.setTipoTransaccion(tipoTransaccion);
				kardex.setPrecioEntradaCompra(ap.getPrecioCompra());
				kardex.setPrecioEntradaVenta(ap.getPrecioVentaContado());
				kardex.setUsuarioRegistro(ap.getUsuarioRegistro());
				kardex.setAlmacen(ap.getAlmacen());
				kardex.setProducto(ap.getProducto());
				createS(kardex);
			}
			//EGRESO DE CAJA | verificar si va a ordenes por pagar
			if( !  ordenIngreso.isPorPagar()){
				MovimientoCaja movimientoCaja = new MovimientoCaja();
				movimientoCaja.setCorrelativo(String.format("%06d",movimientoCajaDao.correlativoMovimiento(ordenIngreso.getGestion())));
				movimientoCaja.setEstado("PR");
				movimientoCaja.setFechaAprobacion(ordenIngreso.getFechaRegistro());
				movimientoCaja.setFechaDocumento(ordenIngreso.getFechaRegistro());
				movimientoCaja.setFechaRegistro(ordenIngreso.getFechaRegistro());
				movimientoCaja.setGestion(ordenIngreso.getGestion());
				movimientoCaja.setMoneda("DOLAR");
				movimientoCaja.setMotivoIngreso("EGRESO POR COMPRA");
				movimientoCaja.setNumeroDocumento(ordenIngreso.getNumeroDocumento());
				movimientoCaja.setObservacion("Ninguna");
				movimientoCaja.setTipoCambio(tipoCambioDao.obtenerPorEmpresaYFecha(empresa, new Date()).getUnidad());
				movimientoCaja.setTipoDocumento(ordenIngreso.getTipoDocumento());
				movimientoCaja.setTotalImporteExtranjero(ordenIngreso.getTotalImporte());
				movimientoCaja.setTotalImporteNacional(ordenIngreso.getTotalImporte()*movimientoCaja.getTipoCambio());
				movimientoCaja.setUsuarioAprobacion(usuarioAprobacion);
				movimientoCaja.setUsuarioRegistro(ordenIngreso.getUsuarioRegistro());
				movimientoCaja.setTipo("EGRESO");
				movimientoCaja = movimientoCajaDao.registrarBasic(movimientoCaja);
			}else{
				//Generar una orden por pagar
			}
			commitTransaction();
			FacesUtil.infoMessage("Proceso Correcto", "Orden Ingreso Nº "+ordenIngreso.getCorrelativo());
			return true;
		}catch(Exception e){
			rollbackTransaction();
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			return false;
		}
	}

	public boolean modificar(OrdenIngreso ordenIngreso,List<DetalleOrdenIngreso> listDetalleOrdenIngreso,List<DetalleOrdenIngreso> listDetalleOrdenIngresoEliminados,List<AlmacenProducto> listAlmacenProducto){
		try{
			beginTransaction();
			update(ordenIngreso);
			for(DetalleOrdenIngreso det: listDetalleOrdenIngreso){
				det.setOrdenIngreso(ordenIngreso);
				updateE(det);
			}
			for(DetalleOrdenIngreso d: listDetalleOrdenIngresoEliminados){
				if(d.getId() != 0){
					d.setEstado("RM");
					//detalleOrdenIngresoRegistration.updated(d);
				}
			}
			for(AlmacenProducto ap:listAlmacenProducto){
				updateR(ap);
			}
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Orden Ingreso Nº "+ordenIngreso.getCorrelativo());
			return true;
		}catch(Exception e){
			rollbackTransaction();
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			return false;
		}
	}

	public boolean eliminar(OrdenIngreso ordenIngreso,List<DetalleOrdenIngreso> listDetalleOrdenIngreso,List<AlmacenProducto> listAlmacenProducto){
		try{
			beginTransaction();
			ordenIngreso.setEstado("RM");
			update(ordenIngreso);
			for(DetalleOrdenIngreso det: listDetalleOrdenIngreso){
				det.setEstado("RM");
				updateE(det);
			}
			for(AlmacenProducto ap:listAlmacenProducto){
				ap.setEstado("RM");
				updateR(ap);
			}
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "Orden Ingreso Nº "+ordenIngreso.getCorrelativo());
			return true;
		}catch(Exception e){
			rollbackTransaction();
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			return false;
		}
	}

	public List<OrdenIngreso> findAllOrderedByID() {
		String query = "select ser from OrdenIngreso ser where ser.estado='AC' or ser.estado='IN' or ser.estado='PR' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<OrdenIngreso> findAllOrderedByIDGestion(Gestion gestion) {
		String query = "select ser from OrdenIngreso ser where ( ser.estado='AC' or ser.estado='PR' or ser.estado='AN') and ser.gestion.id="+gestion.getId()+" order by ser.id desc";
		return executeQueryResulList(query);
	}

	public int obtenerNumeroOrdenIngreso2( Gestion gestion){
		//Integer year = Integer.parseInt( new SimpleDateFormat("yyyy").format(date));
		String query = "select em from OrdenIngreso em where (em.estado='AC' or em.estado='IN' or em.estado='PR') and em.gestion.id="+gestion.getId()+"  order by em.id asc";
		List<OrdenIngreso> list = executeQueryResulList(query);
		OrdenIngreso orden = list.size()>0?list.get(list.size()-1):null;
		String query2 = "select em.correlativo from orden_salida em ";

		return orden==null? 1 :Integer.valueOf(orden.getCorrelativo())+ 1;
	}

	public int obtenerNumeroOrdenIngreso( Gestion gestion){
		String query = "select count(em.id) from  orden_ingreso em where (em.estado='AC' or em.estado='IN' or em.estado='PR') and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}

	public int totalNuevosPorGestion( Gestion gestion){
		String query = "select count(em.id) from  orden_ingreso em where em.estado='AC' and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue();	
	}

	public int totalProcesadosPorGestion( Gestion gestion){
		String query = "select count(em.id) from  orden_ingreso em where em.estado='PR' and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue();	
	}

	public List<OrdenIngreso> obtenerPorTamanio(int inicio,int maxRows,Map filters) {
		return findAllBySize(inicio, maxRows,filters);
	}

}
