package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.richfaces.cdi.push.Push;

import com.erp360.dao.ModuloDao;
import com.erp360.dao.PermisoDao;
import com.erp360.dao.PrivilegioDao;
import com.erp360.dao.RolDao;
import com.erp360.dao.UsuarioRolDao;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.Modulo;
import com.erp360.model.Permiso;
import com.erp360.model.Privilegio;
import com.erp360.model.Roles;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;
import com.erp360.model.Usuario;
import com.erp360.model.UsuarioRol;
import com.erp360.util.EDPermiso;


@Named(value = "permisoController")
@ConversationScoped
public class PermisoController implements Serializable {

	private static final long serialVersionUID = 6042338866890601312L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;

	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;

	//DAO
	private @Inject RolDao rolesRepository;
	private  @Inject ModuloDao moduloRepository;
	private @Inject PermisoDao permisoReppository;
	private @Inject PrivilegioDao privilegioDao;
	private @Inject UsuarioRolDao usuerRolRepository;

	//ESTADO
	private boolean modificar = false;

	//LIST
	private List<Roles> listRoles = new ArrayList<Roles>();
	private List<Permiso> listPermiso = new ArrayList<Permiso>();
	private List<Modulo> listModulo = new ArrayList<Modulo>();
	private List<Privilegio> listPrivilegio = new ArrayList<Privilegio>();

	//OBJECT
	private Roles selectedRoles;

	//TREE
	private TreeNode root;
	private TreeNode[] selectedNodes;
	private String selectionModeTreeNode;

	//SESSION
	private @Inject SessionMain sessionMain; //variable del login
	private Usuario usuarioSession;
	private Gestion gestionSesion;
	private Empresa empresaLogin;

	@PostConstruct
	public void initNewPermiso() {
		usuarioSession = sessionMain.getUsuarioLogin();
		gestionSesion = sessionMain.getGestionLogin();
		empresaLogin = sessionMain.getEmpresaLogin();

		listModulo = moduloRepository.findAllOrderByID();
		listPermiso = permisoReppository.findAllOrderByName();
		listRoles = rolesRepository.obtenerOrdenAscPorId();

		loadDefault();

	}

	private void loadDefault(){
		selectedRoles = new Roles();
		selectionModeTreeNode = "none";
		cargarPermisos();
	}

	public void initConversation() {
		if (!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient()) {
			conversation.begin();
			System.out.println(">>>>>>>>>> CONVERSACION INICIADA...");
		}
	}

	public String endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
			System.out.println(">>>>>>>>>> CONVERSACION TERMINADA...");
		}
		return "orden_ingreso.xhtml?faces-redirect=true";
	}

	/**
	 * cargar todos los permisos por defecto no seleccionados 
	 */
	private void cargarPermisos(){
		root = new DefaultTreeNode(new EDPermiso(0, "",null, null,false), null);
		for(Modulo mod : listModulo){
			TreeNode tn1 = new DefaultTreeNode(new EDPermiso(0, mod.getNombre(),mod, null,false),root);
			tn1.setExpanded(true);
			tn1.setSelected(false);
			List<Permiso> listPermmiso = obtenerPermisoByModulo(mod);
			for(Permiso p: listPermmiso){
				TreeNode tn2 = new DefaultTreeNode("1",new EDPermiso(0,p.getNombre(), mod,p,false),tn1);
				tn2.setExpanded(true);
				tn1.setSelected(false);
			}
		}
	}

	/**
	 * cargar todos los permisos por privilegio 
	 */
	private void cargarPermisosConPrivilegios(){
		root = new DefaultTreeNode(new EDPermiso(0, "",null, null,false), null);
		for(Modulo mod : listModulo){
			TreeNode tn1 = new DefaultTreeNode(new EDPermiso(0, mod.getNombre(),mod, null,false),root);
			tn1.setExpanded(true);
			tn1.setSelected(existModulo(mod));
			List<Permiso> listPermmiso = obtenerPermisoByModulo(mod);
			for(Permiso p: listPermmiso){
				TreeNode tn2 = new DefaultTreeNode("1",new EDPermiso(0,p.getNombre(), mod,p,false),tn1);
				tn2.setExpanded(true);
				tn2.setSelected(existPermiso(p));
			}
		}
	}

	private boolean existPermiso(Permiso permiso){
		for(Privilegio p : listPrivilegio){
			if(p.getPermiso().equals(permiso)){
				return true;
			}
		}
		return false;
	}

	private boolean existModulo(Modulo modulo){
		for(Privilegio p : listPrivilegio){
			if(p.getPermiso().getModulo().equals(modulo)){
				return true;
			}
		}
		return false;
	}

	private List<Permiso> obtenerPermisoByModulo(Modulo modulo){
		List<Permiso> listPermisoAux = new ArrayList<Permiso>();
		for(Permiso p: this.listPermiso){
			if(p.getModulo().equals(modulo)){
				listPermisoAux.add(p);
			}
		}
		return listPermisoAux;
	}

	public void onRowSelectRol(SelectEvent event) {
		modificar = true ;
		selectionModeTreeNode = "checkbox";
		listPrivilegio = privilegioDao.findAllByRol(selectedRoles);
		cargarPermisosConPrivilegios();
	}

	public void guardar(){
		System.out.println("guardar length "+selectedNodes.length);
		if(selectedNodes==null){
			FacesUtil.infoMessage("Seleccione Permisos", "");
			return;
		}
		List<Privilegio> listPrivilegioAnteriores = privilegioDao.findAllByRol(selectedRoles);
		List<Privilegio> listPrivilegioNuevos = new ArrayList<>();
		for(TreeNode t: selectedNodes){
			EDPermiso e = (EDPermiso)t.getData();
			if(e.getPermiso()!=null){
				Privilegio privilegio = new Privilegio();
				privilegio.setEstado("AC");
				privilegio.setPermiso(e.getPermiso());
				privilegio.setRoles(selectedRoles);
				listPrivilegioNuevos.add(privilegio);
			}
		}
		boolean sw = privilegioDao.registrar(listPrivilegioAnteriores,listPrivilegioNuevos);
		if(sw){
			if(selectedNodes.length>0){
				//verificar si el rol modificado es el loggeado, para cargar los nuevos datos
				if(isRolUsuarioLogin(selectedRoles)){
					FacesUtil.infoMessage("Permisos Registrados!", "total "+selectedNodes.length+ "   Los cambios se aplicaran despues de reiniciar la Sesion");
				}else{
					FacesUtil.infoMessage("Permisos Registrados!", "total "+selectedNodes.length);
				}
				loadDefault();
			}
		}
	}

	private boolean isRolUsuarioLogin(Roles roles){
		UsuarioRol userRolesAux = usuerRolRepository.obtenerPorUsuario(usuarioSession);
		if(roles.getId()==userRolesAux.getRol().getId()){
			System.out.println("isRolUsuarioLogin -> true");
			return true;
		}
		System.out.println("isRolUsuarioLogin -> false");
		return false;
	}

	public void cancelar(){
		System.out.println("cancelar()");
		modificar = false;
		loadDefault();
	}

	//--GET AND SETTER
	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public List<Roles> getListRoles() {
		return listRoles;
	}

	public void setListRoles(List<Roles> listRoles) {
		this.listRoles = listRoles;
	}

	public List<Permiso> getListPermiso() {
		return listPermiso;
	}

	public void setListPermiso(List<Permiso> listPermiso) {
		this.listPermiso = listPermiso;
	}

	public List<Modulo> getListModulo() {
		return listModulo;
	}

	public void setListModulo(List<Modulo> listModulo) {
		this.listModulo = listModulo;
	}

	public List<Privilegio> getListPrivilegio() {
		return listPrivilegio;
	}

	public void setListPrivilegio(List<Privilegio> listPrivilegio) {
		this.listPrivilegio = listPrivilegio;
	}

	public Roles getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(Roles selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		System.out.println("setSelectedNodes()");
		this.selectedNodes = selectedNodes;
	}

	public String getSelectionModeTreeNode() {
		return selectionModeTreeNode;
	}

	public void setSelectionModeTreeNode(String selectionModeTreeNode) {
		this.selectionModeTreeNode = selectionModeTreeNode;
	}

}
