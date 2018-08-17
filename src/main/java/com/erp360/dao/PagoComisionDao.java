package com.erp360.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.erp360.caja.behaviors.CajaServicio;
import com.erp360.interfaces.ICajaMovimientoDao;
import com.erp360.interfaces.ICajaSesionDao;
import com.erp360.model.CajaMovimiento;
import com.erp360.model.CajaSesion;
import com.erp360.model.Cliente;
import com.erp360.model.Cobranza;
import com.erp360.model.CuentaCobrar;
import com.erp360.model.Ejecutivo;
import com.erp360.model.EjecutivoComisiones;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.KardexCliente;
import com.erp360.model.MovimientoCaja;
import com.erp360.model.NotaVenta;
import com.erp360.model.PagoComision;
import com.erp360.model.PlanCobranza;
import com.erp360.model.Sucursal;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.O;
import com.erp360.util.P;
import com.erp360.util.Q;
import com.erp360.util.S;
import com.erp360.util.SessionMain;
import com.erp360.util.U;
import com.erp360.util.V;
import com.erp360.util.W;
/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Stateless
public class PagoComisionDao extends DataAccessObjectJpa<PagoComision,PlanCobranza,NotaVenta,CajaMovimiento,O, P, Q, U, V, W> {

	public PagoComisionDao(){
		super(PagoComision.class,PlanCobranza.class,NotaVenta.class,CajaMovimiento.class);
	}
	
	private @Inject MovimientoCajaDao movimientoCajaDao;
	private @Inject KardexClienteDao kardexClienteDao;
	private @Inject CuentaCobrarDao deudaClienteDao;
	private @Inject NotaVentaDao notaVentaDao;
	private @Inject ICajaMovimientoDao cajaMovimientoDao;
	private @Inject CajaServicio cajaServicio;
	private @Inject ICajaSesionDao cajaSesionDao;
	private @Inject EjecutivoComisionesDao ejecutivoComisionesDao;
	private @Inject SessionMain sessionMain;

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
			//cobranza = create(cobranza);
			
			//ejecutivo
			//EjecutivoCliente ejecutivoCliente = ejecutivoClienteDao.getEjecutivoClienteByIdCliente(notaVenta.getCliente());
			Ejecutivo ejecutivo = cobranza.getCuentaCobrar().getNotaVenta().getEjecutivo();
			if(ejecutivo != null){
				//Ejecutivo ejecutivo = ejecutivoCliente.getEncargadoVenta();
				double comision = ejecutivo.getPorcentaje();
				double importe = cobranza.getMontoExtranjero()*(comision/100);
				EjecutivoComisiones ejecutivoComisiones = new EjecutivoComisiones();
				ejecutivoComisiones.setNotaVenta(null);
				ejecutivoComisiones.setCobranza(cobranza);
				ejecutivoComisiones.setEstado("AC");
				ejecutivoComisiones.setFechaRegistro(cobranza.getFechaRegistro());
				ejecutivoComisiones.setPagado(Boolean.FALSE);
				ejecutivoComisiones.setUsuarioRegistro(cobranza.getUsuarioRegistro());
				ejecutivoComisiones.setPorcentaje(comision);
				ejecutivoComisiones.setImporte(importe);
				ejecutivoComisiones.setEjecutivo(ejecutivo);
				ejecutivoComisionesDao.registrar(ejecutivoComisiones);
			}
			//caja movimiento
			
			//caja movimiento
			CajaMovimiento cajaMovimiento=cajaServicio.IngresoPorCobranza(cobranza);
			CajaSesion cajaSesion=cajaSesionDao.RetornarPorId(cajaMovimiento.getCajaSesion().getId());
			
			cajaSesion.setSaldoNacional(cajaSesion.getSaldoNacional()+cajaMovimiento.getMonto());
			cajaSesion.setSaldoExtranjero(cajaSesion.getSaldoExtranjero()+cajaMovimiento.getMontoExtranjero());
							
			
			cajaSesion=cajaSesionDao.update(cajaSesion);
			sessionMain.setCajaSesion(cajaSesion);
			
//			cajaMovimiento.setSaldoExtranjero(cajaSesion.getSaldoExtranjero());
//			cajaMovimiento.setSaldoNacional(cajaSesion.getSaldoNacional());
//			
//			CajaMovimiento c=cajaMovimientoDao.create(cajaMovimiento);
//			
//			CajaMovimiento cajaMovimiento=cajaServicio.IngresoPorCobranza(cobranza);
//			CajaSesion cajaSesion=cajaSesionDao.RetornarPorId(cajaMovimiento.getCajaSesion().getId());
			
			cajaSesion.setSaldoNacional(cajaSesion.getSaldoNacional()+cajaMovimiento.getMonto());
			cajaSesion.setSaldoExtranjero(cajaSesion.getSaldoExtranjero()+cajaMovimiento.getMontoExtranjero());
			cajaSesionDao.update(cajaSesion);
			cajaMovimiento.setSaldoExtranjero(cajaSesion.getSaldoExtranjero());
			cajaMovimiento.setSaldoNacional(cajaSesion.getSaldoNacional());
			CajaMovimiento c=cajaMovimientoDao.create(cajaMovimiento);
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
			//KardexCliente kcIngreso = generarKardexIngreso(cliente,cobranza.getFechaRegistro(),cobranza.getCodigo(),cobranza.getTipoCambio(),cobranza.getMontoExtranjero(),cobranza.getMontoNacional(),0,0,null,cobranza.getUsuarioRegistro());
			//kardexClienteDao.registrarBasic(kcIngreso);
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
	

	public PagoComision modificar(PagoComision usuario){
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


	public List<PagoComision> obtenerOrdenAscPorId(){
		return findAscAllOrderedByParameter("id");
	}

	public List<PagoComision> obtenerOrdenDescPorId(){
		return findDescAllOrderedByParameter("id");
	}

	
}
