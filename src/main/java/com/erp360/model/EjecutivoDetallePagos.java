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
@Table(name="ejecutivo_detalle_pagos" ,schema="public")
public class EjecutivoDetallePagos implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_ejecutivo_pagos", nullable=false)
	private EjecutivoPagos ejecutivoPagos;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_ejecutivo_comisiones", nullable=false)
	private EjecutivoComisiones ejecutivoComisiones;

	@Column(name = "importe", nullable = false)
	private Double importe;
	
	private String estado;

	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="usuario_registro")
	private String usuarioRegistro;
	
	public EjecutivoDetallePagos() {
		super();
		this.id = 0;
	}
	
	@Override
	public String toString() {
		return " [id=" + id + ", estado=" + estado
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
			if(!(obj instanceof EjecutivoDetallePagos)){
				return false;
			}else{
				if(((EjecutivoDetallePagos)obj).id==this.id){
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

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public EjecutivoComisiones getEjecutivoComisiones() {
		return ejecutivoComisiones;
	}

	public void setEjecutivoComisiones(EjecutivoComisiones ejecutivoComisiones) {
		this.ejecutivoComisiones = ejecutivoComisiones;
	}

	public EjecutivoPagos getEjecutivoPagos() {
		return ejecutivoPagos;
	}

	public void setEjecutivoPagos(EjecutivoPagos ejecutivoPagos) {
		this.ejecutivoPagos = ejecutivoPagos;
	}


}