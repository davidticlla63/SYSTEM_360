package com.erp360.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;


/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity
@Table(name = "tipo_proveedor", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"nombre","id_empresa"}))
public class TipoProveedor implements Serializable {

	private static final long serialVersionUID = -8812447129953055293L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="nombre",nullable=false )
	private String nombre;
	
	@Column(name="descripcion",nullable=false )
	private String descripcion;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;

	@Size(max = 2) //AC , IN
	private String estado;

	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;

	@Column(name="usuario_registro",nullable=false )
	private String UsuarioRegistro;

	public TipoProveedor() {
		super();
		this.id = 0;
		this.estado = "AC";
	}

	@Override
	public String toString() {
		return "Tipo Proveedor "+id ;
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
			if(!(obj instanceof TipoProveedor)){
				return false;
			}else{
				if(((TipoProveedor)obj).id==this.id){
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
		return UsuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		UsuarioRegistro = usuarioRegistro;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}


