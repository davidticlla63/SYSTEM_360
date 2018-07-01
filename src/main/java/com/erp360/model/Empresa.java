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
@Table(name = "empresa", catalog = "public", uniqueConstraints = @UniqueConstraint(columnNames="razon_social"))
public class Empresa implements Serializable {

	private static final long serialVersionUID = -5304022702792729682L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="razon_social", nullable=false)
	private String razonSocial;
	
	private String direccion;
	
	private String telefono;
	
	private String nit;
	private String ciudad;
	
	@Column(name = "foto_perfil", nullable = true)
	private byte[] fotoPerfil;
	
	@Column(name = "peso_foto", nullable = true)
	private int pesoFoto;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;
	
	private Date fecha_registro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;
	
	//datos para representante legal
	@Column(name="actividad_economica",nullable=true )
	private String actividadEconomica;
	
	@Column(name="carnet_identidad", nullable=true, length=20)
	private String carnetIdentidad;
	
	@Column(name="representante_legal", nullable=true, length=200)
	private String representanteLegal;

	public Empresa() {
		super();
		this.id = 0;
		this.razonSocial = "";
		this.direccion = "";
		this.telefono = "";
		this.nit = "";
		this.ciudad = "";
		this.pesoFoto = 0;
	}
	
	@Override
	public String toString() {
		return razonSocial;
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
			if(!(obj instanceof Empresa)){
				return false;
			}else{
				if(((Empresa)obj).id==this.id){
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
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Date getFecha_registro() {
		return fecha_registro;
	}

	public void setFecha_registro(Date fecha_registro) {
		this.fecha_registro = fecha_registro;
	}
	
	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public byte[] getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(byte[] fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public int getPesoFoto() {
		return pesoFoto;
	}

	public void setPesoFoto(int pesoFoto) {
		this.pesoFoto = pesoFoto;
	}

	public String getRepresentanteLegal() {
		return representanteLegal;
	}

	public void setRepresentanteLegal(String representanteLegal) {
		this.representanteLegal = representanteLegal;
	}

	public String getCarnetIdentidad() {
		return carnetIdentidad;
	}

	public void setCarnetIdentidad(String carnetIdentidad) {
		this.carnetIdentidad = carnetIdentidad;
	}

	public String getActividadEconomica() {
		return actividadEconomica;
	}

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

}


