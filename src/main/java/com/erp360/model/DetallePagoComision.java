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
@Table(name = "detalle_pago_comision", schema = "public")
public class DetallePagoComision implements Serializable {

	private static final long serialVersionUID = -8812447129953055293L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="monto_extranjero",nullable=true )
	private double montoExtranjero;
	
	@Column(name="monto_nacional",nullable=true )
	private double montoNacional;
		
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_pago_comision", nullable=false)
	private PagoComision pagoComision;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_ejecutivo_comisiones", nullable=false)
	private EjecutivoComisiones ejecutivoComisiones;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="usuario_registro",nullable=false )
	private String UsuarioRegistro;

	public DetallePagoComision() {
		super();
		this.id = 0;
		this.estado = "AC";
	}
	
	@Override
	public String toString() {
		return ""+id ;
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
			if(!(obj instanceof DetallePagoComision)){
				return false;
			}else{
				if(((DetallePagoComision)obj).id==this.id){
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

	public String getUsuarioRegistro() {
		return UsuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		UsuarioRegistro = usuarioRegistro;
	}

	public double getMontoExtranjero() {
		return montoExtranjero;
	}

	public void setMontoExtranjero(double montoExtranjero) {
		this.montoExtranjero = montoExtranjero;
	}

	public double getMontoNacional() {
		return montoNacional;
	}

	public void setMontoNacional(double montoNacional) {
		this.montoNacional = montoNacional;
	}

	public PagoComision getPagoComision() {
		return pagoComision;
	}

	public void setPagoComision(PagoComision pagoComision) {
		this.pagoComision = pagoComision;
	}

	public EjecutivoComisiones getEjecutivoComisiones() {
		return ejecutivoComisiones;
	}

	public void setEjecutivoComisiones(EjecutivoComisiones ejecutivoComisiones) {
		this.ejecutivoComisiones = ejecutivoComisiones;
	}
}


