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
@Table(name = "ejecutivo", schema = "public")
public class Ejecutivo  implements Serializable {

	private static final long serialVersionUID = 1419889919632323263L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="codigo",nullable=false )
	private String codigo;
	
	@Column(name="celular",nullable=true )
	private String celular;
	
	@Column(name="telefono",nullable=true )
	private String telefono;

	@Size(max = 255)
	@Column(name="direccion",nullable=true )
	private String direccion;
	
	@Column(name="correo",nullable=true )
	private String correo;

	//cliente normal
	@Column(name="ci",nullable=true )
	private String ci;
	
	@Column(name="nombres",nullable=true )
	private String nombres;
	
	@Column(name="apellidos",nullable=true )
	private String apellidos;
	
	@Column(name="porcentaje",nullable=true )
	private Double porcentaje;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;
	
	@Column(name = "foto", nullable = true)
	private byte[] foto;
	
	@Column(name = "peso_foto", nullable = true)
	private int pesoFoto;
	
	@Column(name="sales_person",nullable=true )
	private boolean salesPerson;
	
	@Column(name="publisher",nullable=true )
	private boolean publisher;

	@Size(max = 2) //AC , IN
	private String estado;

	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;

	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;

	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;
	
	@OneToMany(mappedBy="ejecutivo")
	private List<NotaVenta>  notasVenta;

	public Ejecutivo() {
		super();
		this.id= 0 ;
		this.nombres= "";
		this.codigo= "";
		this.telefono= "";
		this.direccion= "";
		this.correo= "";
		this.estado="AC";
		this.setFechaRegistro(new Date());
		this.pesoFoto = 0;
		this.salesPerson = true;
		this.publisher = false;
		this.porcentaje = 20d;
	}

	@Override
	public String toString() {
		return nombres;
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
			if(!(obj instanceof Ejecutivo)){
				return false;
			}else{
				if(((Ejecutivo)obj).id==this.id){
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

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
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

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public int getPesoFoto() {
		return pesoFoto;
	}

	public void setPesoFoto(int pesoFoto) {
		this.pesoFoto = pesoFoto;
	}

	public boolean isSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(boolean salesPerson) {
		this.salesPerson = salesPerson;
	}

	public boolean isPublisher() {
		return publisher;
	}

	public void setPublisher(boolean publisher) {
		this.publisher = publisher;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

}


