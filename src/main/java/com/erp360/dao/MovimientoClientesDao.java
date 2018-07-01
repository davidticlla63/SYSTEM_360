package com.erp360.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.erp360.model.Cliente;
import com.erp360.model.PlanCobranza;
import com.erp360.util.E;
import com.erp360.util.EDMovimientoClientes;
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
public class MovimientoClientesDao extends DataAccessObjectJpa<PlanCobranza, E, R, S, O, P, Q, U, V, W> {

	public MovimientoClientesDao(){
		super(PlanCobranza.class);
	}


	//	public List<Almacen> obtener100UltimosAlmacen() {
	//		try { 
	//			String query = "select ser from Almacen ser order by ser.fechaRegistro desc";
	//			System.out.println("Consulta: " + query);
	//			List<Almacen> listaAlmacen = em.createQuery(query)
	//					.setMaxResults(100).getResultList();
	//			return listaAlmacen;
	//		} catch (Exception e) {
	//			System.out.println("Error en findAll100UltimosAlmacen: "
	//					+ e.getMessage());
	//			return null;
	//		}
	//	}

	//	public List<Almacen> findAllAlmacenForQueryNombre(String criterio) {
	//		try {
	//			String query = "select ser from Almacen ser where upper(ser.nombre) like '%"
	//					+ criterio + "%' and ser.estado='AC' order by ser.nombre asc";
	//			System.out.println("Consulta: " + query);
	//			List<Almacen> listaAlmacen = executeQueryResulList(query);
	//			return listaAlmacen;
	//		} catch (Exception e) {
	//			System.out.println("Error en findAllAlmacenForDescription: "
	//					+ e.getMessage());
	//			return null;
	//		}
	//	}

	public List<EDMovimientoClientes> obtenerDelMesActual(){
		/**
		 * SELECT COUNT(pc.id) AS cantidad,SUM(pc.monto_nacional) AS total_monto_nac,
			SUM(pc.monto_extranjero) AS total_monto_ext,
			SUM(pc.interes_mensual) AS total_monto_int_nac,
			SUM(pc.interes_mensual_extranjero) AS total_monto_int_ext,
			(SELECT 0 ) AS total_multa_nac,
			(SELECT 0 ) AS total_multa_ext,
			cl.id,
			cl.nombres,cl.apellidos,
			(SELECT 'NACIONAL') AS moneda
			FROM plan_cobranza pc
			INNER JOIN nota_venta nv ON pc.id_nota_venta = nv.id
			INNER JOIN cliente cl ON nv.id_cliente = cl.id
			WHERE pc.estado_cobro='NC' AND 
			date_part('year', pc.fecha_pago) = 2016 AND
			      date_part('month', pc.fecha_pago) = 10
			GROUP BY cl.id
		 */
		List<EDMovimientoClientes> list = new ArrayList<>(); 
		Date fechaActual = new Date();
		Integer year = Integer.parseInt( new SimpleDateFormat("yyyy").format(fechaActual));
		Integer month = Integer.parseInt( new SimpleDateFormat("MM").format(fechaActual));
		String queryString = "SELECT COUNT(pc.id) AS cantidad,SUM(pc.monto_nacional) AS total_monto_nac,"
				+" SUM(pc.monto_extranjero) AS total_monto_ext,"
				+" SUM(pc.interes_mensual) AS total_monto_int_nac,"
				+" SUM(pc.interes_mensual_extranjero) AS total_monto_int_ext,"
				+" (SELECT CAST(0.0 AS double precision) ) AS total_multa_nac,"
				+" (SELECT CAST(0.0 AS double precision) ) AS total_multa_ext,"
				+" cl.id,"
				+" cl.nombres,cl.apellidos"
				//+" (SELECT 'NACIONAL') AS moneda"
				+" FROM plan_cobranza pc"
				+" INNER JOIN nota_venta nv ON pc.id_nota_venta = nv.id"
				+" INNER JOIN cliente cl ON nv.id_cliente = cl.id"
				+" WHERE pc.estado_cobro='NC' AND "
				+" date_part('year', pc.fecha_pago) = "+year+" AND date_part('month', pc.fecha_pago) = "+month
				+" GROUP BY cl.id";
		System.out.println("queryString: "+queryString);
		Query query = getEntityManager().createNativeQuery(queryString);
		List<Object[]> rs2 = query.getResultList();
		for (Object[] o : rs2) {
			BigInteger cantidadCuotasPendientes =  (BigInteger) o[0];
			double totalMontoNac = (double) o[1];
			double totalMontoExt = (double) o[2];
			double totalMontoIntNac = (double) o[3];
			double totalMontoIntExt = (double) o[4];
			double totalMontoMultaNac = (double) o[5];
			double totalMontoMultaExt = (double) o[6];
			Integer clienteId = (Integer) o[7];
			String nombres = (String) o[8];
			String apellidos = (String)  o[9];
			String moneda = "";//(String)  o[10];
			Cliente cliente = new Cliente();
			cliente.setId(clienteId);
			cliente.setNombres(nombres);
			cliente.setApellidos(apellidos);
			EDMovimientoClientes edmc = new EDMovimientoClientes(cliente,cantidadCuotasPendientes.intValue(),totalMontoNac,totalMontoExt,totalMontoIntNac,totalMontoIntExt,totalMontoMultaNac,totalMontoMultaExt,moneda);
			list.add(edmc);
		}
		return list;
	}

}
