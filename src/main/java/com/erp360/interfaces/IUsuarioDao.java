package com.erp360.interfaces;

import java.util.List;

import com.erp360.model.Empresa;
import com.erp360.model.Roles;
import com.erp360.model.Usuario;
import com.erp360.model.UsuarioSucursal;

/**
 * Interface used to interact with the Usuario.
 * 
 * @author mauriciobejaranorivera
 *
 */
public interface IUsuarioDao {

	/**
	 * Use only intermediate transactions
	 * 
	 * @param usuario
	 * @return
	 */
	Usuario create(Usuario usuario);

	/**
	 * Use only intermediate transactions
	 * 
	 * @param update
	 * @return
	 */
	Usuario update(Usuario update);

	/**
	 * registrar object
	 * 
	 * @param Usuario
	 * @return Usuario
	 */
	Usuario registrar(Usuario usuario);
	
	/**
	 * registrar object
	 * 
	 * @param Usuario,List<UsuarioSucursal>
	 * @return boolean
	 */
	boolean registrar(Usuario usuario,List<UsuarioSucursal> listUsuarioSucursal);

	/**
	 * modificar object
	 * 
	 * @param Usuario
	 * @return Usuario
	 */
	boolean modificar(Usuario usuario);
	
	/**
	 * modificar object
	 * 
	 * @param Usuario,List<UsuarioSucursal>,List<UsuarioSucursal>
	 * @return boolean
	 */
	boolean modificar(Usuario usuario,List<UsuarioSucursal> listUsuarioSucursal,List<UsuarioSucursal> listUsuarioSucursalEliminadas);

	/**
	 * eliminar object
	 * 
	 * @param usuario
	 * @return
	 */
	boolean eliminar(Usuario usuario);

	/**
	 * 
	 * @param login
	 * @param password
	 * @return Usuario
	 */
	Usuario obtenerPorLoginYPassword(String login, String password);
	
	Usuario obtenerPorId(Integer id);

	/**
	 * 
	 * @return
	 */
	List<Usuario> obtenerTodosActivosEInactivosPorOrdenAsc();
	
	/**
	 * 
	 * @return
	 */
	List<Usuario> obtenerTodosActivosEInactivosPorCompaniaPorOrdenAsc(Empresa empresa);

	Usuario findByLogin(String login, String password);
	
	List<Usuario> obtenerTodosPorRol(Roles rol);

}
