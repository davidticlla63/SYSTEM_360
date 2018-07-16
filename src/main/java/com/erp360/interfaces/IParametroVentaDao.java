package com.erp360.interfaces;

import com.erp360.model.Empresa;
import com.erp360.model.ParametroVenta;

/**
 * Interface used to interact with the ParametroEmpresa.
 * 
 * @author mauriciobejaranorivera
 *
 */
public interface IParametroVentaDao {
	
	ParametroVenta create(ParametroVenta parametroVenta);
	
	boolean modificar(ParametroVenta parametroVenta);

	ParametroVenta obtenerPorEmpresa(Empresa empresa);

}
