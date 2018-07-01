package com.erp360.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.erp360.model.AlmacenProducto;
import com.erp360.model.DetalleOrdenIngreso;
import com.erp360.model.DetalleProducto;
import com.erp360.model.DetalleTomaInventario;
import com.erp360.model.DetalleTomaInventarioOrdenIngreso;
import com.erp360.model.Gestion;
import com.erp360.model.KardexProducto;
import com.erp360.model.OrdenIngreso;
import com.erp360.model.TomaInventario;
import com.erp360.util.FacesUtil;
import com.erp360.util.V;
import com.erp360.util.W;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Stateless
public class TomaInventarioDao extends DataAccessObjectJpa<TomaInventario,DetalleTomaInventarioOrdenIngreso,OrdenIngreso,DetalleOrdenIngreso,DetalleTomaInventario, DetalleProducto, Gestion, AlmacenProducto, KardexProducto, W> {

	public TomaInventarioDao(){
		super(TomaInventario.class,DetalleTomaInventarioOrdenIngreso.class,OrdenIngreso.class,DetalleOrdenIngreso.class,DetalleTomaInventario.class,DetalleProducto.class,Gestion.class,AlmacenProducto.class,KardexProducto.class);
	}
	
	public boolean modificar(TomaInventario tomaInventario){
		try{
			beginTransaction();
			update(tomaInventario);
			commitTransaction();
			return true;
		}catch(Exception e){
			String cause = e.getMessage();
			System.out.println(e.getMessage());
			rollbackTransaction();
			return false;
		}
	}

	/**
	 * 
	 * @param tomaInventario
	 * @param detTomInvOrdIng
	 * @param ordenIngreso
	 * @param listaDetalleOrdenIngreso
	 * @param listDetalleTomaInventario
	 * @return
	 */
	public boolean registrar(TomaInventario tomaInventario,DetalleTomaInventarioOrdenIngreso detTomInvOrdIng,OrdenIngreso ordenIngreso,List<DetalleOrdenIngreso> listaDetalleOrdenIngreso,List<DetalleTomaInventario> listDetalleTomaInventario){
		try{
			//set fechas
			//set usuarioRegistro
			beginTransaction();
			//tomaInventario
			tomaInventario = create(tomaInventario);
			System.out.println(tomaInventario.toString());
			//ordenIngreso
			ordenIngreso.setEstado(tomaInventario.getEstado());
			ordenIngreso.setFechaRegistro(tomaInventario.getFechaRegistro());
			ordenIngreso.setUsuarioRegistro(tomaInventario.getUsuarioRegistro());
			ordenIngreso = createR(ordenIngreso);
			System.out.println(ordenIngreso.toString());
			//detTomInvOrdIng
			detTomInvOrdIng.setTomaInventario(tomaInventario);
			detTomInvOrdIng.setOrdenIngreso(ordenIngreso);
			detTomInvOrdIng.setEstado(tomaInventario.getEstado());
			detTomInvOrdIng.setUsuarioRegistro(tomaInventario.getUsuarioRegistro());
			detTomInvOrdIng.setFechaRegistro(tomaInventario.getFechaRegistro());
			detTomInvOrdIng = createE(detTomInvOrdIng);
			System.out.println(detTomInvOrdIng.toString());
			//listaDetalleOrdenIngreso
			for(DetalleOrdenIngreso doi: listaDetalleOrdenIngreso){
				doi.setOrdenIngreso(ordenIngreso);
				doi.setEstado(tomaInventario.getEstado());
				doi.setFechaRegistro(tomaInventario.getFechaRegistro());
				doi.setUsuarioRegistro(tomaInventario.getUsuarioRegistro());
				doi = createS(doi);
				System.out.println(doi.toString());
			}
			//listDetalleTomaInventario
			for(DetalleTomaInventario dti: listDetalleTomaInventario){
				dti.setTomaInventario(tomaInventario);
				dti.setEstado(tomaInventario.getEstado());
				dti.setFechaRegistro(tomaInventario.getFechaRegistro());
				dti.setUsuarioRegistro(tomaInventario.getUsuarioRegistro());
				dti = createO(dti);
				System.out.println(dti.toString());
			}
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "Toma de Inventario "+tomaInventario.getId());
			return true;
		}catch(Exception e){
			String cause = e.getMessage();
			System.out.println(e.getMessage());
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al registrar");
			}
			rollbackTransaction();
			return false;
		}
	}

	public boolean procesarTomaInventarioInicial(OrdenIngreso ordenIngreso,TomaInventario tomaInventario,Gestion gestion,List<AlmacenProducto>listAlmacenProducto,List<DetalleProducto>listDetalleProducto){
		try{
			//set fechas
			Date fechaActual = ordenIngreso.getFechaAprobacion();
			//set usuarioRegistro
			beginTransaction();
			//ordenIngreso
			ordenIngreso = updateR(ordenIngreso);
			//tomaInventario
			tomaInventario = update(tomaInventario);
			//gestion
			gestion = updateQ(gestion);
			//AlmacenProducto
			for(AlmacenProducto ap: listAlmacenProducto){
				ap.setFechaRegistro(fechaActual);
				ap = createU(ap);
			}
			//DetalleProducto
			for(DetalleProducto dp: listDetalleProducto){
				dp.setFechaRegistro(fechaActual);
				dp.setFecha(fechaActual);
				dp = createP(dp);
				//kardex
				KardexProducto kardex = new KardexProducto();
				kardex.setEstado("AC");
				kardex.setFechaRegistro(fechaActual);
				kardex.setOrdenIngreso(ordenIngreso);
				kardex.setStockEntrante(dp.getStockInicial());
				kardex.setTipoMovimiento("OI");//orden de ingreso
				kardex.setTipoTransaccion("INVENTARIO INICIAL");
				kardex.setPrecioEntradaCompra(dp.getPrecioCompra());
				kardex.setPrecioEntradaVenta(dp.getPrecioVentaContado());
				kardex.setUsuarioRegistro(dp.getUsuarioRegistro());
				kardex.setAlmacen(dp.getAlmacen());
				kardex.setProducto(dp.getProducto());
				createV(kardex);
			}
			commitTransaction();
			FacesUtil.infoMessage("Toma de Inventario Procesada Correctamente", "Toma de Inventario Nº"+tomaInventario.getId());
			return true;
		}catch(Exception e){
			String cause = e.getMessage();
			System.out.println(e.getMessage());
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al registrar");
			}
			rollbackTransaction();
			return false;
		}
	}

	public List<TomaInventario> findAllOrderedByIDGestion(Gestion gestion) {
		String query = "select ser from TomaInventario ser where (ser.estado='AC' or ser.estado='IN' or ser.estado='RE' or ser.estado='PR' or ser.estado='CN' or ser.estado='CE') and ser.gestion.id="+gestion.getId()+" order by ser.id desc";
		return executeQueryResulList(query);
	}
}
