package com.erp360.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * 
 * @author david
 *
 */
@Entity
@Table(name = "tipo_cliente", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class TipoCliente implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2201662513863129181L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;	
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="porcentaje")
	private Double porcentaje;
	
	@Size(max = 5) //DESC , ASC
	@Column(name="tipo_descuento")
	private String tipoDescuento;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_empresa", nullable=false)
	private Empresa empresa;
	
	@Size(max = 2) //AC , IN
	@Column(name="estado", nullable=false )
	private String estado;
	
	@Column(name="fecha_registro",nullable=true )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;
	
	@Column(name="usuario_registro",nullable=true )
	private String usuarioRegistro;

	public TipoCliente() {
		super();
		this.id = 0;
		this.tipoDescuento="ASC";
		this.porcentaje=0d;
		
	}
	
	@Override
	public String toString() {
		return String.valueOf(nombre);
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
			if(!(obj instanceof TipoCliente)){
				return false;
			}else{
				if(((TipoCliente)obj).id==this.id){
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
	
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String concepto) {
		this.nombre = concepto;
	}

	public String isTipoDescuento() {
		return tipoDescuento;
	}

	public void setTipoDescuento(String tipoDescuento) {
		this.tipoDescuento = tipoDescuento;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public String getTipoDescuento() {
		return tipoDescuento;
	}

	

}


