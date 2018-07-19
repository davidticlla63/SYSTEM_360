package com.erp360.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;

/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity
@Table(name="ejecutivo_cliente" ,schema="public")
public class EjecutivoCliente implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_ejecutivo", nullable=false)
	private Ejecutivo ejecutivo;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_cliente", nullable=true)
	private Cliente cliente;
	
	private String estado;

	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;
	
	@Column(name="fecha_modificacion",nullable=true)
	private Date fechaModificacion;

	public EjecutivoCliente() {
		super();
		this.id = 0;	
		this.ejecutivo= new Ejecutivo();
		this.cliente = new Cliente();
	}
	
	@Override
	public String toString() {
		return "EjecutivoCliente [id=" + id + ", estado=" + estado
				+ ", fechaRegistro=" + fechaRegistro + ", usuarioRegistro="
				+ usuarioRegistro  + "]";
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}else{
			if(!(obj instanceof EjecutivoCliente)){
				return false;
			}else{
				if(((EjecutivoCliente)obj).id==this.id){
					return true;
				}else{
					return false;
				}
			}
		}
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Ejecutivo getEjecutivo() {
		return ejecutivo;
	}

	public void setEjecutivo(Ejecutivo ejecutivo) {
		this.ejecutivo = ejecutivo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}