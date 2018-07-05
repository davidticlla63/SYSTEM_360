package com.erp360.util;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.erp360.dao.EmpresaDao;
import com.erp360.dao.GestionDao;
import com.erp360.dao.PrivilegioDao;
import com.erp360.dao.TipoCambioDao;
import com.erp360.dao.UsuarioDao;
import com.erp360.dao.UsuarioRolDao;
import com.erp360.interfaces.IUsuarioSucursalDao;
import com.erp360.model.CajaSesion;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.Privilegio;
import com.erp360.model.Roles;
import com.erp360.model.Sucursal;
import com.erp360.model.TipoCambio;
import com.erp360.model.Usuario;
import com.erp360.model.UsuarioSucursal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Class SessionMain, datos persistente durante la session del usuario
 * @author mauricio.bejarano.rivera
 *
 */

//sessionMain.usuarioLogin
@Named
@SessionScoped
public class SessionMain implements Serializable {

	private static final long serialVersionUID = -381361211607201207L;

	private @Inject FacesContext facesContext;
	
	//DAO
	private @Inject UsuarioDao usuarioDao;
	private @Inject EmpresaDao empresaDao;
	private @Inject GestionDao gestionDao;
	private @Inject PrivilegioDao privilegioDao;
	private @Inject UsuarioRolDao usuarioRolDao;
	private @Inject TipoCambioDao tipoCambioDao;
	private @Inject IUsuarioSucursalDao usuarioSucursalDao;
	
	//Object
	private Usuario usuarioLogin;
	private Empresa empresaLogin;
	private Gestion gestionLogin;
	private TipoCambio tipoCambio;
	private Sucursal sucursalLogin;
	private CajaSesion cajaSesion;
	
	
	private String pathFisico;
	private String urlPath;
	private String caja;

	private boolean seActualizoTipoCambio = false;

	@PostConstruct
	public void initSessionMain(){
		System.out.println("----- initSessionMain() --------");
		usuarioLogin = null;
		setEmpresaLogin(null);
		gestionLogin = null;
		
		pathFisico = "";
		urlPath = "";
		
		tipoCambio = tipoCambioDao.obtenerPorEmpresaYFecha(getEmpresaLogin(), new Date());
		System.out.println("tipoCambio: "+tipoCambio);
		if( tipoCambio == null ){
			Date fechaActual = new Date();
			TipoCambio tipoCambio2 = tipoCambioDao.obtenerUltimoRegistro(getEmpresaLogin());
			System.out.println("tipoCambio2: "+tipoCambio2);
			tipoCambio2.setId(0);
			tipoCambio2.setFecha(fechaActual);
			tipoCambio2.setFechaRegistro(fechaActual);
			tipoCambio2.setUsuarioRegistro("system");
			tipoCambio = tipoCambioDao.registrar(tipoCambio2);
		}
	}
	
	public Usuario validarUsuario_(String username,String password){
		//if(usuarioLoggin == null){
		return	usuarioDao.obtenerPorLogin(username, password);
		//}
		//return getUsuarioLoggin();
	}

	/**
	 * Verifica si la pagina tiene permiso de acceso
	 * @param pagina
	 * @return boolean
	 */
	public boolean tienePermisoPagina(String pagina){
		if( pagina.equals("profile.xhtml")  || pagina.equals("dashboard.xhtml") || pagina.equals("manual_usuario.xhtml") 
				|| pagina.equals("orden_servicio_index.xhtml") || pagina.equals("documento.xhtml") || pagina.equals("cliente.xhtml")
				|| pagina.equals("orden_servicio.xhtml")
				|| pagina.equals("facturacion_index.xhtml")
				|| pagina.equals("factura.xhtml")
				|| pagina.equals("factura_list.xhtml")
				|| pagina.equals("factura_orden_servicio.xhtml")
				|| pagina.equals("certificacion.xhtml")
				|| pagina.equals("tipo_hoja.xhtml")
				|| pagina.equals("directorio.xhtml")){
			return true;
		}
//		for(Permiso p: listPermiso){
//			if(p.getDetallePagina().getPagina().getPath().equals(pagina)){
//				return true;
//			}
//		}
		return false;
	}

	/**
	 * Verifica si la pagina tiene permiso de acceso
	 * @param pagina
	 * @return boolean
	 */
	public boolean tienePermisoPaginaAccion(String pagina,String accion){
//		for(Permiso p: listPermiso){
//			Accion accionAux = p.getDetallePagina().getAccion();
//			Pagina paginaAux = p.getDetallePagina().getPagina();
//			if(paginaAux.getNombre().equals(pagina) && accionAux.getNombre().equals(accion)){
//				return true;
//			}
//		}
		return false;
	}

	public void setAttributeSession(String key,String value){
		try{
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			session.setAttribute(key, value);
		}catch(Exception e){
			System.out.println("setAttributeSession() ERROR: "+e.getMessage());
		}		
	}

	public String getAttributeSession(String key){
		try {
			HttpSession request = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			return request.getAttribute(key)!=null ? (String) request.getAttribute(key):null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean removeAttributeSession(String key){
		try {
			HttpSession request = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			request.removeAttribute(key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Gestion getGestionLogin() {
		if(gestionLogin == null){
			this.gestionLogin = gestionDao.findByGestionCierreActivo();
		}
		return gestionLogin;
	}
	
	public boolean verificarPermiso(String permiso){
		try{
			String permisoAux = getAttributeSession(permiso)!=null ? (String) getAttributeSession(permiso):"IN";
			if( ! permisoAux.equals("AC")){
				return false;
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void cargarPermisosUsuarioLogin(){
		try{
			Roles rol = usuarioRolDao.obtenerPorUsuario(usuarioLogin).getRol();
			List<Privilegio> listPrivilegio = privilegioDao.obtenerTodosPorRoles(rol);
			System.out.println(listPrivilegio.size());
			for(Privilegio p : listPrivilegio){
				setAttributeSession(p.getPermiso().getNombre(), "AC");
			}
		}catch(Exception e){

		}
	}

	//----------------------------------------

	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}

	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

	public String getPathFisico() {
		if(pathFisico.trim().isEmpty()){
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			pathFisico = (String) servletContext.getRealPath("/resources/temp/");
			return pathFisico;
		}
		return pathFisico;
	}

	public void setPathFisico(String pathFisico) {
		this.pathFisico = pathFisico;
	}

	public String getUrlPath() {
		if(urlPath.trim().isEmpty()){
			HttpServletRequest request = (HttpServletRequest) facesContext
					.getExternalContext().getRequest();
			String urlPath2 = request.getRequestURL().toString();
			urlPath = urlPath2.substring(0, urlPath2.length()
					- request.getRequestURI().length())
					+ request.getContextPath() + "/";
			return urlPath;
		}
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public Empresa getEmpresaLogin() {
		if(empresaLogin == null){
			try{
				empresaLogin = empresaDao.findById(1);
			}catch(Exception e){
				empresaLogin =  null;
			}
		}
		return empresaLogin;
	}

	public void setEmpresaLogin(Empresa empresaLogin) {
		this.empresaLogin = empresaLogin;
	}

	public boolean isSeActualizoTipoCambio() {
		return seActualizoTipoCambio;
	}

	public void setSeActualizoTipoCambio(boolean seActualizoTipoCambio) {
		this.seActualizoTipoCambio = seActualizoTipoCambio;
	}

	public TipoCambio getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(TipoCambio tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	public CajaSesion getCajaSesion() {
		return cajaSesion;
	}

	public void setCajaSesion(CajaSesion cajaSesion) {
		if (cajaSesion==null) {
			setCaja("Sin CAJA");
		}else{
			setCaja(cajaSesion.getCaja().getNombre());
		}
		this.cajaSesion = cajaSesion;
	}

	public String getCaja() {
		return caja;
	}

	public void setCaja(String caja) {
		this.caja = caja;
	}

	public Sucursal getSucursalLogin() {
		if (sucursalLogin.getId().equals(0)) {
			List<UsuarioSucursal> listAux = usuarioSucursalDao
					.obtenerTodosPorUsuario(getUsuarioLogin());
			if (listAux.size() > 0) {
				sucursalLogin = listAux.get(0).getSucursal();
			} else {
				// temporal
				sucursalLogin = new Sucursal();
				sucursalLogin.setId(1);
				sucursalLogin.setNombre("CASA MATRIZ");
			}
		}
//		System.out.println("Sucursal Session: " + sucursalLogin);
		return sucursalLogin;
	}

	public void setSucursalLogin(Sucursal sucursalLogin) {
		this.sucursalLogin = sucursalLogin;
	}
}
