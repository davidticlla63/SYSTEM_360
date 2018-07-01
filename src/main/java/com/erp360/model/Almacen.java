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
@Table(name="almacen" ,schema="public", uniqueConstraints = @UniqueConstraint(columnNames={"codigo","id_gestion"}))
public class Almacen implements Serializable {

	private static final long serialVersionUID = 347357319180148125L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String direccion;

	@Column(name="codigo",nullable=true)
	private String codigo;

	private String nombre;

	private String telefono;

	@Column(name="tipo_almacen")
	private String tipoAlmacen;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_gestion", nullable=true)
	private Gestion gestion;

	private String estado;

	@Column(name="usuario_registro")
	private String usuarioRegistro;

	@Column(name="fecha_registro")
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion" , nullable=true)
	private Date fechaModificacion;

	public Almacen() {
		this.id = 0 ;
		this.nombre ="";
		this.codigo = "";
		this.telefono="";
		this.direccion="";
		this.estado= "AC";
		this.tipoAlmacen="ALMACEN CENTRAL";
	}
		
	@Override
	public String toString() {
		return "Almacen [id=" + id + ", direccion=" + direccion + ", codigo="
				+ codigo + ", nombre=" + nombre + ", telefono=" + telefono
				+ ", tipoAlmacen=" + tipoAlmacen + ", gestion=" + gestion
				+ ", estado=" + estado + ", usuarioRegistro=" + usuarioRegistro
				+ ", fechaRegistro=" + fechaRegistro + ", fechaModificacion="
				+ fechaModificacion + "]";
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
			if(!(obj instanceof Almacen)){
				return false;
			}else{
				if(((Almacen)obj).id.intValue()==this.id.intValue()){
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

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
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

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getTipoAlmacen() {
		return tipoAlmacen;
	}

	public void setTipoAlmacen(String tipoAlmacen) {
		this.tipoAlmacen = tipoAlmacen;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

}