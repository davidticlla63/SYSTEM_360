package com.erp360.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.erp360.model.Empresa;

/**
 * @author mauriciobejaranorivera
 *
 */
public class FacturacionUtil {

	/**
	 * Convertir monstos en literal
	 * @param totalFactura
	 * @return
	 */
	public static String obtenerMontoLiteral(double totalFactura) {
		String totalLiteral;
		try {
			totalLiteral = NumerosToLetras.convertNumberToLetter(totalFactura);
			return totalLiteral;
		} catch (Exception e) {
			System.out.println("Error en obtenerMontoLiteral: "+ e.getMessage());
			return "Error Literal";
		}
	}
	
//	public static String armarCadenaQR(FacturaVenta factura, Empresa empresaLogin) {
//		String cadenaQR = "";
//		try {
//			cadenaQR = new String();
//
//			// NIT emisor
//			cadenaQR = cadenaQR.concat(empresaLogin.getNit());
//			cadenaQR = cadenaQR.concat("|");
//
//			// Numero de Factura
//			cadenaQR = cadenaQR.concat(factura.getNumeroFactura());
//			cadenaQR = cadenaQR.concat("|");
//
//			// Numero de Autorizacion
//			cadenaQR = cadenaQR.concat(factura.getNumeroAutorizacion());
//			cadenaQR = cadenaQR.concat("|");
//
//			// Fecha de Emision
//			cadenaQR = cadenaQR.concat(Time.convertSimpleDateToString(factura
//					.getFechaFactura()));
//			cadenaQR = cadenaQR.concat("|");
//
//			// Total Bs
//			cadenaQR = cadenaQR.concat(String.valueOf(factura
//					.getTotalFacturado()));
//			cadenaQR = cadenaQR.concat("|");
//
//			// Importe Base para el Credito Fiscal
//			cadenaQR = cadenaQR.concat(String.valueOf(factura
//					.getImporteBaseDebitoFiscal()));
//			cadenaQR = cadenaQR.concat("|");
//
//			// Codigo de Control
//			cadenaQR = cadenaQR.concat(factura.getCodigoControl());
//			cadenaQR = cadenaQR.concat("|");
//
//			// NIT / CI del Comprador
//			cadenaQR = cadenaQR.concat(factura.getNitCi());
//			cadenaQR = cadenaQR.concat("|");
//
//			// Importe ICE/IEHD/TASAS [cuando corresponda]
//			cadenaQR = cadenaQR.concat("0");
//			cadenaQR = cadenaQR.concat("|");
//
//			// Importe por ventas no Gravadas o Gravadas a Tasa Cero [cuando
//			// corresponda]
//			cadenaQR = cadenaQR.concat("0");
//			cadenaQR = cadenaQR.concat("|");
//
//			// Importe no Sujeto a Credito Fiscal [cuando corresponda]
//			cadenaQR = cadenaQR.concat("0");
//			cadenaQR = cadenaQR.concat("|");
//
//			// Descuentos Bonificaciones y Rebajas Obtenidas [cuando
//			// corresponda]
//			cadenaQR = cadenaQR.concat("0");
//
//			return cadenaQR;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.err.println("Error en armarCadenaQR: " + e.getMessage());
//			return cadenaQR;
//		}
//	}
//	
	
	public static String armarCadenaQR(String nit,int numeroFactura,String numeroAutorizacion,Date fechaFactura,double totalFacturado,String codigoControl,String nitCi) {
		String cadenaQR = "";
		try {
			cadenaQR = new String();

			// NIT emisor
			cadenaQR = cadenaQR.concat(nit);
			cadenaQR = cadenaQR.concat("|");

			// Numero de Factura
			cadenaQR = cadenaQR.concat(String.valueOf(numeroFactura));
			cadenaQR = cadenaQR.concat("|");

			// Numero de Autorizacion
			cadenaQR = cadenaQR.concat(numeroAutorizacion);
			cadenaQR = cadenaQR.concat("|");

			// Fecha de Emision
			cadenaQR = cadenaQR.concat(obtenerFechaEmision(fechaFactura));
			cadenaQR = cadenaQR.concat("|");

			// Total Bs
			cadenaQR = cadenaQR.concat(String.valueOf(totalFacturado));
			cadenaQR = cadenaQR.concat("|");

			// Importe Base para el Credito Fiscal
			cadenaQR = cadenaQR.concat(String.valueOf(totalFacturado));
			cadenaQR = cadenaQR.concat("|");

			// Codigo de Control
			cadenaQR = cadenaQR.concat(codigoControl);
			cadenaQR = cadenaQR.concat("|");

			// NIT / CI del Comprador
			cadenaQR = cadenaQR.concat(nitCi);
			cadenaQR = cadenaQR.concat("|");

			// Importe ICE/IEHD/TASAS [cuando corresponda]
			cadenaQR = cadenaQR.concat("0");
			cadenaQR = cadenaQR.concat("|");

			// Importe por ventas no Gravadas o Gravadas a Tasa Cero [cuando
			// corresponda]
			cadenaQR = cadenaQR.concat("0");
			cadenaQR = cadenaQR.concat("|");

			// Importe no Sujeto a Credito Fiscal [cuando corresponda]
			cadenaQR = cadenaQR.concat("0");
			cadenaQR = cadenaQR.concat("|");

			// Descuentos Bonificaciones y Rebajas Obtenidas [cuando
			// corresponda]
			cadenaQR = cadenaQR.concat("0");

			return cadenaQR;
		} catch (Exception e) {
			e.printStackTrace();
			return cadenaQR;
		}
	}
	
	public static String obtenerFechaEmision(Date fechaEmision) {
		try {
			String DATE_FORMAT = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			return sdf.format(fechaEmision);

		} catch (Exception e) {
			return "Error Fecha Emision";
		}
	}
	
	public static String obtenerCodigoControl(Date fechaFactura,int numeroFactura,String numeroAutorizacion,String llaveControl, double totalBolivianos, String nitCi) {
		try {
			CodigoControl7 cc = new CodigoControl7();
			int montoFactura = (int) totalBolivianos;
			cc.setNumeroAutorizacion(numeroAutorizacion);
			cc.setNumeroFactura(numeroFactura);
			cc.setNitci(nitCi);
			cc.setFechaTransaccion(fechaFactura);
			cc.setMonto(montoFactura);
			cc.setLlaveDosificacion(llaveControl);
			// Obtener Codigo Control V7
			String codigoControlV7 = cc.obtener();
			return codigoControlV7;
		} catch (Exception e) {
			return "Error CC";
		}
	}
	
//	public static FacturaVenta calcularDatosFacturacion(FacturaVenta newFactura,
//			Dosificacion dosificacion, NitCliente nitCliente) throws FacturacionNotAppliesException {
//		System.out.println("Ingreso a calcularDatosFacturacion");
//		newFactura.setFechaLimiteEmision(dosificacion.getFechaLimiteEmision()); // Fecha
//		newFactura.setNumeroAutorizacion(dosificacion.getNumeroAutorizacion());	// Emision
//		newFactura.setTotalLiteral(obtenerMontoLiteral(newFactura.getTotalFacturado()));
//		// tipo de cliente
//		if (nitCliente.getCliente().getTipo().equals("NATURAL")) { // NAURAL o  JURIDICO
//			newFactura.setNitCi(nitCliente.getCliente().getNit());// CI del  Comprador
//		} else {
//			newFactura.setNitCi(nitCliente.getNit());// NIT del Comprador
//		}
//		newFactura.setConcepto("Venta: " + newFactura.getNumeroFactura());
//		newFactura.setFechaRegistro(new Date());
//		newFactura.setNumeroFactura("" + dosificacion.getNumeroSecuencia());
//		newFactura.setNitCi(nitCliente.getNit());
//		newFactura.setCliente(nitCliente.getCliente());//RAZON SOCIAL
//		newFactura.setCodigoRespuestaRapida(armarCadenaQR(newFactura,
//				newFactura.getSucursal().getEmpresa()));
//		newFactura.setId(null);
//
////		newFactura.setTipoCambio(6.96);// cambiar
//
//		// LIBRO DE VENTA
//		newFactura.setImporteICE(0);
//		newFactura.setImporteExportaciones(0);
//		newFactura.setImporteVentasGrabadasTasaCero(0);
//		newFactura.setImporteSubTotal(newFactura.getTotalFacturado()
//				- newFactura.getImporteICE()
//				- newFactura.getImporteExportaciones()
//				- newFactura.getImporteVentasGrabadasTasaCero());
//		newFactura.setImporteDescuentosBonificaciones(newFactura.getTotalDescuentoNacional());
//		newFactura.setImporteBaseDebitoFiscal(newFactura.getImporteSubTotal()
//				- newFactura.getImporteDescuentosBonificaciones());
//		if (newFactura.getActividad().isCreditoFiscal()) {
//			newFactura
//					.setDebitoFiscal(newFactura.getImporteBaseDebitoFiscal() * 0.13);
//			newFactura.setCreditoFiscalLabel("");
//		} else {
//			newFactura.setImporteBaseDebitoFiscal(0);
//			newFactura.setDebitoFiscal(0);
//			newFactura.setCreditoFiscalLabel("Sin Derecho a Credito Fiscal");
//		}
//		newFactura.setGestion(Time.obtenerFormatoYYYY(newFactura.getFechaFactura()));
//		newFactura.setMes(Time.obtenerFormatoMM(newFactura.getFechaFactura()));
//		System.out.println("finalizo  calcularDatosFacturacion");
//		return newFactura;
//
//	}
//	
//	public static FacturaVenta cargarFacturaVenta(Dosificacion dosificacion,String nit_ci,Double totalBs,Date fechaFactura,String usuarioRegistro){
//		int numeroFactura = dosificacion.getNumeroSecuencia();
//		String numeroAutorizacion =  dosificacion.getNumeroAutorizacion();
//		String llaveControl =dosificacion.getLlaveControl();
//		Date fechaFactura1 = fechaFactura;
//		String nitCi = nit_ci;
//		double totalFacturado =totalBs;
//		double totalBolivianos = totalBs;		
//		FacturaVenta fv = new FacturaVenta();
//		fv.setFechaLimiteEmision(dosificacion.getFechaLimiteEmision());
//		fv.setTotalEfectivo(totalBs);
//		fv.setTotalFacturado(totalBs);
//		fv.setFechaRegistro(new Date());
//		fv.setFechaFactura(fechaFactura1);
//		fv.setNumeroFactura(String.valueOf(numeroFactura));
//		fv.setCambio(0);
//		fv.setCodigoControl(FacturacionUtil.obtenerCodigoControl(fechaFactura1, numeroFactura, numeroAutorizacion, llaveControl, totalBolivianos, nitCi));
//		fv.setCodigoRespuestaRapida(FacturacionUtil.armarCadenaQR(nitCi, numeroFactura, numeroAutorizacion, fechaFactura1, totalFacturado, fv.getCodigoControl(), nitCi));
//		fv.setConcepto("Venta");
//		fv.setCreditoFiscalLabel("0");
//		fv.setDebitoFiscal(0d);
//		fv.setEstado("AC");
//		fv.setImporteBaseDebitoFiscal(0d);
//		fv.setImporteDescuentosBonificaciones(0d);
//		fv.setImporteExportaciones(0d);
//		fv.setImporteICE(0d);
//		fv.setImporteSubTotal(0d);
//		fv.setImporteVentasGrabadasTasaCero(0d);
//		fv.setNumeroAutorizacion(numeroAutorizacion);
//		fv.setTipoCambio(0d);
//		fv.setTipoPago("EFECTIVO");		
//		fv.setTotalLiteral(FacturacionUtil.obtenerMontoLiteral(totalFacturado));
//		fv.setTotalPagar(0d);
//		fv.setUsuarioRegistro(usuarioRegistro);
////		fv.setProductoNotaVenta(notaVenta);
////		notaVenta.setFacturaVenta(fv);
//		return fv;
//	}
//	
	
}
