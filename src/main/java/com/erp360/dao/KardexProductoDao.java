package com.erp360.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.KardexProducto;
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
public class KardexProductoDao extends DataAccessObjectJpa<KardexProducto,E,R,S,O, P, Q, U, V, W> {

	public KardexProductoDao(){
		super(KardexProducto.class);
	}

	public List<KardexProducto> obtenerPorFecha(Date fechaInicio,Date fechaFin){
		String query = "select em from KardexProducto em  where em.estado='AC' AND em.fechaRegistro>=:stDate AND em.fechaRegistro<=:edDate order by em.fechaRegistro desc";
		return executeQueryResulListByDate(query, fechaInicio, fechaFin);
	}

	public KardexProducto obtenerPorOrdenSalida(OrdenSalida ordenSalida){
		try{
			String query = "select em from KardexProducto em  where em.ordenSalida.id="+ordenSalida.getId();
			return executeQuerySingleResult(query);
		}catch(Exception e){
			return new KardexProducto();
		}
	}

}
