package com.erp360.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.Cliente;
import com.erp360.model.KardexCliente;
import com.erp360.model.NotaVenta;
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
public class KardexClienteDao extends DataAccessObjectJpa<KardexCliente,E,R,S,O, P, Q, U, V, W> {

	public KardexClienteDao(){
		super(KardexCliente.class);
	}

	public KardexCliente registrarBasic(KardexCliente kardexCliente) throws Exception{
		kardexCliente = create(kardexCliente);
		return kardexCliente;
	}

	public KardexCliente registrar(KardexCliente kardexCliente){
		try{
			beginTransaction();
			kardexCliente = create(kardexCliente);
			commitTransaction();
			return kardexCliente;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}
			rollbackTransaction();
			return null;
		}
	}

	public boolean modificar(KardexCliente kardexCliente){
		try{
			beginTransaction();
			update(kardexCliente);
			commitTransaction();
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}
			rollbackTransaction();
			return false;
		}
	}

	public boolean eliminar(KardexCliente kardexCliente){
		try{
			beginTransaction();
			kardexCliente.setEstado("RM");
			update(kardexCliente);
			commitTransaction();
			return true;
		}catch(Exception e){
			String cause=e.getMessage();
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}
			rollbackTransaction();
			return false;
		}
	}

	public List<KardexCliente> obtenerKardexPorCliente(Cliente cliente){
		try{
			String query = "select em from KardexCliente em where em.cliente.id="+cliente.getId()+" AND em.estado='AC' order by em.fechaRegistro asc";
			System.out.println("query: "+query);
			return executeQueryResulList(query);
		}catch(Exception e){
			return new ArrayList<>();
		}
	}
	
	public List<KardexCliente> obtenerKardexPorVenta(NotaVenta notaVenta){
		try{
			String query = "select em from KardexCliente em where em.notaVenta.id="+notaVenta.getId();
			return executeQueryResulList(query);
		}catch(Exception e){
			return new ArrayList<KardexCliente>();
		}
	}

}
