package com.erp360.model;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;





/**
 * 
 * @author david
 *
 */
@Entity
@Table(name = "caja", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class Caja implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="codigo",nullable=false )
	private String codigo;
	
	@Column(name="nombre",nullable=false )
	private String nombre;

	@Column(name = "descripcion", nullable = false)
	private String descripcion;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_empresa", nullable = false)
	private Empresa empresa;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_gestion", nullable = true)
	private Gestion gestion;

	@Size(max = 15)// AC , IN , RM
	@Column(name = "estado", nullable = false)
	private String estado;
	
	@Size(max = 15)// AB(abierta), CE(cerrada)
	@Column(name = "opcion", nullable = false)
	private String opcion;

	@Column(name = "usuario_registro", nullable = false)
	private String usuarioRegistro;

	@Column(name = "fecha_registro", nullable = false)
	private Date fechaRegistro;

	@Column(name = "fecha_modificacion", nullable = true)
	private Date fechaModificacion;
	
	@Column(name = "comprobante_unico", nullable = true)
	private boolean comprobanteUnico;
	
	@OneToMany(mappedBy = "caja", orphanRemoval=true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CajaUsuario> listaUsuarios = new ArrayList<>();

	public Caja(Double tipoCambio) {
		this.id = 0;	
		this.estado = "AC";
	    this.codigo="Generado por el Sistema";
		this.gestion=new Gestion();
		this.usuarioRegistro = "";
		this.comprobanteUnico=false;
		this.listaUsuarios = new ArrayList<>();
	}
	public Caja() {
		
		this.id = 0;
         this.codigo="Generado por el Sistema";
		this.estado = "AC";
		
		this.gestion=new Gestion();
		this.usuarioRegistro = "";
	}
	
	@Override
	public String toString() {
		return "Caja [id=" + id + ", nombre=" + nombre + ", descripcion="
				+ descripcion + ", gestion="
				+ gestion + ", estado=" + estado + ", opcion=" + opcion
				+ ", usuarioRegistro=" + usuarioRegistro + ", fechaRegistro="
				+ fechaRegistro + ", fechaModificacion=" + fechaModificacion
				+ "]";
	}
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else {
			if (!(obj instanceof Caja)) {
				return false;
			} else {
				if (((Caja) obj).id == this.id) {
					return true;
				} else {
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

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Gestion getGestion() {
		return gestion;
	}
	public void setGestion(Gestion gestion) {
		this.gestion = gestion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public String getOpcion() {
		return opcion;
	}
	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public List<CajaUsuario> getListaUsuarios() {
		return listaUsuarios;
	}
	public void setListaUsuarios(List<CajaUsuario> listaUsuarios) {
			this.listaUsuarios = listaUsuarios;
			for (CajaUsuario cajaUsuario : this.listaUsuarios) {
				cajaUsuario.setCaja(this);
			}
	}
	public void addUsuario(CajaUsuario cajaUsuario){
		listaUsuarios.add(cajaUsuario);
		cajaUsuario.setCaja(this);
	}
	public boolean isComprobanteUnico() {
		return comprobanteUnico;
	}
	public void setComprobanteUnico(boolean comprobanteUnico) {
		this.comprobanteUnico = comprobanteUnico;
	}

	
	
	
}
