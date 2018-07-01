package com.erp360.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.AlmacenProducto;
import com.erp360.model.Cliente;
import com.erp360.model.DetalleOrdenSalida;
import com.erp360.model.DetalleReservaVenta;
import com.erp360.model.Gestion;
import com.erp360.model.KardexProducto;
import com.erp360.model.OrdenSalida;
import com.erp360.model.ParametroInventario;
import com.erp360.model.ParametroVenta;
import com.erp360.model.PlanCobranza;
import com.erp360.model.ReservaVenta;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.U;
import com.erp360.util.V;
import com.erp360.util.W;
/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@Stateless
public class ReservaVentaDao extends DataAccessObjectJpa<ReservaVenta,DetalleReservaVenta,PlanCobranza,OrdenSalida,DetalleOrdenSalida, AlmacenProducto, KardexProducto, U, V, W> {

	public ReservaVentaDao(){
		super(ReservaVenta.class,DetalleReservaVenta.class,PlanCobranza.class,OrdenSalida.class,DetalleOrdenSalida.class,AlmacenProducto.class,KardexProducto.class);
	}
	
	public boolean modificarBasic(ReservaVenta reservaVenta) throws Exception{
		try{
			update(reservaVenta);
			return true;
		}catch(Exception e){
			return false;
		}
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
	public ReservaVenta registrar(String observacion,Usuario usuarioAprobacion,ReservaVenta reservaVenta,List<DetalleReservaVenta> listDetalleReservaVenta,List<PlanCobranza> listPlanCobranza,Gestion gestionSesion,ParametroInventario param,ParametroVenta param2){
		try{
			beginTransaction();
			//crear reserva venta
			reservaVenta.setMovimientoCaja(null);
			//reservaVenta = create(reservaVenta);
			for(DetalleReservaVenta dnv:listDetalleReservaVenta){
				dnv.setId(0);
				dnv.setEstado("AC");
				dnv.setFechaRegistro(reservaVenta.getFechaRegistro());
				dnv.setReservaVenta(reservaVenta);
				dnv.setUsuarioRegistro(reservaVenta.getUsuarioRegistro());
				//createE(dnv);
			}
			reservaVenta.setListDetalleReservaVenta(listDetalleReservaVenta);
			for(PlanCobranza pc: listPlanCobranza){
				pc.setId(0);
				pc.setEstado("AC");
				pc.setFechaRegistro(reservaVenta.getFechaRegistro());
				pc.setNotaVenta(null);
				//pc.setReservaVenta(reservaVenta);
				pc.setUsuarioRegistro(reservaVenta.getUsuarioRegistro());
				//createR(pc);
			}
			//reservaVenta.setPlanCobranzas(listPlanCobranza);
			//----
			reservaVenta = create(reservaVenta);
			//----
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Reserva Venta Nº "+reservaVenta.getCodigo());
			return reservaVenta;
		}catch(Exception e){
			e.printStackTrace();
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

	public int cantidadVentasDia(Integer dia,Integer mes,Integer anio){
		String query = "select count(em) from  reserva_venta em where em.estado='AC' and date_part('month', em.fecha_registro) ="+mes+" and date_part('year', em.fecha_registro) ="+anio+" and date_part('day', em.fecha_registro) ="+dia;
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue();	
	}

	public double totalVentasDia(Integer dia,Integer mes,Integer anio){
		try{
			String query = "select sum(em.montoTotal) from  ReservaVenta em where em.estado='AC' and date_part('month', em.fechaRegistro) ="+mes+" and date_part('year', em.fechaRegistro) ="+anio+" and date_part('day', em.fechaRegistro) ="+dia;
			return executeQuerySingleResultNumber(query).doubleValue();
		}catch(Exception e){
			return 0;
		}
	}

	public double totalVentasMes(Integer mes,Integer anio){
		try{
			String query = "select sum(em.montoTotal) from  ReservaVenta em where em.estado='AC' and date_part('month', em.fechaRegistro) ="+mes+" and date_part('year', em.fechaRegistro) ="+anio;
			//return ((Number) executeNativeQuerySingleResult(query)).doubleValue();
			return executeQuerySingleResultNumber(query).doubleValue();
		}catch(Exception e){
			return 0;
		}
	}

	public List<ReservaVenta> obtenerTodosOrdenadosPorId() {
		String query = "select ser from ReservaVenta ser where (ser.estado='AC' or ser.estado='IN' or ser.estado='PR' or ser.estado='PN') order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<ReservaVenta> obtenerTodosActivosOrdenadosPorId() {
		String query = "select ser from ReservaVenta ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public int correlativoNotaVenta2(){
		return countTotalRecord("nota_venta").intValue();
	}

	public int correlativoNotaVenta( Gestion gestion){
		String query = "select count(em) from  reserva_venta em where (em.estado='AC' or em.estado='IN' or em.estado='PR' or em.estado='PN') and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}

	public List<ReservaVenta> obtenerPorCliente(Cliente cliente){
		try{
			return findAllActiveOtherTableAndParameter("Cliente", "cliente", "id", cliente.getId());
		}catch(Exception e){
			return new ArrayList<>();
		}
	}
	public ReservaVenta obtenerPorId(Integer id){
		try{
			return findById(id);
		}catch(Exception e){
			return null;
		}
	}
}
