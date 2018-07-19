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
@Table(name="ejecutivo_pagos" ,schema="public")
public class EjecutivoPagos implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_encargado_venta", nullable=false)
	private Ejecutivo encargadoVenta;

	@Column(name = "importe", nullable = false)
	private Double importe;
	
	private String estado;

	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;
	
	public EjecutivoPagos() {
		super();
		this.id = 0;	
		this.encargadoVenta= new Ejecutivo();
	}
	
	@Override
	public String toString() {
		return "AlmacenEncargado [id=" + id + ", estado=" + estado
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
			if(!(obj instanceof EjecutivoPagos)){
				return false;
			}else{
				if(((EjecutivoPagos)obj).id==this.id){
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

	public Ejecutivo getEncargadoVenta() {
		return encargadoVenta;
	}

	public void setEncargadoVenta(Ejecutivo encargadoVenta) {
		this.encargadoVenta = encargadoVenta;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}


}