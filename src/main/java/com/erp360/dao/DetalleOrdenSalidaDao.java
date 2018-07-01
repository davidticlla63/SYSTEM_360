package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;
import com.erp360.model.DetalleOrdenSalida;
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


@Stateless
public class DetalleOrdenSalidaDao extends DataAccessObjectJpa<DetalleOrdenSalida,E,R,S,O, P, Q, U, V, W> {

	public DetalleOrdenSalidaDao(){
		super(DetalleOrdenSalida.class);
	}
	
	public List<DetalleOrdenSalida> findAll() {
		String query = "select em from DetalleOrdenSalida em ";
		return executeQueryResulList(query);
	}

	public List<DetalleOrdenSalida> findAllOrderByDesc() {
		String query = "select em from DetalleOrdenSalida em, OrdenSalida oc where em.estado='AC' and em.estado='IN' and oc.id=em.ordenSalida.id  order by em.id desc";
		return executeQueryResulList(query);
	}

	public List<DetalleOrdenSalida> findAllByOrdenSalida(OrdenSalida ordenSalida) {
		String query = "select em from DetalleOrdenSalida em where em.estado='AC' and em.ordenSalida.id="+ordenSalida.getId()+"  order by em.id desc";
		return executeQueryResulList(query);
	}
	/*
	 * SELECT
     proy.nombre AS nombre_proyecto,
     proy.descripcion AS descripcion_proyecto,
     gestion.gestion AS gestion,
     dos.total AS subtotal,
     os.fecha_aprobacion AS fecha,
     ( select SUM(dos1.total) AS total from detalle_orden_salida dos1 WHERE
     gestion.id=$P{pIdGestion} AND os.fecha_aprobacion >= to_date($P{pFechaInicio},'dd-MM-yyyy') AND
     os.fecha_aprobacion <= to_date($P{pFechaFin},'dd-MM-yyyy') AND
     os.estado='PR' )

FROM
      detalle_orden_salida dos
      INNER JOIN orden_salida os ON dos.id_orden_salida = os.id
      INNER JOIN proyecto proy ON dos.id_orden_salida = os.id
      INNER JOIN gestion gestion ON os.id_gestion = gestion.id
WHERE
     gestion.id=$P{pIdGestion} AND os.fecha_aprobacion >= to_date($P{pFechaInicio},'dd-MM-yyyy') AND
     os.fecha_aprobacion <= to_date($P{pFechaFin},'dd-MM-yyyy') AND
     os.estado='PR'
	 */
	
//	@SuppressWarnings("unchecked")
//	public List<DetalleOrdenSalida> findByFechas(Date fechaInicial,Date fechaFinal) {
//		String query = "select em from DetalleOrdenSalida em,OrdenSalida os where em.ordenSalida.id=os.id and os.estado='PR' and os.fechaAprobacion>=:stDate and os.fechaAprobacion<=:edDate order by os.id desc";
//		return em.createQuery(query).setParameter("stDate", fechaInicial).setParameter("edDate", fechaFinal).getResultList();
//	}
//	
	/*
	 *  select  os.fecha_aprobacion,partida.nombre , SUM(dos.total) , os.correlativo from detalle_orden_salida dos,producto producto,partida partida,orden_salida os
 where dos.id_producto = producto.id and
 dos.id_orden_salida = os.id and
 producto.id_partida = partida.id and
 os.estado = 'PR'
 group by partida.nombre,dos.total,os.correlativo,os.fecha_aprobacion
	 
	
	@SuppressWarnings("unchecked")
	public List<DetalleOrdenSalida> findTotalPartidaByFechas(Date fechaInicial,Date fechaFinal) {
		String query = "select em from DetalleOrdenSalida em,OrdenSalida os where em.ordenSalida.id=os.id and os.estado='PR' and os.fechaAprobacion>=:stDate and os.fechaAprobacion<=:edDate order by os.id desc";
		log.info("Query DetalleOrdenSalida: "+query);
		return em.createQuery(query).setParameter("stDate", fechaInicial).setParameter("edDate", fechaFinal).getResultList();
	}
	*/
	
	
}
