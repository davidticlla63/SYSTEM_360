package com.erp360.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.erp360.caja.behaviors.CajaServicio;
import com.erp360.interfaces.ICajaMovimientoDao;
import com.erp360.interfaces.ICajaSesionDao;
import com.erp360.model.Almacen;
import com.erp360.model.AlmacenProducto;
import com.erp360.model.CajaMovimiento;
import com.erp360.model.CajaSesion;
import com.erp360.model.Cliente;
import com.erp360.model.CuentaCobrar;
import com.erp360.model.DetalleNotaVenta;
import com.erp360.model.DetalleOrdenSalida;
import com.erp360.model.Ejecutivo;
import com.erp360.model.EjecutivoComisiones;
import com.erp360.model.Gestion;
import com.erp360.model.KardexCliente;
import com.erp360.model.KardexProducto;
import com.erp360.model.MovimientoCaja;
import com.erp360.model.NotaVenta;
import com.erp360.model.OrdenSalida;
import com.erp360.model.ParametroCobranza;
import com.erp360.model.ParametroInventario;
import com.erp360.model.ParametroVenta;
import com.erp360.model.PlanCobranza;
import com.erp360.model.Producto;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;
import com.erp360.util.V;
import com.erp360.util.W;
/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@Stateless
public class NotaVentaDao extends DataAccessObjectJpa<NotaVenta,DetalleNotaVenta,PlanCobranza,OrdenSalida,DetalleOrdenSalida, AlmacenProducto, KardexProducto, CajaMovimiento, V, W> {

	private @Inject MovimientoCajaDao movimientoCajaDao;
	private @Inject CuentaCobrarDao deudaClienteDao;
	private @Inject AlmacenProductoDao almacenProductoDao;
	private @Inject OrdenSalidaDao ordenSalidaDao;
	private @Inject KardexClienteDao kardexClienteDao;
	private @Inject KardexProductoDao kardexProductoDao;
	private @Inject ICajaMovimientoDao cajaMovimientoDao;
	private @Inject CajaServicio cajaServicio;
	private @Inject EjecutivoComisionesDao ejecutivoComisionesDao;
	private @Inject ICajaSesionDao cajaSesionDao;
	private @Inject SessionMain sessionMain;

	public NotaVentaDao(){
		super(NotaVenta.class,DetalleNotaVenta.class,PlanCobranza.class,OrdenSalida.class,DetalleOrdenSalida.class,AlmacenProducto.class,KardexProducto.class,CajaMovimiento.class);
	}
	
	public NotaVenta anularNotaVenta(NotaVenta notaVenta, Almacen almacen){
		try{
			beginTransaction();
			// cambiar el estado
			notaVenta.setEstadoPago("AN");
			// movimiento de caja
			notaVenta.getMovimientoCaja().setEstado("AN");
			update(notaVenta);
			System.out.println("notaVenta: "+notaVenta.getEstadoPago());
			// kardex del cliente
			List<KardexCliente> kardexClientes = kardexClienteDao.obtenerKardexPorVenta(notaVenta);
			for(KardexCliente kc : kardexClientes){
				kc.setEstado("AN");
				kardexClienteDao.update(kc);
				System.out.println("kardexC: "+kc.getEstado());
			}
			for(DetalleNotaVenta dnv : notaVenta.getDetailSalesNotes()){
				// inventario (ultimo almacen_producto)
				AlmacenProducto ap = almacenProductoDao.obtenerAlmacenProductoPorPEPS(dnv.getProducto(),almacen);
				ap.setStock(ap.getStock()+dnv.getCantidad());
				almacenProductoDao.update(ap);
				System.out.println("ap: "+ap.getEstado());
			}
			//CuentaCobrar
			CuentaCobrar cuentaCobrar = deudaClienteDao.obtenerPorNotaVenta(notaVenta);
			cuentaCobrar.setEstado("AN");
			deudaClienteDao.update(cuentaCobrar);
			System.out.println("cuentaCobrar: "+cuentaCobrar.getEstado());
			//OrdenSalida ordenSalida
			OrdenSalida ordenSalida = ordenSalidaDao.obtenerPorNotaVenta(notaVenta);
			ordenSalida.setEstado("AN");
			updateS(ordenSalida);
			System.out.println("ordenSalida: id="+ordenSalida.getId()+", estado="+ordenSalida.getEstado());
			// kardex del producto
			KardexProducto kp = kardexProductoDao.obtenerPorOrdenSalida(ordenSalida);
			System.out.println("kardexProductos: "+kp);
			kp.setEstado("AN");	
			updateQ(kp);
			System.out.println("kp: "+kp.getEstado());
			commitTransaction();
			return notaVenta;
		}catch(Exception e){
			String cause = e.getMessage();
			System.out.println("error: "+cause);
			rollbackTransaction();
			return null;
		}
	}
	
	/**
	 * Registrar Nota Venta, implica la disminucion de stock de los productos a traves de orden de salida
	 * @param notaVenta
	 * @param listDetalleNotaVenta
	 * @param listPlanCobranza
	 * @param gestionSesion
	 * @param param
	 * @param param2
	 * @return
	 */
	public NotaVenta update(String observacion,Usuario usuario,NotaVenta notaVenta,List<DetalleNotaVenta> listDetalleNotaVenta,List<PlanCobranza> listPlanCobranza,Gestion gestionSesion,ParametroInventario param,ParametroCobranza param2,ParametroVenta param3){
		double totalPlanPagoNacional = notaVenta.getMontoTotal() - notaVenta.getCuotaInicial();
		double totalPlanPagoExtranjero = notaVenta.getMontoTotalExtranjero() - notaVenta.getCuotaInicialExtranjero();
		try{
			beginTransaction();
				//crear movimiento a caja
				MovimientoCaja movimientoCaja = new MovimientoCaja();
				movimientoCaja.setCorrelativo(String.format("%06d",movimientoCajaDao.correlativoMovimiento(gestionSesion)));
				movimientoCaja.setEstado("PR");
				movimientoCaja.setFechaAprobacion(notaVenta.getFechaRegistro());
				movimientoCaja.setFechaDocumento(notaVenta.getFechaRegistro());
				movimientoCaja.setFechaRegistro(notaVenta.getFechaRegistro());
				movimientoCaja.setGestion(notaVenta.getGestion());
				movimientoCaja.setMoneda(notaVenta.getMoneda());
				movimientoCaja.setObservacion("Ninguna");
				movimientoCaja.setTipoCambio(notaVenta.getTipoCambio());
				if(notaVenta.getTipoVenta().equals("CREDITO")){
					movimientoCaja.setNumeroDocumento("0");
					movimientoCaja.setTipoDocumento("NOTA VENTA");
					movimientoCaja.setMotivoIngreso("INGRESO POR CUOTA INICIAL VENTA DE PRODUCTO(S) AL CREDITO");
					movimientoCaja.setTotalImporteExtranjero(notaVenta.getCuotaInicialExtranjero());
					movimientoCaja.setTotalImporteNacional(notaVenta.getCuotaInicial());
					if(notaVenta.getEstadoPago().equals("RE")){
						movimientoCaja.setTipoDocumento("NOTA VENTA(RESERVA)");	
						movimientoCaja.setMotivoIngreso("INGRESO POR RESERVA VENTA DE PRODUCTO(S) AL CREDITO");
						movimientoCaja.setTotalImporteExtranjero(notaVenta.getMontoReservaExtranjero());
						movimientoCaja.setTotalImporteNacional(notaVenta.getMontoReserva());
					}else if(notaVenta.getEstadoPago().equals("CO")){
						//aqui para cotizacion
					}
				}else if(notaVenta.getTipoVenta().equals("CONTADO")){
					movimientoCaja.setMotivoIngreso("INGRESO POR VENTA AL CONTADO DE PRODUCTO(S)");
					movimientoCaja.setNumeroDocumento("0");
					movimientoCaja.setTipoDocumento("NOTA VENTA");
					movimientoCaja.setTotalImporteExtranjero(notaVenta.getMontoTotalExtranjero());
					movimientoCaja.setTotalImporteNacional(notaVenta.getMontoTotal());
				}
				movimientoCaja.setUsuarioAprobacion(usuario);
				movimientoCaja.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
				movimientoCaja.setTipo("INGRESO");
				movimientoCaja = movimientoCajaDao.registrarBasic(movimientoCaja);
				notaVenta.setMovimientoCaja(movimientoCaja);
				notaVenta = update(notaVenta);
				movimientoCaja.setNumeroDocumento(notaVenta.getCodigo());
				//kardex cliente
				KardexCliente kcEgreso = generarKardexEgreso(notaVenta,notaVenta.getCliente(),notaVenta.getFechaRegistro(),notaVenta.getCodigo(),notaVenta.getTipoCambio(),notaVenta.getMontoTotalExtranjero(),notaVenta.getMontoTotal(),notaVenta.getMontoReservaExtranjero(),notaVenta.getMontoReserva(),notaVenta.getEstadoPago(),notaVenta.getUsuarioRegistro());
				KardexCliente kcIngreso = generarKardexIngreso(notaVenta,notaVenta.getCliente(),notaVenta.getFechaRegistro(),notaVenta.getCodigo(),notaVenta.getTipoCambio(),notaVenta.getCuotaInicialExtranjero(),notaVenta.getCuotaInicial(),notaVenta.getMontoReservaExtranjero(),notaVenta.getMontoReserva(),notaVenta.getEstadoPago(),notaVenta.getUsuarioRegistro());
				if( ! notaVenta.getEstadoPago().equals("CO")){//no deberia registrar si es una cotizacion
					kardexClienteDao.registrarBasic(kcEgreso);
					kardexClienteDao.registrarBasic(kcIngreso);
				}
				if(totalPlanPagoNacional>0){//si la deuda no es mayor a cero entonces un tipoVenta='CONTADO'
					CuentaCobrar cuentaCobrar = new CuentaCobrar();
					cuentaCobrar.setCorrelativo(String.format("%06d",deudaClienteDao.correlativoCuentaCobrar()));
					cuentaCobrar.setEstado("PN");
					cuentaCobrar.setFechaRegistro(notaVenta.getFechaRegistro());
					cuentaCobrar.setMoneda(notaVenta.getMoneda());
					cuentaCobrar.setNotaVenta(notaVenta);
					cuentaCobrar.setObservacion(observacion);
					cuentaCobrar.setSaldoExtranjero(totalPlanPagoExtranjero);//aqui va el monto total del plan de pago credito
					cuentaCobrar.setSaldoNacional(totalPlanPagoNacional);//aqui va el monto total del plan de pago credito
					cuentaCobrar.setTipoCambio(notaVenta.getTipoCambio());
					cuentaCobrar.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
					cuentaCobrar = deudaClienteDao.registrarBasic(cuentaCobrar);
				}
			
			double cant = 0;
			for(DetalleNotaVenta dnv:listDetalleNotaVenta){
				//dnv.setId(0);
				//dnv.setEstado("AC");
				dnv.setFechaRegistro(notaVenta.getFechaRegistro());
				//dnv.setNotaVenta(notaVenta);
				dnv.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
				updateE(dnv);
				if(dnv.getId()==null){
					//createE(dnv); si se agrego nuevos
				}
				cant = cant + dnv.getCantidad();
			}
			//verificar si se va a registrar el plan de cobranzas(cuando es una reserva o una cotizacion, pero con un estado inactivo)
			for(PlanCobranza pc: listPlanCobranza){
				//pc.setId(0);
				pc.setEstado("AC");
				//if(notaVenta.getEstadoPago().equals("CO") ){
				//	pc.setEstado("PN");
				//}
				pc.setFechaRegistro(notaVenta.getFechaRegistro());
				//pc.setNotaVenta(notaVenta);
				pc.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
				updateR(pc);
				if(pc.getId()==null){
					//createR(pc); si se agrego nuevos
				}
			}
				//-----Verificar el Tipo Disminucion de stock
				if(param2.getTipoDisminucionStock().equals("VOS")  ){
					//GENERA VARIAS ORDEN DE SALIDA
					List<OrdenSalida> lsitOrdenSalida = new ArrayList<>();
					List<DetalleOrdenSalida> listDetalleOrdenSalida = new ArrayList<>();
					for(DetalleNotaVenta detalleNotaVenta : listDetalleNotaVenta){
						AlmacenProducto almacenProducto = productoTieneStock(detalleNotaVenta.getProducto(),param,param3.getAlmacenVenta());
						if(almacenProducto==null){
							FacesUtil.infoMessage("Validación de Stock", "El producto "+detalleNotaVenta.getProducto().getNombre()+" NO tiene existencia, y no se puede proceder a hacer la venta.");
							rollbackTransaction();
							return null;
						}else if(almacenProducto.getStock()<detalleNotaVenta.getCantidad()){
							FacesUtil.infoMessage("Validación de Stock", "El producto "+detalleNotaVenta.getProducto().getNombre()+" SOLO tiene "+detalleNotaVenta.getCantidad()+" existencia(s).");
							rollbackTransaction();
							return null;
						}
						OrdenSalida ordenSalida = obtenerOrdenSalidadPorAlmacen(lsitOrdenSalida,almacenProducto.getAlmacen());
						ordenSalida.setAlmacen(almacenProducto.getAlmacen());
						ordenSalida.setEstado("AC");
						ordenSalida.setGestion(gestionSesion);
						ordenSalida.setObservacion("ninguna");
						ordenSalida.setMotivoSalida("Salida por Venta Nº "+notaVenta.getCodigo());
						ordenSalida.setNotaVenta(notaVenta);
						ordenSalida.setTotalImporte(ordenSalida.getTotalImporte()+(detalleNotaVenta.getCantidad()*detalleNotaVenta.getPrecio()));
						ordenSalida.setTotalImporteExtranjero(ordenSalida.getTotalImporteExtranjero()+(detalleNotaVenta.getCantidad()*detalleNotaVenta.getPrecioExtranjero()));
						ordenSalida.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
						ordenSalida.setFechaRegistro(notaVenta.getFechaRegistro());
						ordenSalida.setFechaPedido(notaVenta.getFechaRegistro());
						ordenSalida.setFechaAprobacion(notaVenta.getFechaRegistro());
						if(! agregarOrdenSalidaALista(lsitOrdenSalida,ordenSalida)){
							int numeroCorrelativo = ordenSalidaDao.obtenerNumeroOrdenSalida(gestionSesion);
							ordenSalida.setCorrelativo(String.format("%06d", numeroCorrelativo));;
							ordenSalida = createS(ordenSalida);
							lsitOrdenSalida.add(ordenSalida);
						}else{
							updateS(ordenSalida);
						}
					}
					for(DetalleNotaVenta detalleNotaVenta : listDetalleNotaVenta){
						//verificar y agrupar productos por almacen -> metodo que recupera la orden de servicio de acuerdo al almacen
						//luego se debe saber a cual orden de salida debe ser asigndaa el detalle de la orden de salida
						AlmacenProducto almacenProducto = productoTieneStock(detalleNotaVenta.getProducto(),param,param3.getAlmacenVenta());
						OrdenSalida ordenSalida = obtenerOrdenSalidadPorAlmacen(lsitOrdenSalida,almacenProducto.getAlmacen());
						DetalleOrdenSalida detalleOrdenSalida = new DetalleOrdenSalida();
						detalleOrdenSalida.setCantidadEntregada(detalleNotaVenta.getCantidad());
						detalleOrdenSalida.setCantidadSolicitada(detalleNotaVenta.getCantidad());
						detalleOrdenSalida.setEstado("AC");
						detalleOrdenSalida.setObservacion("Ninguna");
						detalleOrdenSalida.setOrdenSalida(ordenSalida);
						detalleOrdenSalida.setPrecioUnitario(detalleNotaVenta.getPrecio());
						detalleOrdenSalida.setProducto(detalleNotaVenta.getProducto());
						detalleOrdenSalida.setTotal(detalleNotaVenta.getPrecio()*detalleNotaVenta.getCantidad());
						detalleOrdenSalida.setFechaRegistro(notaVenta.getFechaRegistro());
						detalleOrdenSalida.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
						//createE(detalleNotaVenta);
						listDetalleOrdenSalida.add(detalleOrdenSalida);
						//actualizar stock AlmacenProducto
						almacenProducto.setStock(almacenProducto.getStock()-detalleNotaVenta.getCantidad());
						updateP(almacenProducto);
						//kardex
						KardexProducto kardex = new KardexProducto();
						kardex.setEstado("AC");
						kardex.setFechaRegistro(notaVenta.getFechaRegistro());
						kardex.setOrdenSalida(ordenSalida);
						kardex.setStockSaliente(detalleNotaVenta.getCantidad());
						kardex.setTipoMovimiento("OS");//orden de ingreso
						kardex.setTipoTransaccion("SALIDA X VENTA- Nº NOTA VENTA "+notaVenta.getCodigo());
						kardex.setPrecioSalida(detalleNotaVenta.getPrecio());
						kardex.setStockSaliente(detalleNotaVenta.getCantidad());
						kardex.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
						kardex.setAlmacen(almacenProducto.getAlmacen());
						kardex.setProducto(almacenProducto.getProducto());
						createQ(kardex);
					}
				}else if(param2.getTipoDisminucionStock().equals("OT")){
					//GENERAR ORDEN DE TRASPASO A UN SOLO ALMACEN 
					//LUEGO GENERAR UNA SOLA ORDEN DE SALIDA 4321`qwer1
					//				for(DetalleNotaVenta detalleNotaVenta : listDetalleNotaVenta){
					//
					//				}
				}
			
			//----
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "NotaVenta "+notaVenta.getCodigo());
			return notaVenta;
		}catch(Exception e){
			String cause=e.getMessage();
			System.out.println("error: "+cause);
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return null;
		}
	}

	/**
	 * Registrar Nota Venta, implica la disminucion de stock de los productos a traves de orden de salida
	 * @param notaVenta
	 * @param listDetalleNotaVenta
	 * @param listPlanCobranza
	 * @param gestionSesion
	 * @param param
	 * @param param2
	 * @return
	 */
	public NotaVenta registrarSinCuotaInicial(String observacion,Usuario usuario,NotaVenta notaVenta,List<DetalleNotaVenta> listDetalleNotaVenta,List<PlanCobranza> listPlanCobranza,Gestion gestionSesion,ParametroInventario param,ParametroCobranza param2,ParametroVenta param3){
		double totalPlanPagoNacional = notaVenta.getMontoTotal() - notaVenta.getCuotaInicial();
		double totalPlanPagoExtranjero = notaVenta.getMontoTotalExtranjero() - notaVenta.getCuotaInicialExtranjero();
		try{
			beginTransaction();
			notaVenta = create(notaVenta);
			
			double cant = 0;
			for(DetalleNotaVenta dnv:listDetalleNotaVenta){
				dnv.setId(0);
				dnv.setEstado("AC");
				dnv.setFechaRegistro(notaVenta.getFechaRegistro());
				dnv.setNotaVenta(notaVenta);
				dnv.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
				createE(dnv);
				cant = cant + dnv.getCantidad();
			}
			//verificar si se va a registrar el plan de cobranzas(cuando es una reserva o una cotizacion, pero con un estado inactivo)
			for(PlanCobranza pc: listPlanCobranza){
				pc.setId(0);
				pc.setEstado("AC");
				//if(notaVenta.getEstadoPago().equals("CO") ){
				//	pc.setEstado("PN");
				//}
				pc.setFechaRegistro(notaVenta.getFechaRegistro());
				pc.setNotaVenta(notaVenta);
				pc.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
				createR(pc);
			}
			//solo deberia registrar si la venta es diferente a RE=RESERVA
			if(!notaVenta.getEstadoPago().equals("CO")){
				//-----Verificar el Tipo Disminucion de stock
				if(param2.getTipoDisminucionStock().equals("VOS") ){
					//GENERA VARIAS ORDEN DE SALIDA
					List<OrdenSalida> lsitOrdenSalida = new ArrayList<>();
					List<DetalleOrdenSalida> listDetalleOrdenSalida = new ArrayList<>();
					for(DetalleNotaVenta detalleNotaVenta : listDetalleNotaVenta){
						AlmacenProducto almacenProducto = productoTieneStock(detalleNotaVenta.getProducto(),param,param3.getAlmacenVenta());
						if(almacenProducto==null){
							FacesUtil.infoMessage("Validación de Stock", "El producto "+detalleNotaVenta.getProducto().getNombre()+" NO tiene existencia, y no se puede proceder a hacer la venta.");
							rollbackTransaction();
							return null;
						}else if(almacenProducto.getStock()<detalleNotaVenta.getCantidad()){
							FacesUtil.infoMessage("Validación de Stock", "El producto "+detalleNotaVenta.getProducto().getNombre()+" SOLO tiene "+detalleNotaVenta.getCantidad()+" existencia(s).");
							rollbackTransaction();
							return null;
						}
						OrdenSalida ordenSalida = obtenerOrdenSalidadPorAlmacen(lsitOrdenSalida,almacenProducto.getAlmacen());
						ordenSalida.setAlmacen(almacenProducto.getAlmacen());
						ordenSalida.setEstado("AC");
						ordenSalida.setGestion(gestionSesion);
						ordenSalida.setObservacion("ninguna");
						ordenSalida.setMotivoSalida("Salida por Venta Nº "+notaVenta.getCodigo());
						ordenSalida.setTotalImporte(ordenSalida.getTotalImporte()+(detalleNotaVenta.getCantidad()*detalleNotaVenta.getPrecio()));
						ordenSalida.setTotalImporteExtranjero(ordenSalida.getTotalImporteExtranjero()+(detalleNotaVenta.getCantidad()*detalleNotaVenta.getPrecioExtranjero()));
						ordenSalida.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
						ordenSalida.setFechaRegistro(notaVenta.getFechaRegistro());
						ordenSalida.setFechaPedido(notaVenta.getFechaRegistro());
						ordenSalida.setFechaAprobacion(notaVenta.getFechaRegistro());
						ordenSalida.setNotaVenta(notaVenta);
						if(! agregarOrdenSalidaALista(lsitOrdenSalida,ordenSalida)){
							int numeroCorrelativo = ordenSalidaDao.obtenerNumeroOrdenSalida(gestionSesion);
							ordenSalida.setCorrelativo(String.format("%06d", numeroCorrelativo));;
							ordenSalida = createS(ordenSalida);
							lsitOrdenSalida.add(ordenSalida);
						}else{
							updateS(ordenSalida);
						}
					}
					for(DetalleNotaVenta detalleNotaVenta : listDetalleNotaVenta){
						//verificar y agrupar productos por almacen -> metodo que recupera la orden de servicio de acuerdo al almacen
						//luego se debe saber a cual orden de salida debe ser asigndaa el detalle de la orden de salida
						AlmacenProducto almacenProducto = productoTieneStock(detalleNotaVenta.getProducto(),param,param3.getAlmacenVenta());
						OrdenSalida ordenSalida = obtenerOrdenSalidadPorAlmacen(lsitOrdenSalida,almacenProducto.getAlmacen());
						DetalleOrdenSalida detalleOrdenSalida = new DetalleOrdenSalida();
						detalleOrdenSalida.setCantidadEntregada(detalleNotaVenta.getCantidad());
						detalleOrdenSalida.setCantidadSolicitada(detalleNotaVenta.getCantidad());
						detalleOrdenSalida.setEstado("AC");
						detalleOrdenSalida.setObservacion("Ninguna");
						detalleOrdenSalida.setOrdenSalida(ordenSalida);
						detalleOrdenSalida.setPrecioUnitario(detalleNotaVenta.getPrecio());
						detalleOrdenSalida.setProducto(detalleNotaVenta.getProducto());
						detalleOrdenSalida.setTotal(detalleNotaVenta.getPrecio()*detalleNotaVenta.getCantidad());
						detalleOrdenSalida.setFechaRegistro(notaVenta.getFechaRegistro());
						detalleOrdenSalida.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
						createE(detalleNotaVenta);
						listDetalleOrdenSalida.add(detalleOrdenSalida);
						//actualizar stock AlmacenProducto
						almacenProducto.setStock(almacenProducto.getStock()-detalleNotaVenta.getCantidad());
						updateP(almacenProducto);
						//kardex
						KardexProducto kardex = new KardexProducto();
						kardex.setEstado("AC");
						kardex.setFechaRegistro(notaVenta.getFechaRegistro());
						kardex.setOrdenSalida(ordenSalida);
						kardex.setStockSaliente(detalleNotaVenta.getCantidad());
						kardex.setTipoMovimiento("OS");//orden de ingreso
						kardex.setTipoTransaccion("SALIDA X VENTA- Nº NOTA VENTA "+notaVenta.getCodigo());
						kardex.setPrecioSalida(detalleNotaVenta.getPrecio());
						kardex.setStockSaliente(detalleNotaVenta.getCantidad());
						kardex.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
						kardex.setAlmacen(almacenProducto.getAlmacen());
						kardex.setProducto(almacenProducto.getProducto());
						createQ(kardex);
					}
				}else if(param2.getTipoDisminucionStock().equals("OT")){
					//GENERAR ORDEN DE TRASPASO A UN SOLO ALMACEN 
					//LUEGO GENERAR UNA SOLA ORDEN DE SALIDA 4321`qwer1
					//				for(DetalleNotaVenta detalleNotaVenta : listDetalleNotaVenta){
					//
					//				}
				}
			}
			//----
			commitTransaction();
			if(!notaVenta.getEstadoPago().equals("CO")){
				FacesUtil.infoMessage("Registro Correcto", "NotaVenta "+notaVenta.getCodigo());
			}else{
				FacesUtil.infoMessage("Registro Correcto", "Cotización "+notaVenta.getCodigo());	
			}
			return notaVenta;
		}catch(Exception e){
			String cause=e.getMessage();
			System.out.println("error: "+cause);
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return null;
		}
	}
	
	/**
	 * Registrar Nota Venta, implica la disminucion de stock de los productos a traves de orden de salida
	 * @param notaVenta
	 * @param listDetalleNotaVenta
	 * @param listPlanCobranza
	 * @param gestionSesion
	 * @param param
	 * @param param2
	 * @return
	 */
	public NotaVenta registrar(String observacion,Usuario usuario,NotaVenta notaVenta,List<DetalleNotaVenta> listDetalleNotaVenta,List<PlanCobranza> listPlanCobranza,Gestion gestionSesion,ParametroInventario param,ParametroCobranza param2,ParametroVenta param3){
		double totalPlanPagoNacional = notaVenta.getMontoTotal() - notaVenta.getCuotaInicial();
		double totalPlanPagoExtranjero = notaVenta.getMontoTotalExtranjero() - notaVenta.getCuotaInicialExtranjero();
		try{
			beginTransaction();
			if( ! notaVenta.getEstadoPago().equals("CO")){
				//crear movimiento a caja
				MovimientoCaja movimientoCaja = new MovimientoCaja();
				movimientoCaja.setCorrelativo(String.format("%06d",movimientoCajaDao.correlativoMovimiento(gestionSesion)));
				movimientoCaja.setEstado("PR");
				movimientoCaja.setFechaAprobacion(notaVenta.getFechaRegistro());
				movimientoCaja.setFechaDocumento(notaVenta.getFechaRegistro());
				movimientoCaja.setFechaRegistro(notaVenta.getFechaRegistro());
				movimientoCaja.setGestion(notaVenta.getGestion());
				movimientoCaja.setMoneda(notaVenta.getMoneda());
				movimientoCaja.setObservacion("Ninguna");
				movimientoCaja.setTipoCambio(notaVenta.getTipoCambio());
				if(notaVenta.getTipoVenta().equals("CREDITO")){
					movimientoCaja.setNumeroDocumento("0");
					movimientoCaja.setTipoDocumento("NOTA VENTA");
					movimientoCaja.setMotivoIngreso("INGRESO POR CUOTA INICIAL VENTA DE PRODUCTO(S) AL CREDITO");
					movimientoCaja.setTotalImporteExtranjero(notaVenta.getCuotaInicialExtranjero());
					movimientoCaja.setTotalImporteNacional(notaVenta.getCuotaInicial());
					if(notaVenta.getEstadoPago().equals("RE")){
						movimientoCaja.setTipoDocumento("NOTA VENTA(RESERVA)");	
						movimientoCaja.setMotivoIngreso("INGRESO POR RESERVA VENTA DE PRODUCTO(S) AL CREDITO");
						movimientoCaja.setTotalImporteExtranjero(notaVenta.getMontoReservaExtranjero());
						movimientoCaja.setTotalImporteNacional(notaVenta.getMontoReserva());
					}else if(notaVenta.getEstadoPago().equals("CO")){
						//aqui para cotizacion
					}
				}else if(notaVenta.getTipoVenta().equals("CONTADO")){
					movimientoCaja.setMotivoIngreso("INGRESO POR VENTA AL CONTADO DE PRODUCTO(S)");
					movimientoCaja.setNumeroDocumento("0");
					movimientoCaja.setTipoDocumento("NOTA VENTA");
					movimientoCaja.setTotalImporteExtranjero(notaVenta.getMontoTotalExtranjero());
					movimientoCaja.setTotalImporteNacional(notaVenta.getMontoTotal());
				}
				movimientoCaja.setUsuarioAprobacion(usuario);
				movimientoCaja.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
				movimientoCaja.setTipo("INGRESO");
				movimientoCaja = movimientoCajaDao.registrarBasic(movimientoCaja);
				notaVenta.setMovimientoCaja(movimientoCaja);
				notaVenta = create(notaVenta);
				//ejecutivo
				//EjecutivoCliente ejecutivoCliente = ejecutivoClienteDao.getEjecutivoClienteByIdCliente(notaVenta.getCliente());
				Ejecutivo ejecutivo = notaVenta.getEjecutivo();
				if(ejecutivo != null){
					//Ejecutivo ejecutivo = ejecutivoCliente.getEncargadoVenta();
					double comision = ejecutivo.getPorcentaje();
					double importe = notaVenta.getCuotaInicialExtranjero()*(comision/100);
					EjecutivoComisiones ejecutivoComisiones = new EjecutivoComisiones();
					ejecutivoComisiones.setNotaVenta(notaVenta);
					ejecutivoComisiones.setEstado("AC");
					ejecutivoComisiones.setFechaRegistro(notaVenta.getFechaRegistro());
					ejecutivoComisiones.setPagado(Boolean.FALSE);
					ejecutivoComisiones.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
					ejecutivoComisiones.setPorcentaje(comision);
					ejecutivoComisiones.setImporte(importe);
					ejecutivoComisiones.setEjecutivo(ejecutivo);
					ejecutivoComisionesDao.registrar(ejecutivoComisiones);
				}
				//caja movimiento
				CajaMovimiento cajaMovimiento=cajaServicio.IngresoPorVenta(notaVenta);
				CajaSesion cajaSesion=cajaSesionDao.RetornarPorId(cajaMovimiento.getCajaSesion().getId());
				
				cajaSesion.setSaldoNacional(cajaSesion.getSaldoNacional()+cajaMovimiento.getMonto());
				cajaSesion.setSaldoExtranjero(cajaSesion.getSaldoExtranjero()+cajaMovimiento.getMontoExtranjero());
								
				
				cajaSesion=cajaSesionDao.update(cajaSesion);
				sessionMain.setCajaSesion(cajaSesion);
				
				cajaMovimiento.setSaldoExtranjero(cajaSesion.getSaldoExtranjero());
				cajaMovimiento.setSaldoNacional(cajaSesion.getSaldoNacional());
				
				CajaMovimiento c=cajaMovimientoDao.create(cajaMovimiento);
				
				
				
				movimientoCaja.setNumeroDocumento(notaVenta.getCodigo());
				//kardex cliente
				KardexCliente kcEgreso = generarKardexEgreso(notaVenta,notaVenta.getCliente(),notaVenta.getFechaRegistro(),notaVenta.getCodigo(),notaVenta.getTipoCambio(),notaVenta.getMontoTotalExtranjero(),notaVenta.getMontoTotal(),notaVenta.getMontoReservaExtranjero(),notaVenta.getMontoReserva(),notaVenta.getEstadoPago(),notaVenta.getUsuarioRegistro());
				KardexCliente kcIngreso = generarKardexIngreso(notaVenta,notaVenta.getCliente(),notaVenta.getFechaRegistro(),notaVenta.getCodigo(),notaVenta.getTipoCambio(),notaVenta.getCuotaInicialExtranjero(),notaVenta.getCuotaInicial(),notaVenta.getMontoReservaExtranjero(),notaVenta.getMontoReserva(),notaVenta.getEstadoPago(),notaVenta.getUsuarioRegistro());
				if( ! notaVenta.getEstadoPago().equals("CO")){//no deberia registrar si es una cotizacion
					kardexClienteDao.registrarBasic(kcEgreso);
					kardexClienteDao.registrarBasic(kcIngreso);
				}
					if(totalPlanPagoNacional>0){//si la deuda no es mayor a cero entonces un tipoVenta='CONTADO'
						CuentaCobrar cuentaCobrar = new CuentaCobrar();
						cuentaCobrar.setCorrelativo(String.format("%06d",deudaClienteDao.correlativoCuentaCobrar()));
						cuentaCobrar.setEstado("PN");
						cuentaCobrar.setFechaRegistro(notaVenta.getFechaRegistro());
						cuentaCobrar.setMoneda(notaVenta.getMoneda());
						cuentaCobrar.setNotaVenta(notaVenta);
						cuentaCobrar.setObservacion(observacion);
						cuentaCobrar.setSaldoExtranjero(totalPlanPagoExtranjero);//aqui va el monto total del plan de pago credito
						cuentaCobrar.setSaldoNacional(totalPlanPagoNacional);//aqui va el monto total del plan de pago credito
						cuentaCobrar.setTipoCambio(notaVenta.getTipoCambio());
						cuentaCobrar.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
						cuentaCobrar = deudaClienteDao.registrarBasic(cuentaCobrar);
					}
				
			}else{
				notaVenta = create(notaVenta);
			}
			
			double cant = 0;
			for(DetalleNotaVenta dnv:listDetalleNotaVenta){
				dnv.setId(0);
				dnv.setEstado("AC");
				dnv.setFechaRegistro(notaVenta.getFechaRegistro());
				dnv.setNotaVenta(notaVenta);
				dnv.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
				createE(dnv);
				cant = cant + dnv.getCantidad();
			}
			//verificar si se va a registrar el plan de cobranzas(cuando es una reserva o una cotizacion, pero con un estado inactivo)
			for(PlanCobranza pc: listPlanCobranza){
				pc.setId(0);
				pc.setEstado("AC");
				//if(notaVenta.getEstadoPago().equals("CO") ){
				//	pc.setEstado("PN");
				//}
				pc.setFechaRegistro(notaVenta.getFechaRegistro());
				pc.setNotaVenta(notaVenta);
				pc.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
				createR(pc);
			}
			//solo deberia registrar si la venta es diferente a RE=RESERVA
			if(!notaVenta.getEstadoPago().equals("CO")){
				//-----Verificar el Tipo Disminucion de stock
				if(param2.getTipoDisminucionStock().equals("VOS") ){
					//GENERA VARIAS ORDEN DE SALIDA
					List<OrdenSalida> lsitOrdenSalida = new ArrayList<>();
					List<DetalleOrdenSalida> listDetalleOrdenSalida = new ArrayList<>();
					for(DetalleNotaVenta detalleNotaVenta : listDetalleNotaVenta){
						AlmacenProducto almacenProducto = productoTieneStock(detalleNotaVenta.getProducto(),param,param3.getAlmacenVenta());
						if(almacenProducto==null){
							FacesUtil.infoMessage("Validación de Stock", "El producto "+detalleNotaVenta.getProducto().getNombre()+" NO tiene existencia, y no se puede proceder a hacer la venta.");
							rollbackTransaction();
							return null;
						}else if(almacenProducto.getStock()<detalleNotaVenta.getCantidad()){
							FacesUtil.infoMessage("Validación de Stock", "El producto "+detalleNotaVenta.getProducto().getNombre()+" SOLO tiene "+detalleNotaVenta.getCantidad()+" existencia(s).");
							rollbackTransaction();
							return null;
						}
						OrdenSalida ordenSalida = obtenerOrdenSalidadPorAlmacen(lsitOrdenSalida,almacenProducto.getAlmacen());
						ordenSalida.setAlmacen(almacenProducto.getAlmacen());
						ordenSalida.setEstado("AC");
						ordenSalida.setGestion(gestionSesion);
						ordenSalida.setObservacion("ninguna");
						ordenSalida.setMotivoSalida("Salida por Venta Nº "+notaVenta.getCodigo());
						ordenSalida.setTotalImporte(ordenSalida.getTotalImporte()+(detalleNotaVenta.getCantidad()*detalleNotaVenta.getPrecio()));
						ordenSalida.setTotalImporteExtranjero(ordenSalida.getTotalImporteExtranjero()+(detalleNotaVenta.getCantidad()*detalleNotaVenta.getPrecioExtranjero()));
						ordenSalida.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
						ordenSalida.setFechaRegistro(notaVenta.getFechaRegistro());
						ordenSalida.setFechaPedido(notaVenta.getFechaRegistro());
						ordenSalida.setFechaAprobacion(notaVenta.getFechaRegistro());
						ordenSalida.setNotaVenta(notaVenta);
						if(! agregarOrdenSalidaALista(lsitOrdenSalida,ordenSalida)){
							int numeroCorrelativo = ordenSalidaDao.obtenerNumeroOrdenSalida(gestionSesion);
							ordenSalida.setCorrelativo(String.format("%06d", numeroCorrelativo));;
							ordenSalida = createS(ordenSalida);
							lsitOrdenSalida.add(ordenSalida);
						}else{
							updateS(ordenSalida);
						}
					}
					for(DetalleNotaVenta detalleNotaVenta : listDetalleNotaVenta){
						//verificar y agrupar productos por almacen -> metodo que recupera la orden de servicio de acuerdo al almacen
						//luego se debe saber a cual orden de salida debe ser asigndaa el detalle de la orden de salida
						AlmacenProducto almacenProducto = productoTieneStock(detalleNotaVenta.getProducto(),param,param3.getAlmacenVenta());
						OrdenSalida ordenSalida = obtenerOrdenSalidadPorAlmacen(lsitOrdenSalida,almacenProducto.getAlmacen());
						DetalleOrdenSalida detalleOrdenSalida = new DetalleOrdenSalida();
						detalleOrdenSalida.setCantidadEntregada(detalleNotaVenta.getCantidad());
						detalleOrdenSalida.setCantidadSolicitada(detalleNotaVenta.getCantidad());
						detalleOrdenSalida.setEstado("AC");
						detalleOrdenSalida.setObservacion("Ninguna");
						detalleOrdenSalida.setOrdenSalida(ordenSalida);
						detalleOrdenSalida.setPrecioUnitario(detalleNotaVenta.getPrecio());
						detalleOrdenSalida.setProducto(detalleNotaVenta.getProducto());
						detalleOrdenSalida.setTotal(detalleNotaVenta.getPrecio()*detalleNotaVenta.getCantidad());
						detalleOrdenSalida.setFechaRegistro(notaVenta.getFechaRegistro());
						detalleOrdenSalida.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
						createE(detalleNotaVenta);
						listDetalleOrdenSalida.add(detalleOrdenSalida);
						//actualizar stock AlmacenProducto
						almacenProducto.setStock(almacenProducto.getStock()-detalleNotaVenta.getCantidad());
						updateP(almacenProducto);
						//kardex
						KardexProducto kardex = new KardexProducto();
						kardex.setEstado("AC");
						kardex.setFechaRegistro(notaVenta.getFechaRegistro());
						kardex.setOrdenSalida(ordenSalida);
						kardex.setStockSaliente(detalleNotaVenta.getCantidad());
						kardex.setTipoMovimiento("OS");//orden de ingreso
						kardex.setTipoTransaccion("SALIDA X VENTA- Nº NOTA VENTA "+notaVenta.getCodigo());
						kardex.setPrecioSalida(detalleNotaVenta.getPrecio());
						kardex.setStockSaliente(detalleNotaVenta.getCantidad());
						kardex.setUsuarioRegistro(notaVenta.getUsuarioRegistro());
						kardex.setAlmacen(almacenProducto.getAlmacen());
						kardex.setProducto(almacenProducto.getProducto());
						createQ(kardex);
					}
				}else if(param2.getTipoDisminucionStock().equals("OT")){
					//GENERAR ORDEN DE TRASPASO A UN SOLO ALMACEN 
					//LUEGO GENERAR UNA SOLA ORDEN DE SALIDA 4321`qwer1
					//				for(DetalleNotaVenta detalleNotaVenta : listDetalleNotaVenta){
					//
					//				}
				}
			}
			//----
			commitTransaction();
			if(!notaVenta.getEstadoPago().equals("CO")){
				FacesUtil.infoMessage("Registro Correcto", "NotaVenta "+notaVenta.getCodigo());
			}else{
				FacesUtil.infoMessage("Registro Correcto", "Cotización "+notaVenta.getCodigo());	
			}
			return notaVenta;
		}catch(Exception e){
			String cause=e.getMessage();
			System.out.println("error: "+cause);
			if (cause.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			}else{
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return null;
		}
	}

	private KardexCliente generarKardexEgreso(NotaVenta notaVenta,Cliente cliente,Date fechaRegistro,String codigoNotaVenta,double tipoCambio,double montoTotalExtranjero,double montoTotal,double montoReservaExtranjero,double montoReserva,String estadoPago,String usuarioRegistro) {
		KardexCliente kcEgreso = new KardexCliente();
		kcEgreso.setCliente(cliente);
		kcEgreso.setEstado("AC");
		kcEgreso.setFechaDocumento(fechaRegistro);
		kcEgreso.setFechaRegistro(fechaRegistro);
		kcEgreso.setMoneda("DOLAR");
		kcEgreso.setMotivo("EGRESO X VENTA Nº "+codigoNotaVenta);
		kcEgreso.setTipoDocumento("NOTA DE VENTA");
		kcEgreso.setTotalImporteExtranjero(montoTotalExtranjero*(-1));
		kcEgreso.setTotalImporteNacional(montoTotal*(-1));
		kcEgreso.setUsuarioRegistro(usuarioRegistro);
		if(estadoPago.equals("RE")){
			kcEgreso.setMotivo("EGRESO X RESERVA VENTA Nº "+codigoNotaVenta);
			kcEgreso.setTipoDocumento("NOTA VENTA(RESERVA)");
		}
		kcEgreso.setNumeroDocumento(codigoNotaVenta);
		kcEgreso.setObservacion("Ninguna");
		kcEgreso.setSaldo(0);
		kcEgreso.setTipo("EGRESO");
		kcEgreso.setTipoCambio(tipoCambio);
		kcEgreso.setNotaVenta(notaVenta);
		return kcEgreso;
	}
	
	private KardexCliente generarKardexIngreso(NotaVenta notaVenta,Cliente cliente,Date fechaRegistro,String codigoNotaVenta,double tipoCambio,double montoTotalExtranjero,double montoTotal,double montoReservaExtranjero,double montoReserva,String estadoPago,String usuarioRegistro) {
		KardexCliente kcEgreso = new KardexCliente();
		kcEgreso.setCliente(cliente);
		kcEgreso.setEstado("AC");
		kcEgreso.setFechaDocumento(fechaRegistro);
		kcEgreso.setFechaRegistro(fechaRegistro);
		kcEgreso.setMoneda("DOLAR");
		kcEgreso.setMotivo("INGRESO X VENTA Nº "+codigoNotaVenta +" (CUOTA INICIAL)");
		kcEgreso.setTipoDocumento("NOTA DE VENTA");
		kcEgreso.setTotalImporteExtranjero(montoTotalExtranjero);
		kcEgreso.setTotalImporteNacional(montoTotal);
		kcEgreso.setUsuarioRegistro(usuarioRegistro);
		if(estadoPago.equals("RE")){
			kcEgreso.setMotivo("INGRESO X RESERVA VENTA Nº "+codigoNotaVenta);
			kcEgreso.setTipoDocumento("NOTA VENTA(RESERVA)");
			kcEgreso.setTotalImporteExtranjero(montoReservaExtranjero);
			kcEgreso.setTotalImporteNacional(montoReserva);
			kcEgreso.setUsuarioRegistro(usuarioRegistro);
		}
		kcEgreso.setNumeroDocumento(codigoNotaVenta);
		kcEgreso.setObservacion("Ninguna");
		kcEgreso.setSaldo(0);
		kcEgreso.setTipo("INGRESO");
		kcEgreso.setTipoCambio(tipoCambio);
		kcEgreso.setNotaVenta(notaVenta);
		return kcEgreso;
	}

	/**
	 * Si ya existe dicho Almacen en una ordenSalida Anterior.
	 * @param listOrdenSalida
	 * @param almacen
	 * @return OrdenSalida anterior que ya fue agregada en la lista | new OrdenSalida(); nueva
	 */
	private OrdenSalida obtenerOrdenSalidadPorAlmacen(List<OrdenSalida> listOrdenSalida,Almacen almacen){
		for(OrdenSalida os : listOrdenSalida){
			if(os.getAlmacen().getId().equals(almacen.getId())){
				return os;
			}
		}
		return new OrdenSalida();
	}

	/**
	 * Verifica si la ORDENSALIDA se encuentra en la lista(Compara con el ALmacen)
	 * @param listOrdenSalida
	 * @param ordenSalida
	 * @return TRUE si no existe la OrdenSalida en la lista, FALSE si ya existe en la lista
	 */
	private boolean agregarOrdenSalidaALista(List<OrdenSalida> listOrdenSalida,OrdenSalida ordenSalida){
		for(OrdenSalida os: listOrdenSalida){
			if(os.getAlmacen().getId().equals(ordenSalida.getAlmacen().getId())){
				return true;
			}
		}
		return false;
	}


	private AlmacenProducto productoTieneStock(Producto selectedProducto,ParametroInventario param,Almacen almacen){
		//Verificar que tipo de Metodo {PEPS | UEPS | PPP}
		String valuacionInventario = param.getTipoValuacion();
		//Verificar si tiene stock
		AlmacenProducto almacenProducto = new AlmacenProducto();
		switch (valuacionInventario) {
		case  "PEPS":
			almacenProducto = almacenProductoDao.obtenerAlmacenProductoPorPEPS(selectedProducto,almacen);
			break;
		case  "UEPS":
			almacenProducto = almacenProductoDao.obtenerAlmacenProductoPorUEPS(selectedProducto,almacen);
			break;
		case  "PPP":
			almacenProducto = almacenProductoDao.obtenerAlmacenProductoPorPPP(selectedProducto,almacen);
			break;

		default:
			break;
		}
		return almacenProducto;//==null?false:almacenProducto.getStock()>=selectedDetalleNotaVenta.getCantidad();
	}

	public int cantidadVentasDia(Integer dia,Integer mes,Integer anio){
		String query = "select count(em) from  nota_venta em where em.estado='AC' and date_part('month', em.fecha_registro) ="+mes+" and date_part('year', em.fecha_registro) ="+anio+" and date_part('day', em.fecha_registro) ="+dia;
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue();	
	}

	public double totalVentasDia(Integer dia,Integer mes,Integer anio){
		try{
			String query = "select sum(em.montoTotalExtranjero) from  NotaVenta em where em.estado='AC' and date_part('month', em.fechaRegistro) ="+mes+" and date_part('year', em.fechaRegistro) ="+anio+" and date_part('day', em.fechaRegistro) ="+dia;
			return executeQuerySingleResultNumber(query).doubleValue();
		}catch(Exception e){
			return 0;
		}
	}

	public double totalVentasMes(Integer mes,Integer anio){
		try{
			String query = "select sum(em.montoTotalExtranjero) from  NotaVenta em where em.estado='AC' and date_part('month', em.fechaRegistro) ="+mes+" and date_part('year', em.fechaRegistro) ="+anio;
			//return ((Number) executeNativeQuerySingleResult(query)).doubleValue();
			return executeQuerySingleResultNumber(query).doubleValue();
		}catch(Exception e){
			return 0;
		}
	}

	public int cantidadVentasContadoMes(Integer mes,Integer anio){
		try{
			String query = "select count(em.id) from  NotaVenta em where em.tipoVenta='CONTADO' and em.estado='AC' and date_part('month', em.fechaRegistro) ="+mes+" and date_part('year', em.fechaRegistro) ="+anio;
			//return ((Number) executeNativeQuerySingleResult(query)).doubleValue();
			return executeQuerySingleResultNumber(query).intValue();
		}catch(Exception e){
			return 0;
		}
	}

	public double totalVentasContadoMes(Integer mes,Integer anio){
		try{
			String query = "select sum(em.montoTotalExtranjero) from  NotaVenta em where em.tipoVenta='CONTADO' and em.estado='AC' and date_part('month', em.fechaRegistro) ="+mes+" and date_part('year', em.fechaRegistro) ="+anio;
			//return ((Number) executeNativeQuerySingleResult(query)).doubleValue();
			return executeQuerySingleResultNumber(query).doubleValue();
		}catch(Exception e){
			return 0;
		}
	}

	public int cantidadVentasCreditoMes(Integer mes,Integer anio){
		try{
			String query = "select count(em.id) from  NotaVenta em where em.tipoVenta='CREDITO' and em.estado='AC' and date_part('month', em.fechaRegistro) ="+mes+" and date_part('year', em.fechaRegistro) ="+anio;
			//return ((Number) executeNativeQuerySingleResult(query)).doubleValue();
			return executeQuerySingleResultNumber(query).intValue();
		}catch(Exception e){
			return 0;
		}
	}

	public double totalVentasCreditoMes(Integer mes,Integer anio){
		try{
			String query = "select sum(em.montoTotalExtranjero) from  NotaVenta em where em.tipoVenta='CREDITO' and em.estado='AC' and date_part('month', em.fechaRegistro) ="+mes+" and date_part('year', em.fechaRegistro) ="+anio;
			//return ((Number) executeNativeQuerySingleResult(query)).doubleValue();
			return executeQuerySingleResultNumber(query).doubleValue();
		}catch(Exception e){
			return 0;
		}
	}
	
	public List<NotaVenta> obtenerTodosOrdenadosPorId() {
		String query = "select ser from NotaVenta ser where ser.estado='AC' or ser.estado='IN' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public List<NotaVenta> obtenerTodosOrdenadosPorFecha() {
		String query = "select ser from NotaVenta ser where ser.estado='AC' or ser.estado='IN' order by ser.fechaRegistro desc";
		return executeQueryResulList(query);
	}

	public List<NotaVenta> obtenerTodosActivosOrdenadosPorId() {
		String query = "select ser from NotaVenta ser where ser.estado='AC' order by ser.id desc";
		return executeQueryResulList(query);
	}

	public int correlativoNotaVenta2(){
		return countTotalRecord("nota_venta").intValue();
	}

	public int correlativoNotaVenta( Gestion gestion){
		String query = "select count(em) from  nota_venta em where (em.estado='AC' or em.estado='IN' or em.estado='PR' or em.estado='AN') and em.id_gestion="+gestion.getId();
		return ((BigInteger) executeNativeQuerySingleResult(query)).intValue()+1;	
	}

	public List<NotaVenta> obtenerPorCliente(Cliente cliente){
		try{
			return findAllActiveOtherTableAndParameter("Cliente", "cliente", "id", cliente.getId());
		}catch(Exception e){
			return new ArrayList<>();
		}
	}

	public List<NotaVenta> obtenerPorTamanio(int inicio,int maxRows,Map filters) {
		return findAllBySize(inicio, maxRows,filters);
	}

}
