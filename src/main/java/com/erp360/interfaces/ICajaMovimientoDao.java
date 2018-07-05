package com.erp360.interfaces;

import java.util.Date;
import java.util.List;

import com.erp360.enums.TipoMovimiento;
import com.erp360.enums.TipoPago;
import com.erp360.model.Caja;
import com.erp360.model.CajaMovimiento;
import com.erp360.model.CajaSesion;
import com.erp360.model.Sucursal;
import com.erp360.util.CajaIngreso;






/**
 * Interface used to interact with the familiaproducto.
 * 
 * @author mauriciobejaranorivera
 *
 */
public interface ICajaMovimientoDao {
	
	/**
	 * Use only intermediate transactions
	 * @param familiaproducto
	 * @return
	 */
	CajaMovimiento create(CajaMovimiento CajaMovimiento);
	
	/**
	 * Use only intermediate transactions
	 * @param CajaMovimiento
	 * @return
	 */
	CajaMovimiento update(CajaMovimiento caja);
	
	CajaMovimiento registrar(CajaMovimiento caja) ;
	
	boolean eliminar(CajaMovimiento caja) ;
	boolean procesar(CajaMovimiento caja) ;
	List<CajaIngreso> listarPorSesionEIngresosYModoDePago(CajaSesion cajaSesion);
	List<CajaIngreso> listarPorSesionEgresosYModoDePago(CajaSesion cajaSesion);
	/**
	 * modificar object
	 * @param familiaproducto
	 * @return familiaproducto
	 */
	boolean modificar(CajaMovimiento caja) ;
	CajaMovimiento RetornarPorId(Integer id);
	//List<CajaMovimiento> RetornarOnCompletePorCompania(Compania compania, String nombre);	
	
	//List<CajaMovimiento> obtenerTodasPorEmpresa();
	//List<CajaMovimiento> onComplete(String query);

	CajaMovimiento registrarMovimientoDeCaja(CajaSesion cajaSesion,
			TipoMovimiento tipoMovimiento, String tipo, double total,
			TipoPago tipoPago);

	List<CajaMovimiento> listarMovimientosPorSesion(CajaSesion cajaSesion);

	List<CajaMovimiento> obtenerPorSucursalEntreFechas(Sucursal sucursal,
			Caja caja, Date fechaini, Date fechafin);

	//CajaMovimiento registrar(CajaMovimiento examen, Comprobante comprobante);

	List<CajaMovimiento> obtenerPorSessionCajaEntreFechas(
			CajaSesion cajaSesion, Date fechaini, Date fechafin);

	List<CajaMovimiento> obtenerMovimientosSinComprobantes(CajaSesion cajaSesion);
  
}
