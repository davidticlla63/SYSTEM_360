package com.erp360.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Almacen;
import com.erp360.model.AlmacenEncargado;
import com.erp360.model.Usuario;
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
public class AlmacenDao extends DataAccessObjectJpa<Almacen,AlmacenEncargado,R,S,O, P, Q, U, V, W> {

	public AlmacenDao(){
		super(Almacen.class,AlmacenEncargado.class);
	}

	public Almacen registrar(Almacen almacen,List<AlmacenEncargado> almacenEncargado){
		try{
			beginTransaction();
			almacen = create(almacen);
			//
			for(AlmacenEncargado almEn: almacenEncargado){
				almEn.setFechaRegistro(almacen.getFechaRegistro());
				almEn.setAlmacen(almacen);
				createE(almEn);
			}
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Almacen "+almacen.getId());
			return almacen;
		}catch(Exception e){
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

	public boolean modificar(Almacen almacen,List<AlmacenEncargado> listEncargado,List<AlmacenEncargado> listEncargadoEliminado){
		try{
			beginTransaction();
			update(almacen);
			for(AlmacenEncargado ae: listEncargado){
				if(ae.getId()<0){
					ae.setId(0);
					createE(ae);
				}else{
					updateE(ae);
				}
			}
			for(AlmacenEncargado ae: listEncargadoEliminado){
				ae.setEstado("RM");
				updateE(ae);
			}
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Almacen "+almacen.getId());
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return false;
		}
	}

	public boolean eliminar(Almacen almacen){
		try{
			beginTransaction();
			almacen.setCodigo(new Date()+"|"+almacen.getCodigo());
			update(almacen);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "Almacen "+almacen.getId());
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return false;
		}
	}

	public int correlativo(){
		String query = "select count(em) from  almacen em where (em.estado='AC' or em.estado='IN')";
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}

	public List<Almacen> obtenerTodosOrdenadosPorDescripcion(){
		String  query = "select em from Almacen em where em.estado='AC' or em.estado='IN order by em.descripcion asc";
		return executeQueryResulList(query);
	}

	public List<Almacen> obtenerTodosOrdenadosPorId() {
		String query = "select ser from Almacen ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<Almacen> obtenerTodosActivosOrdenadosPorId() {
		String query = "select ser from Almacen ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public Almacen obtenerPorUsuarior(Usuario user) {
		String query = "select ser from Almacen ser where (ser.estado='AC' or ser.estado='IN') and ser.encargado.id="
				+ user.getId() + " order by ser.id desc";
		List<Almacen> listAlmacen = executeQueryResulList(query);
		if (listAlmacen.size() > 0) {
			return listAlmacen.get(0);
		} else {
			Almacen almacen = new Almacen();
			almacen.setId(-1);
			return almacen;
		}
	}

	public Almacen findByCodigo(String codigo) {
		try{
			String query = "select ser from Almacen ser where (ser.estado='AC' or ser.estado='IN') and upper(ser.codigo)='"
					+ codigo + "'";
			return (Almacen) executeQuerySingleResult(query);
		}catch(Exception e){
			System.out.println("Error : " + e.getMessage());
			return null;
		}
	}


	public List<Almacen> obtenerTodosOrdenadosdPorFechaRegistro() {
		String  query = "select em from Almacen em where em.estado='AC' or em.estado='IN order by em.fechaRegistro asc";
		return executeQueryResulList(query);
	}

	public List<Almacen> obtenerTodosPorDescripcion(String criterio) {
		try {
			String query = "select ser from Almacen ser where ser.nombre like '%"
					+ criterio + "%'";
			List<Almacen> listaAlmacen = executeQueryResulList(query);
			return listaAlmacen;
		} catch (Exception e) {
			System.out.println("Error en findAllAlmacenForDescription: "
					+ e.getMessage());
			return null;
		}
	}
	public List<Almacen> traerAlmacenActivas() {
		try {
			String query = "select ser from Almacen ser where ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta traerAlmacenActivas: " + query);
			List<Almacen> listaAlmacen = executeQueryResulList(query);
			return listaAlmacen;
		} catch (Exception e) {
			System.out.println("Error en traerAlmacenActivas: "
					+ e.getMessage());
			return null;
		}
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

	public List<Almacen> findAllAlmacenForQueryNombre(String criterio) {
		try {
			String query = "select ser from Almacen ser where upper(ser.nombre) like '%"
					+ criterio + "%' and ser.estado='AC' order by ser.nombre asc";
			System.out.println("Consulta: " + query);
			List<Almacen> listaAlmacen = executeQueryResulList(query);
			return listaAlmacen;
		} catch (Exception e) {
			System.out.println("Error en findAllAlmacenForDescription: "
					+ e.getMessage());
			return null;
		}
	}

}
