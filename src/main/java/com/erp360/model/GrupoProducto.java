package com.erp360.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Date;


/**
 * The persistent class for the almacen database table.
 * 
 */
@Entity 
@Table(name="grupo_producto" ,schema="public", uniqueConstraints = @UniqueConstraint(columnNames="codigo"))
public class GrupoProducto implements Serializable {

	private static final long serialVersionUID = 5047606646242681208L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String codigo;
	
	private String nombre;
	
	private String descripcion;
	
	@Column(name="porcentaje_venta_credito")
	private double porcentajeVentaCredito;
	
	@Column(name="porcentaje_venta_contado")
	private double porcentajeVentaContado;

	@Size(max = 2) //AC , IN
	@Column(name="estado", nullable=false )
	private String estado;
	
	@Column(name="fecha_registro")
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true)
	private Date fechaModificacion;
	
	@Column(name="usuario_registro")
	private String usuarioRegistro;

	public GrupoProducto() {
		super();
		this.id = 0;
		this.nombre = "";
		this.codigo = "";
		this.descripcion = "Ninguna";
		this.porcentajeVentaCredito = 25;
		this.porcentajeVentaContado = 25;
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
			if(!(obj instanceof GrupoProducto)){
				return false;
			}else{
				if(((GrupoProducto)obj).id.intValue() == this.id.intValue()){
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

	public double getPorcentajeVentaCredito() {
		return porcentajeVentaCredito;
	}

	public void setPorcentajeVentaCredito(double porcentajeVentaCredito) {
		this.porcentajeVentaCredito = porcentajeVentaCredito;
	}

	public double getPorcentajeVentaContado() {
		return porcentajeVentaContado;
	}

	public void setPorcentajeVentaContado(double porcentajeVentaContado) {
		this.porcentajeVentaContado = porcentajeVentaContado;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

}