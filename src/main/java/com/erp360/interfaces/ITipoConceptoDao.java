package com.erp360.interfaces;

import java.util.List;

import com.erp360.model.TipoConcepto;
import com.erp360.model.Empresa;
import com.erp360.model.Usuario;




/**
 * Interface used to interact with the familiaproducto.
 * 
 * @author mauriciobejaranorivera
 *
 */
public interface ITipoConceptoDao {
	
	/**
	 * Use only intermediate transactions
	 * @param familiaproducto
	 * @return
	 */
	TipoConcepto create(TipoConcepto TipoConcepto);
	List<TipoConcepto> listaPorEmpresaYActivos(Empresa empresa);
	/**
	 * Use only intermediate transactions
	 * @param TipoConcepto
	 * @return
	 */
	TipoConcepto update(TipoConcepto caja);
	
	TipoConcepto registrar(TipoConcepto caja) ;
	
	boolean eliminar(TipoConcepto caja) ;
	boolean procesar(TipoConcepto caja) ;
	
	/**
	 * modificar object
	 * @param familiaproducto
	 * @return familiaproducto
	 */
	boolean modificar(TipoConcepto caja) ;
	TipoConcepto RetornarPorId(Integer id);	
	//List<TipoConcepto> obtenerTodasPorEmpresa();
	//List<TipoConcepto> onComplete(String query);
	List<TipoConcepto> obtenerPorEmpresa(String nombre, Empresa empresa);
	boolean esCajero(Usuario usuario, TipoConcepto caja);
	List<TipoConcepto> RetornarOnCompletePorEmpresa(Empresa compania, String nombre);
	List<TipoConcepto> obtenerPorEmpresa(Empresa compania);
  
}
