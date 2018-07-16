package com.erp360.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.interfaces.ITipoCambioDao;
import com.erp360.model.Empresa;
import com.erp360.model.TipoCambio;
import com.erp360.util.DateUtility;
import com.erp360.util.E;
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
public class TipoCambioDao extends DataAccessObjectJpa<TipoCambio, E, R, S, O, P, Q, U, V, W>{

	public TipoCambioDao(){
		super(TipoCambio.class);
	}
	public TipoCambio registrar(TipoCambio tipoCambio){
		try{
			beginTransaction();
			tipoCambio = create(tipoCambio);
			commitTransaction();
			return tipoCambio;
		}catch(Exception e){
			rollbackTransaction();
			return null;
		}
	}

	public TipoCambio modificar(TipoCambio usuario){
		try{
			update(usuario);
			FacesUtil.infoMessage("Modificación Correcta", "TipoCambio "+usuario.getId());
			return usuario;
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
	public List<TipoCambio> obtenerOrdenAscPorId(){
		return findAscAllOrderedByParameter("id");
	}
	
	public List<TipoCambio> obtenerOrdenDescPorId(){
		return findDescAllOrderedByParameter("id");
	}
	
	public  List<TipoCambio> obtenerPorEmpresa(Empresa empresa){
		return findAllActivosByParameter("empresa", empresa);
	}
	

	public TipoCambio obtenerPorEmpresaYFecha(Empresa empresa,Date fecha){
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha); 
			String year = new SimpleDateFormat("yyyy").format(fecha);
			Integer month = Integer.parseInt(new SimpleDateFormat("MM").format(fecha).toString());
			String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			String query = "select em from TipoCambio em where em.empresa.id="+empresa.getId() + " and date_part('year',fecha)="+year+" and date_part('month',fecha)="+month+" and date_part('day',fecha)="+day;
			System.out.println("query: "+query);
			return executeQuerySingleResult(query);
		}catch(Exception e){
			return null;
		}
		//return findAllByParameterDateAndTwoParameter("fecha", fecha, "empresa", empresa);
	}
	
	public TipoCambio obtenerPorEmpresaDiaAnterior(Empresa empresa){
		try{
			Date date1 = new Date();
			Date fecha = DateUtility.restarDiasFecha(date1,1);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha); 
			String year = new SimpleDateFormat("yyyy").format(fecha);
			Integer month = Integer.parseInt(new SimpleDateFormat("MM").format(fecha).toString());
			String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			String query = "select em from TipoCambio em where em.empresa.id="+empresa.getId() + " and date_part('year',fecha)="+year+" and date_part('month',fecha)="+month+" and date_part('day',fecha)="+day;
			return executeQuerySingleResult(query);
		}catch(Exception e){
			return null;
		}
	}
	
	public TipoCambio obtenerUltimoRegistro(Empresa empresa) {
		try{
			return findLastActiveRecord("empresa.id",empresa.getId());
		}catch(Exception e){
			return null;
		}
	}
}
