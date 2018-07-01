package com.erp360.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Date;


/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity 
@Table(name="linea_producto" ,schema="public", uniqueConstraints = @UniqueConstraint(columnNames="codigo"))
public class LineaProducto implements Serializable {

	private static final long serialVersionUID = 5047606646242681208L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String codigo;
	
	private String nombre;
	
	private String descripcion;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_grupo_producto", nullable=false)
	private GrupoProducto grupoProducto;

	@Size(max = 2) //AC , IN
	@Column(name="estado", nullable=false )
	private String estado;
	
	@Column(name="fecha_registro")
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true)
	private Date fechaModificacion;
	
	@Column(name="usuario_registro")
	private String usuarioRegistro;

	public LineaProducto() {
		super();
		this.id = 0;
		this.nombre = "";
		this.codigo = "";
		this.descripcion = "Ninguna";
	}
	
	@Override
	public String toString() {
		return nombre ;
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
			if(!(obj instanceof LineaProducto)){
				return false;
			}else{
				if(((LineaProducto)obj).id.intValue() == this.id.intValue()){
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

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public GrupoProducto getGrupoProducto() {
		return grupoProducto;
	}

	public void setGrupoProducto(GrupoProducto grupoProducto) {
		this.grupoProducto = grupoProducto;
	}

}