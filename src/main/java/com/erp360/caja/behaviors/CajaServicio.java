package com.erp360.caja.behaviors;

import java.util.Date;

import javax.inject.Inject;

import org.apache.xmlbeans.impl.inst2xsd.VenetianBlindStrategy;

import com.erp360.dao.CajaSesionDao;
import com.erp360.enums.TipoMovimiento;
import com.erp360.enums.TipoPago;
import com.erp360.interfaces.ICajaSesionDao;
import com.erp360.model.CajaMovimiento;
import com.erp360.model.CajaMovimientoDetalle;
import com.erp360.model.Cobranza;
import com.erp360.model.NotaVenta;
import com.erp360.util.FacturacionUtil;
import com.erp360.util.SessionMain;

public class CajaServicio {
		
	private @Inject ICajaSesionDao cajaSesionDao;
	private @Inject SessionMain sessionMain;
	
	
	 public CajaMovimiento IngresoPorVenta(NotaVenta notaVenta){
		
		
//		 sessionMain.setCajaSesion(cajaSesionDao.obtenerPorUsuarioyEmpresa(sessionMain.getUsuarioLogin(), sessionMain.getEmpresaLogin()));
		CajaMovimiento cajaMovimiento= new CajaMovimiento();
		cajaMovimiento.setCajaSesion(sessionMain.getCajaSesion());		
		cajaMovimiento.setTipo("I");
		cajaMovimiento.setTipoMovimiento(TipoMovimiento.VEN);
		cajaMovimiento.setTipoPago(TipoPago.EFE);
		cajaMovimiento.setTipoCambio(sessionMain.getTipoCambio().getUnidad());
		cajaMovimiento.setFechaModificacion(new Date());
		cajaMovimiento.setFechaRegistro(new Date());
		cajaMovimiento.setSucursal(sessionMain.getSucursalLogin());
		cajaMovimiento.setRazonSocial(notaVenta.getCliente().getNombres()+" "+notaVenta.getCliente().getApellidos());
		cajaMovimiento.setDucumento(notaVenta.getCodigo());
		if(notaVenta.getTipoVenta().equals("CREDITO")){
			cajaMovimiento.setMontoExtranjero(notaVenta.getCuotaInicialExtranjero());
			cajaMovimiento.setMonto(notaVenta.getCuotaInicial());
			cajaMovimiento.setDescripcion("INGRESO POR CUOTA INICIAL VENTA DE PRODUCTO(S) AL CREDITO :"+notaVenta.getCodigo());
			cajaMovimiento.setMontoLiteral(FacturacionUtil.obtenerMontoLiteral(cajaMovimiento.getMonto()));
		
			if(notaVenta.getEstadoPago().equals("RE")){
				
				cajaMovimiento.setMontoExtranjero(notaVenta.getMontoReservaExtranjero());
				cajaMovimiento.setMonto(notaVenta.getMontoReserva());
				cajaMovimiento.setDescripcion("INGRESO POR RESERVA VENTA DE PRODUCTO(S) AL CREDITO :"+notaVenta.getCodigo());
				cajaMovimiento.setMontoLiteral(FacturacionUtil.obtenerMontoLiteral(cajaMovimiento.getMonto()));
				
			}else if(notaVenta.getEstadoPago().equals("CO")){
				//aqui para cotizacion
			}
		}else if(notaVenta.getTipoVenta().equals("CONTADO")){
			cajaMovimiento.setMontoExtranjero(notaVenta.getMontoTotalExtranjero());
			cajaMovimiento.setMonto(notaVenta.getMontoTotal());
			cajaMovimiento.setDescripcion("INGRESO POR VENTA AL CONTADO DE PRODUCTO(S) :"+notaVenta.getCodigo());
			cajaMovimiento.setMontoLiteral(FacturacionUtil.obtenerMontoLiteral(cajaMovimiento.getMonto()));
			
		}
		 CajaMovimientoDetalle detalle= new CajaMovimientoDetalle();
		 detalle.setCajaMovimiento(cajaMovimiento);
		 detalle.setDescripcion(cajaMovimiento.getDescripcion());
		 detalle.setMonto(cajaMovimiento.getMonto());
		 detalle.setMontoExtranjero(cajaMovimiento.getMontoExtranjero());
		 detalle.setEstado("AC");
		 detalle.setFechaModificacion(new Date());
		 detalle.setFechaRegistro(new Date());
		 detalle.setNotaVenta(notaVenta);
		 detalle.setSucursal(cajaMovimiento.getSucursal());
		 detalle.setUsuarioRegistro(cajaMovimiento.getUsuarioRegistro());
		 detalle.setTipoCambio(cajaMovimiento.getTipoCambio());
		 cajaMovimiento.getListaCajaMovimientoDetalles().add(detalle);
		
		return cajaMovimiento;
	}
	 
	 
	 public CajaMovimiento IngresoPorCobranza(Cobranza cobranza){
			
					
			//sessionMain.setCajaSesion(cajaSesionDao.obtenerPorUsuarioyEmpresa(sessionMain.getUsuarioLogin(), sessionMain.getEmpresaLogin()));
			CajaMovimiento cajaMovimiento= new CajaMovimiento();
			cajaMovimiento.setCajaSesion(sessionMain.getCajaSesion());		
			cajaMovimiento.setTipo("I");
			cajaMovimiento.setTipoMovimiento(TipoMovimiento.VEN);
			cajaMovimiento.setTipoPago(TipoPago.EFE);
			cajaMovimiento.setTipoCambio(sessionMain.getTipoCambio().getUnidad());
			cajaMovimiento.setFechaModificacion(new Date());
			cajaMovimiento.setFechaRegistro(new Date());
			cajaMovimiento.setSucursal(sessionMain.getSucursalLogin());
			cajaMovimiento.setRazonSocial(cobranza.getGlosa());
			
			cajaMovimiento.setMontoExtranjero(cobranza.getMontoExtranjero());
			cajaMovimiento.setMonto(cobranza.getMontoNacional());
			cajaMovimiento.setDescripcion("INGRESO POR CUOTA INICIAL VENTA DE PRODUCTO(S) AL CREDITO :"+cobranza.getCodigo()+" "+sessionMain.getCajaSesion().getCaja().getNombre());
			cajaMovimiento.setMontoLiteral(FacturacionUtil.obtenerMontoLiteral(cajaMovimiento.getMonto()));
			cajaMovimiento.setDucumento(cobranza.getCodigo());
			
			
			
			 CajaMovimientoDetalle detalle= new CajaMovimientoDetalle();
			 detalle.setCajaMovimiento(cajaMovimiento);
			 detalle.setDescripcion(cajaMovimiento.getDescripcion());
			 detalle.setMonto(cajaMovimiento.getMonto());
			 detalle.setMontoExtranjero(cajaMovimiento.getMontoExtranjero());
			 detalle.setEstado("AC");
			 detalle.setFechaModificacion(new Date());
			 detalle.setFechaRegistro(new Date());
			 detalle.setCobranza(cobranza);
			 detalle.setSucursal(cajaMovimiento.getSucursal());
			 detalle.setUsuarioRegistro(cajaMovimiento.getUsuarioRegistro());
			 detalle.setTipoCambio(cajaMovimiento.getTipoCambio());
			 cajaMovimiento.getListaCajaMovimientoDetalles().add(detalle);
			return cajaMovimiento;
		}
}
