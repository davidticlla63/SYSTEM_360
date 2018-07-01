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
@Table(name = "parametro_venta", schema = "public")
public class ParametroVenta implements Serializable {

	private static final long serialVersionUID = 7580337636061446309L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="coeficiente_interes_mensual",nullable=true )
	private double coeficienteInteresMensual;
	
	@Column(name="coeficiente_interes_semanal",nullable=true )
	private double coeficienteInteresSemanal;
	
	@Column(name="coeficiente_interes_quincenal",nullable=true )
	private double coeficienteInteresQuincenal;
	
	@Column(columnDefinition = "text",nullable=true)
	private String contrato;
	
	//------
	@Size(max = 2) //AC , IN
	@Column(name="estado",nullable=true )
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="fecha_modificacion",nullable=true )
	private Date fechaModificacion;
	
	@Column(name="usuario_registro",nullable=false )
	private String usuarioRegistro;

	public ParametroVenta() {
		super();
		this.id = 0;
		this.coeficienteInteresMensual = 2.25;
		this.coeficienteInteresSemanal = 2.25;
		this.coeficienteInteresQuincenal = 2.25;
	}
	
	@Override
	public String toString() {
		return String.valueOf(id);
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
			if(!(obj instanceof ParametroVenta)){
				return false;
			}else{
				if(((ParametroVenta)obj).id==this.id){
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
	
	public double getCoeficienteInteresMensual() {
		return coeficienteInteresMensual;
	}

	public void setCoeficienteInteresMensual(double coeficienteInteresMensual) {
		this.coeficienteInteresMensual = coeficienteInteresMensual;
	}

	public double getCoeficienteInteresSemanal() {
		return coeficienteInteresSemanal;
	}

	public void setCoeficienteInteresSemanal(double coeficienteInteresSemanal) {
		this.coeficienteInteresSemanal = coeficienteInteresSemanal;
	}

	public double getCoeficienteInteresQuincenal() {
		return coeficienteInteresQuincenal;
	}

	public void setCoeficienteInteresQuincenal(double coeficienteInteresQuincenal) {
		this.coeficienteInteresQuincenal = coeficienteInteresQuincenal;
	}

	public String getContrato() {
		return contrato;
	}

	public void setContrato(String contrato) {
		this.contrato = contrato;
	}
}


