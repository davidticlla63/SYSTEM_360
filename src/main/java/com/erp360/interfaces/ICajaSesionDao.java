package com.erp360.interfaces;

import java.util.List;

import com.erp360.model.Caja;
import com.erp360.model.CajaSesion;
import com.erp360.model.Empresa;
import com.erp360.model.Usuario;





/**
 * Interface used to interact with the familiaproducto.
 * 
 * @author mauriciobejaranorivera
 *
 */
public interface ICajaSesionDao {
	
	/**
	 * Use only intermediate transactions
	 * @param familiaproducto
	 * @return
	 */
	CajaSesion RetornarPorCaja(Caja caja);
	CajaSesion create(CajaSesion CajaSesion);
	List<CajaSesion> listaPorCompaniaUSuarioYActivos(Empresa empresa, Usuario us);
	/**
	 * Use only intermediate transactions
	 * @param CajaSesion
	 * @return
	 */
	CajaSesion update(CajaSesion caja);
	
	CajaSesion registrar(CajaSesion caja) ;
	
	boolean eliminar(CajaSesion caja) ;
	boolean procesar(CajaSesion caja) ;
	
	/**
	 * modificar object
	 * @param familiaproducto
	 * @return familiaproducto
	 */
	boolean modificar(CajaSesion caja) ;
	CajaSesion RetornarPorId(Integer id);
	//List<CajaSesion> RetornarOnCompletePorCompania(Compania compania, String nombre);	
	CajaSesion obtenerPorUsuarioyEmpresa(Usuario usuario, Empresa compania);
	//List<CajaSesion> obtenerTodasPorEmpresa();
	//List<CajaSesion> onComplete(String query);
//	CajaSesion registrar(CajaSesion examen, Comprobante comprobante);
//	boolean modificar(CajaSesion examen, Comprobante comprobante);
  
}
