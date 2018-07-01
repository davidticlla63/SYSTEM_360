package com.erp360.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.erp360.dao.UsuarioDao;
import com.erp360.model.Empresa;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

@Named(value = "perfilController")
@SessionScoped
public class PerfilController implements Serializable {

private static final long serialVersionUID = -2989737706810995315L;
	
	//DAO
	private @Inject UsuarioDao usuarioDao;

	private Usuario newUsuario;

	private boolean modificar = false;
	private boolean modificarPerfil;
	
	private String nombreUsuario; 
	private @Inject SessionMain sessionMain; //variable del login
	private Empresa empresaLogin;
	
	@PostConstruct
	public void initNewPerfil() {
		System.out.println("initNewPerfil()");
		setNombreUsuario(sessionMain.getUsuarioLogin().getLogin());
		setEmpresaLogin(sessionMain.getEmpresaLogin());
		newUsuario = sessionMain.getUsuarioLogin();
		modificar = false;
		modificarPerfil = false;
	}
	
	public void actionButtonModificar(){
		System.out.println("actionButtonModificar");
		modificarPerfil = true;
	}
	
	public void modificarUsuario(){
		//usuarioRegistration.update(newUsuario);
		sessionMain.setUsuarioLogin(newUsuario);
		FacesUtil.infoMessage("Usuario modificado correctamente", ""+newUsuario.getNombre());
		modificarPerfil = false;
	}
	
	//------GET AND SETTER ----
	
	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}
	
	public void cambiarModificar(){
		setModificar(false);
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public Empresa getEmpresaLogin() {
		return empresaLogin;
	}

	public void setEmpresaLogin(Empresa empresaLogin) {
		this.empresaLogin = empresaLogin;
	}

	public boolean isModificarPerfil() {
		return modificarPerfil;
	}

	public void setModificarPerfil(boolean modificarPerfil) {
		this.modificarPerfil = modificarPerfil;
	}

	public Usuario getNewUsuario() {
		return newUsuario;
	}

	public void setNewUsuario(Usuario newUsuario) {
		this.newUsuario = newUsuario;
	}
}
