package com.erp360.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * @author mauriciobejaranorivera
 * 
 */
@Entity
@Table(name="adjunto", catalog="public")
public class Adjunto implements Serializable {

	private static final long serialVersionUID = -1381801811631362656L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	//Ej: docx
	@Column(name="ext_file",nullable=true )
	private String extFile;
	
	//Ej: nameFile.docx
	@Column(name="name_file",nullable=true )
	private String nameFile;
	
	//Ej: nameFile.docx
	@Column(name="name_file_original",nullable=true )
	private String nameFileOriginal;
	
	//Ej: /adj/673GJASDGF76|nameFile.docx
	@Column(name="path",nullable=true )
	private String path;
	
	@ManyToOne(fetch=FetchType.EAGER )
    @JoinColumn(name="id_cliente", nullable=false)
	private Cliente cliente;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=false )
	private Date fechaModificacion;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;

	public Adjunto() {
		super();
		this.id = 0;
		this.nameFile = "file.docx";
		this.extFile = "docx;";
	}

	@Override
	public String toString() {
		return String.valueOf(id);
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			if(((Adjunto)obj).id==this.id){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getExtFile() {
		return extFile;
	}

	public void setExtFile(String extFile) {
		this.extFile = extFile;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getNameFileOriginal() {
		return nameFileOriginal;
	}

	public void setNameFileOriginal(String nameFileOriginal) {
		this.nameFileOriginal = nameFileOriginal;
	}
}



