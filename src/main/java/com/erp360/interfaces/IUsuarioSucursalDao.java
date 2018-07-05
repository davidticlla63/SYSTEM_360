package com.erp360.interfaces;

import java.util.List;

import com.erp360.model.Empresa;
import com.erp360.model.Sucursal;
import com.erp360.model.Usuario;
import com.erp360.model.UsuarioSucursal;

/**
 * Interface used to interact with the UsuarioSucursal.
 * 
 * @author david
 *
 */
public interface IUsuarioSucursalDao {

	/**
	 * Use only intermediate transactions
	 * 
	 * @param usuario
	 * @return
	 */
	UsuarioSucursal create(UsuarioSucursal usuarioSucursal);

	/**
	 * Use only intermediate transactions
	 * 
	 * @param update
	 * @return
	 */
	UsuarioSucursal update(UsuarioSucursal update);

	/**
	 * registrar object
	 * 
	 * @param UsuarioSucursal
	 * @return UsuarioSucursal
	 */
	UsuarioSucursal registrar(UsuarioSucursal usuarioSucursal);

	/**
	 * modificar object
	 * 
	 * @param UsuarioSucursal
	 * @return UsuarioSucursal
	 */
	UsuarioSucursal modificar(UsuarioSucursal usuarioSucursal);

	/**
	 * eliminar object
	 * 
	 * @param usuario
	 * @return
	 */
	boolean eliminar(UsuarioSucursal usuarioSucursal);


	/**
	 * 
	 * @return List<UsuarioSucursal>
	 */
	List<UsuarioSucursal> obtenerTodosActivosEInactivosPorOrdenAsc();
	
	/**
	 * 
	 * @return List<UsuarioSucursal>
	 */
	List<UsuarioSucursal> obtenerTodosPorEmpresa(Empresa empresa);
	
	List<UsuarioSucursal> obtenerTodosPorSucursal(Sucursal sucursal);
	
	/**
	 * Otener Listado de UsuaioSucursal por Usuario
	 * @param Usuario
	 * @return List<UsuarioSucursal>
	 */
	List<UsuarioSucursal> obtenerTodosPorUsuario(Usuario usuario);

}
