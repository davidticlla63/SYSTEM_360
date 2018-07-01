package com.erp360.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.cdi.push.Push;

import com.erp360.dao.EmpresaDao;
import com.erp360.dao.GestionDao;
import com.erp360.model.Empresa;
import com.erp360.model.Gestion;
import com.erp360.model.Usuario;
import com.erp360.util.FacesUtil;
import com.erp360.util.SessionMain;

@Named(value = "empresaController")
@ConversationScoped
public class EmpresaController implements Serializable {

	private static final long serialVersionUID = 5399619661135190257L;

	public static final String PUSH_CDI_TOPIC = "pushCdi";

	@Inject
	Conversation conversation;
	
	@Inject
	@Push(topic = PUSH_CDI_TOPIC)
	Event<String> pushEventSucursal;
	
	//DAO
	private @Inject GestionDao gestionDao;
	private @Inject EmpresaDao empresaDao;
	
	//STATE
	private boolean modificar = false;
	
	//VAR
	private Integer gestion;
	private String gestionIniciada;
	private Empresa empresaSesion;
	private String nuevaGestion;

	//OBJECT
	private Empresa newEmpresa;
	private Gestion newGestion;

	//LIST
	private List<Gestion> listGestion = new ArrayList<Gestion>();

	//login
	private @Inject SessionMain sessionMain; //variable del login
	private Usuario usuarioSession;

	@PostConstruct
	public void initNewEmpresa() {
		beginConversation();
		usuarioSession = sessionMain.getUsuarioLogin();
		empresaSesion = sessionMain.getEmpresaLogin();

		loadValuesDefault();
	}

	public void loadValuesDefault(){
		this.modificar = false;
		this.listGestion = gestionDao.obtenerActivosOrdenAscPorId();
		this.newGestion = new Gestion();
		this.nuevaGestion = String.valueOf(obtenerGestionActiva().getGestion());
		this.gestionIniciada = obtenerGestionActiva().isIniciada()?"SI":"NO";
		this.newEmpresa = empresaDao.findById(1);
	}

	private Gestion obtenerGestionActiva(){
		for(Gestion g: listGestion){
			if(g.getEstadoCierre().equals("AC")){
				return g;
			}
		}
		return null;
	}

	public void beginConversation() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	public void endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
	}

	public void modificarEmpresa() {
		try {
			Date fechaActual = new Date();
			newEmpresa.setRazonSocial(newEmpresa.getRazonSocial().toUpperCase());
			newEmpresa.setFechaModificacion(new Date());
			newEmpresa.setFotoPerfil(sessionMain.getEmpresaLogin().getFotoPerfil());
			newEmpresa.setPesoFoto(sessionMain.getEmpresaLogin().getPesoFoto());
			Empresa emp = empresaDao.modificar(newEmpresa);
			if(emp!=null){
				sessionMain.setEmpresaLogin(emp);
				loadValuesDefault();
			}

			for(Gestion g: listGestion ){
				if(g.getId() == 0){//nueva gestion
					g.setEstado("AC");
					//g.setUsuarioRegistro(nombreUsuario);
					g.setEmpresa(newEmpresa);
					g.setFechaRegistro(fechaActual);
					//gestionRegistration.create(g);
				}else{
					g.setFechaModificacion(fechaActual);
					//gestionRegistration.update(g);
				}
			}
			FacesUtil.infoMessage("Empresa Modificada", "Empresa "+newEmpresa.getRazonSocial());
			modificar = false;
			loadValuesDefault();
		} catch (Exception e) {
			FacesUtil.errorMessage("Error al modificar");
		}
	}

	public void registrarGestion(){
		for(Gestion g: listGestion){
			g.setEstadoCierre("CE");
		}
		newGestion.setId(0);
		newGestion.setEstadoCierre("AC");
		listGestion.add( newGestion);
	}

	public String urlServletLogoEmpresa(){
		String url = "";//FacesUtil.getUrlPath()+"ServletLogoEmpresa?idFormatoEmpresa="+formatoEmpresa.getId();
		return url;
	}

	public void modificarForm(){
		modificar = true;
	}

	public void actualizarForm(){
		modificar = false;
	}

	// ----------------   get and set  ----------------------

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public Gestion getNewGestion() {
		return newGestion;
	}

	public void setNewGestion(Gestion newGestion) {
		this.newGestion = newGestion;
	}

	public List<Gestion> getListGestion() {
		return listGestion;
	}

	public void setListGestion(List<Gestion> listGestion) {
		this.listGestion = listGestion;
	}

	public Integer getGestion() {
		return gestion;
	}

	public void setGestion(Integer gestion) {
		this.gestion = gestion;
	}

	public String getNuevaGestion() {
		return nuevaGestion;
	}

	public void setNuevaGestion(String nuevaGestion) {
		this.nuevaGestion = nuevaGestion;
		for(Gestion g: listGestion){
			g.setEstadoCierre("CE");
			if(g.getGestion() == Integer.parseInt(nuevaGestion)){
				g.setEstadoCierre("AC");
			}
		}
	}

	public String getGestionIniciada() {
		return gestionIniciada;
	}

	public void setGestionIniciada(String gestionIniciada) {
		this.gestionIniciada = gestionIniciada;
	}

	public Empresa getNewEmpresa() {
		return newEmpresa;
	}

	public void setNewEmpresa(Empresa newEmpresa) {
		this.newEmpresa = newEmpresa;
	}
}
