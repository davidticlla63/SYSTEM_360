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
@Table(name = "usuario", catalog = "public", uniqueConstraints = @UniqueConstraint(columnNames="login"))
public class Usuario implements Serializable {

	private static final long serialVersionUID = -2919972501169538062L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre;
	private String email;
	private String login;
	private String password;
	
	@Column(name="encargado_venta",nullable=true )
	private boolean encargadoVenta;

	@Size(max = 2) //AC , IN
	private String state;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;

	@Column(name = "foto_perfil", nullable = true)
	private byte[] fotoPerfil;
	
	@Column(name = "peso_foto", nullable = true)
	private int pesoFoto;

	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;
	
	@ManyToOne(fetch = FetchType.EAGER, optional=false)
	@JoinColumn(name = "id_sucursal", nullable = true)
	private Sucursal sucursal;

	public Usuario() {
		super();
		this.id = 0 ;
		this.nombre = "";
		this.email = " ";
		this.login = "";
		this.password = "";
		this.state = "AC";
		this.sucursal = new Sucursal();
		this.encargadoVenta = false;
	}
	
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", email=" + email
				+ ", login=" + login + ", password=" + password
				+ ", encargadoVenta=" + encargadoVenta + ", state=" + state
				+ ", fechaRegistro=" + fechaRegistro + ", fechaModificacion="
				+ fechaModificacion + ", fotoPerfil=data" + ", pesoFoto=" + pesoFoto
				+ ", usuarioRegistro=" + usuarioRegistro + ", sucursal="
				+ sucursal + "]";
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
			if(!(obj instanceof Usuario)){
				return false;
			}else{
				if(((Usuario)obj).id==this.id){
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public byte[] getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(byte[] fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public int getPesoFoto() {
		return pesoFoto;
	}

	public void setPesoFoto(int pesoFoto) {
		this.pesoFoto = pesoFoto;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public boolean isEncargadoVenta() {
		return encargadoVenta;
	}

	public void setEncargadoVenta(boolean encargadoVenta) {
		this.encargadoVenta = encargadoVenta;
	}
}


