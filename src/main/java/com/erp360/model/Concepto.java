package com.erp360.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * 
 * @author david
 *
 */
@Entity
@Table(name = "concepto", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class Concepto implements Serializable {

	private static final long serialVersionUID = 1017112418637684740L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;	
	
	@Column(name="concepto")
	private String concepto;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_tipo_concepto", nullable=false)
	private TipoConcepto tipoConcepto;
	
	@Size(max = 2) //AC , IN
	@Column(name="estado", nullable=false )
	private String estado;
	
	@Column(name="fecha_registro",nullable=true )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;
	
	@Column(name="usuario_registro",nullable=true )
	private String usuarioRegistro;

	public Concepto() {
		super();
		this.id = 0;
		
	}
	
	@Override
	public String toString() {
		return String.valueOf(concepto);
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
			if(!(obj instanceof Concepto)){
				return false;
			}else{
				if(((Concepto)obj).id==this.id){
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
	
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public TipoConcepto getTipoConcepto() {
		return tipoConcepto;
	}

	public void setTipoConcepto(TipoConcepto tipoConcepto) {
		this.tipoConcepto = tipoConcepto;
	}

	

}


