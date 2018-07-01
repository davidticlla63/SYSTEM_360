package com.erp360.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.erp360.dao.ClienteDao;
import com.erp360.model.Cliente;

import javax.enterprise.context.RequestScoped;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */

@Named(value = "dynamicResourceClienteController")
@RequestScoped
public class DynamicResourceClienteController  extends AbstractDynamicResourceHandler implements Serializable{

	private static final long serialVersionUID = -3756873687377670050L;

	//REPOSITORY
	private @Inject ClienteDao clienteDao;

	//OBJECT
	private StreamedContent streamedContentImage;

	@PostConstruct
	public void init(){
		System.out.println("init() - DynamicResourceClienteController");
	}

	private byte[] toByteArrayUsingJava(InputStream is)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();
		while (reads != -1) {
			baos.write(reads);
			reads = is.read();
		}
		return baos.toByteArray();
	}

	public void handleFileUpload(FileUploadEvent event) {
		System.out.println("handleFileUpload()");
		//FacesUtil.infoMessage("Correcto", event.getFile().getFileName() + " fue cargado.");
		FacesUtil.infoMessage("Imagen Cargada.",event.getFile().getFileName());
		try {
			InputStream molde = event.getFile().getInputstream();
			byte[] image = toByteArrayUsingJava(molde);
			FacesUtil.setSessionAttribute("imagenCliente", image);
			System.out.println("cargado Correctamente");
		} catch (Exception e) {
			FacesUtil.errorMessage("No se pudo subir la imagen.");
		}
	}

	public StreamedContent getImage(){
		String mimeType = "image/jpg";
		InputStream is = null;
		try{
			byte[] image = (byte[]) FacesUtil.getSessionAttribute("imagenCliente");
			is= new ByteArrayInputStream(image);
			return new DefaultStreamedContent(new ByteArrayInputStream(toByteArrayUsingJava(is)), mimeType);
		}catch(Exception e){
			System.out.println("getImagen -> error : "+e.getMessage());
			return null;
		}
	}

	@Override
	protected StreamedContent buildStreamedContentImage(FacesContext context, Integer idObject) throws Exception {
		InputStream is = null;
		streamedContentImage=null;
		Cliente entity = clienteDao.findById(idObject);
		try {
			streamedContentImage= getImage();
			if(streamedContentImage==null){
				if(idObject!=0 && entity.getFoto()!=null){
					is = new ByteArrayInputStream(entity.getFoto());
					streamedContentImage= new DefaultStreamedContent(new ByteArrayInputStream(
							toByteArrayUsingJava(is)));
					return streamedContentImage;
				}else{
					String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/gfx");
					InputStream inputStream = new FileInputStream(path+ File.separator +  "avatar.png");
					streamedContentImage= new DefaultStreamedContent(inputStream, "image/png");
				}
			}
		} catch (Exception e) {
			System.out.println("Error obenter Imagen: "+e.getMessage());
		}
		return streamedContentImage;
	}

	public void setStreamedContentImageTecnico(StreamedContent streamedContentImageTecnico) {
		this.streamedContentImage = streamedContentImageTecnico;
	}


}
