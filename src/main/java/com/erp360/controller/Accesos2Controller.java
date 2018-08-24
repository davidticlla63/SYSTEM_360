package com.erp360.controller;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.erp360.dao.ModuloDao;
import com.erp360.dao.PermisoDao;
import com.erp360.dao.PrivilegioDao;
import com.erp360.dao.RolDao;
import com.erp360.dao.UsuarioRolDao;
import com.erp360.model.Modulo;
import com.erp360.model.Permiso;
import com.erp360.model.Privilegio;
import com.erp360.model.Roles;
import com.erp360.struct.EDMenu;
import com.erp360.util.SessionMain;


/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "accesos2Controller")
@ViewScoped
public class Accesos2Controller implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private @Inject RolDao rolesRepository;
	private  @Inject ModuloDao moduloRepository;
	private @Inject PermisoDao permisoReppository;
	private @Inject PrivilegioDao privilegioDao;
	private @Inject UsuarioRolDao usuerRolRepository;

	// LIST
	private List<Permiso> listPermisos;
	private List<Modulo> listModulo;
	private List<Permiso> listPermiso = new ArrayList<Permiso>();
	private List<Privilegio> listPrivilegio = new ArrayList<Privilegio>();
	private List<Roles> listRoles = new ArrayList<Roles>();
	private List<EDMenu> edMenus = new ArrayList<EDMenu>();

	// COMPONENT

	// SESSION
	private @Inject SessionMain sessionMain;
	
	

	@PostConstruct
	public void init() {
		System.out.println("init - access2Controller");
		loadDefault();
	}

	public void loadDefault() {
		System.out.println("Ingreso a loadDefault");
		
		listModulo = moduloRepository.findAllOrderByID();
		setListPermiso(permisoReppository.findAllOrderByName());
		setListRoles(rolesRepository.obtenerOrdenAscPorId());
		
		
		listPrivilegio = sessionMain.getListPrivilegios();
		edMenus = new ArrayList<EDMenu>();
		cargarModulo();
		cagarMenu();
		for (Modulo modulo : listModulo) {
			EDMenu edMenu = new EDMenu();
			edMenu.setModulo(modulo);
			edMenu.setMenus(obtenerMenusPorModulo(modulo));
			edMenus.add(edMenu);
		}

	}
	
	private List<Permiso> obtenerMenusPorModulo(Modulo modulo) {
		List<Permiso> listAux = new ArrayList<>();
		for (Privilegio ma : listPrivilegio) {
			if (ma.getPermiso().getModulo().equals(modulo)) {
				if (!listAux.contains(ma.getPermiso())) {
					listAux.add(ma.getPermiso());
				}
			}
		}
		return listAux;
	}



	// PROCESO

	private void cargarModulo() {
		listModulo = new ArrayList<>();
		for (Permiso ma : listPermisos) {
			if (!listModulo.contains(ma.getModulo())) {
				listModulo.add(ma.getModulo());
			}
		}
		Collections.sort(listModulo);
	}

	private void cagarMenu() {
		listPermiso= new ArrayList<Permiso>();
		for (Permiso ma : listPermisos) {
			if (!listPermiso.contains(ma)) {
				listPermiso.add(ma);
			}
		}
	}

	public String verificarMenu(String url) {
		// System.out.println("Ingreso a verificarMenu "+url);
		for (Permiso menu : listPermiso) {
			if (menu.getRuta().equals(url) //&& menu.getTipo().equals("1")
					) {
				return "inline";
			}
		}
		return "none";
	}

	public boolean verificarPagina(String url) {
		// System.out.println("Ingreso a verificarMenu "+url);
		if (url.equals("login.xhtml") || url.equals("index.xhtml") || url.equals("index_.xhtml")
				|| url.equals("profile.xhtml") || url.equals("dashboard.xhtml") || url.equals("certificacion.xhtml")
				|| url.equals("administradorDB.xhtml")) {
			return true;// excepciones
		}
		for (Permiso menu : listPermiso) {
			if (menu.getRuta().equals(url) //&& menu.getTipo().equals("1")
					) {
				return true;
			}
		}
		return false;
	}

	public String verificarMenuParam(String url) {
		System.out.println("Ingreso a verificarMenu " + url);
		for (Permiso menu : listPermiso) {
			if (menu.getRuta().equals(url)) {
				return "inline";
			}
		}
		return "none";
	}

	public String verificarModulo(String nombre) {
		// System.out.println("Ingreso a verificarMenu "+nombre);
		for (Modulo modulo : listModulo) {
			if (modulo.getNombre().equals(nombre)) {
				return "inline";
			}
		}
		return "none";
	}

	
	/**
	 * @return the edMenus
	 */
	public List<EDMenu> getEdMenus() {
		return edMenus;
	}

	/**
	 * @param edMenus
	 *            the edMenus to set
	 */
	public void setEdMenus(List<EDMenu> edMenus) {
		this.edMenus = edMenus;
	}

	

	public List<Permiso> getListPermiso() {
		return listPermiso;
	}

	public void setListPermiso(List<Permiso> listPermiso) {
		this.listPermiso = listPermiso;
	}

	public List<Privilegio> getListPrivilegio() {
		return listPrivilegio;
	}

	public void setListPrivilegio(List<Privilegio> listPrivilegio) {
		this.listPrivilegio = listPrivilegio;
	}

	public List<Roles> getListRoles() {
		return listRoles;
	}

	public void setListRoles(List<Roles> listRoles) {
		this.listRoles = listRoles;
	}

	public List<Permiso> getListPermisos() {
		return listPermisos;
	}

	public void setListPermisos(List<Permiso> listPermisos) {
		this.listPermisos = listPermisos;
	}

	public List<Modulo> getListModulo() {
		return listModulo;
	}

	public void setListModulo(List<Modulo> listModulo) {
		this.listModulo = listModulo;
	}

}
