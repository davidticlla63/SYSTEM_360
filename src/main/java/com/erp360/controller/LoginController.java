package com.erp360.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.erp360.dao.EmpresaDao;
import com.erp360.dao.PrivilegioDao;
import com.erp360.dao.UsuarioDao;
import com.erp360.dao.UsuarioRolDao;
import com.erp360.interfaces.ICajaSesionDao;
import com.erp360.model.Empresa;
import com.erp360.model.Usuario;
import com.erp360.model.UsuarioRol;
import com.erp360.util.DateUtility;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;
import com.erp360.model.Privilegio;

@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

	private static final long serialVersionUID = 903796340358480031L;
	
	private @Inject SessionMain sessionMain; //variable del login
	private @Inject UsuarioRolDao usuarioRolDao;
	private @Inject UsuarioDao usuarioDao;
	private @Inject EmpresaDao empresaDao;
	private @Inject PrivilegioDao privilegioDao;
	private @Inject ICajaSesionDao cajaSesionDao;

	private String username;
	private String password;

	//temporal
	private StreamedContent fotoPerfilTemp;

	private UploadedFile file;
	
	private UploadedFile fileLogo;;

	private boolean modificar = false;

	@PostConstruct
	public void initNewLogin() {
		System.out.println("initNewLogin()");
		username = "";
		password = "";
	}

	public void login() {
		System.out.println(" ------- login() ----");
		if(username.isEmpty() || password.isEmpty()){
			System.out.println("login() -> Usuario o Password sin datos.");
			FacesUtil.errorMessage("Ingresar Usuario y Contraseña.");
			return; 
		}

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		Usuario usuarioSession = usuarioDao.obtenerPorLogin(username, password);
		if(usuarioSession!=null){
			//validacion usuario eliminado
			if(usuarioSession.getState().equals("RM")){
				FacesUtil.infoMessage("Verificar!","Usuario o contraseña incorrecta");
				return;
			}
			//validacion usuario inactivo
			if(usuarioSession.getState().equals("IN")){
				FacesUtil.infoMessage("Usuario Inactivo","");
				return;
			}
			try {
				if (request.getUserPrincipal() != null) {
					logout();
				}
				request.login(username, password);
				load(usuarioSession);
				cargarPermisosUserSession();
				try {
					context.getExternalContext().redirect(request.getContextPath() + "/pages/dashboard.xhtml");
					cargarPermisosEnSession();
				} catch (IOException ex) {
					context.addMessage(null, new FacesMessage("Error!", "Ocurrio un Error!"));
				}
			} catch (ServletException e) {
				System.out.println("login() -> "+ e.toString());
				context.addMessage(null, new FacesMessage("Verificar!", "Usuario o contraseña incorrecta"));
			}
		} else{
			System.out.println("login() -> No existe Usuario");
			FacesUtil.errorMessage("Revisar Usuario o Contraseña."); 
		}
	}

	private void load(Usuario usuario){
		this.usuarioSession = usuario;
		UsuarioRol usuarioRolV1 = usuarioRolDao.obtenerPorUsuario(usuario);
		
		sessionMain.setUsuarioLogin(usuario);
		sessionMain.setCajaSesion(cajaSesionDao.obtenerPorUsuarioyEmpresa(usuario, sessionMain.getEmpresaLogin()));
		//sessionMain.cargarPermisos(usuarioRolV1.getRoles());
		setImageUserSession();
		setImageLogo();
	}
	
	private void cargarPermisosEnSession(){
		sessionMain.cargarPermisosUsuarioLogin();
	}

	public void logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		HttpSession session = request.getSession(false);
		System.out.println( "User ({0}) Cerrando sesion #" + DateUtility.getCurrentDateTime()+" user"+ request.getUserPrincipal().getName());
		if (session != null) {
			session.invalidate();
			try {
				context.getExternalContext().redirect(request.getContextPath() + "/login.xhtml");
			} catch (IOException e) {
				System.out.println("logout() -> "+e.toString());
			}
		}
	}

	public void verificarTipoCambio(){
		System.out.println("verificarTipoCambio()");
		RequestContext.getCurrentInstance().execute("stickyTipoCambio()");
		int test = 0;
		if( 0 == test){
			//RequestContext.getCurrentInstance().execute("stickyTipoCambio()");
		}
	}

	private static byte[] toByteArrayUsingJava(InputStream is) throws IOException{ 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();
		while(reads != -1){
			baos.write(reads); reads = is.read(); 
		}
		return baos.toByteArray();
	}

	public StreamedContent getImageUserSession() {
		String mimeType = "image/jpg";
		StreamedContent file;
		InputStream is = null;
		try {
			HttpSession request = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			byte[] image = (byte[]) request.getAttribute("imageUser");
			is = new ByteArrayInputStream(image);
			return new DefaultStreamedContent(new ByteArrayInputStream(
					toByteArrayUsingJava(is)), mimeType);
		} catch (Exception e) {
			System.out.println("getEmpresaSession() -> error : "
					+ e.getMessage());
			return null;
		}
	}

	public void setImageUserSession() {
		// cargar foto del usuario
		try {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			byte[] image = sessionMain.getUsuarioLogin().getFotoPerfil();
			if (image == null) {
				image = toByteArrayUsingJava(getImageDefault().getStream());
			}
			session.setAttribute("imageUser", image);

		} catch (Exception e) {
			System.out.println("setImageUserSession() - Error: "
					+ e.getMessage());
		}
	}

	private StreamedContent getImageDefault() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream("avatar.png");
		return new DefaultStreamedContent(stream, "image/png");
	}
	
	private StreamedContent getImageDefaultLogo() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream("logo.png");
		return new DefaultStreamedContent(stream, "image/png");
	}

	///perfil
	private Usuario usuarioSession;

	public void upload() {
		setModificar(true);
		System.out.println("upload()  file:" + file);
		if (file != null) {
			usuarioSession.setFotoPerfil(file.getContents());
			usuarioSession.setPesoFoto(file.getContents().length);
			usuarioDao.modificar(usuarioSession);
			setImageUserSession2();
			System.out.println("upload()  OK");
		}
	}
	
	public void setImageUserSession2() {
		// cargar foto del usuario
		try {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			byte[] image = usuarioSession.getFotoPerfil();
			session.setAttribute("imageUser", image);

		} catch (Exception e) {
			System.out.println("setImageUserSession() - Error: "
					+ e.getMessage());
		}
	}
	
	public void uploadLogo() {
		setModificar(true);
		System.out.println("upload()  fileLogo:" + fileLogo);
		if (fileLogo != null) {
			Empresa empresa = sessionMain.getEmpresaLogin();
			empresa.setFotoPerfil(fileLogo.getContents());
			empresa.setPesoFoto(fileLogo.getContents().length);
			empresa = empresaDao.modificar(empresa);
			sessionMain.setEmpresaLogin(empresa);
			setImageLogo2();
			System.out.println("uploadLogo()  OK");
		}
	}
	
	public void setImageLogo() {
		// cargar foto del usuario
		try {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			byte[] image = sessionMain.getEmpresaLogin().getFotoPerfil();
			if (image == null) {
				image = toByteArrayUsingJava(getImageDefaultLogo().getStream());
			}
			session.setAttribute("imageLogo", image);

		} catch (Exception e) {
			System.out.println("setImageUserSession() - Error: "
					+ e.getMessage());
		}
	}

	public void setImageLogo2() {
		// cargar foto del usuario
		try {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			byte[] image = sessionMain.getEmpresaLogin().getFotoPerfil();
			session.setAttribute("imageLogo", image);

		} catch (Exception e) {
			System.out.println("setImageUserSession() - Error: "
					+ e.getMessage());
		}
	}
	
	public StreamedContent getImageLogo() {
		String mimeType = "image/png";
		StreamedContent file;
		InputStream is = null;
		try {
			HttpSession request = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			byte[] image = (byte[]) request.getAttribute("imageLogo");
			is = new ByteArrayInputStream(image);
			return new DefaultStreamedContent(new ByteArrayInputStream(
					toByteArrayUsingJava(is)), mimeType);
		} catch (Exception e) {
			System.out.println("getEmpresaSession() -> error : "
					+ e.getMessage());
			return null;
		}
	}
	
	//PERMISOS
	private void cargarPermisosUserSession(){
		try{
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			com.erp360.model.UsuarioRol usuarioRol = usuarioRolDao.obtenerPorUsuario(usuarioSession);
			List<Privilegio> listPrivilegio = privilegioDao.obtenerTodosPorRoles(usuarioRol.getRol());
			for(Privilegio p : listPrivilegio){
				session.setAttribute(p.getPermiso().getNombre(), "AC");
			}
		}catch(Exception e){

		}
	}
	
	public void permisoValidado(String permiso){
		try {
			System.out.println("permisoValidado("+permiso+")");
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request1 = (HttpServletRequest) context
					.getExternalContext().getRequest();
			HttpSession request = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			String permisoAux = request.getAttribute(permiso)!=null ? (String) request.getAttribute(permiso):"IN";
			if( ! permisoAux.equals("AC")){
				FacesContext.getCurrentInstance().getExternalContext()
				.redirect(request1.getContextPath() +"/error403.xhtml");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// ----------- Getters and Setters ------------

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public StreamedContent getFotoPerfilTemp() {
		return fotoPerfilTemp;
	}

	public void setFotoPerfilTemp(StreamedContent fotoPerfilTemp) {
		this.fotoPerfilTemp = fotoPerfilTemp;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public UploadedFile getFileLogo() {
		return fileLogo;
	}

	public void setFileLogo(UploadedFile fileLogo) {
		this.fileLogo = fileLogo;
	}
}
