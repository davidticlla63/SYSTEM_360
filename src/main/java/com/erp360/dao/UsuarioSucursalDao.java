package com.erp360.dao;

import java.util.List;

import javax.ejb.Stateless;

import com.erp360.interfaces.IUsuarioSucursalDao;
import com.erp360.model.Empresa;
import com.erp360.model.Sucursal;
import com.erp360.model.Usuario;
import com.erp360.model.UsuarioSucursal;
import com.erp360.util.FacesUtil;

/**
 * 
 * @author david
 *
 */
@Stateless
public class UsuarioSucursalDao extends DataAccessObjectGeneric<UsuarioSucursal> implements IUsuarioSucursalDao {

	public UsuarioSucursalDao() {
		super(UsuarioSucursal.class);
	}

	@Override
	public UsuarioSucursal registrar(UsuarioSucursal usuario) {
		try {
			beginTransaction();
			usuario = create(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Registro Correcto", "UsuarioSucursal ");
			return usuario;
		} catch (Exception e) {
			String cause = e.getMessage();
			System.err.println("Error en registrar : "+cause);
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
	public UsuarioSucursal modificar(UsuarioSucursal usuario) {
		try {
			beginTransaction();
			usuario = update(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Modificación Correcta", "UsuarioSucursal "
					+ usuario.toString());
			return usuario;
		} catch (Exception e) {
			String cause = e.getMessage();
			System.err.println("Error en registrar : "+cause);
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
	public boolean eliminar(UsuarioSucursal usuario) {
		try {
			beginTransaction();
			// usuario.setState("RM");
			// usuario.setLogin(new Date() + "|" + usuario.getLogin());
			UsuarioSucursal bar = update(usuario);
			commitTransaction();
			FacesUtil.infoMessage("Eliminación Correcta", "UsuarioSucursal "
					+ usuario.toString());
			return bar != null ? true : false;
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al eliminar");
			rollbackTransaction();
			return false;
		}
	}

	@Override
	public List<UsuarioSucursal> obtenerTodosActivosEInactivosPorOrdenAsc() {
		return findAllByParameterObjectTwoQueryOr("estado", "estado", "AC",
				"IN");
	}

	@Override
	public List<UsuarioSucursal> obtenerTodosPorEmpresa(Empresa empresa) {
		return ((List<UsuarioSucursal>) super.getEntityManager()
				.createNamedQuery("UsuarioSucursal.findAllByEmpresa")
				.setParameter("idEmpresa", empresa.getId()).getResultList());
	}
	
	@Override
	public List<UsuarioSucursal> obtenerTodosPorSucursal(Sucursal sucursal) {
		return ((List<UsuarioSucursal>) super.getEntityManager()
				.createNamedQuery("UsuarioSucursal.findAllBySucursal")
				.setParameter("idSucursal", sucursal.getId()).getResultList());
	}

	@Override
	public List<UsuarioSucursal> obtenerTodosPorUsuario(Usuario usuario) {
		return ((List<UsuarioSucursal>) super.getEntityManager()
				.createNamedQuery("UsuarioSucursal.findAllByUsuario")
				.setParameter("idUsuario", usuario.getId()).getResultList());
	}

}
