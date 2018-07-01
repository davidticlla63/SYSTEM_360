package com.erp360.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Class Privilegio
 * @author Mauricio.Bejarano.Rivera
 * @version v1.0
 * 
 */
@Entity
@Table(name = "privilegio", schema = "public")
public class Privilegio implements Serializable {

	private static final long serialVersionUID = 3945217112655219859L;
	private Integer id;
	private Permiso permiso;
	private Roles roles;
	
	private String estado;
	private Date fechaModificacion;
	private Date fechaRegistro;
	private String usuarioRegistro;

	public Privilegio() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}
	
	@Override
	public String toString() {
		return String.valueOf(id) ;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}else{
			if(!(obj instanceof Cliente)){
				return false;
			}else{
				if(((Privilegio)obj).id==this.id){
					return true;
				}else{
					return false;
				}
			}
		}
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name="id_permiso")
	public Permiso getPermiso() {
		return permiso;
	}

	public void setPermiso(Permiso permiso) {
		this.permiso = permiso;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name="id_roles")
	public Roles getRoles() {
		return roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
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
}


