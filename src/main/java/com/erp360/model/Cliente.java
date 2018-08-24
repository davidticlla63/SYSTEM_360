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
@Table(name = "cliente", schema = "public")
public class Cliente  implements Serializable {

	private static final long serialVersionUID = 1419889919632323263L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="codigo",nullable=false )
	private String codigo;
	
	@Column(name="celular_1",nullable=true )
	private String celular1;
	
	@Column(name="celular_2",nullable=true )
	private String celular2;
	
	@Column(name="telefono",nullable=true )
	private String telefono;

	@Size(max = 255)
	@Column(name="direccion",nullable=true )
	private String direccion;
	
	@Column(name="correo",nullable=true )
	private String correo;

	//tipo de cliente
	@Column(name="tipo",nullable=false )
	private String tipo;//NATURAL o JURIDICO

	//cliente normal
	@Column(name="ci",nullable=true )
	private String ci;
	
	@Column(name="nombres",nullable=true )
	private String nombres;
	
	@Column(name="apellidos",nullable=true )
	private String apellidos;

	//cliente juridico
	@Column(name="nit",nullable=true )
	private String nit;

	@Column(name="razon_social",nullable=true )
	private String razonSocial;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_tipo_cliente", nullable=true)
	private TipoCliente tipoCliente;
	
	@Transient
	private Ejecutivo ejecutivo;
	
	@Column(name = "foto", nullable = true)
	private byte[] foto;
	
	@Column(name = "peso_foto", nullable = true)
	private int pesoFoto;

	@Size(max = 2) //AC , IN
	private String estado;

	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;

	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;

	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;

	public Cliente() {
		super();
		this.id= 0 ;
		this.nombres = new String();
		this.codigo =  new String();
		this.telefono =  new String();
		this.tipo = "NATURAL";
		this.nit = new String();
		this.razonSocial = new String();
		this.direccion = "";
		this.correo = "";
		this.estado="AC";
		this.setFechaRegistro(new Date());
		this.pesoFoto = 0;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", codigo=" + codigo + ", nombres= "+nombres+ ", apellidos= "+apellidos+"]";
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
			if(!(obj instanceof Cliente)){
				return false;
			}else{
				if(((Cliente)obj).id.intValue()==this.id.intValue()){
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

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCelular1() {
		return celular1;
	}

	public void setCelular1(String celular1) {
		this.celular1 = celular1;
	}

	public String getCelular2() {
		return celular2;
	}

	public void setCelular2(String celular2) {
		this.celular2 = celular2;
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

	public TipoCliente getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public Ejecutivo getEjecutivo() {
		return ejecutivo;
	}

	public void setEjecutivo(Ejecutivo ejecutivo) {
		this.ejecutivo = ejecutivo;
	}

}


