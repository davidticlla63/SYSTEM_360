/**
 * 
 */
package com.erp360.util;

import org.primefaces.model.UploadedFile;

import com.erp360.model.Adjunto;

/**
 * @author mauriciobejaranorivera
 *
 */
public class EDAdjunto {
	
	private Adjunto adjunto;
	private UploadedFile uploadedFile;
	
	public EDAdjunto(Adjunto adjunto, UploadedFile uploadedFile) {
		super();
		this.adjunto = adjunto;
		this.uploadedFile = uploadedFile;
	}

	public Adjunto getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(Adjunto adjunto) {
		this.adjunto = adjunto;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	

}
