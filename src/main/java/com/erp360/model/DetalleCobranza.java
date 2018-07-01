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
@Table(name = "detalle_cobranza", schema = "public")
public class DetalleCobranza implements Serializable {

	private static final long serialVersionUID = -8812447129953055293L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="subtotal_nacional",nullable=false )
	private double subTotalNacional;
	
	@Column(name="subtotal_exranjero",nullable=false )
	private double subTotalExranjero;
	
	@Column(name="numero_pago", nullable=false)
	private int numeroPago;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="id_cobranza", nullable=false)
	private Cobranza cobranza;
	
	@Size(max = 2) //AC , IN
	private String estado;
	
	@Column(name="fecha_registro",nullable=false )
	private Date fechaRegistro;
	
	@Column(name="usuario_registro",nullable=false )
	private String UsuarioRegistro;

	public DetalleCobranza() {
		super();
		this.id = 0;
		this.subTotalNacional = 0;
		this.subTotalExranjero = 0;
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
			if(!(obj instanceof DetalleCobranza)){
				return false;
			}else{
				if(((DetalleCobranza)obj).id==this.id){
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

	public Cobranza getCobranza() {
		return cobranza;
	}

	public void setCobranza(Cobranza cobranza) {
		this.cobranza = cobranza;
	}

	public double getSubTotalNacional() {
		return subTotalNacional;
	}

	public void setSubTotalNacional(double subTotalNacional) {
		this.subTotalNacional = subTotalNacional;
	}

	public double getSubTotalExranjero() {
		return subTotalExranjero;
	}

	public void setSubTotalExranjero(double subTotalExranjero) {
		this.subTotalExranjero = subTotalExranjero;
	}

	public int getNumeroPago() {
		return numeroPago;
	}

	public void setNumeroPago(int numeroPago) {
		this.numeroPago = numeroPago;
	}

}


