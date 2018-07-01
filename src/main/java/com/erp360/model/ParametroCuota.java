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
@Table(name = "parametro_cuota", schema = "public")
public class ParametroCuota implements Serializable {

	private static final long serialVersionUID = 7580337636061446309L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	//MONEDA DOLAR
	@Column(name="monto_rango_inicial",nullable=false )
	private double montoRangoInicial;
	
	//MONEDA DOLAR
	@Column(name="monto_rango_final",nullable=false )
	private double montoRangoFinal;
	
	@Column(name="porcentaje_cuota_inicial",nullable=false )
	private double porcentajeCuotaInicial;
	
	@Column(name="numero_cuotas",nullable=false )
	private Integer numeroCuotas;

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

	public ParametroCuota() {
		super();
		this.id = 0;
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
			if(!(obj instanceof ParametroCobranza)){
				return false;
			}else{
				if(((ParametroCuota)obj).id==this.id){
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

	public double getMontoRangoInicial() {
		return montoRangoInicial;
	}

	public void setMontoRangoInicial(double montoRangoInicial) {
		this.montoRangoInicial = montoRangoInicial;
	}

	public double getMontoRangoFinal() {
		return montoRangoFinal;
	}

	public void setMontoRangoFinal(double montoRangoFinal) {
		this.montoRangoFinal = montoRangoFinal;
	}

	public Integer getNumeroCuotas() {
		return numeroCuotas;
	}

	public void setNumeroCuotas(Integer numeroCuotas) {
		this.numeroCuotas = numeroCuotas;
	}

	public double getPorcentajeCuotaInicial() {
		return porcentajeCuotaInicial;
	}

	public void setPorcentajeCuotaInicial(double porcentajeCuotaInicial) {
		this.porcentajeCuotaInicial = porcentajeCuotaInicial;
	}
}