package com.erp360.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;


/**
 * 
 * @author mauriciobejaranorivera
 *
 */
@Entity
@Table(name = "proveedor", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"nombre","id_empresa"}))
public class Proveedor implements Serializable {

	private static final long serialVersionUID = -8812447129953055293L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="nombre", nullable=false )
	private String nombre;
	
	@Column(name="codigo", nullable=false )
	private String codigo;
	
	@Column(name="direccion", nullable=false )
	private String direccion;
	
	@Column(name="nit", nullable=false )
	private String nit;

	@Column(name="nombre_contacto", nullable=false )
	private String nombreContacto;
	
	@Column(name="ci_contacto", nullable=false )
	private String ciContacto;
	
	@Column(name="telefono_contacto", nullable=false )
	private String telefonoContacto;
	
	@Column(name="direccion_contacto", nullable=false )
	private String direccionContacto;
	
	@Column(name="email_contacto", nullable=false )
	private String emailContacto;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="id_gestion", nullable=false)
	private Gestion gestion;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="usuario_registro",nullable=false )
	private String UsuarioRegistro;

	public Proveedor() {
		super();
		this.id = 0;
		this.estado = "AC";
		this.nombre = "";
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
			if(!(obj instanceof Proveedor)){
				return false;
			}else{
				if(((Proveedor)obj).id==this.id){
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getNombreContacto() {
		return nombreContacto;
	}

	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	public String getCiContacto() {
		return ciContacto;
	}

	public void setCiContacto(String ciContacto) {
		this.ciContacto = ciContacto;
	}

	public String getTelefonoContacto() {
		return telefonoContacto;
	}

	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}

	public String getDireccionContacto() {
		return direccionContacto;
	}

	public void setDireccionContacto(String direccionContacto) {
		this.direccionContacto = direccionContacto;
	}

	public String getEmailContacto() {
		return emailContacto;
	}

	public void setEmailContacto(String emailContacto) {
		this.emailContacto = emailContacto;
	}

//	public TipoProveedor getTipoProveedor() {
//		return tipoProveedor;
//	}
//
//	public void setTipoProveedor(TipoProveedor tipoProveedor) {
//		this.tipoProveedor = tipoProveedor;
//	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Gestion getGestion() {
		return gestion;
	}

	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}
	

}


