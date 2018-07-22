package com.erp360.interfaces;

import java.util.List;

import com.erp360.model.TipoCliente;
import com.erp360.model.Empresa;
import com.erp360.model.Usuario;




/**
 * Interface used to interact with the familiaproducto.
 * 
 * @author david
 *
 */
public interface ITipoClienteDao {
	
	/**
	 * Use only intermediate transactions
	 * @param familiaproducto
	 * @return
	 */
	TipoCliente create(TipoCliente TipoCliente);
	List<TipoCliente> listaPorEmpresaYActivos(Empresa empresa);
	/**
	 * Use only intermediate transactions
	 * @param TipoCliente
	 * @return
	 */
	TipoCliente update(TipoCliente caja);
	
	TipoCliente registrar(TipoCliente caja) ;
	
	boolean eliminar(TipoCliente caja) ;
	boolean procesar(TipoCliente caja) ;
	
	/**
	 * modificar object
	 * @param familiaproducto
	 * @return familiaproducto
	 */
	boolean modificar(TipoCliente caja) ;
	TipoCliente RetornarPorId(Integer id);	
	//List<TipoCliente> obtenerTodasPorEmpresa();
	//List<TipoCliente> onComplete(String query);
	List<TipoCliente> obtenerPorEmpresa(String nombre, Empresa empresa);
	boolean esCajero(Usuario usuario, TipoCliente caja);
	List<TipoCliente> RetornarOnCompletePorEmpresa(Empresa compania, String nombre);
	List<TipoCliente> obtenerPorEmpresa(Empresa compania);
  
}
