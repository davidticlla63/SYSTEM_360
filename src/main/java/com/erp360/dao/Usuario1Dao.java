package com.erp360.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.erp360.interfaces.IUsuarioDao;
import com.erp360.interfaces.IUsuarioSucursalDao;
import com.erp360.model.Empresa;
import com.erp360.model.Roles;
import com.erp360.model.Usuario;
import com.erp360.model.UsuarioSucursal;
import com.erp360.util.FacesUtil;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Stateless
public class Usuario1Dao extends DataAccessObjectGeneric<Usuario> implements IUsuarioDao {

	private @Inject IUsuarioSucursalDao usuarioSucursalDao;

	public Usuario1Dao() {
		super(Usuario.class);
	}

	/**
	 * registrar object
	 * 
	 * @param Usuario,List<UsuarioSucursal>
	 * @return Usuario
	 */
	@Override
	public boolean registrar(Usuario usuario,List<UsuarioSucursal> listUsuarioSucursal){
		try {
			beginTransaction();
			usuario = create(usuario);
			for(UsuarioSucursal us:listUsuarioSucursal){
				us.setUsuario(usuario);
				usuarioSucursalDao.create(us);
			}
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto","Usuario " + usuario.getNombre());
			return true;
		} catch (Exception e) {
			String cause = e.getMessage();
			if (cause
					.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			} else {
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return false;
		}
	}

	@Override
	public Usuario registrar(Usuario usuario) {
		try {
			beginTransaction();
			usuario = create(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto","Usuario " + usuario.getNombre());
			return usuario;
		} catch (Exception e) {
			String cause = e.getMessage();
			if (cause
					.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			} else {
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return null;
		}
	}

	@Override
	public boolean modificar(Usuario usuario,List<UsuarioSucursal> listUsuarioSucursal,List<UsuarioSucursal> listUsuarioSucursalEliminadas) {
		try {
			beginTransaction();
			update(usuario);
			for(UsuarioSucursal us : listUsuarioSucursal){
				if(us.getId().intValue()<=0){
					us.setId(0);
					us.setFechaRegistro(usuario.getFechaRegistro());
					us.setUsuario(usuario);
					us.setUsuarioRegistro(usuario.getUsuarioRegistro());
					usuarioSucursalDao.create(us);
				}else{
					us.setFechaModificacion(usuario.getFechaModificacion());
					usuarioSucursalDao.update(us);
				}
			}
			for(UsuarioSucursal us:listUsuarioSucursalEliminadas){
				us.setEstado("RM");
				usuarioSucursalDao.update(us);
			}
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta","Usuario " + usuario.getNombre());
			return true;
		} catch (Exception e) {
			String cause = e.getMessage();
			if (cause
					.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			} else {
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return false;
		}
	}

	@Override
	public boolean modificar(Usuario usuario) {
		try {
			//beginTransaction();
			update(usuario);
			//commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta","Usuario " + usuario.getNombre());
			return true;
		} catch (Exception e) {
			String cause = e.getMessage();
			if (cause
					.contains("org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
				FacesUtil.errorMessage("Ya existe un registro igual.");
			} else {
				FacesUtil.errorMessage("Error al modificar");
			}
			rollbackTransaction();
			return false;
		}
	}

	@Override
	public boolean eliminar(Usuario usuario) {
		try {
			beginTransaction();
			usuario.setState("RM");
			usuario.setLogin(new Date() + "|" + usuario.getLogin());
			Usuario bar = update(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta","Usuario " + usuario.getNombre());
			return bar != null ? true : false;
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al eliminar");
			rollbackTransaction();
			return false;
		}
	}

	@Override
	public Usuario obtenerPorLoginYPassword(String login, String password) {
		try {
			return findByParameterObjectTwo("login", "password", login, password);
		} catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
			return null;
		}

	}
	@Override
	public Usuario obtenerPorId(Integer id) {
		String query = "select us from Usuario us where us.id="+id+" and (us.state='AC' or us.estado='IN')";
		return executeQuerySingleResult(query);

	}

	@Override
	public List<Usuario> obtenerTodosActivosEInactivosPorOrdenAsc() {
		return findAllByParameterObjectTwoQueryOr("state", "state", "AC",
				"IN");
	}

	@Override
	public Usuario findByLogin(String login, String password) {
		try {
			return findByParameterObjectTwo("login", "password", login,
					password);
		} catch (Exception e) {
			System.out.println("Error : " + e.getMessage());
			return null;
		}
	}

	@Override
	public List<Usuario> obtenerTodosActivosEInactivosPorCompaniaPorOrdenAsc(Empresa empresa){
		return findAllByParameterObjectTwoQueryOr("state", "state", "AC","IN");
	}

	/* (non-Javadoc)
	 * @see com.teds.erp360.interfaces.dao.IUsuarioDao#obtenerTodosPorRol(com.teds.erp360.model.Rol)
	 */
	@Override
	public List<Usuario> obtenerTodosPorRol(Roles rol) {
		String query = "select em from Usuario em where em.rol.id="+rol.getId()+" and (em.state='AC' or em.state='IN')";		
		return executeQueryResulList(query);
	}

	

}
