package com.erp360.interfaces;

import java.util.List;

import com.erp360.model.Concepto;
import com.erp360.model.Empresa;
import com.erp360.model.Usuario;




/**
 * Interface used to interact with the familiaproducto.
 * 
 * @author mauriciobejaranorivera
 *
 */
public interface IConceptoDao {
	
	/**
	 * Use only intermediate transactions
	 * @param familiaproducto
	 * @return
	 */
	Concepto create(Concepto Concepto);
	List<Concepto> listaPorEmpresaYActivos(Empresa empresa);
	/**
	 * Use only intermediate transactions
	 * @param Concepto
	 * @return
	 */
	Concepto update(Concepto caja);
	
	Concepto registrar(Concepto caja) ;
	
	boolean eliminar(Concepto caja) ;
	boolean procesar(Concepto caja) ;
	
	/**
	 * modificar object
	 * @param familiaproducto
	 * @return familiaproducto
	 */
	boolean modificar(Concepto caja) ;
	Concepto RetornarPorId(Integer id);	
	//List<Concepto> obtenerTodasPorEmpresa();
	//List<Concepto> onComplete(String query);
	List<Concepto> obtenerPorEmpresa(String nombre, Empresa empresa);
	boolean esCajero(Usuario usuario, Concepto caja);
	List<Concepto> RetornarOnCompletePorEmpresa(Empresa compania, String nombre);
	List<Concepto> obtenerPorEmpresa(Empresa compania);
  
}
