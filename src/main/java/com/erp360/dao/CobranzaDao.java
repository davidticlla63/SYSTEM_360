package com.erp360.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.erp360.caja.behaviors.CajaServicio;
import com.erp360.interfaces.ICajaMovimientoDao;
import com.erp360.model.CajaMovimiento;
import com.erp360.model.Cliente;
import com.erp360.model.Cobranza;
import com.erp360.model.CuentaCobrar;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.KardexCliente;
import com.erp360.model.MovimientoCaja;
import com.erp360.model.NotaVenta;
import com.erp360.model.PlanCobranza;
import com.erp360.model.Sucursal;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.O;
import com.erp360.util.P;
import com.erp360.util.Q;
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
public class CobranzaDao extends DataAccessObjectJpa<Cobranza,PlanCobranza,NotaVenta,CajaMovimiento,O, P, Q, U, V, W> {

	public CobranzaDao(){
		super(Cobranza.class,PlanCobranza.class,NotaVenta.class,CajaMovimiento.class);
	}
	
	private @Inject MovimientoCajaDao movimientoCajaDao;
	private @Inject KardexClienteDao kardexClienteDao;
	private @Inject CuentaCobrarDao deudaClienteDao;
	private @Inject NotaVentaDao notaVentaDao;
	private @Inject ICajaMovimientoDao cajaMovimientoDao;
	private @Inject CajaServicio cajaServicio;

	public Cobranza registrar(Usuario usuario,Cobranza cobranza,List<PlanCobranza> planCobranzas,Cliente cliente,int numeroCuotasPendientePorCobrar){
		try{
			beginTransaction();
			
			//crear movimiento a caja
			MovimientoCaja movimientoCaja = new MovimientoCaja();
			movimientoCaja.setCorrelativo(String.format("%06d",movimientoCajaDao.correlativoMovimiento(cobranza.getGestion())));
			movimientoCaja.setEstado("PR");
			movimientoCaja.setFechaAprobacion(cobranza.getFechaRegistro());
			movimientoCaja.setFechaDocumento(cobranza.getFechaRegistro());
			movimientoCaja.setFechaRegistro(cobranza.getFechaRegistro());
			movimientoCaja.setGestion(cobranza.getGestion());
			movimientoCaja.setMoneda("DOLAR");
			movimientoCaja.setNumeroDocumento(cobranza.getCodigo());
			movimientoCaja.setObservacion("Ninguna");
			movimientoCaja.setTipoCambio(cobranza.getTipoCambio());
			movimientoCaja.setTipoDocumento("COBRANZA");
			movimientoCaja.setMotivoIngreso("INGRESO POR COBRO DE CUOTA(S)");
			movimientoCaja.setTotalImporteExtranjero(cobranza.getMontoExtranjero());
			movimientoCaja.setTotalImporteNacional(cobranza.getMontoNacional());
			movimientoCaja.setUsuarioAprobacion(usuario);
			movimientoCaja.setUsuarioRegistro(cobranza.getUsuarioRegistro());
			movimientoCaja.setTipo("INGRESO");
			movimientoCaja = movimientoCajaDao.registrarBasic(movimientoCaja);
			//registro de cobranza
			cobranza.setMovimientoCaja(movimientoCaja);
			cobranza = create(cobranza);
			
			//caja movimiento
			CajaMovimiento c=cajaMovimientoDao.create(cajaServicio.IngresoPorCobranza(cobranza));
			//modificacion plan cobranza
			for(PlanCobranza pc: planCobranzas){
				pc.setEstadoCobro("CO");
				updateE(pc);
			}
			System.out.println("cobranza.getCuentaCobrar().getNotaVenta() : "+cobranza.getCuentaCobrar().getNotaVenta());
			CuentaCobrar cuentaCobrar = deudaClienteDao.obtenerPorNotaVenta(cobranza.getCuentaCobrar().getNotaVenta());
			//verificar si ya es la ultima cuota
			if(numeroCuotasPendientePorCobrar==0){
				NotaVenta nv = cuentaCobrar.getNotaVenta();
				nv.setEstadoPago("PG");
				notaVentaDao.update(nv);
			}
			KardexCliente kcIngreso = generarKardexIngreso(cliente,cobranza.getFechaRegistro(),cobranza.getCodigo(),cobranza.getTipoCambio(),cobranza.getMontoExtranjero(),cobranza.getMontoNacional(),0,0,null,cobranza.getUsuarioRegistro());
			kardexClienteDao.registrarBasic(kcIngreso);
			//actualizar cuentas por cobrar del cliente
			cuentaCobrar.setSaldoExtranjero( cuentaCobrar.getSaldoExtranjero()- cobranza.getMontoExtranjero());//aqui va el monto total del plan de pago credito
			cuentaCobrar.setSaldoNacional( cuentaCobrar.getSaldoNacional() - cobranza.getMontoNacional());//aqui va el monto total del plan de pago credito
			cuentaCobrar.setFechaModificacion(cobranza.getFechaRegistro());
			if(cuentaCobrar.getSaldoExtranjero() == 0){
				cuentaCobrar.setEstado("PG");
			}
			deudaClienteDao.update(cuentaCobrar);
			commitTransaction();
			FacesUtil.infoMessage("", "Registro Correcto ");
			return cobranza;
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
	
	private KardexCliente generarKardexIngreso(Cliente cliente,Date fechaRegistro,String codigoNotaVenta,double tipoCambio,double montoTotalExtranjero,double montoTotal,double montoReservaExtranjero,double montoReserva,String estadoPago,String usuarioRegistro) {
		KardexCliente kcEgreso = new KardexCliente();
		kcEgreso.setCliente(cliente);
		kcEgreso.setEstado("AC");
		kcEgreso.setFechaDocumento(fechaRegistro);
		kcEgreso.setFechaRegistro(fechaRegistro);
		kcEgreso.setMoneda("DOLAR");
		kcEgreso.setMotivo("INGRESO X COBRO DE CUOTA Nº "+codigoNotaVenta );
		kcEgreso.setTipoDocumento("COBRANZA");
		kcEgreso.setTotalImporteExtranjero(montoTotalExtranjero);
		kcEgreso.setTotalImporteNacional(montoTotal);
		kcEgreso.setUsuarioRegistro(usuarioRegistro);
		kcEgreso.setNumeroDocumento(codigoNotaVenta);
		kcEgreso.setObservacion("Ninguna");
		kcEgreso.setSaldo(0);
		kcEgreso.setTipo("INGRESO");
		kcEgreso.setTipoCambio(tipoCambio);
		return kcEgreso;
	}

	public Cobranza modificar(Cobranza usuario){
		try{
			beginTransaction();
			usuario = update(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "Pago "+usuario.getId());
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

	public boolean eliminar(Cobranza usuario){
		try{
			beginTransaction();
			Cobranza usr = modificar(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "Pago "+usuario.getId());
			return usr!=null?true:false;
		}catch(Exception e){
			FacesUtil.errorMessage("Error al eliminar");
			rollbackTransaction();
			return false;
		}
	}

	public List<Cobranza> obtenerOrdenAscPorId(){
		return findAscAllOrderedByParameter("id");
	}

	public List<Cobranza> obtenerOrdenDescPorId(){
		return findDescAllOrderedByParameter("id");
	}

	public int obtenerNumeroPago(Date date,Empresa empresa, Sucursal sucursal){
		Integer year = Integer.parseInt( new SimpleDateFormat("yyyy").format(date));
		//and em.tipoComprobante.id="+tc.getId()+"
		String query = "select em from Pago em where (em.estado='AC' or em.estado='IN') and em.sucursal.id="+sucursal.getId()+" and em.empresa.id="+empresa.getId()+" and date_part('year', em.fechaRegistro) ="+year;
		System.out.println("Query Compra: "+query);
		return (( List<Cobranza>)executeQueryResulList(query)).size() + 1;
	}

	public int obtenerCorrelativo2(Gestion gestion){
		String query = "select count(em) from  cobranza em where (em.estado='AC' or em.estado='IN' or em.estado='PR') and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}
	
	public double totalCobranzaMes(Integer mes,Integer anio){
		try{
			String query = "select sum(em.montoExtranjero) from  Cobranza em where em.estado='AC' and date_part('month', em.fechaCobranza) ="+mes+" and date_part('year', em.fechaCobranza) ="+anio;
			//return ((Number) executeNativeQuerySingleResult(query)).doubleValue();
			return executeQuerySingleResultNumber(query).doubleValue();
		}catch(Exception e){
			return 0;
		}
	}
	
	public double totalCobranzaDia(Integer dia,Integer mes,Integer anio){
		try{
			String query = "select sum(em.montoExtranjero) from  Cobranza em where em.estado='AC' and date_part('month', em.fechaCobranza) ="+mes+" and date_part('year', em.fechaCobranza) ="+anio+" and date_part('day', em.fechaCobranza) ="+dia;
			//return ((Number) executeNativeQuerySingleResult(query)).doubleValue();
			return executeQuerySingleResultNumber(query).doubleValue();
		}catch(Exception e){
			return 0;
		}
	}
}
