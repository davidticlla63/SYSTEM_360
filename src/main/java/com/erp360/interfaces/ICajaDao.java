package com.erp360.interfaces;

import java.util.List;

import com.erp360.model.Caja;
import com.erp360.model.Empresa;
import com.erp360.model.Usuario;




/**
 * Interface used to interact with the familiaproducto.
 * 
 * @author mauriciobejaranorivera
 *
 */
public interface ICajaDao {
	
	/**
	 * Use only intermediate transactions
	 * @param familiaproducto
	 * @return
	 */
	Caja create(Caja Caja);
	List<Caja> listaPorEmpresaYActivos(Empresa empresa);
	/**
	 * Use only intermediate transactions
	 * @param Caja
	 * @return
	 */
	Caja update(Caja caja);
	
	Caja registrar(Caja caja) ;
	
	boolean eliminar(Caja caja) ;
	boolean procesar(Caja caja) ;
	
	/**
	 * modificar object
	 * @param familiaproducto
	 * @return familiaproducto
	 */
	boolean modificar(Caja caja) ;
	Caja RetornarPorId(Integer id);
	List<Caja> RetornarOnCompletePorEmpresa(Empresa empresa,Usuario usuario, String nombre);	
	//List<Caja> obtenerTodasPorEmpresa();
	//List<Caja> onComplete(String query);
	List<Caja> obtenerPorEmpresa(String nombre, Empresa empresa);
  
}
