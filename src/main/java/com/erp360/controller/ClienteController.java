package com.erp360.controller;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.erp360.dao.ClienteAdicionalDao;
import com.erp360.dao.ClienteDao;
import com.erp360.interfaces.ITipoClienteDao;
import com.erp360.model.Adjunto;
import com.erp360.model.Cliente;
import com.erp360.model.ClienteAdicional;
import com.erp360.model.Concepto;
import com.erp360.model.TipoCliente;
import com.erp360.model.TipoConcepto;
import com.erp360.util.EDAdjunto;
import com.erp360.util.FacesUtil;
import com.erp360.util.FtpUtil;
import com.erp360.util.SessionMain;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@ManagedBean(name = "clienteController")
@ViewScoped
public class ClienteController implements Serializable {

	private static final long serialVersionUID = -7148739425514986109L;

	//DAO
	private @Inject SessionMain sessionMain; //variable del login
	private @Inject ClienteDao clienteDao;
	//private @Inject ClienteAdicionalDao clienteAdicionalDao;
	private @Inject ITipoClienteDao tipoClienteDao;

	//estados
	private boolean crear ;
	private boolean registrar ;
	private boolean modificar ;

	//VARIABLES
	private String nombreEstado="ACTIVO";
	private byte[] data;

	//OBJECT
	private Cliente newCliente;
	private Cliente selectedCliente;
	//private ClienteAdicional newClienteAdicional;
	private UploadedFile uploadedFile;

	//LISTAS
	private List<Cliente> listClientes = new ArrayList<Cliente>();
	private String[] listEstado = {"ACTIVO","INACTIVO"};
	private List<String> listAdjuntoImagenes;
	private List<Adjunto> listAdjunto;
	private List<EDAdjunto> listUploadedFile;
	public static List<TipoCliente> tipoClientes = new ArrayList<TipoCliente>();
	//MAP
	private MapModel emptyModel;
	private Marker marker;
	private String mapAddress;

	@PostConstruct
	public void init() {
		loadDefault();
	}

	private void loadDefault(){
		FacesUtil.setSessionAttribute("imagen", null);
		FacesUtil.setSessionAttribute("imagenCliente", null);
		listUploadedFile = new ArrayList<>();
		listAdjunto = new ArrayList<>();
		listAdjuntoImagenes = new ArrayList<>();
		listAdjuntoImagenes.add("casa.jpg");
		//newClienteAdicional = new ClienteAdicional();
		emptyModel = new DefaultMapModel();

		crear = true;
		registrar = false;
		modificar = false;

		newCliente = new Cliente();
		newCliente.setCodigo(String.format("%08d",clienteDao.obtenerCorrelativo2()));
		selectedCliente = new Cliente();
		listClientes = clienteDao.obtenerTodosActivosOrdenadosPorId();
	}

	// -- event action

	public void loadDialogImage() {
		FacesUtil.showDialog("dlgImagenCliente");
		FacesUtil.updateComponent("dlgImagenCliente");
	}

	public List<Cliente> getListClientes() {
		return listClientes;
	}

	public void registrar(){
		//byte[] image = (byte[]) FacesUtil.getSessionAttribute("imagen");
		byte[] imageCliente = (byte[]) FacesUtil.getSessionAttribute("imagenCliente");
		//if(image!=null){
		//	newClienteAdicional.setFoto(image);
		//	newClienteAdicional.setPesoFoto(image.length);
		//}
		String estado = nombreEstado.equals("ACTIVO")?"AC":"IN";
		Date fechaActual = new  Date();
		//newClienteAdicional.setEstado("AC");
		//newClienteAdicional.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		//newClienteAdicional.setFechaRegistro(fechaActual);
		if(imageCliente!=null){
			newCliente.setFoto(imageCliente);
			newCliente.setPesoFoto(imageCliente.length);
		}
		newCliente.setEstado(estado);
		newCliente.setFechaRegistro(fechaActual);
		newCliente.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		newCliente.setEmpresa(sessionMain.getEmpresaLogin());
		Cliente c = clienteDao.registrar(newCliente,null);
		if(c!=null){
			loadDefault();
		}
	}

	public void loadDialogCuenta(){
		FacesUtil.showDialog("dlgPlanCuenta");	
	}

	///------

	public void modificar(){
		//byte[] image = (byte[]) FacesUtil.getSessionAttribute("imagen");
		byte[] imageCliente = (byte[]) FacesUtil.getSessionAttribute("imagenCliente");
		//if(image!=null){
		//	newClienteAdicional.setFoto(image);
		//	newClienteAdicional.setPesoFoto(image.length);
		//}
		String estado = nombreEstado.equals("ACTIVO")?"AC":"IN";
		Date fechaActual = new  Date();
		if(imageCliente!=null){
			newCliente.setFoto(imageCliente);
			newCliente.setPesoFoto(imageCliente.length);
		}
		//newClienteAdicional.setEstado("AC");
		//newClienteAdicional.setFechaModificacion(fechaActual);
		
		newCliente.setEstado(estado);
		newCliente.setFechaRegistro(fechaActual);
		newCliente.setUsuarioRegistro(sessionMain.getUsuarioLogin().getLogin());
		boolean sw = clienteDao.modificar(newCliente,null);
		if(sw){
			loadDefault();
		}
	}

	public void eliminar(){
		boolean sw = clienteDao.eliminar(newCliente,null);
		if(sw){
			loadDefault();
		}
	}

	public void actualizarFormReg(){
		crear = true;
		registrar = false;
		modificar = false;
		newCliente = new Cliente();	
		selectedCliente = new Cliente();
	}

	public void cambiarAspecto(){
		crear = false;
		registrar = true;
		modificar = false;
	}

	public void onRowSelect(SelectEvent event) {
		newCliente = new Cliente();
		newCliente = selectedCliente;
		if(newCliente.getPesoFoto()>0){
			FacesUtil.setSessionAttribute("imagenCliente", newCliente.getFoto());
		}
		//newClienteAdicional = clienteAdicionalDao.obtenerPorCLiente(newCliente);
		//if(newClienteAdicional!=null){
		//	LatLng coord1 = new LatLng(newClienteAdicional.getUbicacionLatitud(), newClienteAdicional.getUbicacionLongitud());
		//	emptyModel.addOverlay(new Marker(coord1, newClienteAdicional.getUbicacionTitulo()));
		//	if(newClienteAdicional.getPesoFoto()>0){
		//		FacesUtil.setSessionAttribute("imagen", newClienteAdicional.getFoto());
		//	}
		//}
		nombreEstado = newCliente.getEstado().equals("AC")?"ACTIVO":"INACTIVO";
		crear = false;
		registrar = false;
		modificar = true;
	}

	//MAP MARKER

	public void addMarker() {
		//emptyModel = new DefaultMapModel();
		//marker = new Marker(new LatLng(newClienteAdicional.getUbicacionLatitud(), newClienteAdicional.getUbicacionLongitud()), newClienteAdicional.getUbicacionTitulo());
		//emptyModel.addOverlay(marker);
		//FacesUtil.infoMessage("Ubicacion Agregada Correctamente", newClienteAdicional.getUbicacionTitulo());
	}

	public void onMarkerSelect(OverlaySelectEvent event) {
		marker = (Marker) event.getOverlay();
	}

	public void onGeocode(GeocodeEvent event) {
		List<GeocodeResult> results = event.getResults();

		if (results != null && !results.isEmpty()) {
			// LatLng center = results.get(0).getLatLng();
			// centerGeoMap = center.getLat() + "," + center.getLng();

			for (int i = 0; i < results.size(); i++) {
				GeocodeResult result = results.get(i);
				emptyModel.addOverlay(new Marker(result.getLatLng(), result.getAddress()));
			}
		}
	}

	//ADDRESS
	public void loadAddress(){
		System.out.println("address: "+mapAddress);
		RequestContext requestContext = RequestContext.getCurrentInstance();  
		requestContext.execute("PF('gmap').geocode("+mapAddress+")");
	}

	//ADJUNTOS

	//DOWNLOAD FILE
	public StreamedContent getStreamedAdjunto(String url) {
		try {
			// Read the PDF
			InputStream is1 = FtpUtil.downloadFile("user_mv", "pass_mv", "ip_mv",url);
			StreamedContent streamedManual = new DefaultStreamedContent(is1,"application/"+obtenerExtension(url), url);
			return streamedManual;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String obtenerExtension(String nameFile){
		return "pdf";
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			uploadedFile = event.getFile();
			if(uploadedFile!=null){
				String nameFile = getCadenaAlfanumAleatoria(5).concat(uploadedFile.getFileName().replace(" ", "_"));
				String url = "/ADJ/"+nameFile;
				String extFile = "none";

				Adjunto adj = new Adjunto(); 
				adj.setNameFileOriginal(uploadedFile.getFileName());
				adj.setNameFile(nameFile);
				adj.setEstado("AC");
				adj.setFechaRegistro(new Date());
				adj.setPath(url);
				adj.setExtFile(extFile);
				//adj.setUsuarioRegistro(usuarioSession.getLogin());
				//adjuntoRegistration.create(adj);  
				listAdjunto.add(adj);
				EDAdjunto edAdjunto = new EDAdjunto(adj,uploadedFile);
				listUploadedFile.add(edAdjunto);

				//guardar en el server ftp (Pero al momento que le de registrar)
				//InputStream input = uploadedFile.getInputstream();
				//FtpUtil.upLoadFile("user_mv", "pass_mv", "ip_mv",  input,url);

				//enviarAdjunto(nameFile,url);
				FacesUtil.hideDialog("dlgUpload");
			}else{
				FacesUtil.errorMessage("No se pudo cargar el archivo");
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.errorMessage("Error al enviar Mensaje");
		}
	}

	private String getCadenaAlfanumAleatoria(int longitud){
		StringBuffer cadenaAleatoria = new StringBuffer();
		long milis = new java.util.GregorianCalendar().getTimeInMillis();
		Random r = new Random(milis);
		int i = 0;
		while ( i < longitud){
			char c = (char)r.nextInt(255);
			if ( (c >= '0' && c <='9') || (c >='A' && c <='Z') ){
				cadenaAleatoria.append(c);
				i ++;
			}
		}
		return cadenaAleatoria.toString();
	}

	//CAMERA
	public void oncapture(CaptureEvent captureEvent) {
		data = captureEvent.getData();
		FacesUtil.setSessionAttribute("imagenCliente", data);
		FacesUtil.hideDialog("dlgphotoCam");
	}
	
	
	//tipo cliente
	public List<TipoCliente> onCompleteTipoCliente(String query) {
		// ystem.out.println("Entro en Oncomplete Caja"+ query);
		tipoClientes = tipoClienteDao.RetornarOnCompletePorEmpresa(
				sessionMain.getEmpresaLogin(), query.toUpperCase());
		return tipoClientes;
	}

	// ACTION

	public void onSelectTipoCliente(SelectEvent event) {
		newCliente.setTipoCliente((TipoCliente) event.getObject());
	}

	// --------------   get and set  ---------------

	public boolean isModificar() {
		return modificar;
	}

	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	public String getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	public String[] getListEstado() {
		return listEstado;
	}

	public void setListEstado(String[] listEstado) {
		this.listEstado = listEstado;
	}

	public boolean isCrear() {
		return crear;
	}

	public void setCrear(boolean crear) {
		this.crear = crear;
	}

	public boolean isRegistrar() {
		return registrar;
	}

	public void setRegistrar(boolean registrar) {
		this.registrar = registrar;
	}

	public Cliente getNewCliente() {
		return newCliente;
	}

	public void setNewCliente(Cliente newCliente) {
		this.newCliente = newCliente;
	}

	public Cliente getSelectedCliente() {
		return selectedCliente;
	}

	public void setSelectedCliente(Cliente selectedCliente) {
		this.selectedCliente = selectedCliente;
	}

//	public ClienteAdicional getNewClienteAdicional() {
//		return newClienteAdicional;
//	}
//
//	public void setNewClienteAdicional(ClienteAdicional newClienteAdicional) {
//		this.newClienteAdicional = newClienteAdicional;
//	}

	public MapModel getEmptyModel() {
		return emptyModel;
	}

	public void setEmptyModel(MapModel emptyModel) {
		this.emptyModel = emptyModel;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public List<String> getListAdjuntoImagenes() {
		return listAdjuntoImagenes;
	}

	public void setListAdjuntoImagenes(List<String> listAdjuntoImagenes) {
		this.listAdjuntoImagenes = listAdjuntoImagenes;
	}

	public List<Adjunto> getListAdjunto() {
		return listAdjunto;
	}

	public void setListAdjunto(List<Adjunto> listAdjunto) {
		this.listAdjunto = listAdjunto;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public List<EDAdjunto> getListUploadedFile() {
		return listUploadedFile;
	}

	public void setListUploadedFile(List<EDAdjunto> listUploadedFile) {
		this.listUploadedFile = listUploadedFile;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getMapAddress() {
		return mapAddress;
	}

	public void setMapAddress(String mapAddress) {
		this.mapAddress = mapAddress;
	}

	public static List<TipoCliente> getTipoClientes() {
		return tipoClientes;
	}

	public static void setTipoClientes(List<TipoCliente> tipoClientes) {
		ClienteController.tipoClientes = tipoClientes;
	}
}

